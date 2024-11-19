package minpq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Unsorted array (or {@link ArrayList}) implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class UnsortedArrayMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the element-priority pairs in no specific order.
     */
    private final List<PriorityNode<E>> elements;

    /**
     * Constructs an empty instance.
     */
    public UnsortedArrayMinPQ() {
        elements = new ArrayList<>();
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public UnsortedArrayMinPQ(Map<E, Double> elementsAndPriorities) {
        elements = new ArrayList<>(elementsAndPriorities.size());
        for (Map.Entry<E, Double> entry : elementsAndPriorities.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    //write method
    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        elements.add(new PriorityNode<E>(element, priority));
    }

    //write method
    @Override
    public boolean contains(E element) {
        for (PriorityNode<E> node : elements) {
            if (node.getElement().equals(element)) {
                return true;
            }
        }
        return false;
    }


    //write method
    @Override
    public double getPriority(E element) {
        for (PriorityNode<E> ePriorityNode : elements) {
            if (ePriorityNode.getElement().equals(element)) {
                return ePriorityNode.getPriority();
            }
        }
        throw new NoSuchElementException("Element " + element + " is not present in the data structure");
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        int j = 0;
        for (int i = 1; i < elements.size() ; i++) {
            if (elements.get(j).getPriority() > elements.get(i).getPriority()) {
                //System.out.println(elements.get(i).getPriority() + " " + elements.get(i + 1).getPriority());
                j = i;
            }
        }
        return elements.get(j).getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        int j = 0;
        for (int i = 1; i < elements.size(); i++) {
            if (elements.get(j).getPriority() > elements.get(i).getPriority()) {
                j = i;
            }
        }
        PriorityNode<E> returnVal = elements.remove(j);
        return returnVal.getElement();
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        for (PriorityNode<E> ePriorityNode : elements) {
            if (ePriorityNode.getElement().equals(element)) {
                ePriorityNode.setPriority(priority);
            }
        }
    }

    @Override
    public int size() {
        return elements.size();
    }
}
