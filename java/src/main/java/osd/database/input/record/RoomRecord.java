package osd.database.input.record;

import osd.database.Identified;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_room")
public class RoomRecord implements Identified {

    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @Column(name = "building")
    private String building;
    
    @Column(name = "division_id")
    private int divisionId;
    
    @Column(name = "subject")
    private String subject;
    
    @Column(name = "room_type_id")
    private int roomTypeId;

    @Column(name = "room_number")
    private int roomNumber;

	@SuppressWarnings("unused")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	public void setId(int id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public String getBuilding() {
		return building;
	}

	@SuppressWarnings("unused")
	public void setBuilding(String building) {
		this.building = building;
	}

	@SuppressWarnings("unused")
	public String getSubject() {
		return subject;
	}

	@SuppressWarnings("unused")
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@SuppressWarnings("unused")
	public int getDivisionId() {
		return divisionId;
	}

	@SuppressWarnings("unused")
	public void setDivisionId(Object divisionId) {
		this.divisionId = Integer.valueOf(divisionId.toString());
	}

	@SuppressWarnings("unused")
	public int getRoomTypeId() {
		return roomTypeId;
	}

	@SuppressWarnings("unused")
	public void setRoomTypeId(Object roomTypeId) {
		this.roomTypeId = Integer.valueOf(roomTypeId.toString());
	}

	@SuppressWarnings("unused")
	public int getRoomNumber() {
        return roomNumber;
    }

	@SuppressWarnings("unused")
    public void setRoomNumber(Object roomNumber) {
        this.roomNumber = Integer.valueOf(roomNumber.toString());
    }

}
