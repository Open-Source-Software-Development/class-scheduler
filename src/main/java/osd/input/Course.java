package osd.input;

public class Course {
	
	int studentInt;
	String courseId;
	String divId;
	RoomType roomReq;
	int level;
	int sectionCapacity;
	
	public Course(int tStudentInt, String tCourseId, String tDivId, RoomType tRoomReq, int tLevel, int tSecCapacity) {
		studentInt = tStudentInt;
		courseId = tCourseId;
		divId = tDivId;
		roomReq = tRoomReq;
		level = tLevel;
		sectionCapacity = tSecCapacity;
	}

	public int getStudentInt() {
		return studentInt;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getDivId() {
		return divId;
	}

	public RoomType getRoomReq() {
		return roomReq;
	}

	public int getLevel() {
		return level;
	}

	public int getSectionCapacity() {
		return sectionCapacity;
	}
}
