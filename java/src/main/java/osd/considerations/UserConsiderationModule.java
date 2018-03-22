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
        // TODO: we still need to break whitelist constraints into subgroups
        return records.stream()
                .map(factory)
                // Break the list up into blacklist and whitelist constraints.
                .collect(Collectors.groupingBy(UserConstraint::isBlacklist))
                .entrySet().stream()
                .flatMap(e -> {
                    // Blacklist constraints.
                    if (e.getKey()) {
                        // Echo the values back unmodified.
                        return e.getValue().stream();
                    // Whitelist constraints.
                    } else {
                        return e.getValue().stream()
                                // Break the list up by primary element.
                                .collect(Collectors.groupingBy(UserConstraint::getWhitelistKey))
                                .values().stream()
                                // "OR" each sublist.
                                .map(l -> l.stream()
                                        // Widening cast to make the next operation happy.
                                        .map(c -> (Constraint)c)
                                        .reduce(Constraint.DUMMY, Constraint::or));
                    }
                }).collect(Collectors.toList());
    }

}
