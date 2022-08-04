package examples.treemap;

public class Entry {

    public int key;
    public Object value;
    public Entry left = null;
    public Entry right = null;
    public Entry parent;
    public boolean color = TreeMap.BLACK;

    public Entry() {
    }

    /**
     * Make a new cell with given key, value, and parent, and with <tt>null</tt>
     * child links, and BLACK color.
     */
    public Entry(int key, Object value, Entry parent) {
        this.key = key;
        this.value = value;
        this.parent = parent;
    }

    /**
     * Test two values for equality. Differs from o1.equals(o2) only in that it
     * copes with with <tt>null</tt> o1 properly.
     */
    private static boolean valEquals(Object o1, Object o2) {
        return (o1 == null ? o2 == null : o1.equals(o2));
    }

    /**
     * Replaces the value currently associated with the key with the given value.
     *
     * @return the value associated with the key before this method was called.
     */
    public Object setValue(Object value) {
        Object oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Entry))
            return false;
        Entry e = (Entry) o;

        return valEquals(key, e.getKey()) && valEquals(value, e.getValue());
    }

    public String toString() {
        return key + "=" + value;
    }

    /**
     * Returns the key.
     *
     * @return the key.
     */
    public int getKey() {
        return key;
    }

    // public int hashCode() {
    // int keyHash = key;
    // int valueHash = (value == null ? 0 : value.hashCode());
    // return keyHash ^ valueHash;
    // }

    /**
     * Returns the value associated with the key.
     *
     * @return the value associated with the key.
     */
    public Object getValue() {
        return value;
    }

}
