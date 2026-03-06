public class Series<T> {
    // Instance Variable
    // The linked list series encapsulates both rowNames and data
    private LL<T> seriesData;

    // Constructor
    public Series(String[] _rowNames, T[] _data) {
        // 1. Initialize the Linked List
        this.seriesData = new LL<T>();

        // 2. Check if _data is null
        if (_data == null) {
            throw new NullPointerException(
                    "Series(String[] _index, T[] _data): _data can't be null. Terminating the program"
            );
        }

        // 3. Handle _rowNames logic with try-catch block as required
        try {
            // Check if _rowNames is null
            // or if lengths differ
            if (_rowNames.length != _data.length) {
                throw new IllegalArgumentException(
                        "Series(String[] _index, T[] _data): the length of _index and _data must be the same"
                );
            }

            // Check for valid row names
            for (String s : _rowNames) {
                if (s == null) {
                    throw new IllegalArgumentException(
                            "Series(String[] _index, T[] _data): _rowNames is not valid"
                    );
                }
            }

            // If all good, populate the list
            for (int i = 0; i < _data.length; i++) {
                this.seriesData.appendNode(_rowNames[i], _data[i]);
            }

        } catch (NullPointerException e) {
            // Case: _rowNames is null. Use default index numbers (0, 1, 2...)
            for (int i = 0; i < _data.length; i++) {
                // index numbers as default row names ("0", "1", "2"...)
                this.seriesData.appendNode(String.valueOf(i), _data[i]);
            }
        }
    }

    // need toString to test it
    @Override
    public String toString() {
        return seriesData.toString();
    }

    // Basic Accessors

    public int getLength() {
        return seriesData.getLength();
    }

    public String[] getRowNames() {
        // LL里的 getIndexArray 刚好就是我们要的 rowNames
        return seriesData.getIndexArray();
    }

    public String[] getData() {
        // LL里的 getDataArray 已经把 T 转成 String 了，直接用
        return seriesData.getDataArray();
    }

    // Append

    public void append(String rn, T d) {
        // 直接调用 LL 的 appendNode
        seriesData.appendNode(rn, d);
    }

    // loc (Label-based Indexing)


    public T loc(String rn) throws NullPointerException, IllegalArgumentException{
        // 1. Exception handling as required
        if (rn == null) {
            throw new NullPointerException("loc(String rn): rn can't be null");
        }
        if (rn.length() == 0) {
            throw new IllegalArgumentException("loc(String rn): rn can't be an empty string");
        }

        // 2. Call LL to search
        // 这里要用 LL.LLNode 变量来接 searchNode 的返回值
        LL.LLNode node = seriesData.searchNode(rn);

        // 3. Return data or null
        if (node == null) {
            return null;
        } else {
            return (T) node.getData();
        }
    }

    // 批量版本的 loc
    public T[] loc(String[] rn) throws NullPointerException, IllegalArgumentException {
        if (rn == null) {
            throw new NullPointerException("loc(String[] rn): rn[] can't be null");
        }
        if (rn.length == 0) {
            throw new IllegalArgumentException("loc(String[] rn): rn[] can't be an empty array");
        }

        // Create Generic Array using the Object casting trick
        T[] results = (T[]) new Object[rn.length];

        for (int i = 0; i < rn.length; i++) {
            // Reuse the single loc() method we just wrote!
            results[i] = loc(rn[i]);
        }

        return results;
    }

    // iloc (Integer-based Indexing)

    public T iloc(int ind) {
        try {
            // LL 不支持直接通过 index 数字访问节点 (没有 getNode(int))
            // 它是单向链表，遍历很慢。
            // 先拿到所有的行名数组，然后通过数组下标找到行名，再用 loc(行名)

            String[] allRowNames = getRowNames(); // 获取所有索引名
            String targetRowName = allRowNames[ind]; // 这里可能会抛出 IndexOutOfBoundsException

            return loc(targetRowName);

        } catch (IndexOutOfBoundsException e) {
            // Handle exception as required
            System.out.println("the index " + ind + " is not valid.. returning null");
            return null;
        }
    }

    // drop (Remove a row)

    public boolean drop(String rn) throws NullPointerException, IllegalArgumentException {
        // 1. Check for invalid inputs as required
        if (rn == null) {
            throw new NullPointerException("drop(String rn): rn can't be null");
        }
        if (rn.length() == 0) {
            throw new IllegalArgumentException("drop(String rn): rn can't be an empty String");
        }

        // 2. Try to remove using LL's removeNode
        try {
            seriesData.removeNode(rn);
            // If we reach here, it means no exception was thrown, so removal was successful
            return true;
        } catch (IllegalArgumentException e) {
            // removeNode throws this if node is not found
            // In this case, we return false as per drop() spec
            return false;
        }
    }

    // fillNull

    public void fillNull(T value) throws IllegalArgumentException{
        // 1. Check for invalid input
        if (value == null) {
            throw new IllegalArgumentException("fillNull(T value): value can't be null");
        }

        // 2. Iterate through all rows to find nulls
        // We get all row names first
        String[] rows = getRowNames();

        for (String r : rows) {
            // Check if the data at this row is null
            if (loc(r) == null) {
                // If data is null, update it with the new value
                seriesData.updateNode(r, value);
            }
        }
    }


}
