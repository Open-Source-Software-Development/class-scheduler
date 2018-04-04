package osd.database;

import javax.inject.Inject;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Sources {

    private final RecordAccession recordAccession;

    @Inject
    Sources(final RecordAccession recordAccession) {
        this.recordAccession = recordAccession;
    }

    public Stream<Section> getSections() {
        return recordAccession.getAllFromDefaultRecord(Course.class)
                .map(Course::getSections)
                .map(Iterable::spliterator)
                .flatMap(spliterator -> StreamSupport.stream(spliterator, false));
    }

    public Stream<Professor> getProfessors() {
        return recordAccession.getAllFromDefaultRecord(Professor.class);
    }

    public Stream<Block> getBlocks() {
        return recordAccession.getAllFromDefaultRecord(Block.class);
    }

    public Stream<Room> getRooms() {
        return recordAccession.getAllFromDefaultRecord(Room.class);
    }


}
