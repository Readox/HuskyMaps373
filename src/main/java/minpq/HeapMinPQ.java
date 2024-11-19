package minpq;

import java.util.*;

/**
 * {@link PriorityQueue} implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class HeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link PriorityQueue} storing {@link PriorityNode} objects representing each element-priority pair.
     */
    private final PriorityQueue<PriorityNode<E>> pq;

    /**
     * Constructs an empty instance.
     */
    public HeapMinPQ() {pq = new PriorityQueue<>(Comparator.comparingDouble(PriorityNode::getPriority));}

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public HeapMinPQ(Map<E, Double> elementsAndPriorities) {
        pq = new PriorityQueue<>(elementsAndPriorities.size(), Comparator.comparingDouble(PriorityNode::getPriority));
        for (Map.Entry<E, Double> entry : elementsAndPriorities.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        pq.add(new PriorityNode<>(element, priority));
    }

    @Override
    public boolean contains(E element) {
        return pq.contains(new PriorityNode<>(element, 0));
    }

    @Override
    //https://stackoverflow.com/questions/50860300/find-an-element-in-a-priorityqueue-by-key
    //https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html
    //https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Iterator.html

    // Proud of programming this because I learned how an Iterator works, and this is what made this run in a timely matter
    public double getPriority(E element) {

        for (Iterator<PriorityNode<E>> it = pq.iterator(); it.hasNext(); ) {
            PriorityNode<E> node = it.next();
            if (node.getElement().equals(element)) {
                return node.getPriority();
            }
        }
        throw new NoSuchElementException("Element " + element + " is not present in the data structure");
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        //System.out.println(pq.size());
        return pq.peek().getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return pq.poll().getElement();
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        pq.remove(new PriorityNode<>(element, 0));
        pq.add(new PriorityNode<>(element, priority));
    }

    @Override
    public int size() {
        return pq.size();
    }
}
