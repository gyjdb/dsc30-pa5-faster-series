public class LL<T> {
    // Inner Class LLNode
    class LLNode {
        // Instance variables
        private String index;
        private T data;
        private LLNode next; // 指向下一个节点的指针

        // Default constructor
        public LLNode() {
            this.index = null;
            this.data = null;
            this.next = null;
        }

        // Constructor with data
        public LLNode(String _index, T _data) {
            this.index = _index;
            this.data = _data;
            this.next = null;
        }

        // Getters and Setters
        public String getIndex() {
            return this.index;
        }

        public T getData() {
            return this.data;
        }

        public void setData(T d) {
            this.data = d;
        }

        // 虽然没显式要求 setNext/getNext，但在实现链表时通常需要
        // 先留着，或者直接通过 LL 类访问
        public LLNode getNext() {
            return this.next;
        }

        public void setNext(LLNode n) {
            this.next = n;
        }
    }

    // LL Class Variables & Constructor

    // Instance variables
    private LLNode head;
    private LLNode tail;
    private int length;

    // Constructor
    public LL() {
        this.length = 0; // The length is set as 0

        // 生成两个 Dummy Nodes
        LLNode dummy1 = new LLNode();
        LLNode dummy2 = new LLNode();

        // 设置 head 和 tail 指向
        // head -> Dummy1
        this.head = dummy1;
        // tail -> Dummy2
        this.tail = dummy2;

        // 链接 Dummy1 -> Dummy2
        this.head.setNext(this.tail);
    }

    // toString Method
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("print the series ...\n");
        sb.append("==================\n");
        LLNode curr = this.head;
        while (curr != null) {
            sb.append(curr.getIndex());
            sb.append("\t: "); //
            sb.append(curr.getData());
            sb.append("\n");
            curr = curr.next; // 移动到下一个节点
        }

        return sb.toString();
    }

    // appendNode Method
    public void appendNode(String _index, T _data) {
        // 1. Handle invalid index (null or empty)
        // If the _index has an invalid index... use the number alternative
        if (_index == null || _index.isEmpty()) {
            _index = String.valueOf(this.length);
        }

        // 2. Create the new node
        LLNode newNode = new LLNode(_index, _data);

        // 3. Find the node BEFORE the tail dummy node
        // start from head and keep going until curr.next is tail
        LLNode curr = this.head;
        while (curr.next != this.tail) {
            curr = curr.next;
        }

        // Now curr is the last real node (or the head dummy if list is empty)
        // 4. Insert the new node between curr and tail
        newNode.next = this.tail; // New node points to tail
        curr.next = newNode;      // Previous node points to new node

        // 5. Update length
        this.length++;
    }

    // getLength Method
    public int getLength() {
        return this.length;
    }

    // Array Getters

    public String[] getDataArray() {
        // 1. Create an array to hold the data
        // size is length (excluding dummies)
        String[] result = new String[this.length];

        // 2. Start from the first real node
        LLNode curr = this.head.next; // Skip head dummy

        // 3. Traverse and collect
        for (int i = 0; i < this.length; i++) {
            // Convert generic data T to String
            // String.valueOf is safe (handles null automatically)
            result[i] = String.valueOf(curr.getData());

            curr = curr.next;
        }

        return result;
    }

    public String[] getIndexArray() {
        // 1. Create an array to hold the indices
        String[] result = new String[this.length];

        // 2. Start from the first real node
        LLNode curr = this.head.next; // Skip head dummy

        // 3. Traverse and collect
        for (int i = 0; i < this.length; i++) {
            result[i] = curr.getIndex();

            curr = curr.next;
        }

        return result;
    }

    // Core Operations

    public LLNode searchNode(String _index) {
        // Start from the first real node
        LLNode curr = this.head.next;

        // Traverse until we hit the tail dummy node
        while (curr != this.tail) {
            if (curr.getIndex().equals(_index)) {
                return curr;
            }
            curr = curr.next;
        }

        // Not found
        return null;
    }

    public void removeNode(String _index) {
        // We need to find the PREVIOUS node of the target
        LLNode curr = this.head;
        boolean found = false;

        // Look ahead at curr.next
        while (curr.next != this.tail) {
            // Check if the NEXT node is the one we want to remove
            if (curr.next.getIndex().equals(_index)) {
                // Found it!
                // Skip the next node: point curr to the one after next
                curr.next = curr.next.next;
                this.length--;
                found = true;
                break; // Job done, exit loop
            }
            curr = curr.next;
        }

        // If went through the whole list and didn't find it
        if (!found) {
            throw new IllegalArgumentException(
                    "removeNode(String _index): No node with an index " + _index + " in the list"
            );
        }
    }

    public void updateNode(String _index, T value) {
        // Reuse searchNode to find the target
        LLNode target = searchNode(_index);

        if (target == null) {
            throw new IllegalArgumentException(
                    "updateNode(String _index, T value): No node with an index " + _index + " in the list"
            );
        }

        // Update the data
        target.setData(value);
    }


}