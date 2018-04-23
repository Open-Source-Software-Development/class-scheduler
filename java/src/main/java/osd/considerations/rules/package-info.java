/**
 * This package exists to hold specific base constraints and base preferences.
 * It's not really a "package" in the conventional sense as it is a convenient
 * repository for things that don't fit anywhere else. These classes are all
 * loaded reflectively, and all should have zero-argument constructors. To be
 * useful, they should all implement {@link osd.considerations.BasePreference}
 * or {@link osd.considerations.BaseConstraint}.
 */
package osd.considerations.rules;