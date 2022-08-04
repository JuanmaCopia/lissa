package examples.hashmap;


public class Entry {

    public int key;
    public Object value;
    public int hash;
    public Entry next;

    public Entry() {
    }

    /**
     * Create new entry.
     */
    Entry(int h, int k, Object v, Entry n) {
        value = v;
        next = n;
        key = k;
        hash = h;
    }

    public Object setValue(Object newValue) {
        Object oldValue = value;
        value = newValue;
        return oldValue;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Entry))
            return false;
        Entry e = (Entry) o;
        int k1 = getKey();
        int k2 = e.getKey();
        if (k1 == k2) {
            Object v1 = getValue();
            Object v2 = e.getValue();
            return v1 == v2 || (v1 != null && v1.equals(v2));
        }
        return false;
    }

    public String toString() {
        return getKey() + "=" + getValue();
    }

    public int getKey() {
        return key;
    }

    // public int hashCode() {
    // return key ^ (value == null ? 0 : value.hashCode());
    // }

    public Object getValue() {
        return value;
    }

    /**
     * This method is invoked whenever the value in an entry is overwritten by an
     * invocation of put(k,v) for a key k that's already in the HashMap.
     */
    void recordAccess(HashMap m) {
    }

    /**
     * This method is invoked whenever the entry is removed from the table.
     */
    void recordRemoval(HashMap m) {
    }

}
