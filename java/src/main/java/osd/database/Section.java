package osd.database;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Represents a specific section of some course.
 */
public interface Section extends Named {

    Course getCourse();

    default Function<Block, Stream<Block>> getBlockingStrategy() {
        return getCourse().getBlockingStrategy();
    }

    static Section of(final Course course, final String suffix) {
        return new Section() {

            @Override
            public Course getCourse() {
                return course;
            }

            @Override
            public String getName() {
                return course.getName() + "-" + suffix;
            }

            @Override
            public boolean equals(final Object o) {
                if (o == null || o.getClass() != getClass()) {
                    return false;
                }
                final Section other = (Section)o;
                return Objects.equals(course, other.getCourse())
                        && Objects.equals(getName(), other.getName());
            }

            @Override
            public int hashCode() {
                return Objects.hash(getCourse(), getName());
            }

            @Override
            public String toString() {
                return getName();
            }

        };
    }

}
