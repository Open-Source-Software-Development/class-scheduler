package osd.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an input element can be constructed from a record type. The
 * annotated constructor should take exactly two arguments. The first is an
 * instance of the record type in question. The second is an {@link RecordConverter}
 * instance that may be used to assist the conversion. Such a constructor must
 * not throw any checked exceptions.
 * @see #filter()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface RecordConversion {

    /**
     * The name of a method to validate incoming records. This method must
     * accept one argument, of exactly the same type as  the first argument to
     * the annotated constructor. It must be static and return {@code boolean}.
     * Any records for which this method return {@code false} are not converted.
     * <p>If not set, no filtering is performed and all records are converted.</p>
     * @return the name of a static method to filter records
     */
    String filter() default "";

}
