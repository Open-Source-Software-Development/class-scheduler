package osd.input;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import osd.database.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Module(
        includes = {
                InputModule.SourcesModule.class,
                // TODO: uncomment this when that class is ready
                //DatabaseModule.class
        }
)
public class InputModule {

    @Provides
    Collection<Section> providesSections(final Collection<CourseRecord> records, final CourseFactory factory) {
        final List<Section> result = new ArrayList<>();
        records.forEach(c ->
                factory.apply(c)
                        .getSections()
                        .forEach(result::add));
        return result;
    }

    @Provides
    Collection<Professor> providesProfessors(final Collection<ProfessorRecord> records, final ProfessorFactory factory) {
        return records.stream().map(factory).collect(Collectors.toList());
    }

    @Provides
    Collection<Block> providesBlocks(final Collection<BlockRecord> records, final BlockFactory factory) {
        return records.stream().map(factory).collect(Collectors.toList());
    }

    @Provides
    Collection<Room> providesRooms(final Collection<RoomRecord> records, final RoomFactory factory) {
        return records.stream().map(factory).collect(Collectors.toList());
    }

    /**
     * Nested interface trick to use @Binds alongside @Provides.
     */
    @Module
    public interface SourcesModule {

        @Binds
        Sources sourcesImpl(SourcesImpl sources);

    }

}
