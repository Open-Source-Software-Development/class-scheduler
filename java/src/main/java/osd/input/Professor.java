package osd.input;

/**
 * Represents a professor. The most important characteristic of a professor
 * that isn't covered by the preferences system is their course capacity.
 */
public interface Professor extends Named {

    /**
     * Gets the maximum number of course sections this professor can teach.
     * Defaults to 4.
     * @return the maximum number of course sections this professor can teach
     */
	default int getCourseCapacity() {
		return 4;
	}

}
