package osd.input;

import osd.database.ProfessorRecord;
import javax.inject.Inject;

public class ProfessorFactory implements DatabaseFactory<ProfessorRecord, Professor> {

	// feel free to expand this constructor if needed
	// just leave the @Inject
	// (remove this comment once you've read it)
	@Inject
	ProfessorFactory() {}

	@Override
	public Professor create(final ProfessorRecord record) {
		// TODO: implement this!
		throw new UnsupportedOperationException("not yet implemented");
	}

}
