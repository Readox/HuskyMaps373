package autocomplete;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {

        for(CharSequence key : terms) {
            overallRoot = addOne(overallRoot, key, 0);
        }
        //System.out.println("__START__" + overallRoot.data);
    }

    private Node addOne(Node n, CharSequence key, int index) {
        char c = key.charAt(index);
        if (n == null) {
            n = new Node(c);
        }

        if (c < n.data) {
            n.left = addOne(n.left, key, index);
        } else if (c > n.data) {
            n.right = addOne(n.right, key, index);
        } else if (index < key.length() - 1) {
            n.mid = addOne(n.mid, key, index + 1);
        }
        else {
            n.isTerm = true; // This was the hardest part since I forgot to do this at the start
            return n;
        }
        return n;
    }




    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        ArrayList<CharSequence> returnlist = new ArrayList<>();
        if (prefix == null) {
            return new ArrayList<>();
        }
        Node start = get(overallRoot, prefix, 0);
        if (start.isTerm == true) {
            returnlist.add(prefix);
        }
        getNext(start.mid, prefix, returnlist);
        return returnlist;
    }
    private void getNext(Node n, CharSequence prefix, ArrayList<CharSequence> list) {
        if (n == null) { //end case
            return;
        }
        if (n.isTerm == true) {
            list.add(prefix.toString() + n.data);
        }

        getNext(n.left, prefix, list);
        getNext(n.mid, prefix.toString()+n.data, list);
        getNext(n.right, prefix, list);
    }

    private Node get(Node n, CharSequence key, int index) {
        if (n == null) {
            return null;
        }
        char c = key.charAt(index);
        //System.out.println("__KEY__" + key);
        if (c < n.data) {
            return get(n.left, key, index);
        } else if (c > n.data) {
            return get(n.right, key, index);
        } else if (index < key.length() - 1) {
            return get(n.mid, key, index + 1);
        }
        else {
            //System.out.println("__INDEX__" + index);
            //System.out.println("__VALUE__" + n.data);
            return n;
        }
    }

    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}
