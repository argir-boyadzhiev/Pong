package pong.gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.net.InetAddress;
import java.util.Vector;

import pong.connection.ClientGameSocket;
import pong.dataTransfer.InputData;
import pong.dataTransfer.OutputData;
import pong.display.Display;
import pong.input.KeyManager;
import pong.player.ObjectRenderer;

public class Client {

	private ClientGameSocket cgs;

	private boolean running;

	private byte id;

	private Vector<ObjectRenderer> objects;
	private byte winner;

	private OutputData outputData;
	private InputData inputData;
	
	private String title;
	private Display display;
	private BufferStrategy bs;
	private Graphics g;
	private int width, height;
	private KeyManager keyManager;
	

	public Client(int width, int height, String title, InetAddress IP, int port, int localPort) {
		
		this.width = width;
		this.height = height;
		this.title = title;
		
		
		
		cgs = new ClientGameSocket(IP, port, localPort);
		outputData = new OutputData();
		getPlayerID();
		inputData = new InputData(id);
	}

	private boolean updateDataFromServer() {
		byte[] initialData = cgs.getUDP();
		if(initialData == null) return false;
		while(true) {
			byte[] buffer = cgs.getUDP();
			if(buffer == null) {
				return outputData.updateFromRawData(initialData);
			}else {
				initialData = buffer;
			}
		}
	}

	private void update() {
		updateInputData();
		updateDataToServer();
		if (!updateDataFromServer())
			return;
		
		if (outputData.winner != -1) {
			winner = outputData.winner;
		} else {
			winner = -1;
		}

		for (int i = 0; i < 3; i++) {
			objects.elementAt(i).update(outputData.positions.elementAt(i));
		}

	}

	private void updateInputData() {

		if (keyManager.keys[KeyEvent.VK_R])
			inputData.restart = 1;
		else
			inputData.restart = 0;

		if (keyManager.keys[KeyEvent.VK_UP] != keyManager.keys[KeyEvent.VK_DOWN]) {
			if (keyManager.keys[KeyEvent.VK_UP])
				inputData.verticalMovement = -1;
			if (keyManager.keys[KeyEvent.VK_DOWN])
				inputData.verticalMovement = 1;
		} else {
			inputData.verticalMovement = 0;
		}
	}

	private void updateDataToServer() {
		byte[] buffer = inputData.toRawData();
		buffer[0] = id;
		cgs.sendUDP(buffer);
	}

	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, width, height);
		// DRAW

		if (winner != -1) {
			renderEndScreen(g);
		} else {
			for (int i = 0; i < 3; i++) {
				objects.elementAt(i).render(g);
			}
		}

		// DRAW
		bs.show();
		g.dispose();
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
				render();
				delta--;
			}
		}
	}
	
	private void waitForOtherCLients() {
		while(true) {
			String buffer = cgs.getTCPMessage();
			if(buffer != null) break;
		}
	}

	public void start() {
		
		display = new Display(title, width, height);
		
		keyManager = new KeyManager();
		display.getJFrame().addKeyListener(keyManager);
		
		objects = new Vector<ObjectRenderer>();

		if (id == 0) {
			objects.add(new ObjectRenderer(20, 60, new Color(0, 255, 0)));
			objects.add(new ObjectRenderer(20, 60, new Color(255, 0, 0)));
		}
		if (id == 1) {
			objects.add(new ObjectRenderer(20, 60, new Color(255, 0, 0)));
			objects.add(new ObjectRenderer(20, 60, new Color(0, 255, 0)));
		}

		objects.add(new ObjectRenderer(20, 20, new Color(0, 0, 0)));
		winner = -1;

		running = true;
		
		waitForOtherCLients();
		mainLoop();
	}

	private void renderEndScreen(Graphics g) {

		if (winner == id) {
			g.setColor(new Color(0, 255, 0));
		} else {
			g.setColor(new Color(255, 0, 0));
		}

		g.fillRect(50, 50, width - 100, height - 100);
	}

	private void getPlayerID() {
		id = cgs.getId();
	}
}
