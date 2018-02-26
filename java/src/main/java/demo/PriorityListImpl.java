package demo;

import schedule.PriorityList;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.IntStream;

class PriorityListImpl<E> implements PriorityList<E> {

    private final List<E> elements;

    @Inject
    PriorityListImpl(final Collection<E> elements) {
        this.elements = new ArrayList<>(elements);
        Collections.shuffle(this.elements);
    }

    @Override
    public PriorityList<E> clone() {
        return new PriorityListImpl<>(elements);
    }

    @Override
    public E pop(final Comparator<E> comparator) {
        if (elements.isEmpty()) {
            return null;
        }
        return pop0(comparator);
    }

    private E pop0(final Comparator<E> comparator) {
        final int size = elements.size();
        final int index = IntStream.rangeClosed(1, size)
                .map(i -> size - i)
                .boxed()
                .max(Comparator.comparing(elements::get, comparator))
                .orElseThrow(NoSuchElementException::new);
        return elements.remove(index);
    }

}
