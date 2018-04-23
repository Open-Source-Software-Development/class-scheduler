/**
 * Scheduler algorithm implementation. The scheduler uses a modified version of
 * backtracking search. Starting with a blank schedule, scheduled courses
 * (called "hunks") are added one at a time. If the algorithm ever realizes
 * it's reached a point where at least some courses can't be scheduled, it
 * "backtracks", rolling back changes until it's reached a sane configuration.
 * As an improvement over naive backtracking, the algorithm tries to schedule
 * difficult courses first, since they're the ones that are the most likely to
 * cause backtracking. Furthermore, not all hunks are created equal; once the
 * algorithm has decided which course to tackle with, hunks for that course
 * which {@linkplain osd.considerations score well} are considered first.
 * @see osd.schedule.Callbacks
 * @see osd.schedule.Sources
 * @see osd.schedule.Lookups
 * @see osd.schedule.Results
 */
package osd.schedule;