package pong.dataTransfer;

public class InputData {

	private byte id;
	public byte verticalMovement;
	public byte restart;

	public InputData(byte id) {
		this.id = id;
		verticalMovement = 0;
		restart = 0;
	}

	public byte[] toRawData() {
		byte[] dataBuffer = new byte[3];
		int j = 0;
		dataBuffer[j++] = id;
		dataBuffer[j++] = verticalMovement;
		dataBuffer[j++] = restart;
		return dataBuffer;
	}

	public boolean updateFromRawData(byte[] dataBuffer) {
		if (isValid(dataBuffer)) {
			verticalMovement = dataBuffer[1];
			restart = dataBuffer[2];
			return true;
		} else
			return false;
	}

	private boolean isValid(byte[] dataBuffer) {
		if (dataBuffer == null)
			return false;
		return true;
	}
}
