package pong.connection;

import java.net.DatagramSocket;
import java.util.Vector;

public class ServerGameSocket {

	private Linker linker;
	private TCPWrapper[] TCPs;
	private UDPWrapper[] UDPs;
	private Vector<byte[]> datagramMessages;

	public ServerGameSocket(int port, int numberOfClients) {
		linker = new Linker(port);
		TCPs = new TCPWrapper[numberOfClients];
		UDPs = new UDPWrapper[numberOfClients];
		for (int i = 0; i < numberOfClients; i++) {
			makeSocketPair(i);
		}
		datagramMessages = new Vector<byte[]>();
	}

	private void makeSocketPair(int index) {
		TCPs[index] = new TCPWrapper(linker.getSocket());
		int portBuffer = getPortBuffer(index);
		System.out.println(portBuffer);
		DatagramSocket datagramSocket = linker.getDatagramSocket();
		UDPs[index] = new UDPWrapper(datagramSocket, TCPs[index].getIP(), portBuffer);
		TCPs[index].sendMessage(String.valueOf(index));
	}

	private int getPortBuffer(int index) {
		while (true) {
			String buffer = TCPs[index].getMessage();
			if (buffer != null) {
				return Integer.parseInt(buffer);
			}
		}
	}

	public void sendTCP(String message, int client) {
		TCPs[client].sendMessage(message);
	}

	public void sendTCPToAll(String message) {
		for (int i = 0; i < TCPs.length; i++) {
			TCPs[i].sendMessage(message);
		}
	}

	public String getTCPMessage(int client) {
		return TCPs[client].getMessage();
	}

	public void sendUDP(byte[] data, int client) {
		UDPs[client].sendData(data);
	}

	public void sendUDPToAll(byte[] data) {
		for (int i = 0; i < UDPs.length; i++) {
			UDPs[i].sendData(data);
		}
	}

	public byte[] getUDP(int client) {
		for (int i = 0; i < datagramMessages.size(); i++) {
			if (getMessageId(i) == client) {
				byte[] buffer = datagramMessages.elementAt(i);
				datagramMessages.remove(i);
				return buffer;
			}
		}
		return null;
	}

	private byte getMessageId(int index) {
		return datagramMessages.elementAt(index)[0];
	}

	public void updateDatagramMessages() {
		while (true) {
			byte[] buffer = UDPs[0].getData();
			if (buffer != null) {
				datagramMessages.add(buffer);
			} else {
				return;
			}
		}
	}
}
