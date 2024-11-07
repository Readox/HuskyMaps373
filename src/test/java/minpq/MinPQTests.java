package minpq;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract class providing test cases for all {@link MinPQ} implementations.
 *
 * @see MinPQ
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class MinPQTests {
    /**
     * Returns an empty {@link MinPQ}.
     *
     * @return an empty {@link MinPQ}
     */
    public abstract <E> MinPQ<E> createMinPQ();

    @Test
    public void wcagIndexAsPriority() throws FileNotFoundException {
        File inputFile = new File("data/wcag.tsv");
        MinPQ<String> reference = new DoubleMapMinPQ<>();
        MinPQ<String> testing = createMinPQ();
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t", 2);
            int index = Integer.parseInt(line[0].replace(".", ""));
            String title = line[1];
            reference.add(title, index);
            testing.add(title, index);
        }
        while (!reference.isEmpty()) {
            assertEquals(reference.removeMin(), testing.removeMin());
        }
        assertTrue(testing.isEmpty());
    }

    @Test
    public void randomPriorities() {
        int[] elements = new int[1000];
        for (int i = 0; i < elements.length; i = i + 1) {
            elements[i] = i;
        }
        Random random = new Random(373);
        int[] priorities = new int[elements.length];
        for (int i = 0; i < priorities.length; i = i + 1) {
            priorities[i] = random.nextInt(priorities.length);
        }

        MinPQ<Integer> reference = new DoubleMapMinPQ<>();
        MinPQ<Integer> testing = createMinPQ();
        for (int i = 0; i < elements.length; i = i + 1) {
            reference.add(elements[i], priorities[i]);
            testing.add(elements[i], priorities[i]);
        }

        for (int i = 0; i < elements.length; i = i+1) {
            int expected = reference.removeMin();
            int actual = testing.removeMin();

            if (expected != actual) {
                int expectedPriority = priorities[expected];
                int actualPriority = priorities[actual];
                assertEquals(expectedPriority, actualPriority);
            }
        }
    }

    @Test
    public void randomIntegersRandomPriorities() {
        MinPQ<Integer> reference = new DoubleMapMinPQ<>();
        MinPQ<Integer> testing = createMinPQ();

        int iterations = 10000;
        int maxElement = 1000;
        Random random = new Random();
        for (int i = 0; i < iterations; i += 1) {
            int element = random.nextInt(maxElement);
            double priority = random.nextDouble();
            reference.addOrChangePriority(element, priority);
            testing.addOrChangePriority(element, priority);
                    //System.out.println(reference.getPriority(element) + " " + testing.getPriority(element));
                    //System.out.println(reference.size() + " " + testing.size());
            assertEquals(reference.peekMin(), testing.peekMin());
            assertEquals(reference.size(), testing.size());


            for (int e = 0; e < maxElement; e += 1) {
                if (reference.contains(e)) {
                            //System.out.println(testing.size());
                            //System.out.println(reference.getPriority(e) + " " + testing.getPriority(e));
                    assertTrue(testing.contains(e));
                    assertEquals(reference.getPriority(e), testing.getPriority(e));
                } else {
                    assertFalse(testing.contains(e));
                }
            }
        }
        for (int i = 0; i < iterations; i += 1) {
            boolean shouldRemoveMin = random.nextBoolean();
            if (shouldRemoveMin && !reference.isEmpty()) {
                assertEquals(reference.removeMin(), testing.removeMin());
            } else {
                int element = random.nextInt(maxElement);
                double priority = random.nextDouble();
                reference.addOrChangePriority(element, priority);
                testing.addOrChangePriority(element, priority);
            }
            if (!reference.isEmpty()) {
                assertEquals(reference.peekMin(), testing.peekMin());
                assertEquals(reference.size(), testing.size());
                for (int e = 0; e < maxElement; e += 1) {
                    if (reference.contains(e)) {
                        assertTrue(testing.contains(e));
                        assertEquals(reference.getPriority(e), testing.getPriority(e));
                    } else {
                        //System.out.println("IndexToRemove: " + e);
                        assertFalse(testing.contains(e));
                    }
                }
            } else {
                assertTrue(testing.isEmpty());
            }
        }
    }



    @Test
    public void ReportAnalyzerSimulation() throws FileNotFoundException {
        MinPQ<String> reference = new DoubleMapMinPQ<>();
        MinPQ<String> testing = createMinPQ();


        File inputFile = new File("data/wcag.tsv");
        Scanner scanner = new Scanner(inputFile);
        List<String> taglist = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t", 2);
            int index = Integer.parseInt(line[0].replace(".", ""));
            String title = line[1];
            taglist.add(title);
        }

        Map<String, Double> map = new HashMap<>();
        for(String s : taglist) {
            if (map.containsKey(s)) {
                map.replace(s, map.get(s) - 1);
            }
            else {
                map.put(s, taglist.size() * 1.0);
            }
        }
        MinPQ<String> tagMap = new OptimizedHeapMinPQ<>(map);
        List<String> mostCommon = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mostCommon.add(tagMap.removeMin());
        }

        // Account for possibility of tags having ties, by updating their priority to be consistent with how many times
        //      the tag has been added to the MinPQ
        int iterations = 10000;
        int maxElement = taglist.size();
        Random random = new Random();
        for (int i = 0; i < iterations; i += 1) {
            int element = random.nextInt(maxElement);
            double priority = random.nextDouble();
            //System.out.println(taglist.get(element).getElement());
            if (mostCommon.contains(element)) {
                reference.addOrChangePriority(taglist.get(element), 0);
                testing.addOrChangePriority(taglist.get(element), 0);
            }
            else {
                reference.addOrChangePriority(taglist.get(element), iterations - i);
                testing.addOrChangePriority(taglist.get(element), iterations - i);
            }
            //System.out.println(reference.getPriority(taglist.get(element).getElement()) + " " + testing.getPriority(taglist.get(element).getElement()));
            //System.out.println(reference.size() + " " + testing.size());
            assertEquals(reference.peekMin(), testing.peekMin());
            assertEquals(reference.size(), testing.size());
        }

        for (int i = 0; i < iterations; i += 1) {
            if (!reference.isEmpty() && !testing.isEmpty()) {
                assertEquals(reference.removeMin(), testing.removeMin());
            }
            else {
                break;
            }
        }
    }


}
