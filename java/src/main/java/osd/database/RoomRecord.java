package osd.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_room")
class RoomRecord extends Record<Room> {

    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @Column(name = "building")
    private String building;
    
    @Column(name = "subject")
    private String subject;
    
    @Column(name = "style")
    private String style;
    
    @Column(name = "division_id")
    private int divisionId;
    
    @Column(name = "room_type_id")
    private int roomTypeId;

    @Column(name = "room_number")
    private int roomNumber;

    @Override
    public String getName() {
        return building + "-" + roomNumber;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Object divisionId) {
		this.divisionId = Integer.valueOf(divisionId.toString());
	}

	public int getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(Object roomTypeId) {
		this.roomTypeId = Integer.valueOf(roomTypeId.toString());
	}

	public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Object roomNumber) {
        this.roomNumber = Integer.valueOf(roomNumber.toString());
    }

    @Override
    Room create(final RecordLookup lookup) {
        return new Room(getName(), lookup.get(RoomType.class, roomTypeId));
    }
}
