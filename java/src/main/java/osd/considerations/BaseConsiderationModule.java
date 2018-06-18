package osd.considerations;

import dagger.Module;
import dagger.Provides;
import osd.util.classpath.Everything;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.stream.Collectors;

@Module
public class BaseConsiderationModule {

    @Provides
    Collection<BaseConstraint> providesRawBaseConstraints(final Everything everything) {
        return everything.extending(BaseConstraint.class)
                .map(BaseConsiderationModule::init)
                .map(BaseConstraint.class::cast)
                .collect(Collectors.toList());
    }

    @Provides
    Collection<BasePreference> providesRawBasePreferences(final Everything everything) {
        return everything.extending(BasePreference.class)
                .map(BaseConsiderationModule::init)
                .map(BasePreference.class::cast)
                .collect(Collectors.toList());
    }

    private static <T> T init(final Class<T> clazz) {
        try {
            final Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (final ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

}
