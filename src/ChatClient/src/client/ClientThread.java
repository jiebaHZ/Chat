package client;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class ClientThread implements Runnable {
	Socket socket = null;
	String name = null;
	public ClientThread(Socket socket, String username) throws Exception {
		this.socket = socket;
		this.name = username;
		new ClientMainGUI(this.socket, username);
		(new Thread(this)).start();
	}

	public void run() {
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			while (true) {
				String str = in.readUTF();
				dealWithMsg(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				socket.close();
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
		case "˽��":
			privateChat(flag, data);
			break;
		case "Ⱥ��":
			publicChat(data);
			break;
		case "����":
			online(data);
			break;
		case "����":
			offline(data);
			break;
		case "�ر�":
			JOptionPane.showMessageDialog(null, "�������ѹرգ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
			break;
		default:
			break;
		}
	}

	public void publicChat(String data) throws Exception {
		String[] temp = data.split("~2/-");
		String name = temp[0];
		String msg = temp[1];
		if (msg != null && msg.length() != 0) {
			if (ClientMainGUI.pubChatFrame == null) {
				ClientMainGUI.pubChatFrame = new ClientPublicChatGUI(socket);
				ClientMainGUI.pubChat.setEnabled(false);
				ClientPublicChatGUI.friendList.setListData(ClientMainGUI.array);
				ClientPublicChatGUI.textArea.setText("��ǰ����������" + (ClientMainGUI.array.size() + 1) + "��" + "\n" + "�ң�  " + this.name);
			}
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time=format.format(date);
			ClientMainGUI.pubChatFrame.chatText.append(time+"\n"+"<" + name + ">" + "��" + "\n");
			ClientMainGUI.pubChatFrame.chatText.append("        " + msg + "\n");
		}
	}

	public void privateChat(String flag, String data) throws Exception {
		String[] temp = data.split("~2/-");
		String name = temp[0];
		String msg = temp[1];
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(date);
		if (msg != null && msg.length() != 0) {
			for (int i = 0; i < ClientMainGUI.priChatFrame.size(); i++) {
				ClientPrivateChatGUI cmg = ClientMainGUI.priChatFrame.get(i);
				if (cmg.name.equals(temp[0])) {
					cmg.chatText.append(time+"\n"+"<" + name + ">" + "��" + "\n");
					cmg.chatText.append("        " + msg + "\n");
					return;
				}
			}
			ClientPrivateChatGUI cpcg = new ClientPrivateChatGUI(socket, name);
			ClientMainGUI.priChatFrame.add(cpcg);
			ClientPrivateChatGUI.hisName.setText("�Է���" + name);
			cpcg.chatText.append(time+"\n"+"<" + name + ">" + "��" + "\n");
			cpcg.chatText.append("        " + msg + "\n");
		}
	}

	public void online(String data) {
		if (ClientMainGUI.array.contains(data))
			return;
		ClientMainGUI.array.add(data);
		ClientMainGUI.friendList.setListData(ClientMainGUI.array);
		if (ClientMainGUI.pubChatFrame != null) {
			ClientPublicChatGUI.friendList.setListData(ClientMainGUI.array);
			ClientPublicChatGUI.textArea.setText("��ǰ����������" + (ClientMainGUI.array.size() + 1) + "��");
		}

	}

	public void offline(String data) {
		if (!ClientMainGUI.array.contains(data))
			return;
		ClientMainGUI.array.remove(data);
		ClientMainGUI.friendList.setListData(ClientMainGUI.array);
		if (ClientMainGUI.pubChatFrame != null) {
			ClientPublicChatGUI.friendList.setListData(ClientMainGUI.array);
			ClientPublicChatGUI.textArea.setText("��ǰ����������" + (ClientMainGUI.array.size() + 1) + "��");
		}
	}
}