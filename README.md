# OSD

Documentation: https://open-source-software-development.github.io/class-scheduler/html/

# Algorithm overview

The scheduler algorithm is a variant of [backtracking search](https://en.wikipedia.org/wiki/Backtracking). In brief, we start with an empty schedule, and repeatedly add entries to it. If the schedule ever gets into an invalid state - if all the professors who can teach a class are unavailable, and that class still has unscheduled sections, for example - the algorithm backs up until it gets back to a valid state. In order to minimize the number of backtracks needed, the algorithm tries to pick the hardest courses and schedule them first.

Consule the various `package-info.java` files for specifics. The root package (`osd`) is a good place to start.
