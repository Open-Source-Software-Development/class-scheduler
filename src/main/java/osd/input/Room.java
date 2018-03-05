package osd.input;

public class Room{
	
	String building;
	String division;
	int id;
	int capacity;
	RoomType type;
	
	public Room(String tBuilding, int tId, int tCapacity, RoomType tType, String tDivision) {
		building = tBuilding;
		id = tId;
		capacity = tCapacity;
		type = tType;
		division = tDivision;
	}

	public String getBuilding() {
		return building;
	}

	public String getDivision() {
		return division;
	}

	public int getId() {
		return id;
	}

	public int getCapacity() {
		return capacity;
	}

	public RoomType getType() {
		return type;
	}

}