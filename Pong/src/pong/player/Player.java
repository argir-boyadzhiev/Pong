package pong.player;

import pong.dataTransfer.InputData;

public class Player {
	public InputData inputData;
	public PlayerLogic playerLogic;

	public Player(InputData inputData, PlayerLogic playerLogic) {
		this.inputData = inputData;
		this.playerLogic = playerLogic;
	}

	public void update() {
		playerLogic.update(inputData.verticalMovement);
	}
}
