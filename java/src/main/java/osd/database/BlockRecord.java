package osd.database;

import javax.persistence.*;

@Entity
@Table(name = "scheduler_block")
class BlockRecord extends Record<Block> {

	@Id @GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "block_id")
	private String blockId;
	
	@Column(name = "day")
	private String day;
	
	@Column(name = "start_time")
	private String startTime;
	
	@Column(name = "end_time")
	private String endTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(Object startTime) {
        this.startTime = startTime.toString();
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(Object endTime) {
    	this.endTime = endTime.toString();
	}

    @Override
    Block create(final RecordAccession lookup) {
        return new Block(id, blockId, day, Integer.valueOf(startTime.split(":")[0]), lookup);
    }

    @Override
    String getName() {
        return blockId;
    }
}
