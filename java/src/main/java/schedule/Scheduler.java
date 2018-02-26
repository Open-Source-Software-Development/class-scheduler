package schedule;

import osd.considerations.Constraint;
import osd.considerations.Preference;
import osd.input.*;
import osd.output.Hunk;

import java.util.*;

class Scheduler {

    private final Trackers trackers;
    private final Constraint constraints;
    private final Preference preferences;

    private final Map<Section, Hunk> hunks = new HashMap<>();

    Scheduler(final Trackers trackers, final Constraint constraints, final Preference preferences) {
        this.trackers = trackers;
        this.constraints = constraints;
        this.preferences = preferences;
    }

    int schedule(final Hunk hunk) {
        schedule0(hunk);
        if (!constraints.evaluate(hunk)) {
            return -1;
        }
        return preferences.evaluate(hunk);
    }

    void remove(final Section section) {
        remove0(section);
    }

    List<Hunk> getHunks() {
        return new ArrayList<>(hunks.values());
    }

    private void schedule0(final Hunk hunk) {
        final Section section = hunk.getSection();
        final Professor professor = hunk.getProfessor();
        final Room room = hunk.getRoom();
        final Block block = hunk.getBlock();
        assert section != null;
        assert professor != null;
        assert room != null;
        assert block != null;

        if (hunks.containsKey(section)) {
            throw new IllegalArgumentException("tried to add hunk for " + section + ", which is already scheduled");
        }

        trackers.changeCourseLoad(professor, 1);
        trackers.forProfessor(professor).consumeOption(block);
        trackers.forBlock(block).consumeOption(room);
        hunks.put(section, hunk);
    }

    private void remove0(final Section section) {
        final Hunk hunk = Objects.requireNonNull(hunks.get(section));
        assert section.equals(hunk.getSection());
        final Professor professor = hunk.getProfessor();
        final Room room = hunk.getRoom();
        final Block block = hunk.getBlock();

        hunks.remove(section);
        trackers.forBlock(block).addOption(room);
        trackers.forProfessor(professor).addOption(block);
        trackers.changeCourseLoad(professor, -1);
    }

}
