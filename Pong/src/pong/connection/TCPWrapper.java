package pong.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCPWrapper {
	private Socket socket;
	
	public TCPWrapper(InetAddress IP, int port) {
		try {
			socket = new Socket(IP, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TCPWrapper(Socket socket) {
		this.socket = socket;
	}
	
	public void sendMessage(String message) {
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getMessage() {
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			if(in.available()>0) {
				return in.readUTF();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public InetAddress getIP() {
		return socket.getInetAddress();
	}
	
}
