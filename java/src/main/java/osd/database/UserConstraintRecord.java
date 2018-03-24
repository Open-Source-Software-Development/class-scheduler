package osd.database;

import osd.considerations.UserConstraint;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_userpreference")
public class UserConstraintRecord {

	// TODO: add column fields, getters, and setters here
	// see https://www.tutorialspoint.com/hibernate/hibernate_annotations.htm

    public UserConstraint toUserConstraint() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

}
