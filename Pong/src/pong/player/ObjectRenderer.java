package pong.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class ObjectRenderer {

	private int width = 20;
	private int height = 60;
	private Color color;

	private Point position;

	public ObjectRenderer() {
		this.position = new Point();
	}

	public ObjectRenderer(int width, int height, Color color) {
		this.width = width;
		this.height = height;
		this.color = color;
		position = new Point(0, 0);
	}

	public void update(Point newPosition) {
		position.y = newPosition.y;
		position.x = newPosition.x;
	}

	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(position.x, position.y, width, height);
	}
}
