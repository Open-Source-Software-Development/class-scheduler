package demo.data;

import osd.input.Block;
import osd.input.Course;
import osd.input.Professor;

import java.util.*;

public enum DemoProfessor implements Professor, DemoNamed {

    ALICE(Arrays.asList(DemoCourse.CSI_101, DemoCourse.CSI_102),
            Arrays.asList(DemoBlock.values())),
    BOB(Arrays.asList(DemoCourse.CSI_101, DemoCourse.CSI_102, DemoCourse.CSI_310),
            Arrays.asList(DemoBlock.values())),
    CHARLIE(Arrays.asList(DemoCourse.CSI_101, DemoCourse.CSI_102, DemoCourse.CSI_201, DemoCourse.CSI_202),
            Arrays.asList(DemoBlock.values())),
    DAVE(Arrays.asList(DemoCourse.CSI_310, DemoCourse.CSI_404),
            Arrays.asList(DemoBlock.values())),
    EVE(Arrays.asList(DemoCourse.CSI_666, DemoCourse.CSI_404, DemoCourse.CSI_202),
            Arrays.asList(DemoBlock.values())),
    ;

    private final Set<Course> qualifications;
    private final Set<Block> blocks;

    DemoProfessor(final Collection<Course> qualifications, final Collection<Block> blocks) {
        this.qualifications = Collections.unmodifiableSet(new HashSet<>(qualifications));
        this.blocks = Collections.unmodifiableSet(new HashSet<>(blocks));
    }

    /*
    @Override
    public Set<Course> getQualifications() {
        return qualifications;
    }

    @Override
    public Set<Block> getPossibleBlocks() {
        return blocks;
    }
    */
}
