package osd.database.input.record;

import osd.database.Identified;

import javax.persistence.*;

@Entity
@Table(name = "scheduler_block")
public class BlockRecord implements Identified {

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

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(int id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getBlockId() {
        return blockId;
    }

    @SuppressWarnings("unused")
    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    @SuppressWarnings("unused")
    public String getDay() {
        return day;
    }

    @SuppressWarnings("unused")
    public void setDay(String day) {
        this.day = day;
    }

    @SuppressWarnings("unused")
    public String getStartTime() {
        return startTime;
    }

    @SuppressWarnings("unused")
    public void setStartTime(Object startTime) {
        this.startTime = startTime.toString();
    }

    @SuppressWarnings("unused")
    public String getEndTime() {
        return endTime;
    }

    @SuppressWarnings("unused")
    public void setEndTime(Object endTime) {
        this.endTime = endTime.toString();
    }

}
