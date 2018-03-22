package osd.considerations;

import dagger.Module;
import dagger.Provides;
import osd.database.UserConstraintFactory;
import osd.database.UserConstraintRecord;
import osd.database.UserPreferenceFactory;
import osd.database.UserPreferenceRecord;

import java.util.Collection;
import java.util.stream.Collectors;

@Module(
        // TODO: uncomment this when that class is ready
        //includes = DatabaseModule.class
)
public interface UserConsiderationModule {

    @Provides
    static Collection<Preference> providesUserPreferences(final Collection<UserPreferenceRecord> records,
                                                          final UserPreferenceFactory factory) {
        return records.stream()
                .map(factory)
                .collect(Collectors.toList());
    }

    @Provides
    static Collection<Constraint> providesUserConstraints(final Collection<UserConstraintRecord> records,
                                                          final UserConstraintFactory factory) {
        return records.stream()
                .map(factory)
                // Break the list up into blacklist and whitelist constraints.
                .collect(Collectors.groupingBy(UserConstraint::getWhitelistKey))
                .entrySet().stream()
                .map(e -> {
                    // Blacklist constraints.
                    if (e.getKey().left() == null) {
                        // Echo the values back unmodified.
                        return e.getValue().stream()
                                // Widening cast to make the next operation happy.
                                .map(c -> (Constraint)c)
                                .reduce(Constraint.DUMMY, Constraint::and);
                    // Whitelist constraints.
                    } else {
                        return e.getValue().stream()
                                // Widening cast to make the next operation happy.
                                .map(c -> (Constraint)c)
                                .reduce(Constraint.DUMMY, Constraint::or);
                    }
                }).collect(Collectors.toList());
    }

}
