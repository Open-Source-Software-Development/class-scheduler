package schedule;

import osd.input.*;
import osd.output.Hunk;
import util.Tuple;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

class Trackers {

    private final Map<Course, CourseTracker> courses = new HashMap<>();
    private final Map<Professor, ProfessorTracker> professors = new HashMap<>();
    private final Map<Block, Tracker<Room>> blocks = new HashMap<>();

    @Inject
    Trackers(final Collection<Course> courseData, final Collection<Professor> professorData,
             final Collection<Block> blockData, final Collection<Room> roomData) {
        courseData.forEach(c -> courses.put(c, new CourseTracker()));
        professorData.forEach(p ->
                p.getQualifications().forEach(c -> courses.get(c).addOption(p))
        );

        professorData.forEach(p -> professors.put(p, new ProfessorTracker(p)));
        professorData.forEach(p ->
            p.getPossibleBlocks().forEach(b -> professors.get(p).addOption(b))
        );

        blockData.forEach(b -> {
            final Tracker<Room> tracker = new Tracker<>();
            roomData.forEach(tracker::addOption);
            blocks.put(b, tracker);
        });
    }

    Tracker<Professor> forCourse(final Course course) {
        return courses.get(course);
    }

    Tracker<Block> forProfessor(final Professor professor) {
        return professors.get(professor);
    }

    Tracker<Room> forBlock(final Block block) {
        return blocks.get(block);
    }

    void changeCourseLoad(final Professor professor, final int delta) {
        professors.get(professor).remainingCapacity -= delta;
    }

    Iterable<Hunk> candidates(final Section section) {
        final List<Hunk> result = new ArrayList<>();
        getCandidates(section).forEach(result::add);
        Collections.shuffle(result);
        return result;
    }

    private Stream<Hunk> getCandidates(final Section section) {
        return forCourse(section.getCourse()).getOptions()
                .flatMap(p -> forProfessor(p).getOptions().map(b -> new Tuple<>(b, p)))
                .flatMap(t -> forBlock(t.getLeft()).getOptions().map(t::cons))
                .map(t -> new Hunk(section, t.getRight().getRight(), t.getLeft(), t.getRight().getLeft()));
    }

    private class CourseTracker extends Tracker<Professor> {

        @Override
        Stream<Professor> getOptions() {
            return super.getOptions().filter(p -> professors.get(p).remainingCapacity > 0);
        }

    }

    private class ProfessorTracker extends Tracker<Block> {

        private int remainingCapacity;

        ProfessorTracker(final Professor professor) {
            remainingCapacity = professor.getMaxCourseLoad();
        }

    }

}
