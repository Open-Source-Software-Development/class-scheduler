package osd.input;

public class Professor{

	static int COURSE_LIMIT = 4;
	int id;
	String name;
	Course[] courses;
	int scheduledSections;
	boolean isAdjunct;
	//TODO preferences
	
	public Professor(int tId, String tName, Course[] tCourses, boolean tIsAdjunct) {
		id = tId;
		name = tName;
		courses = tCourses;
		scheduledSections = 0;
		isAdjunct = tIsAdjunct;
	}
	
	int getCourseCapacity() {
		return COURSE_LIMIT;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Course[] getCourses() {
		return courses;
	}

	public int getScheduledSections() {
		return scheduledSections;
	}

	public boolean isAdjunct() {
		return isAdjunct;
	}

}
