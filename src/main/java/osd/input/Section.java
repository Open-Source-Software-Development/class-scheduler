package osd.input;

public class Section{

	String courseId;
	String sectionId;
	int capacity;
	Professor secProfessor;
	
	public Section(String tCourseId, String tSectionId, int tCapacity, Professor tProfessor) {
		courseId = tCourseId;
		sectionId = tSectionId;
		capacity = tCapacity;
		secProfessor = tProfessor;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getSectionId() {
		return sectionId;
	}

	public int getCapacity() {
		return capacity;
	}

	public Professor getSecProfessor() {
		return secProfessor;
	}

}
