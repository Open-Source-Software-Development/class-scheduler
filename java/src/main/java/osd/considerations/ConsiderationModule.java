package osd.considerations;

import dagger.Module;
import dagger.Provides;
import osd.schedule.Hunk;
import osd.schedule.Lookups;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Module(
    includes = BaseConsiderationModule.class
)
public interface ConsiderationModule {

    @Provides
    static BiFunction<Lookups, Hunk, Integer> providesPreferences(
            final Collection<UserPreference> userPreferences, final Collection<BasePreference> basePreferences) {
        return (lookups, hunk) -> {
            final int preferencesScore = userPreferences.stream()
                    .mapToInt(p -> p.evaluate(hunk))
                    .sum();
            final int basePreferencesScore = basePreferences.stream()
                    .map(b -> b.bind(lookups))
                    .mapToInt(p -> p.evaluate(hunk))
                    .sum();
            return preferencesScore + basePreferencesScore;
        };
    }

    @Provides
    static Predicate<Hunk> providesUserConstraints(final Collection<UserConstraint> records) {
        return records.stream()
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
                .reduce(Constraint.DUMMY, Constraint::and);
    }

    @Provides
    static BiPredicate<Lookups, Hunk> providesBaseConstraints(final Collection<BaseConstraint> baseConstraints) {
        return (lookups, hunk) -> baseConstraints.stream()
                .map(baseConstraint -> baseConstraint.bind(lookups))
                .allMatch(predicate -> predicate.test(hunk));
    }

}
