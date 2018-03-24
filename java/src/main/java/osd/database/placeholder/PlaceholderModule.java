package osd.database.placeholder;

import dagger.Module;
import dagger.Provides;
import osd.database.*;
import osd.main.FlagModule;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * ULTRA PLACEHOLDER. Everything in this package is going away as soon as
 * Steve's Hibernate code is ready.
 */
@Module(
        includes = FlagModule.class
)
public abstract class PlaceholderModule {

    @Provides
    static Collection<Block> providesBlocks(final PlaceholderSources sources) {
        return sources.getBlocks().collect(Collectors.toList());
    }

    @Provides
    static Collection<Room> providesRooms(final PlaceholderSources sources) {
        return sources.getRooms().collect(Collectors.toList());
    }

    @Provides
    static Collection<Section> providesSections(final PlaceholderSources sources) {
        return sources.getSections().collect(Collectors.toList());
    }

    @Provides
    static Collection<Professor> providesProfessors(final PlaceholderSources sources) {
        return sources.getProfessors().collect(Collectors.toList());
    }

    @Provides
    static Collection<UserPreferenceRecord> providesUserPreferenceRecords() {
        return Collections.emptyList();
    }

    @Provides
    static Collection<UserConstraintRecord> providesUserConstraintRecords(final PlaceholderConfig config) {
        return config.getUserConstraints().collect(Collectors.toList());
    }
}
