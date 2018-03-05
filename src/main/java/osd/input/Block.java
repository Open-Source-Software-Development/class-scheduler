package osd.input;

public class Block {
	
	int block;
	int id;
	int day;
	int time;
	
	public Block(int tBlock, int tId, int tDay, int tTime) {
		block = tBlock;
		id = tId;
		day = tDay;
		time = tTime;
	}

	public int getBlock() {
		return block;
	}

	public int getId() {
		return id;
	}

	public int getDay() {
		return day;
	}

	public int getTime() {
		return time;
	}

}
