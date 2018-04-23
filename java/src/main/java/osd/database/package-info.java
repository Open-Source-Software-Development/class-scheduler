/**
 * Database accession wrapper, built on top of Hibernate.
 * <p>This package provides functionality to access records from the database,
 * using Java 8's {@link java.util.stream.Stream} API. It also provides an
 * API to associate raw record types with "wrapper" classes; attempting to
 * stream the wrapper type streams the appropriate raw records, wrapped as
 * appropriate. Currently, this API is limited to the single annotation
 * {@link osd.database.RecordConversion}, which causes some awkwardness when
 * multiple wrappers may be appropriate for the same record type. Improvements
 * here should be a priority, while they're still easy to make.</p>
 * @see osd.database.RecordAccession
 * @see osd.database.RecordConversion
 */
package osd.database;