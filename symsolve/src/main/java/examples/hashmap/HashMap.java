package examples.hashmap;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

import java.util.HashSet;
import java.util.Set;

public class HashMap {

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;
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

    static int hash(int x) {
        return x;
    }

    /**
     * Returns index for hash code h.
     */
    static int indexFor(int h, int length) {
        return h & (length - 1);
    }

    public boolean repOK() {
        Set<Entry> visited = new HashSet<Entry>();

        for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
            if (!isLinkedList(i, visited))
                return false;

        if (visited.size() != size)
            return false;

        Set<Integer> visitedKeys = new HashSet<Integer>();
        for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
            if (!areKeysOK(i, visitedKeys))
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

    private boolean areKeysOK(int index, Set<Integer> visitedKeys) {
        Entry current = getTable(index);
        while (current != null) {
            if (!visitedKeys.add(current.key))
                return false;

            int correctIndex = indexFor(hash(current.key), DEFAULT_INITIAL_CAPACITY);
            if (index != correctIndex)
                return false;

            if (current.hash != hash(current.key))
                return false;

            current = current.next;
        }
        return true;
    }

}
