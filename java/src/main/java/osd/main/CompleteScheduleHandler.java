package osd.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import osd.database.input.*;
import osd.database.output.HunkRecord;
import osd.database.output.RunRecord;
import osd.database.output.SectionRecord;
import osd.schedule.Hunk;
import osd.schedule.Results;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;

class CompleteScheduleHandler implements Consumer<Results> {

    private final SessionFactory sessionFactory;

    @Inject
    CompleteScheduleHandler(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public synchronized void accept(final Results results) {
        Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        final RunRecord run = createRun(session);
        results.getHunks().forEach(hunk -> saveHunk(session, hunk, run));
        transaction.commit();
        session.close();
    }

    private void saveHunk(final Session session, final Hunk hunk, final RunRecord run) {
        final SectionRecord section = convertSection(hunk.getSection(), run);
        final Professor professor = hunk.getProfessor();
        final Room room = hunk.getRoom();
        final Set<Block> blocks = hunk.getBlocks();
        final Block block = pickRepresentativeBlock(blocks);

        session.save(section);
        final HunkRecord hunkRecord = new HunkRecord();
        hunkRecord.setBlockId(block.getId());
        hunkRecord.setProfessorId(professor.getId());
        hunkRecord.setRoomId(room.getId());
        hunkRecord.setSectionId(section.getId());
        session.save(hunkRecord);
    }

    private RunRecord createRun(final Session session) {
        final RunRecord run = new RunRecord();
        session.save(run);
        return run;
    }

    private SectionRecord convertSection(final Section section, final RunRecord run) {
        final Course course = section.getCourse();
        final int courseId = course.getId();
        final int runId = run.getId();
        final String suffix = section.getSuffix();
        final SectionRecord sectionRecord = new SectionRecord();
        sectionRecord.setCourseId(courseId);
        sectionRecord.setSuffix(suffix);
        sectionRecord.setRunId(runId);
        return sectionRecord;
    }

    private Block pickRepresentativeBlock(final Set<Block> blocks) {
        return blocks.stream()
                .sorted(Comparator.comparing(block -> block.toString().length()))
                .iterator().next();
    }

}
