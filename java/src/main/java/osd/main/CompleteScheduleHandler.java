package osd.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import osd.database.*;
import osd.schedule.Hunk;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

class CompleteScheduleHandler implements Consumer<List<Hunk>> {

    private final SessionFactory sessionFactory;

    @Inject
    CompleteScheduleHandler(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public synchronized void accept(final List<Hunk> hunks) {
        Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        final OutputRun run = createRun(session);
        hunks.forEach(hunk -> saveHunk(session, hunk, run));
        transaction.commit();
        session.close();
    }

    private void saveHunk(final Session session, final Hunk hunk, final OutputRun run) {
        final OutputSection section = convertSection(hunk.getSection(), run);
        final Professor professor = hunk.getProfessor();
        final Room room = hunk.getRoom();
        final Set<Block> blocks = hunk.getBlocks();
        final Block block = pickRepresentativeBlock(blocks);

        session.save(section);
        final OutputHunk outputHunk = new OutputHunk();
        outputHunk.setBlockId(block.getId());
        outputHunk.setProfessorId(professor.getId());
        outputHunk.setRoomId(room.getId());
        outputHunk.setSectionId(section.getId());
        session.save(outputHunk);
    }

    private OutputRun createRun(final Session session) {
        final OutputRun run = new OutputRun();
        session.save(run);
        return run;
    }

    private OutputSection convertSection(final Section section, final OutputRun run) {
        final Course course = section.getCourse();
        final int courseId = course.getId();
        final int runId = run.getId();
        final String suffix = section.getSuffix();
        final OutputSection outputSection = new OutputSection();
        outputSection.setCourseId(courseId);
        outputSection.setSuffix(suffix);
        outputSection.setRunId(runId);
        return outputSection;
    }

    private Block pickRepresentativeBlock(final Set<Block> blocks) {
        return blocks.stream()
                .sorted(Comparator.comparing(block -> block.toString().length()))
                .iterator().next();
    }

}
