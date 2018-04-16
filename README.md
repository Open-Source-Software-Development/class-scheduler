# OSD

Documentation: https://open-source-software-development.github.io/class-scheduler/html/

# Algorithm overview

The scheduler algorithm is a variant of [backtracking search](https://en.wikipedia.org/wiki/Backtracking). In brief, we start with an empty schedule, and repeatedly add entries to it. If the schedule ever gets into an invalid state - if all the professors who can teach a class are unavailable, and that class still has unscheduled sections, for example - the algorithm backs up until it gets back to a valid state. In order to minimize the number of backtracks needed, the algorithm tries to pick the hardest courses and schedule them first.

Ordinarily, backtracking search stops as soon as a single candidate is found. However, one of our long-term goals is to provide a multiplicity of candidate schedules to the registrar, so the stop condition is handled by an interface instead. Currently, it has the same behavior of stopping at the first complete schedule, but may easily be changed later.

## Design

The algorithm is implemented in Java, and uses Maven for build automation and [Dagger 2](https://medium.com/@isoron/a-friendly-introduction-to-dagger-2-part-1-dbdf2f3fb17b) for dependency injection. You probably don't need to worry _too_ much about these technologies, but basic familiarity with `@Inject`, `@Module`, and `@Provides` in Dagger will be helpful. They're not _that_ bad, I promise. [Hibernate](https://www.tutorialspoint.com/hibernate/index.htm) is used as the Java-side ORM. Once again, we're only using the basic features, so if you're not familiar, focus on getting an overall picture.

The algorithm itself uses Java 8's stream APIs extensively, since lazy evaluation of candidates is critical to the algorithm's performance.

## Package overview

`osd.database`: Utilities for database accession, built on top of Hibernate. The most important class is `RecordAccession`, which can stream in records by type. Also important is `RecordConversion`, an annotation which can be applied to a class's constructor to let a `RecordAccession` automatically construct it by wrapping raw records.
`osd.database.input`: Exactly what it says on the tin. This class provides classes representing various kinds of scheduling element, which use `@RecordConversion` to permit streaming access from a `RecordAccession`.
`osd.database.input.record`: Hibernate entities corresponding directly to database records - the things that get turned into the `input` classes.
`osd.database.output`: A lightweight package holding Hibernate entities corresponding to output records, such as hunks, runs, seasons, and sections.

`osd.considerations`: Preferences and constraints.
`osd.considerations.base`: Base preferences and constraints. Any implementation of `BasePreference` or `BaseConstraint` placed here will be automatically picked up by the scheduler algorithm

`osd.schedule`: The actual algorithm classes. Of particular interest is `Callbacks`, a "stateful filter" that decides when enough schedules have been generated.

`osd.main`: Application entry point. This has implementations of `osd.schedule.Callbacks` and `osd.schedule.Sources` that give the algorithm the context it needs to run.

`osd.main.util.relation`: Data structures implementing many-to-many, one-to-many, and many-to-one relations.
`osd.main.util.pair`: Data structures implementing ordered pairs. Not particularly interesting.
`osd.main.util.classpath`: An API for finding classes in the runtime classpath. Used to find base preference and base constraint implementations, and to help set up database record classes. Here be dragons!

## Deferred features

Some features were planned but not yet completed. There are TODO comments relating to these, but for completeness' sake:

* "Pregenerated" sections, eg. of COR classes. Currently, the only way to specify sections is by automatically generating them.
* Three-hour courses. The infrastructure for this is actually all in place: `osd.database.input` has an enum called `BlockingStrategy`, and each course is associated with one. However, the current logic is to always use the default strategy of paired blocks.
* Professor capacities. Currently, all professors can teach four classes, with no way to change this.
* Multiple candidates. This will require a rewrite of `osd.main.CallbacksImpl`, and more importantly the definition of a good policy for what schedules should be written (the naive policy of writing all complete ones will generate a LOT of spam as very similar schedules are generated in quick succession).
