package osd.input.placeholder;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import osd.considerations.Constraint;
import osd.considerations.Preference;
import osd.considerations.UserConstraint;
import osd.input.Sources;
import osd.main.FlagModule;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Module(
        includes = FlagModule.class
)
public abstract class PlaceholderModule {

    @Binds
    abstract Sources bindsSources(final PlaceholderSources sources);

    @Provides
    static Collection<Preference> providesPreferences() {
        return Collections.emptyList();
    }

    @Provides
    static Collection<Constraint> providesConstraints(final PlaceholderConfig config) {
        return config.getUserConstraints().get()
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
