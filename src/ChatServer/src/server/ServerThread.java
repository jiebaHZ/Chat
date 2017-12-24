package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Iterator;

//����һ��socket�߳�
public class ServerThread implements Runnable {
	Socket socket = null;
	DataInputStream in;
	DataOutputStream out;

	String userName;
	private String userPassword;

	// ���ݿ����������
	String sql1 = "Select*from client";
	String sql2 = "insert into client values (?,?)";
	Connection con = null;
	Statement stmt = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	public ServerThread(Socket s) throws IOException {
		this.socket = s;
	}

	public void run() {
		try {
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			while (true) {
				String str = in.readUTF();
				dealWithMsg(str);// ��Ϣ����
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void dealWithMsg(String str) throws Exception {
		String[] temp = str.split("-1_~");
		String flag = temp[0];// ��ȡ��Ϣ�ı�־
		String data = temp[1];// ��ȡ��Ϣ����ʵ����

		switch (flag) {
		case "��¼":
			login(flag, data);// ����ǵ�¼
			break;
		case "ע��":
			register(flag, data);// �����ע��
			break;
		case "Ⱥ��":
			publicChat(flag, data);// �����Ⱥ��
			break;
		case "˽��":
			privateChat(flag, data);// �����˽��
			break;
		case "�˳�":
			serverThreadClose(flag, data);// ������˳�
			break;
		default:
			break;
		}
	}

	// ��¼
	public void login(String flag, String data) throws Exception {
		connectDatabase();

		String[] temp = data.split("~2/-");// ��ȡ�û���������
		this.userName = temp[0];
		this.userPassword = temp[1];

		while (rs.next()) { // ����û��Ƿ����
			if (rs.getString(1).equals(this.userName)) {
				if (rs.getString(2).equals(this.userPassword)) { // ����������ȷ
					Iterator<ServerThread> it = ServerStart.serverThread.iterator();
					while (it.hasNext()) {// ����Ƿ��ظ���½
						ServerThread st = it.next();
						if (st.userName.equals(this.userName) && st.userPassword.equals(this.userPassword)) {
							this.out.writeUTF("�û��Ѿ����ߣ������ظ���½��");
							this.out.flush();
							return;
						}
					}
					if (it.hasNext() == false) {// ���Ե�¼
						ServerStart.serverThread.add(this);
						// ���·������б�
						ServerGUI.vc1.add(this.userName);
						String str = this.socket.getInetAddress() + ": " + this.socket.getPort();
						ServerGUI.vc2.add(str);

						ServerGUI.list1.setListData(ServerGUI.vc1);
						ServerGUI.list2.setListData(ServerGUI.vc2);
						ServerGUI.label1.setText("�����û��� " + ServerStart.serverThread.size() + "��");

						out.writeUTF("��¼�ɹ���");
						out.flush();

						// ���������߳�Ա����Ϣ
						for (int i = 0; i < ServerStart.serverThread.size(); i++) {
							ServerThread st = ServerStart.serverThread.get(i);
							if (this.userName != st.userName) {
								st.out.writeUTF("����" + "-1_~" + this.userName);
								st.out.flush();
								this.out.writeUTF("����" + "-1_~" + st.userName);
								this.out.flush();
							}
						}
						return;
					}
				} else {
					out.writeUTF("�������");
					out.flush();
					return;
				}
			}
		}
		out.writeUTF("�û������ڣ�");
		out.flush();

		disconnectDatabase();

	}

	// ע��
	public void register(String falg, String data) throws Exception {
		connectDatabase();// �������ݿ�

		String[] temp = data.split("~2/-");// ��ȡ�û���������

		while (rs.next()) {
			if (rs.getString(1).equals(temp[0])) {
				this.out.writeUTF("�û��Ѵ��ڣ�");
				this.out.flush();
				return;
			}
		}
		// д�����ݿ�
		ps = con.prepareStatement(sql2);
		ps.setString(1, temp[0]);
		ps.setString(2, temp[1]);
		ps.executeUpdate();
		this.out.writeUTF("ע��ɹ���");
		this.out.flush();

		disconnectDatabase();
	}

	// Ⱥ��
	public void publicChat(String flag, String data) throws Exception {
		if (data != null && data.length() != 0) {
			for (int i = 0; i < ServerStart.serverThread.size(); i++) {
				ServerThread st = ServerStart.serverThread.get(i);
				if (this.userName != st.userName) {
					st.out.writeUTF(flag + "-1_~" + this.userName + "~2/-" + data);
					st.out.flush();
				}
			}
		}
	}

	// ˽��
	public void privateChat(String flag, String data) throws IOException {
		String[] temp = data.split("~2/-");

		if (temp[1] != null && temp[1].length() != 0) {
			for (int i = 0; i < ServerStart.serverThread.size(); i++) {
				ServerThread st = ServerStart.serverThread.get(i);
				if (temp[0].equals(st.userName)) {
					st.out.writeUTF(flag + "-1_~" + this.userName + "~2/-" + temp[1]);
					st.out.flush();
				}
			}
		}
	}

	// �رշ��������߳�
	public void serverThreadClose(String flag, String data) throws IOException {
		for (int i = 0; i < ServerStart.serverThread.size(); i++) {
			ServerThread st = ServerStart.serverThread.get(i);
			if (data.equals(st.userName)) {
				ServerStart.serverThread.remove(i);
				// ���·������б�
				ServerGUI.vc1.remove(st.userName);
				String str = st.socket.getInetAddress() + ": " + st.socket.getPort();
				ServerGUI.vc2.remove(str);
			}
		}
		ServerGUI.list1.setListData(ServerGUI.vc1);
		ServerGUI.list2.setListData(ServerGUI.vc2);
		ServerGUI.label1.setText("�����û��� " + ServerStart.serverThread.size() + "��");
		// ֪ͨ������
		for (int j = 0; j < ServerStart.serverThread.size(); j++) {
			ServerThread st2 = ServerStart.serverThread.get(j);
			st2.out.writeUTF("����" + "-1_~" + data);
			st2.out.flush();
		}
	}

	// �������ݿ�
	public void connectDatabase() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "0000");
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql1);
	}

	// �Ͽ����ݿ�
	public void disconnectDatabase() throws SQLException {
		con.close();
		stmt.close();
		rs.close();
		ps.close();
	}
}
