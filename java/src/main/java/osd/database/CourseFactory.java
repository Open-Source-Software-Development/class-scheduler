package osd.database;

import osd.input.Course;

import javax.inject.Inject;

public class CourseFactory implements DatabaseFactory<CourseRecord, Course> {

	@Inject
    CourseFactory(/* Add arguments here, if needed.*/) {
		// Do any setup you need here.
	}

	@Override
	public Course create(final CourseRecord record) {
		// TODO: implement this!
		throw new UnsupportedOperationException("not yet implemented");
	}

}
