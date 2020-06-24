package pong.dataTransfer;

import java.awt.Point;
import java.util.Vector;

public class OutputData {

	public Vector<Point> positions;
	public byte winner;

	public OutputData() {
		positions = new Vector<Point>(3);
		positions.add(new Point(0, 0));
		positions.add(new Point(0, 0));
		positions.add(new Point(0, 0));
		winner = -1;
	}

	public byte[] toRawData() {
		byte[] dataBuffer = new byte[20];
		int j = 0;
		byte[] numberBuffer;
		for (int i = 0; i < 3; i++) {
			numberBuffer = toByte(positions.get(i).x);
			dataBuffer[j++] = numberBuffer[0];
			dataBuffer[j++] = numberBuffer[1];
			numberBuffer = toByte(positions.get(i).y);
			dataBuffer[j++] = numberBuffer[0];
			dataBuffer[j++] = numberBuffer[1];
		}
		dataBuffer[j++] = winner;
		return dataBuffer;
	}

	public boolean updateFromRawData(byte[] dataBuffer) {
		if (isValid(dataBuffer)) {
			parseData(dataBuffer);
			return true;
		} else
			return false;
	}

	private boolean isValid(byte[] dataBuffer) {
		if (dataBuffer == null)
			return false;
		return true;
	}

	private void parseData(byte[] dataBuffer) {
		int j = 0;
		byte[] numberBuffer = new byte[2];
		for (int i = 0; i < 3; i++) {
			numberBuffer[0] = dataBuffer[j++];
			numberBuffer[1] = dataBuffer[j++];
			positions.get(i).x = toInt(numberBuffer);
			numberBuffer[0] = dataBuffer[j++];
			numberBuffer[1] = dataBuffer[j++];
			positions.get(i).y = toInt(numberBuffer);
		}
		winner = dataBuffer[j++];
	}

	private byte[] toByte(int number) {
		byte[] buffer = new byte[2];
		buffer[0] = (byte) (number / 256 + Byte.MIN_VALUE);
		buffer[1] = (byte) (number % 256 + Byte.MIN_VALUE);
		return buffer;
	}

	private int toInt(byte[] number) {
		int buffer = 0;
		buffer += ((int) number[0] + Byte.MAX_VALUE + 1) * 256;
		buffer += ((int) number[1] + Byte.MAX_VALUE + 1);
		return buffer;
	}
}
