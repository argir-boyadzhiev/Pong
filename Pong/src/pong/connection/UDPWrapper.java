package pong.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPWrapper {
	private int destionationPort;
	private InetAddress destinationIP;
	private DatagramSocket datagramSocket;

	public UDPWrapper(InetAddress destinationIP, int destionationPort) {
		try {
			datagramSocket = new DatagramSocket();
			datagramSocket.setSoTimeout(1);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.destinationIP = destinationIP;
		this.destionationPort = destionationPort;
	}
	
	public UDPWrapper(InetAddress destinationIP, int destionationPort,  int localPort) {
		try {
			datagramSocket = new DatagramSocket(localPort);
			datagramSocket.setSoTimeout(1);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.destinationIP = destinationIP;
		this.destionationPort = destionationPort;
	}
	
	public int getLocalPort() {
		return datagramSocket.getLocalPort();
	}
	
	public UDPWrapper(DatagramSocket datagramSocket, InetAddress destinationIP, int destionationPort) {
		this.datagramSocket = datagramSocket;
		this.destinationIP = destinationIP;
		this.destionationPort = destionationPort;
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, destinationIP, destionationPort);
		try {
			datagramSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getData() {
		byte[] buffer = new byte[50];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try {
			datagramSocket.receive(packet);
			return buffer;
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return null;
	}
}
