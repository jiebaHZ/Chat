package client;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientLoginGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextField UserName;// �û����ı���
	JPasswordField UserPassword;// �����ı���

	private JLabel pictureLabel;// ͼƬ
	private JLabel iconLabel;// ͼ��
	private JLabel userNameLabel;// �û�����ǩ
	private JLabel userPasswordLabel;// �����ǩ

	private JButton loginButton;// ��¼��ť
	private JButton registerButton;// ע�ᰴť

	Socket socket = null;

	public ClientLoginGUI() throws Exception {
		this.setTitle("צ�۵�");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setSize(420, 320);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		// ���ñ���
		pictureLabel = new JLabel();
		Image imgbackground = new ImageIcon("F:/pictures/background.jpg").getImage();
		pictureLabel.setIcon(new ImageIcon(imgbackground));
		pictureLabel.setSize(420, 150);
		getContentPane().add(pictureLabel);

		// ����ͷ��
		iconLabel = new JLabel();
		Image imgiconlabel = new ImageIcon("F:/pictures/icon.png").getImage();
		iconLabel.setIcon(new ImageIcon(imgiconlabel));
		iconLabel.setBounds(40, 155, 96, 96);
		this.add(iconLabel);

		// ���������
		UserName = new JTextField();
		UserName.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		UserName.setBounds(150, 170, 150, 30);
		this.add(UserName);
		userNameLabel = new JLabel("�û���");
		userNameLabel.setFont(new Font("��������", Font.PLAIN, 15));
		userNameLabel.setBounds(310, 170, 70, 20);
		this.add(userNameLabel);

		UserPassword = new JPasswordField();
		UserPassword.setFont(new Font("SimSun", Font.PLAIN, 12));
		UserPassword.setBounds(150, 200, 150, 30);
		this.add(UserPassword);
		userPasswordLabel = new JLabel("����");
		userPasswordLabel.setFont(new Font("��������", Font.PLAIN, 15));
		userPasswordLabel.setBounds(310, 200, 70, 20);
		this.add(userPasswordLabel);

		// ���õ�¼ע�ᰴť
		loginButton = new JButton("��¼");
		loginButton.setFont(new Font("�����п�", Font.PLAIN, 20));
		loginButton.setBounds(150, 250, 150, 30);
		loginButton.setForeground(Color.blue);
		loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// ���ù��
		this.add(loginButton);

		registerButton = new JButton("ע��");
		registerButton.setFont(new Font("�����п�", Font.PLAIN, 15));
		registerButton.setBounds(344, 260, 70, 30);
		loginButton.setForeground(Color.blue);
		registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(registerButton);

		this.setVisible(true);

		// ��¼��ť����¼�
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cmd = e.getActionCommand();
				if ("��¼".equals(cmd)) {
					try {
						if (!UserName.getText().isEmpty() && !new String(UserPassword.getPassword()).isEmpty()) {
							try {
								socket = new Socket();
								SocketAddress sAddr = new InetSocketAddress("127.0.0.1",6000);
								socket.connect(sAddr, 8000);
							} catch (Exception e1) {
								socket.close();
								e1.printStackTrace();
							}
							if (socket.isClosed()) {
								JOptionPane.showMessageDialog(null, "�������ѹرգ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
								return;
							}
							DataOutputStream out = new DataOutputStream(socket.getOutputStream());
							DataInputStream in = new DataInputStream(socket.getInputStream());
							out.writeUTF(cmd + "-1_~" + UserName.getText() + "~2/-"
									+ new String(UserPassword.getPassword()));
							out.flush();
							String str = in.readUTF();
							if (str.equals("��¼�ɹ���")) {
								ClientLoginGUI.this.dispose();
								new ClientThread(socket, UserName.getText());
							}
							if (str.equals("�û������ڣ�")) {
								JOptionPane.showMessageDialog(null, "�û������ڣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
							}
							if (str.equals("�������")) {
								JOptionPane.showMessageDialog(null, "�������", "��ʾ", JOptionPane.PLAIN_MESSAGE);
							}
							if (str.equals("�û��Ѿ����ߣ������ظ���½��")) {
								JOptionPane.showMessageDialog(null, "�û��Ѿ����ߣ������ظ���½��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
								socket.close();
							}
						} else if (UserName.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "�������û�����", "��ʾ", JOptionPane.PLAIN_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "���������룡", "��ʾ", JOptionPane.PLAIN_MESSAGE);
						}
					} catch (Exception e1) {
						try {
							socket.close();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
						e1.printStackTrace();
					}

				}
			}
		});
		// ע�ᰴť����¼�
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cmd = e.getActionCommand();
				if ("ע��".equals(cmd)) {
					try {
						new ClientRegisterGUI();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
}
