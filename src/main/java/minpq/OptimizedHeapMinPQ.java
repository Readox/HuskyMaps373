package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class OptimizedHeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of element-priority pairs.
     */
    private final List<PriorityNode<E>> elements;
    /**
     * {@link Map} of each element to its associated index in the {@code elements} heap.
     */
    private final Map<E, Integer> elementsToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        elements = new ArrayList<>();
        elementsToIndex = new HashMap<>();
        elements.add(null);
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public OptimizedHeapMinPQ(Map<E, Double> elementsAndPriorities) {
        elements = new ArrayList<>(elementsAndPriorities.size());
        elementsToIndex = new HashMap<>(elementsAndPriorities.size());
        for (E key : elementsAndPriorities.keySet()){
            add(key, elementsAndPriorities.get(key)); //maps can't have dupe keys, so can send info straight to add()
        }

//        System.out.println(isMinHeap());
//        System.out.println(elements.toString());
//        System.out.println(elementsToIndex.toString());
    }

    @Override
    // Interesting Bug: arraylist starting with null at index 0 always
    public void add(E element, double priority) {
        //System.out.println("Adding new element: " + element + "         " + priority);
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        if (!elements.isEmpty() && elements.get(0) == null) {
            elements.set(0, new PriorityNode<>(element, priority));
            elementsToIndex.put(element, 0);
            //System.out.println("Is Empty");
            //System.out.println(elements.toString());
            //System.out.println(elementsToIndex.toString());
        }
        else {
            elements.add(new PriorityNode<>(element, priority));
            elementsToIndex.put(element, elements.size() - 1); // Add new key to hashmap, will likely be changed later but this
                //is necessary for updateHashMap() to work
            swim(elements.size()-1);
            //System.out.println(elements.toString());
            //System.out.println(elementsToIndex.toString());
        }
    }

    @Override
    public boolean contains(E element) {
        //System.out.println(elements.toString());
        //System.out.println("From contains: " + element);
        //System.out.println("element: " + element);
        //System.out.println("hash map" + elementsToIndex.toString());
        //System.out.println("Return Value: " + elementsToIndex.containsKey(element));
        return elementsToIndex.containsKey(element);
        /*
        for (PriorityNode<E> node : elements) {
            if (node != null) {
                if (node.getElement().equals(element)) {
                    return true;
                }
            }

        }
        return false;
         */
    }

    @Override
    public double getPriority(E element) {
        int index = elementsToIndex.get(element); //will throw error if element not found since int can't be null
        return elements.get(index).getPriority();
        /*
        for (PriorityNode<E> ePriorityNode : elements) {
            if (ePriorityNode.getElement().equals(element)) {
                return ePriorityNode.getPriority();
            }
        }

         */
        //throw new NoSuchElementException("Element " + element + " is not present in the data structure");
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        //System.out.println("Peek Min: " + elements.toString());
        return elements.get(0).getElement();
    }

    @Override
    public E removeMin() {

        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }

        //System.out.println("ISMINHEAP: " + isMinHeap());

        //System.out.println("Before: " + elements.toString());
        PriorityNode<E> returnVal = elements.get(0);
        updateHashMap(0, elements.size() - 1);
        Collections.swap(elements, 0, elements.size()-1);
        elements.removeLast();
        elementsToIndex.remove(returnVal.getElement());
        sink(0);
        //System.out.println("Remove Min: " + elements.toString());
        //System.out.println("Return Value: " + returnVal.getElement() + " " + returnVal.getPriority());

        //System.out.println("ISMINHEAP: " + isMinHeap());

        return returnVal.getElement();
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }

        int index = elementsToIndex.get(element); // should throw exception if element is not present
        updateHashMap(index, elements.size() - 1);
        Collections.swap(elements, index, elements.size()-1);
        elements.removeLast();
        elementsToIndex.remove(element);
        sink(index);
        add(element, priority);
        /*
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getElement().equals(element)) {
                Collections.swap(elements, i, elements.size()-1);
                updateHashMap(i, elements.size() - 1);
                elementsToIndex.remove(elements.removeLast());
                sink(i);
                add(element, priority);
                return;
            }
        }

         */
        //throw new NoSuchElementException("Element " + element + " is not present in the data structure");

    }

    @Override
    public int size() {
        //System.out.println(elements.toString());
        return elements.size();
    }


    private void swimattempt(int i) {
        //System.out.println("Swim Before: " + elements.toString());
        while (i > 0 && elements.get(i).getPriority() > elements.get(i/2).getPriority()) {
            Collections.swap(elements, i, i/2);
            i = i/2;
        }
    }

    private void sinkattempt(int i) {
//        PriorityNode<E> item = elements.get(0);
//        PriorityNode<E> temp = elements.get(size() - 1);
//
//        int parent = 0;
//        int child = 1;
//        while (child <= size())
//        {
//            if (child < size() && elements.get(child).getPriority() < elements.get(child + 1).getPriority())
//                child++;
//            if (temp.getPriority() >= elements.get(child).getPriority())
//                break;
//
//            elements.set(parent, elements.get(child));
//            parent = child;
//            child *= 2;
//        }
//        elements.set(parent, temp);



        while (i*2 <= elements.size()) {
            int j = i*2;
            if (j < size() && elements.get(i).getPriority() < elements.get(i + 1).getPriority()) {
                j++;
            }
            if (elements.get(i).getPriority() < elements.get(j).getPriority()) {
                break;
            }
            updateHashMap(i, j);
            Collections.swap(elements, i, j);
            i = j;
        }
    }



    private void swim(int i) {//somewhat inefficient probably
        //System.out.println(i);
        //System.out.println("Swim Before: " + elementsToIndex.toString());
        while (i > 0 && elements.get(i - 1).getPriority() > elements.get(i).getPriority()) {
            updateHashMap(i - 1, i);
            Collections.swap(elements, i - 1, i);
            i = i - 1;
        }
        //System.out.println("Swim After: "+ elementsToIndex.toString());
    }

    private void sink(int i) { //somewhat inefficient probably
        //elements.sort(Comparator.comparingDouble(PriorityNode::getPriority));

        while (i < elements.size() - 1 && elements.get(i).getPriority() > elements.get(i+1).getPriority()) {
            updateHashMap(i, i + 1);
            Collections.swap(elements, i, i +1);
            i = i + 1;
        }
        /*
        while (2 * i <= elements.size()) {
            int j = 2*i;
            if (j < elements.size() && elements.get(j).getPriority() > elements.get(j + 1).getPriority()) {
                j++;
            }
            if (!(elements.get(i).getPriority() > elements.get(j).getPriority())) {
                break;
            }
            Collections.swap(elements, i, j);
            i = j;
        }

         */
    }

    private void updateHashMap(int i, int j) {
        elementsToIndex.replace(elements.get(i).getElement(), j);
        elementsToIndex.replace(elements.get(j).getElement(), i);
    }


    private boolean isMinHeap() {
        for (int i = 0; i < size(); i++) {
            if (elements.get(i) == null) {System.out.print("Here1"); return false;}
        }
        for (int i = size()+1; i < size() - 1; i++) {
            if (elements.get(i) != null) {System.out.print("Here2"); return false;}
        }
//        if (elements.get(0) != null) {
//            System.out.print("Here3");
//            return false;
//        }
        return isMinHeapOrdered(1);
    }

    // is subtree of pq[1..n] rooted at k a min heap?
    private boolean isMinHeapOrdered(int k) {
        if (k > size()) return true;
        int left = 2*k;
        int right = 2*k + 1;
        if (left  <= size() && k > left)  {System.out.print("Here4");return false;}
        if (right <= size() && k > right) {System.out.print("Here5");return false;}
        return isMinHeapOrdered(left) && isMinHeapOrdered(right);
    }


}
