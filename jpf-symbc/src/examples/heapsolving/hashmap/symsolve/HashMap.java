package heapsolving.hashmap.symsolve;

import java.util.HashSet;
import java.util.Set;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

public class HashMap {

    /**
     * The number of key-value mappings contained in this identity hash map.
     */
    public int size;

    public Entry e0;
    public Entry e1;
    public Entry e2;
    public Entry e3;
    public Entry e4;
    public Entry e5;
    public Entry e6;
    public Entry e7;
    public Entry e8;
    public Entry e9;
    public Entry e10;
    public Entry e11;
    public Entry e12;
    public Entry e13;
    public Entry e14;
    public Entry e15;

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;

    public static IFinitization finHashMap(int nodesNum) {

        IFinitization f = FinitizationFactory.create(HashMap.class);

        f.set(HashMap.class, "size", f.createIntSet(0, nodesNum));

        IObjSet entries = f.createObjSet(Entry.class, nodesNum, true);

        f.set(HashMap.class, "e0", entries);
        f.set(HashMap.class, "e1", entries);
        f.set(HashMap.class, "e2", entries);
        f.set(HashMap.class, "e3", entries);
        f.set(HashMap.class, "e4", entries);
        f.set(HashMap.class, "e5", entries);
        f.set(HashMap.class, "e6", entries);
        f.set(HashMap.class, "e7", entries);
        f.set(HashMap.class, "e8", entries);
        f.set(HashMap.class, "e9", entries);
        f.set(HashMap.class, "e10", entries);
        f.set(HashMap.class, "e11", entries);
        f.set(HashMap.class, "e12", entries);
        f.set(HashMap.class, "e13", entries);
        f.set(HashMap.class, "e14", entries);
        f.set(HashMap.class, "e15", entries);

        int maxint = DEFAULT_INITIAL_CAPACITY * nodesNum;

        f.set(Entry.class, "key", f.createIntSet(0, maxint - 1));
        f.set(Entry.class, "hash", f.createIntSet(0, maxint - 1));
        f.set(Entry.class, "next", entries);

        return f;
    }

    Entry getTable(int index) {
        switch (index) {
        case 0:
            return e0;
        case 1:
            return e1;
        case 2:
            return e2;
        case 3:
            return e3;
        case 4:
            return e4;
        case 5:
            return e5;
        case 6:
            return e6;
        case 7:
            return e7;
        case 8:
            return e8;
        case 9:
            return e9;
        case 10:
            return e10;
        case 11:
            return e11;
        case 12:
            return e12;
        case 13:
            return e13;
        case 14:
            return e14;
        case 15:
            return e15;
        default:
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds!");
        }
    }

    public boolean repOK() {
		Set<Entry> visited = new HashSet<Entry>();
		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			if (!isLinkedList(i, visited))
				return false;
		return true;
	}

	private boolean isLinkedList(int index, Set<Entry> visited) {
		Entry current = getTable(index);
		while (current != null) {
			if (!visited.add(current))
				return false;
			current = current.next;
		}
		return true;
	}

	
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

		public int getKey() {
			return key;
		}

		public Object getValue() {
			return value;
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
				if (v1 == v2 || (v1 != null && v1.equals(v2)))
					return true;
			}
			return false;
		}

		// public int hashCode() {
		// return key ^ (value == null ? 0 : value.hashCode());
		// }

		public String toString() {
			return getKey() + "=" + getValue();
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
}
