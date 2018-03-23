package osd.database;

import javax.persistence.*;

@Entity
@Table(name = "scheduler_professor")
class ProfessorRecord implements Professor {

    // TODO: add column fields, getters, and setters here
    // see https://www.tutorialspoint.com/hibernate/hibernate_annotations.htm

    @Override
    public String getName() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

}
