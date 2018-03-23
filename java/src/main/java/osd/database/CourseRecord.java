package osd.database;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_course")
class CourseRecord implements Course {

    // TODO: add column fields, getters, and setters here
    // see https://www.tutorialspoint.com/hibernate/hibernate_annotations.htm

    @Override
    public String getName() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Iterable<Section> getSections() {
        // You might want to return a java.util.ArrayList<Section>, for example.
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

}
