package osd.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

/**
 * Indicates that an input element can be constructed from a record type. The
 * annotated constructor should take exactly two arguments. The first is an
 * instance of the record type in question. The second is an {@link RecordAccession}
 * instance that may be used to assist the conversion. Such a constructor must
 * not throw any checked exceptions.
 * @see #filter()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface RecordConversion {

    /**
     * Implements "partial functions" among conversions. The indicated class
     * should be a {@linkplain Predicate predicate} of whatever the annotated constructor's first
     * argument is. Only records matching that predicate are converted.
     * @deprecated better API TBD
     * @return a predicate over the record type
     */
    @Deprecated
    Class<? extends Predicate> filter() default RecordConversionPredicate.Dummy.class;

}
