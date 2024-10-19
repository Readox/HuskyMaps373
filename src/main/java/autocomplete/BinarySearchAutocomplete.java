package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;

    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        elements.addAll(terms);
        Collections.sort(elements, CharSequence::compare);
    }


    //Deduce from the hint that you need to use the insertion point as the starting location
    // I guess cite the CSE 373 website for being the template for most of this work
    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        if (prefix == null) {
            return new ArrayList<>();
        }
        List<CharSequence> returnlist = new ArrayList<>();
        int i = Collections.binarySearch(elements, prefix, CharSequence::compare);

        if (i < 0) {
            i = -(i + 1);
        }

        for (int j = i; j < elements.size(); j++) {
            if (Autocomplete.isPrefixOf(prefix, elements.get(j))) {
                returnlist.add(elements.get(j));
            }
            else {
                return returnlist;
            }
        }

        return returnlist;
    }
}
