package osd.database.input;

import java.util.Objects;

/**
 * Represents a specific section of some course.
 */
public interface Section {

    Course getCourse();

    String getName();

    String getSuffix();

    static Section of(final Course course, final String suffix) {
        return new Section() {

            @Override
            public Course getCourse() {
                return course;
            }

            public String getName() {
                return course.getName() + "-" + suffix;
            }

            public String getSuffix() {
                return suffix;
            }

            @Override
            public boolean equals(final Object o) {
                if (o == null || o.getClass() != getClass()) {
                    return false;
                }
                final Section other = (Section)o;
                return Objects.equals(course, other.getCourse())
                        && Objects.equals(getSuffix(), other.getSuffix());
            }

            @Override
            public int hashCode() {
                return Objects.hash(getCourse(), getSuffix());
            }

            @Override
            public String toString() {
                return getName();
            }

        };
    }

}
