package pong.player;

import java.awt.Point;

public class BallLogic {

	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;

	private Point position;
	private Point direction;

	public BallLogic(Point position, Point direction, int SCREEN_WIDTH, int SCREEN_HEIGHT) {
		this.position = position;
		this.direction = direction;
		this.SCREEN_WIDTH = SCREEN_WIDTH;
		this.SCREEN_HEIGHT = SCREEN_HEIGHT;
	}

	public void update(Point player1, Point player2) {

		if (colidesWith(player1))
			bounceFrom(player1);
		if (colidesWith(player2))
			bounceFrom(player2);

		if (hitsBorder())
			bounceBack();

		move();
	}

	public Point getPosition() {
		return position;
	}

	private void move() {
		position.x += direction.x;
		position.y += direction.y;
	}

	private boolean colidesWith(Point player) {
		Point nextStep = new Point(position.x + direction.x, position.y + direction.y);
		if (Math.abs((player.x + 10) - (nextStep.x + 10)) < 20 && Math.abs((player.y + 30) - (nextStep.y + 10)) < 40)
			return true;
		return false;
	}

	private void bounceFrom(Point player) {
		direction.x *= -1;
		if(player.y+30+10 < position.y+10) direction.y++;
		if(player.y+30-10 > position.y+10) direction.y--;
	}

	private boolean hitsBorder() {
		int nextStep = position.y + direction.y;
		if (nextStep < 0 || nextStep + 20 > SCREEN_HEIGHT)
			return true;
		return false;
	}

	private void bounceBack() {
		direction.y *= -1;
	}

	public boolean scores() {

		int nextStep = position.x + direction.x;
		if (nextStep < 0 || nextStep + 20 > SCREEN_WIDTH)
			return true;
		return false;

	}

	public byte getWinner() {
		if (position.x > SCREEN_WIDTH / 2)
			return 0;
		else
			return 1;
	}
}
