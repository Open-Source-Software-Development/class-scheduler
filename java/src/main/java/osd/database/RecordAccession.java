package osd.database;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
class RecordAccession {

    private final SessionFactory sessionFactory;
    private final GenericForeignKeyResolver genericForeignKeyResolver;

    private final Map<Class<?>, Map<Integer, ?>> CACHE = new HashMap<>();

    @Inject
    RecordAccession(final SessionFactory sessionFactory, final GenericForeignKeyResolver genericForeignKeyResolver) {
        this.sessionFactory = sessionFactory;
        this.genericForeignKeyResolver = genericForeignKeyResolver;
    }

    <T> T get(final Class<T> clazz, final int id) {
        return clazz.cast(lazy(clazz).get(id));
    }

    <T> Stream<T> getAll(final Class<T> clazz) {
        return lazy(clazz).values().stream();
    }

    Object get(final int typeId, final int id) {
        return get(genericForeignKeyResolver.resolve(typeId), id);
    }

    @SuppressWarnings("unchecked")
    private <T> Map<Integer, T> lazy(final Class<T> clazz) {
        return (Map<Integer, T>)CACHE.computeIfAbsent(clazz, this::initCacheEntry);
    }

    private <T> Map<Integer, T> initCacheEntry(final Class<T> clazz) {
        final Map<Integer, T> result = new HashMap<>();
        for (final Record<T> record : read(clazz)) {
            final T t = record.create(this);
            result.put(record.getId(), t);
        }
        return result;
    }

    private <T> List<Record<T>> read(final Class<T> interfaceType) {
        return getRecordTypesFor(interfaceType)
                .flatMap(this::read0)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private <T> Stream<Record<T>> read0(final Class<? extends Record<T>> recordType) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            final Query query = session.createQuery( "FROM " + recordType.getSimpleName());
            return (Stream<Record<T>>) query.list().stream();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Stream<Class<? extends Record<T>>> getRecordTypesFor(final Class<T> interfaceType) {
        final List<Class<?>> types = new ArrayList<>();
        new FastClasspathScanner()
                .matchSubclassesOf(Record.class, types::add)
                .scan();
        return types.stream()
                .filter(clazz -> isRecordTypeFor(interfaceType, clazz))
                .map(clazz -> (Class<? extends Record<T>>)clazz);
    }

    private boolean isRecordTypeFor(final Class<?> interfaceType, final Class<?> recordType) {
        try {
            final Method method = recordType.getDeclaredMethod("create", RecordAccession.class);
            final Class<?> returnType = method.getReturnType();
            return interfaceType.isAssignableFrom(returnType);
        } catch (final NoSuchMethodException e) {
            return isRecordTypeFor(interfaceType, recordType.getSuperclass());
        }
    }

}
