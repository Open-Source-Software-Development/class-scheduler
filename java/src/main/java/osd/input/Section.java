package osd.input;

/**
 * Represents a specific section of some course.
 */
public interface Section extends Named {

    Course getCourse();

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

        };
    }

}
