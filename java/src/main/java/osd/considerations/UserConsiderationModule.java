package osd.considerations;

import dagger.Module;
import dagger.Provides;
import osd.database.UserConstraintRecord;
import osd.database.UserPreferenceRecord;

import java.util.Collection;
import java.util.stream.Collectors;

@Module
public interface UserConsiderationModule {

    @Provides
    static Collection<Preference> providesUserPreferences(final Collection<UserPreferenceRecord> records) {
        return records.stream()
                .map(UserPreferenceRecord::toUserPreference)
                .collect(Collectors.toList());
    }

    @Provides
    static Collection<Constraint> providesUserConstraints(final Collection<UserConstraintRecord> records) {
        return records.stream()
                .map(UserConstraintRecord::toUserConstraint)
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
                    }})
                .collect(Collectors.toList());
    }

}
