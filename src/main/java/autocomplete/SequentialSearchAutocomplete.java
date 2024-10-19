package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sequential search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class SequentialSearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;

    /**
     * Constructs an empty instance.
     */
    public SequentialSearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        elements.addAll(terms);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        if (prefix == null) {
            return new ArrayList<>();
        }
        List<CharSequence> returnlist = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            if (Autocomplete.isPrefixOf(prefix, elements.get(i))) {
                returnlist.add(elements.get(i));
            }
        }
        return returnlist;
    }
}
