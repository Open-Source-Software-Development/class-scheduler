package schedule;

import osd.input.Section;

import java.util.Comparator;

class Prioritizer implements Comparator<Section> {

    private final Trackers trackers;

    Prioritizer(final Trackers trackers) {
        this.trackers = trackers;
    }

    @Override
    public int compare(final Section o1, final Section o2) {
        return Math.toIntExact(score(o2) - score(o1));
    }

    private long score(final Section section) {
        return trackers.forCourse(section).getOptions()
                .flatMap(p -> trackers.forProfessor(p).getOptions())
                .flatMap(b -> trackers.forBlock(b).getOptions())
                .filter(r -> r.getRoomType() == section.getRoomType())
                .count();
    }

}
