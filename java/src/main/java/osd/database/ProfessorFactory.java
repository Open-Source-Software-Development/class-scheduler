package osd.database;

import osd.input.Professor;

import javax.inject.Inject;

public class ProfessorFactory implements DatabaseFactory<ProfessorRecord, Professor> {

	@Inject
	ProfessorFactory(/* Add arguments here, if needed.*/) {
		// Do any setup you need here.
	}

	@Override
	public Professor create(final ProfessorRecord record) {
		// TODO: implement this!
		throw new UnsupportedOperationException("not yet implemented");
	}

}
