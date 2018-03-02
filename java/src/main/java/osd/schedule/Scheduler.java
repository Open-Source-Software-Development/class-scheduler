package osd.schedule;

import osd.input.Section;
import osd.output.Hunk;

import java.util.List;

interface Scheduler {

    boolean addHunk(final Hunk hunk);

    void removeHunk(final Section section);

    List<Hunk> getHunks();

}
