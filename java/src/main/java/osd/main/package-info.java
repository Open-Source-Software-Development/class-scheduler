/**
 * Scheduler entry point. The {@code main} method and primary Dagger component
 * are located in {@link osd.main.SchedulingMain}. Important configuration
 * regarding schedule acceptance policies happens in
 * {@link osd.main.CallbacksImpl}.
 * <p>There's room for improvement regarding the layout of this package, since
 * there's a lot of stuff about saving to databases that doesn't, strictly
 * speaking, belong here.</p>
 */
package osd.main;