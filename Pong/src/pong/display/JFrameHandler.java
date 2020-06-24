package pong.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import pong.input.KeyManager;

public class JFrameHandler {
	private Display display;
	private int width, height;
	private BufferStrategy bs;
	private Graphics g;
	private KeyManager keyManager;

	public JFrameHandler(String title, int width, int height) {
		display = new Display(title, width, height);
		keyManager = new KeyManager();
		display.getJFrame().addKeyListener(keyManager);
	}

	public Graphics getNewGraphicsObject() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return null;
		}
		g = bs.getDrawGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, width, height);
		return g;
	}

	public void print() {
		bs.show();
	}

	public void resetGraphics() {
		g.dispose();
	}

	public int getScreenWidth() {
		return width;
	}

	public int getScreenHeight() {
		return height;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}
}
