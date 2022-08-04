package heapsolving.combatantstatistic.symsolve;

import java.util.HashSet;
import java.util.Set;

public class HashMapIntDataSet {

	EntryIDS e0;
	EntryIDS e1;
	EntryIDS e2;
	EntryIDS e3;
	EntryIDS e4;
	EntryIDS e5;
	EntryIDS e6;
	EntryIDS e7;

	/**
	 * The number of key-value mappings contained in this identity hash map.
	 */
	int size;

	/**
	 * The next size value at which to resize (capacity * load factor).
	 * 
	 * @serial
	 */
	int threshold;

	/**
	 * The default initial capacity - MUST be a power of two.
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 8;


	EntryIDS getTable(int index) {
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
		default:
			throw new IndexOutOfBoundsException("Index " + index + " is out of bounds!");
		}
	}

	private void addEntriesToEntryIDSSet(Set<EntryIDS> es, EntryIDS e) {
		EntryIDS current = e;
		while (current != null) {
			es.add(current);
			current = current.next;
		}
	}

	public Set<EntryIDS> entrySet() {
		Set<EntryIDS> es = new HashSet<EntryIDS>();
		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			addEntriesToEntryIDSSet(es, getTable(i));
		return es;
	}

	private boolean isLL(EntryIDS e, HashSet<EntryIDS> visited) {
		EntryIDS current = e;
		while (current != null) {
			if (!visited.add(current))
				return false;
			current = current.next;
		}
		return true;
	}

	public boolean repOK() {
		HashSet<EntryIDS> visited = new HashSet<EntryIDS>();
		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			if (!isLL(getTable(i), visited))
				return false;

		return true;
	}

	public static class EntryIDS {
		public int key;
		public DataSet value;
		public int hash;
		public EntryIDS next;

		public DataSet getValue() {
			return value;
		}
	}

}
