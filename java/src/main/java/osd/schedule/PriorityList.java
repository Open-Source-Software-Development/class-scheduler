package osd.schedule;

/**
 * A priority queue specialized for backtracking search. Once the list is
 * constructed, no new elements may be added. The sole mutation operation pops
 * the highest-priority element.
 * @param <E>
 */
interface PriorityList<E> {

    /**
     * Copies this priority list. Subsequently {@linkplain #pop() popping}
     * either list must not change the other.
     * @return a copy of this priority list
     */
    PriorityList<E> clone();

    /**
     * Removes a high-priority element from this list and returns it. Implementations may always return the highest
     * priority element, or simply a "best guess"; do not rely on this method returning <em>the</em> best element.
     * <p>If no more elements are available, returns {@code null}.</p>
     * @return a high-priority element
     */
    E pop();

}
