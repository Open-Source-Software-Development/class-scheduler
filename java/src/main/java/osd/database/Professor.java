package osd.database;

/**
 * Represents a professor. The most important characteristic of a professor
 * that isn't covered by the preferences system is their course capacity.
 */
public class Professor extends SchedulingElement {

	Professor(final String name) {
		super(name);
	}

	/**
     * Gets the maximum number of course sections this professor can teach.
     * Defaults to 4.
     * @return the maximum number of course sections this professor can teach
     */

	public int getCourseCapacity() {
		return 4;
	}

}
