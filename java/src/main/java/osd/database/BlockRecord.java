package osd.database;

import javax.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "scheduler_block")
class BlockRecord implements Block {

	@Id @GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;

	@Column(name = "block_id")
	private String blockId;
	
	@Column(name = "day")
	private String day;
	
	@Column(name = "start_time")
	private Time startTime;
	
	@Column(name = "end_time")
	private Time endTime;

    @Override
    public Block getPrevious() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Block getPairedWith() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

	@Override
	public Block getNext() {
		// TODO Auto-generated method stub
		return null;
	}
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
}
