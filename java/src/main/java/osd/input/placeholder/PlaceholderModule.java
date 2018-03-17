package osd.input.placeholder;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import osd.considerations.Constraint;
import osd.considerations.Preference;
import osd.input.Sources;
import osd.main.FlagModule;

import java.util.Collection;
import java.util.Collections;

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
    static Collection<Constraint> providesConstraints() {
        return Collections.emptyList();
    }
}
