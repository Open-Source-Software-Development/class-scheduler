package osd.input;

import java.util.Collections;

/**
 * Represents a specific section of some course.
 */
public interface Section extends Course {

    @Override
    default Iterable<Section> getSections() {
        return Collections.singleton(this);
    }

}
