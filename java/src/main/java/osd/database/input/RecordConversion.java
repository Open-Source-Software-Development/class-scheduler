package osd.database.input;

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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface RecordConversion {
}
