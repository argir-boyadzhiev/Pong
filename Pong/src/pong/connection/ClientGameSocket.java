package pong.connection;

import java.net.InetAddress;

public class ClientGameSocket {

	private byte Id;
	private TCPWrapper TCP;
	private UDPWrapper UDP;

	public ClientGameSocket(InetAddress IP, int port, int localPort) {
		TCP = new TCPWrapper(IP, port);
		UDP = new UDPWrapper(IP, port, localPort);
		int localDatagramPort = UDP.getLocalPort();
		TCP.sendMessage(String.valueOf(localDatagramPort));
		setId();
	}

	private void setId() {
		while (true) {
			String buffer = TCP.getMessage();
			if (buffer != null) {
				Id = (byte) Integer.parseInt(buffer);
				return;
			}
		}
	}

	public byte getId() {
		return Id;
	}

	public void sendTCP(String message) {
		TCP.sendMessage(message);
	}

	public String getTCPMessage() {
		return TCP.getMessage();
	}

	public void sendUDP(byte[] data) {
		UDP.sendData(data);
	}

	public byte[] getUDP() {
		return UDP.getData();
	}
}
