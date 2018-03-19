package osd.database;

import dagger.Module;
import dagger.Provides;

import java.util.Collection;

@Module
public class DatabaseModule {

    public DatabaseModule(/* Add arguments here, if needed. */) {
        // ...
    }

    @Provides
    Collection<BlockRecord> providesBlockRecords() {
        // TODO: implement this
        throw new UnsupportedOperationException();
    }

    @Provides
    Collection<ProfessorRecord> providesProfessorRecords() {
        // TODO: implement this
        throw new UnsupportedOperationException();
    }

    @Provides
    Collection<RoomRecord> providesRoomRecords() {
        // TODO: implement this
        throw new UnsupportedOperationException();
    }

    @Provides
    Collection<CourseRecord> providesCourseRecords() {
        // TODO: implement this
        throw new UnsupportedOperationException();
    }

    @Provides
    Collection<UserConstraintRecord> providesUserConstraintRecords() {
        // TODO: implement this
        throw new UnsupportedOperationException();
    }

    @Provides
    Collection<UserPreferenceRecord> providesUserPreferenceRecords() {
        // TODO: implement this
        throw new UnsupportedOperationException();
    }

}
