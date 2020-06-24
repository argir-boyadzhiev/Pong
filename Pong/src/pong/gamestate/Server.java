package pong.gamestate;

import java.awt.Point;

import pong.connection.ServerGameSocket;
import pong.dataTransfer.InputData;
import pong.dataTransfer.OutputData;
import pong.player.BallLogic;
import pong.player.Player;
import pong.player.PlayerLogic;

public class Server {

	private int SCREEN_WIDTH, SCREEN_HEIGHT;
	private boolean running;

	private Player player1, player2;
	private BallLogic ball;

	private ServerGameSocket sgs;
	private OutputData outputData;

	public Server(int width, int height, int port) {
		player1 = new Player(new InputData((byte) 0), new PlayerLogic(new Point(0, 0), SCREEN_WIDTH, SCREEN_HEIGHT));
		player2 = new Player(new InputData((byte) 1), new PlayerLogic(new Point(0, 0), SCREEN_WIDTH, SCREEN_HEIGHT));
		SCREEN_WIDTH = width;
		SCREEN_HEIGHT = height;
		sgs = new ServerGameSocket(port, 2);
	}

	private void update() {
		updateInputsFromClients();

		if (ball.scores()) {
			outputData.winner = ball.getWinner();
			if (player1.inputData.restart == 1 || player2.inputData.restart == 1) {
				reset();
			}
		} else {

			ball.update(player1.playerLogic.getPosition(), player2.playerLogic.getPosition());

			player1.update();
			player2.update();
		}

		updateOutputData();
		updateDataToClient();
	}

	private void updateDataToClient() {
		sgs.sendUDPToAll(outputData.toRawData());
	}

	private void updateInputsFromClients() {
		sgs.updateDatagramMessages();

		byte[] initialData = sgs.getUDP(0);
		if (initialData != null) {
			while (true) {
				byte[] buffer = sgs.getUDP(0);
				if (buffer == null) {
					player1.inputData.updateFromRawData(initialData);
					break;
				} else {
					initialData = buffer;
				}
			}
		}

		initialData = sgs.getUDP(1);
		if (initialData != null) {
			while (true) {
				byte[] buffer = sgs.getUDP(1);
				if (buffer == null) {
					player2.inputData.updateFromRawData(initialData);
					break;
				} else {
					initialData = buffer;
				}
			}
		}
	}

	private void reset() {
		outputData.winner = -1;

		player1.playerLogic = new PlayerLogic(new Point(10, 10), SCREEN_WIDTH, SCREEN_HEIGHT);
		player2.playerLogic = new PlayerLogic(new Point(SCREEN_WIDTH - 50, 10), SCREEN_WIDTH, SCREEN_HEIGHT);

		ball = new BallLogic(new Point(200, 200), new Point(3, 2), SCREEN_WIDTH, SCREEN_HEIGHT);

		running = true;

	}

	private void updateOutputData() {
		outputData.positions.get(0).setLocation(player1.playerLogic.getPosition());
		outputData.positions.get(1).setLocation(player2.playerLogic.getPosition());
		outputData.positions.get(2).setLocation(ball.getPosition());
	}

	private void mainLoop() {
		int fps = 60;
		double timePerTick = 1_000_000_000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			lastTime = now;

			if (delta >= 1) {
				update();
				delta--;
			}
		}
	}
	
	private void startClients() {
		sgs.sendTCPToAll("GO!");
	}

	public void start() {
		outputData = new OutputData();
		reset();
		startClients();
		mainLoop();
	}
}
