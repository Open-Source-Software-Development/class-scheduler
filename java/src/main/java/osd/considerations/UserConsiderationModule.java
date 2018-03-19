package osd.considerations;

import dagger.Module;
import dagger.Provides;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import osd.database.UserConstraintFactory;
import osd.database.UserConstraintRecord;
import osd.database.UserPreferenceFactory;
import osd.database.UserPreferenceRecord;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Module(
        // TODO: uncomment this when that class is ready
        //includes = DatabaseModule.class
)
public class UserConsiderationModule {

    @Provides
    Collection<Preference> providesUserPreferences(final Collection<UserPreferenceRecord> records,
                                                   final UserPreferenceFactory factory) {
        return records.stream().map(factory).collect(Collectors.toList());
    }

    @Provides
    Collection<Constraint> providesUserConstraints(final Collection<UserConstraintRecord> records,
                                                   final UserConstraintFactory factory) {
        return records.stream().map(factory).collect(Collectors.toList());
    }

}
