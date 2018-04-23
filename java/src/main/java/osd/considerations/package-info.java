/**
 * Classes representing preferences and constraints. Broadly speaking, there
 * are four categories, divided along two axes:
 * <ul>
 *     <li><em>Constraints</em>, representing hard rules which must
 *     be followed;</li>
 *     <li><em>Preferences</em>, representing things which are "nice"
 *     but not essential.</li>
 * </ul>
 * <ul>
 *     <li><em>User</em> constraints/preferences, which are season-specific;</li>
 *     <li><em>Base</em> constraints/preferences, which aren't.</li>
 * </ul>
 * <p>The intention of base constraints and preferences is to encode core
 * business rules, which should not be tied to any specific season. For
 * example, the rule that a professor cannot teach more classes than they have
 * capacity for is a base constraint.</p>
 * <p>Conversely, user constraints and preferences may change from season to
 * season. For example, the list of which professors are qualified to teach
 * which courses depends on what professors are currently employed. Note,
 * however, that these do not have to be professor-specific: "user" here should
 * be interpreted as referring to the system operator, not specifically
 * professors. These are read in from the database and are very flexible.</p>
 * <p>Another critical difference is that user preferences and constraints must
 * be referentially transparent: a (section, professor, room, blocks) tuple
 * either meets the preference or constraint, or it doesn't. Base constraints
 * and preferences may consider the entire state of the schedule.</p>
 * @see osd.considerations.rules base constraint/preference implementations
 */
package osd.considerations;