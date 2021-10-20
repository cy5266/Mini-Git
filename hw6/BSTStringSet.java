import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {

        Node insertLoc = locate(s);
        if (insertLoc == null) {
            _root = new Node(s);
        }
        else {
            if (insertLoc.s.compareTo(s) > 0) {
                insertLoc.left = new Node(s);
            } else {
                insertLoc.right = new Node(s);
            }
        }
        // FIXME: PART A
    }

    @Override
    public boolean contains(String s) {
        Node result = locate(s);

        if (result == null) {
            return false;
        }
        else if (result.s.equals(s) && result != null) {
            return true;
        }
        return false;
        // FIXME: PART A
    }

    public Node locate(String s) {
        if (_root == null) {
            return null;
        }

        Node temp = _root;
        Node returnResult = temp;

        while (true) {
            if (temp.s.compareTo(s) > 0) {
                returnResult = temp.left;
            }
            else if (temp.s.compareTo(s) < 0) {
                returnResult = temp.right;
            }
            else if (temp.s.compareTo(s) == 0 || returnResult == null){
                return temp;
            }
            else {
                temp = returnResult;
            }
        }

    }

    @Override
    public List<String> asList() {

        ArrayList<String> finalList = new ArrayList<>();

        BSTIterator test = new BSTIterator(_root);
        //what happens if there are no elements in the tree?
        while (test.hasNext()) {
            finalList.add(test.next());
        }
        return finalList;
//        return null; // FIXME: PART A. MUST BE IN SORTED ORDER, ASCENDING
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    // @Override
    public Iterator<String> iterator(String low, String high) {
        return null;  // FIXME: PART B (OPTIONAL)
    }


    /** Root node of the tree. */
    private Node _root;
}
