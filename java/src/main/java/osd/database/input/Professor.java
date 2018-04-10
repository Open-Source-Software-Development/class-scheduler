package osd.database.input;

import osd.database.RecordConversion;
import osd.database.RecordConverter;
import osd.database.input.record.ProfessorRecord;

/**
 * Represents a professor. The most important characteristic of a professor
 * that isn't covered by the preferences system is their course capacity.
 */
public class Professor extends SchedulingElement {

	public Professor(final int id, final String name) {
		super(id, name);
	}

	@RecordConversion
    Professor(final ProfessorRecord record, final RecordConverter recordConverter) {
	    super(record.getId(), record.getFirstName() + " " + record.getLastName());
    }

	/**
     * Gets the maximum number of course sections this professor can teach.
     * Defaults to 4.
     * @return the maximum number of course sections this professor can teach
     */

	public int getCourseCapacity() {
	    // TODO: real implementation
		return 4;
	}

}
