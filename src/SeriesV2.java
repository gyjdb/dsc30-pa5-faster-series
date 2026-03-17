public class SeriesV2<T> implements Series<T> {
    private LL<T> seriesData;
    private BST<String, T> seriesDataBST;

    private String resolveRowName(String rn) {
        if (rn != null && rn.length() > 0) {
            return rn;
        }

        int candidate = this.seriesData.getLength();
        String generatedName = String.valueOf(candidate);
        while (this.seriesDataBST.searchNode(generatedName) != null) {
            candidate++;
            generatedName = String.valueOf(candidate);
        }
        return generatedName;
    }

    private void appendSynchronized(String rowName, T data) {
        if (this.seriesDataBST.searchNode(rowName) != null) {
            throw new IllegalArgumentException(
                    "append(String rn, T d): rn already exists"
            );
        }

        this.seriesData.appendNode(rowName, data);
        this.seriesDataBST.addNode(rowName, data);
    }

    public SeriesV2(String[] _rowNames, T[] _data) {
        this.seriesData = new LL<T>();
        this.seriesDataBST = new BST<String, T>();

        if (_data == null) {
            throw new NullPointerException(
                    "Series(String[] _index, T[] _data): _data can't be null. Terminating the program"
            );
        }

        try {
            if (_rowNames.length != _data.length) {
                throw new IllegalArgumentException(
                        "Series(String[] _index, T[] _data): the length of _index and _data must be the same"
                );
            }

            for (String s : _rowNames) {
                if (s == null) {
                    throw new IllegalArgumentException(
                            "Series(String[] _index, T[] _data): _rowNames is not valid"
                    );
                }
            }

            for (int i = 0; i < _data.length; i++) {
                String finalRowName = resolveRowName(_rowNames[i]);
                appendSynchronized(finalRowName, _data[i]);
            }
        } catch (NullPointerException e) {
            for (int i = 0; i < _data.length; i++) {
                String finalRowName = resolveRowName(null);
                appendSynchronized(finalRowName, _data[i]);
            }
        }
    }

    public String toString() {
        return this.seriesData.toString();
    }

    public int getLength() {
        return this.seriesData.getLength();
    }

    public String[] getRowNames() {
        return this.seriesData.getIndexArray();
    }

    public String[] getData() {
        return this.seriesData.getDataArray();
    }

    public void append(String rn, T d) {
        String finalRowName = resolveRowName(rn);
        appendSynchronized(finalRowName, d);
    }

    public T loc(String rn) throws NullPointerException, IllegalArgumentException {
        if (rn == null) {
            throw new NullPointerException("loc(String rn): rn can't be null");
        }
        if (rn.length() == 0) {
            throw new IllegalArgumentException("loc(String rn): rn can't be an empty string");
        }

        BST<String, T>.BSTNode node = this.seriesDataBST.searchNode(rn);

        if (node == null) {
            return null;
        }
        return node.getData();
    }

    public T[] loc(String[] rn) throws NullPointerException, IllegalArgumentException {
        if (rn == null) {
            throw new NullPointerException("loc(String[] rn): rn[] can't be null");
        }
        if (rn.length == 0) {
            throw new IllegalArgumentException("loc(String[] rn): rn[] can't be an empty array");
        }

        T[] results = (T[]) new Object[rn.length];

        for (int i = 0; i < rn.length; i++) {
            results[i] = loc(rn[i]);
        }

        return results;
    }

    public T iloc(int ind) {
        try {
            String[] rowNames = getRowNames();
            String target = rowNames[ind];
            return loc(target);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("the index " + ind + " is not valid.. returning null");
            return null;
        }
    }

    public boolean drop(String rn) throws NullPointerException, IllegalArgumentException {
        if (rn == null) {
            throw new NullPointerException("drop(String rn): rn can't be null");
        }
        if (rn.length() == 0) {
            throw new IllegalArgumentException("drop(String rn): rn can't be an empty String");
        }

        BST<String, T>.BSTNode node = this.seriesDataBST.searchNode(rn);
        if (node == null) {
            return false;
        }

        this.seriesData.removeNode(rn);
        this.seriesDataBST.removeNode(rn);
        return true;
    }

    public void fillNull(T value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("fillNull(T value): value can't be null");
        }

        String[] rows = getRowNames();
        for (String row : rows) {
            if (loc(row) == null) {
                this.seriesData.updateNode(row, value);
                this.seriesDataBST.updateNode(row, value);
            }
        }
    }
}
