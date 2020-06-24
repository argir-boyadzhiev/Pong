package pong.player;

import java.awt.Point;

public class PlayerLogic {

	private int SCREEN_HEIGHT;

	private int speed;

	private Point position;

	public PlayerLogic(Point position, int SCREEN_WIDTH, int SCREEN_HEIGHT) {
		this.position = position;
		this.SCREEN_HEIGHT = SCREEN_HEIGHT;

		speed = 5;
	}

	public void update(int verticalMovement) {
		move(verticalMovement);
		if (outOfBoundaries())
			getBackInBoundaries();
	}

	private boolean outOfBoundaries() {
		if (position.y < 0 || position.y + 60 > SCREEN_HEIGHT)
			return true;
		return false;
	}

	private void getBackInBoundaries() {
		if (position.y < 0)
			position.y = 1;
		if (position.y + 60 > SCREEN_HEIGHT)
			position.y = SCREEN_HEIGHT - 61;
	}

	private void move(int verticalMovement) {
		position.y += verticalMovement * speed;
	}

	public Point getPosition() {
		return position;
	}
}
