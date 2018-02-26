package osd.input;

public interface Course extends Named {

	/**
	 * Returns all the sections for this course. This should consider both
	 * "pregenerated" sections specified in the database, and "additional"
	 * sections specified in the course's "sections" column.
	 */
	Iterable<Section> getSections();

}
