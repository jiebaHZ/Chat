package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.AbstractList;
import java.util.Vector;

public class ServerStart {

	static AbstractList<ServerThread> serverThread = new Vector<ServerThread>();

	// �����������ˣ���ʼ�ڶ˿ں��ϼ���
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(6000);
		new ServerGUI();
		try {
			while (true) {
				Socket s = server.accept();
				ServerThread st = new ServerThread(s);
				(new Thread(st)).start();
			}
		} finally {
			server.close();
		}
	}
}
