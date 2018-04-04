package osd.database;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
class RecordAccession {

    private final SessionFactory sessionFactory;
    private final GenericForeignKeyResolver genericForeignKeyResolver;

    private final Map<String, Map<Integer, ?>> CACHE = new HashMap<>();

    @Inject
    RecordAccession(final SessionFactory sessionFactory, final GenericForeignKeyResolver genericForeignKeyResolver) {
        this.sessionFactory = sessionFactory;
        this.genericForeignKeyResolver = genericForeignKeyResolver;
    }

    <T> T get(final Class<T> clazz, final int id) {
        return clazz.cast(lazy(clazz).get(id));
    }

    <T> Stream<T> getAllFromDefaultRecord(final Class<T> interfaceType) {
        return lazy(interfaceType).values().stream();
    }

    <T> Stream<T> getAll(final Class<T> interfaceType, final Class<? extends Record<T>> recordType) {
        return lazy(interfaceType, recordType.getSimpleName()).values().stream();
    }

    Object get(final int typeId, final int id) {
        return get(genericForeignKeyResolver.resolve(typeId), id);
    }

    private <T> Map<Integer, T> lazy(final Class<T> clazz) {
        return lazy(clazz, getDefaultRecordType(clazz));
    }

    @SuppressWarnings("unchecked")
    private <T> Map<Integer, T> lazy(final Class<T> clazz, final String recordType) {
        return (Map<Integer, T>)CACHE.computeIfAbsent(recordType, unused -> initCacheEntry(clazz, recordType));
    }

    private String getDefaultRecordType(final Class<?> clazz) {
        return clazz.getSimpleName() + "Record";
    }

    private <T> Map<Integer, T> initCacheEntry(final Class<T> clazz, final String recordType) {
        final Map<Integer, T> result = new HashMap<>();
        for (final Record<T> record : read(clazz, recordType)) {
            final T t = record.create(this);
            result.put(record.getId(), t);
        }
        return result;
    }

    private <T> List<Record<T>> read(final Class<T> interfaceType, final String recordType) {
        return read0(interfaceType, recordType)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private <T> Stream<Record<T>> read0(final Class<T> interfaceType, final String recordType) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            final Query query = session.createQuery( "FROM " + recordType);
            return (Stream<Record<T>>) query.list().stream();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
