package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name = "scheduler_block")
public class BlockRecord implements Record {

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

}
