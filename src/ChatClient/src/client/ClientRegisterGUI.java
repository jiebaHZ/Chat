package client;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientRegisterGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jpanel;

	private JLabel UserName = new JLabel("�û���");
	private JLabel UserPassword = new JLabel("����:");
	private JLabel ConfirmPassword = new JLabel("ȷ������:");

	private JTextField userName;
	private JPasswordField userPassword;
	private JPasswordField confirmPassword;

	private JButton Confirm = new JButton("ע��");
	
	Socket socket = null;

	public ClientRegisterGUI() {
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(430, 350);
		this.setLocationRelativeTo(null);

		jpanel = new JPanel();
		jpanel.setLayout(null);
		UserName.setFont(new Font("Dialog", 0, 20));
		UserName.setBounds(87, 47, 127, 30);
		UserPassword.setFont(new Font("Dialog", 0, 20));
		UserPassword.setBounds(87, 104, 127, 30);
		ConfirmPassword.setFont(new Font("Dialog", 0, 20));
		ConfirmPassword.setBounds(87, 163, 127, 30);
		userName = new JTextField();
		userName.setBounds(224, 48, 127, 30);
		userPassword = new JPasswordField();
		userPassword.setBounds(224, 107, 127, 30);
		confirmPassword = new JPasswordField();
		confirmPassword.setBounds(224, 166, 127, 30);
		Confirm.setFont(new Font("Dialog", 0, 15));
		Confirm.setBounds(150, 259, 100, 40);

		jpanel.add(UserName);
		jpanel.add(UserPassword);
		jpanel.add(ConfirmPassword);
		jpanel.add(userName);
		jpanel.add(userPassword);
		jpanel.add(confirmPassword);
		jpanel.add(Confirm);

		this.getContentPane().add(jpanel);
		this.setVisible(true);

		// ע�ᰴť����¼�
		Confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cmd = e.getActionCommand();
				if ("ע��".equals(cmd)) {
					try {
						if (!userName.getText().isEmpty() && !new String(userPassword.getPassword()).isEmpty()
								&& !new String(confirmPassword.getPassword()).isEmpty()) {
							if (new String(userPassword.getPassword())
									.equals(new String(confirmPassword.getPassword()))) { // ������������һ��
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
								out.writeUTF(cmd + "-1_~" + userName.getText() + "~2/-"
										+ new String(userPassword.getPassword()));
								out.flush();
								DataInputStream in = new DataInputStream(socket.getInputStream());
								String str = in.readUTF();
								if (str.equals("ע��ɹ���")) {
									JOptionPane.showMessageDialog(null, "ע��ɹ���", "��ʾ", JOptionPane.PLAIN_MESSAGE);
									socket.close();
								}
								if (str.equals("�û��Ѵ��ڣ�")) {
									JOptionPane.showMessageDialog(null, "�û��Ѵ��ڣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
								}
								ClientRegisterGUI.this.dispose();
							} else {
								JOptionPane.showMessageDialog(null, "�����������벻һ�£�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
							}
						} else if (userName.getText().isEmpty()) {
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
	}
}
