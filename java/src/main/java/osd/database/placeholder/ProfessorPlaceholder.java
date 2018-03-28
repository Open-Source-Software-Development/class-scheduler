package osd.database.placeholder;

import osd.database.Professor;

class ProfessorPlaceholder extends NamedPlaceholder implements Professor {

    ProfessorPlaceholder(final String[] row) {
        super(row);
    }

}
