package osd.input;

public class BlockSet {

	Block blockA;
	Block blockB;
	int id;
	Boolean isWhole;
	
	public BlockSet(Block a, Block b, int tId) {
		blockA = a;
		blockB = b;
		id = tId;
		isWhole = true;
	}

	public Boolean getIsWhole() {
		return isWhole;
	}

	public void setIsWhole(Boolean isWhole) {
		this.isWhole = isWhole;
	}

	public Block getBlockA() {
		return blockA;
	}

	public Block getBlockB() {
		return blockB;
	}

	public int getId() {
		return id;
	}
}
