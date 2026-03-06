public class SeriesV1<T> implements Series<T> {
    private LL<T> seriesData;

    public SeriesV1(String[] _rowNames, T[] _data) {
        this.seriesData = new LL<T>();

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
                this.seriesData.appendNode(_rowNames[i], _data[i]);
            }
        } catch (NullPointerException e) {
            for (int i = 0; i < _data.length; i++) {
                this.seriesData.appendNode(String.valueOf(i), _data[i]);
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
        this.seriesData.appendNode(rn, d);
    }

    public T loc(String rn) throws NullPointerException, IllegalArgumentException {
        if (rn == null) {
            throw new NullPointerException("loc(String rn): rn can't be null");
        }
        if (rn.length() == 0) {
            throw new IllegalArgumentException("loc(String rn): rn can't be an empty string");
        }

        LL<T>.LLNode node = this.seriesData.searchNode(rn);

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

        try {
            this.seriesData.removeNode(rn);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void fillNull(T value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("fillNull(T value): value can't be null");
        }

        String[] rows = getRowNames();
        for (String row : rows) {
            if (loc(row) == null) {
                this.seriesData.updateNode(row, value);
            }
        }
    }
}
