package client;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class ClientPrivateChatGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextArea chatText;// �����ı���
	JTextArea sendText;// �����ı���

	private JPanel sendPanel;// �������
	private JPanel chatPanel;// �������

	private JLabel hisXiu;// ��_�Է�
	private JLabel myXiu;// ��_�Լ�
	private JLabel toolBack;// ����������
	static JLabel hisName;//�Է��ǳ�

	private JButton sendButton;// ����
	private JButton closeButton;// ����

	private JSplitPane splitPane1;// ���ָ����
	private JSplitPane splitPane2;// �Ҳ�ָ����
	private JSplitPane splitPane3;// �ָܷ����

	String name = null;
	Socket socket = null;

	public ClientPrivateChatGUI(Socket socket, String name) throws Exception {
		setResizable(false);
		this.name = name;
		this.socket = socket;
		this.setSize(598, 544);
		this.setLocationRelativeTo(null);

		// ���ָ���������·ָ��壬�ټ����������
		sendPanel = new JPanel();
		chatPanel = new JPanel();
		splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, chatPanel, sendPanel);
		splitPane1.setDividerLocation(341);
		splitPane1.setDividerSize(20);
		splitPane1.setEnabled(false);

		// �Ҳ�ָ���������·ָ��壬����������ǩ
		hisXiu = new JLabel();
		myXiu = new JLabel();
		splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, hisXiu, myXiu);
		splitPane2.setDividerLocation(250);
		splitPane2.setDividerSize(10);
		splitPane2.setEnabled(false);

		// ���������ָ������
		splitPane3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, splitPane1, splitPane2);
		splitPane3.setDividerLocation(400);
		splitPane3.setDividerSize(10);
		splitPane3.setEnabled(false);

		// ������������
		// 1�����ı���
		sendPanel.setLayout(null);
		chatPanel.setLayout(null);

		// 2���빤��������Label
		toolBack = new JLabel();
		toolBack.setBounds(0, 0, 397, 30);
		chatPanel.add(toolBack);

		// 3���밴ť
		closeButton = new JButton("�ر�");
		closeButton.setFont(new Font("�����п�", Font.PLAIN, 15));
		closeButton.setBounds(218, 126, 78, 25);

		sendButton = new JButton("����");
		sendButton.setFont(new Font("�����п�", Font.PLAIN, 15));
		sendButton.setBounds(319, 126, 78, 25);

		sendPanel.add(closeButton);
		sendPanel.add(sendButton);

		sendText = new JTextArea();
		sendText.setFont(new Font("����", Font.PLAIN, 14));
		sendText.setRows(6);

		JScrollPane scrollPane_1 = new JScrollPane(sendText);
		scrollPane_1.setBounds(0, 0, 397, 126);
		sendPanel.add(scrollPane_1);

		hisName = new JLabel("");
		hisName.setForeground(SystemColor.textHighlight);
		hisName.setFont(new Font("����", Font.PLAIN, 20));
		hisName.setBounds(0, 126, 191, 25);
		sendPanel.add(hisName);

		chatText = new JTextArea();
		chatText.setFont(new Font("����", Font.PLAIN, 14));
		chatText.setRows(15);
		chatText.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(chatText);
		scrollPane.setBounds(0, 30, 397, 308);
		chatPanel.add(scrollPane);

		// 4Label����ͼƬ
		Image imghis = new ImageIcon("F:/pictures/his.jpg").getImage().getScaledInstance(180, 250,
				Image.SCALE_AREA_AVERAGING);
		hisXiu.setIcon(new ImageIcon(imghis));

		Image imgmy = new ImageIcon("F:/pictures/my.jpg").getImage().getScaledInstance(180, 250,
				Image.SCALE_AREA_AVERAGING);
		myXiu.setIcon(new ImageIcon(imgmy));

		Image imgback = new ImageIcon("F:/pictures/back.png").getImage().getScaledInstance(400, 30,
				Image.SCALE_AREA_AVERAGING);
		toolBack.setIcon(new ImageIcon(imgback));

		this.getContentPane().add(splitPane3);
		this.setVisible(true);

		// ����¼�
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					String str = sendText.getText();
					if (str.trim().length() == 0)
						return;
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF("˽��" + "-1_~" + name + "~2/-" + str);
					out.flush();
					sendText.setText("");
					Date date=new Date();
					DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time=format.format(date);
					chatText.append(time + "\n"+"<" + ClientMainGUI.userName + ">" + "��" + "\n");
					chatText.append("        " + str + "\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				for (int i = 0; i < ClientMainGUI.priChatFrame.size(); i++) {
					ClientPrivateChatGUI cmg = ClientMainGUI.priChatFrame.get(i);
					if (cmg.name.equals(name)) {
						ClientMainGUI.priChatFrame.remove(i);
					}
				}
				dispose();
			}
		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				for (int i = 0; i < ClientMainGUI.priChatFrame.size(); i++) {
					ClientPrivateChatGUI cmg = ClientMainGUI.priChatFrame.get(i);
					if (cmg.name.equals(name)) {
						ClientMainGUI.priChatFrame.remove(i);
					}
				}
				dispose();
			}
		});
	}
}