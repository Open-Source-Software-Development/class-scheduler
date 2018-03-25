package osd.database;

import osd.considerations.UserPreference;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_userpreference")
public class UserPreferenceRecord {

	// TODO: add column fields, getters, and setters here
	// see https://www.tutorialspoint.com/hibernate/hibernate_annotations.htm

    public UserPreference toUserPreference() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

}
