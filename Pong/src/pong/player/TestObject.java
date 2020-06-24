package pong.player;

import java.awt.Color;
import java.awt.Graphics;

public class TestObject {
	private int x;
	private int y;
	
	public TestObject() {
		x=0;
		y=0;
	}
	
	public void update() {
		if(x<500) x++;
		else x=0;
		if(y<200) y++;
		else y=0;
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0,0,0));
		g.fillRect(x, y, 50, 50);
	}
}
