package osd.input;

public interface Professor extends Named {

	default int getCourseCapacity() {
		return 4;
	}

}
