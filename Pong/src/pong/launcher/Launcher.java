package pong.launcher;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import pong.gamestate.Client;
import pong.gamestate.Server;

public class Launcher {

	private static String gameName = "Pong";
	private static int SCREEN_WIDTH = 800;
	private static int SCREEN_HEIGHT = 500;

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.println("1. Client." + '\n' + "2. Server.");
		int state = Integer.parseInt(scanner.nextLine());

		int port = 2719;

		if (state == 1) {
			System.out.println("Server IP:");
			String serverName = scanner.nextLine();
			System.out.println("Local port:");
			int localPort = Integer.parseInt(scanner.nextLine());
			scanner.close();
			createClient(serverName, port, localPort);
		} else {
			scanner.close();
			createServer(port);
		}
	}

	private static void createClient(String serverName, int port, int localPort) {
		InetAddress IP;
		try {
			IP = InetAddress.getByName(serverName);
			Client client = new Client(800, 500, gameName, IP, port, localPort);
			client.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private static void createServer(int port) {
		Server server = new Server(SCREEN_WIDTH, SCREEN_HEIGHT, port);
		server.start();
	}
}
