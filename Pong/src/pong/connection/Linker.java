package pong.connection;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Linker {

	private int port;
	private ServerSocket serverSocket;
	private DatagramSocket serverDatagramSocket = null;
	
	public Linker(int port) {
		this.port = port;
		createServerSocket();
		setTimeout(60000);
		setDatagramSocketPort();
	}
	
	private void createServerSocket() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setTimeout(int miliseconds) {
		try {
			serverSocket.setSoTimeout(miliseconds);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		try {
			Socket buffer = serverSocket.accept();
			return buffer;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setDatagramSocketPort() {
		try {
			serverDatagramSocket = new DatagramSocket(port);
			serverDatagramSocket.setSoTimeout(1);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public DatagramSocket getDatagramSocket () {
		return serverDatagramSocket;
	}
}
