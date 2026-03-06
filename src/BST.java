public class BST<I extends Comparable<I>, T> {

    class BSTNode {
        private I index;
        private T data;
        private BSTNode left;
        private BSTNode right;

        /**
         * Default constructor. Sets all instance variables to be null.
         */
        public BSTNode() {
            this.index = null;
            this.data = null;
            this.left = null;
            this.right = null;
        }

        /**
         * Constructor. Sets data and index to be _data and _index respectively.
         */
        public BSTNode(I _index, T _data) {
            this.index = _index;
            this.data = _data;
            this.left = null;
            this.right = null;
        }

        /**
         * Returns the index stored in this node.
         */
        public I getIndex() {
            return this.index;
        }

        /**
         * Returns the data stored in this node.
         */
        public T getData() {
            return this.data;
        }

        /**
         * Updates the data in this node to the specified value.
         */
        public void setData(T d) {
            this.data = d;
        }

        /**
         * Returns a string representation of the node, indicating its index and data.
         */
        public String toString() {
            return "index:\t" + this.index + ",\tdata:\t" + this.data + "\n";
        }
    }


    private BSTNode root;
    private int size;

    /**
     * Constructor. Initializes an empty BST with root set to null and size set to 0.
     */
    public BST() {
        this.root = null;
        this.size = 0;
    }


    /**
     * Performs an in-order traversal of the BST and records indices and data values.
     */
    private String inOrderTraversal(BSTNode node) {
        if (node == null) {
            return "";
        }

        return inOrderTraversal(node.left) + node.toString() + inOrderTraversal(node.right);
    }

    /**
     * Returns a string representation of the entire BST using in-order traversal.
     */
    public String toString() {
        return "In-order Traversal of the BST ...\n==================\n" + inOrderTraversal(this.root);
    }

    /**
     * Returns the size of the BST, i.e., the number of valid nodes.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Adds a new node with the specified index and data to the BST.
     */
    public void addNode(I _index, T _data) {
        BSTNode newNode = new BSTNode(_index, _data);

        if (this.root == null) {
            this.root = newNode;
            this.size++;
            return;
        }

        BSTNode curr = this.root;
        BSTNode parent = null;

        while (curr != null) {
            parent = curr;
            int cmp = _index.compareTo(curr.getIndex());

            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return;
            }
        }

        if (_index.compareTo(parent.getIndex()) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        this.size++;
    }

    /**
     * Searches for a node with the specified index in the BST.
     */
    public BSTNode searchNode(I _index) {
        BSTNode curr = this.root;

        while (curr != null) {
            int cmp = _index.compareTo(curr.getIndex());

            if (cmp == 0) {
                return curr;
            } else if (cmp < 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        return null;
    }

    /**
     * Removes a node with the specified index from the BST.
     */
    public void removeNode(I _index) {
        BSTNode parent = null;
        BSTNode curr = this.root;

        while (curr != null && _index.compareTo(curr.getIndex()) != 0) {
            parent = curr;
            if (_index.compareTo(curr.getIndex()) < 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        if (curr == null) {
            throw new IllegalArgumentException(
                    "removeNode(I _index): No node with an index " + _index + " in the BST"
            );
        }

        if (curr.left != null && curr.right != null) {
            BSTNode succParent = curr;
            BSTNode succ = curr.right;

            while (succ.left != null) {
                succParent = succ;
                succ = succ.left;
            }

            curr.index = succ.index;
            curr.data = succ.data;

            parent = succParent;
            curr = succ;
        }

        BSTNode child;
        if (curr.left != null) {
            child = curr.left;
        } else {
            child = curr.right;
        }

        if (parent == null) {
            this.root = child;
        } else if (parent.left == curr) {
            parent.left = child;
        } else {
            parent.right = child;
        }

        this.size--;
    }

    /**
     * Updates a node's data with a new value, given its index.
     */
    public void updateNode(I _index, T _newData) {
        BSTNode target = searchNode(_index);

        if (target == null) {
            throw new IllegalArgumentException(
                    "updateNode(I _index, T _newData): No node with an index " + _index + " in the BST"
            );
        }

        target.setData(_newData);
    }


/************************************ GRADING CODE (DO NOT MODIFY) ************************************ */
    /**
     * Performs a pre-order traversal of the BST.
     */
    private void preOrderTraversal(BSTNode node, int[] idx, String[] arr, boolean dataFlag) {
        // DO NOT CHANGE THIS. THIS FOR TESTING PURPOSES
        if(node == null)
            return;

        if(dataFlag)
            arr[idx[0]] = String.valueOf(node.getData());
        else
            arr[idx[0]] = String.valueOf(node.getIndex());
        idx[0]++;

        preOrderTraversal(node.left, idx, arr, dataFlag);
        preOrderTraversal(node.right, idx, arr, dataFlag);
    }

    /**
     * Returns an array of data values in pre-order traversal order.
     * @return A String array containing the data values of all nodes in pre-order order
     */
    public String[] getDataArray() {
        /// DO NOT CHANGE THIS. THIS FOR TESTING PURPOSES
        String[] dataArr = new String[size];
        preOrderTraversal(this.root, new int[1], dataArr, true);
        return dataArr;
    }

    /**
     * Returns an array of index values in pre-order traversal order.
     * @return A String array containing the index values of all nodes in pre-order order
     */
    public String[] getIndexArray() {
        // DO NOT CHANGE THIS. THIS FOR TESTING PURPOSES
        String[] indexArr = new String[size];
        preOrderTraversal(this.root, new int[1], indexArr, false);
        return indexArr;
    }

/****************************************************************************************************** */

}
