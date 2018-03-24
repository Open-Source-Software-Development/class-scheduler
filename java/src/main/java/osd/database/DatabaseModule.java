package osd.database;

import dagger.Module;
import dagger.Provides;
import osd.considerations.UserConstraint;
import osd.considerations.UserPreference;

import java.util.Collection;

@Module
public class DatabaseModule {

    public DatabaseModule(/* Add arguments here, if needed. */) {
        // You might want to use FlagModule as an argument.
        // It gives you an API for finding the database hostname,
        // username, etc...
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
    Collection<UserConstraint> providesUserConstraintRecords() {
        // TODO: implement this
        // See UserConstraintRecord
        throw new UnsupportedOperationException();
    }

    @Provides
    Collection<UserPreference> providesUserPreferenceRecords() {
        // TODO: implement this
        // See UserConstraintRecord
        throw new UnsupportedOperationException();
    }

}
