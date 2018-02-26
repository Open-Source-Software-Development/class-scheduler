package schedule;

import java.util.Comparator;

public interface PriorityList<E> {

    PriorityList<E> clone();

    /**
     * Removes a high-priority element from this list and returns it. Implementations may always return the highest
     * priority element, or simply a "best guess"; do not rely on this method returning <em>the</em> best element.
     * <p>If no more elements are available, returns {@code null}.</p>
     * @return a high-priority element
     */
    E pop(final Comparator<E> comparator);

}
