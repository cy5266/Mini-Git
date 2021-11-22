package gitlet;

import java.util.ArrayList;

public class LinkedHash {


    //basically edit this class and nodes to allow for a commit object
    // allow commit

    //add a function to extract values by key

    protected DNode _currentNode;

    public LinkedHash()
    {
        _currentNode = null;
    }

    public void insertBack(String SHA1, ArrayList d) {
        if (_currentNode == null)
        {
            _currentNode = new DNode(null, SHA1, d);
        }
        else
        {
            DNode insert = new DNode( _currentNode, SHA1, d);
            _currentNode  =  insert;
        }
    }

    public ArrayList<String> getCurrent() {
        return _currentNode._SHABlob;
    }


    static class DNode {
        /** Previous DNode. */
        protected DNode _parent;
        /** SHA1 Key for each commit. */
        protected String key;
        /** Value contained in DNode. */
        private ArrayList<String> _SHABlob;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(String SHA1, ArrayList<String> val) {
            this(null, SHA1, val);
        }

        /**
         * @param prev previous DNode.
         */
        protected DNode(DNode prev, String SHA1, ArrayList<String> SHABlob) {
            _parent = prev;
            _SHABlob = SHABlob;
            key = SHA1;
        }
    }

}

// we seralize the blob and then add it to commit object, which we serialize again and add it into the linkedlist
