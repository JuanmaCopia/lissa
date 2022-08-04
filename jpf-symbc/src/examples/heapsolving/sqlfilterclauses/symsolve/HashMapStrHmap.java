package heapsolving.sqlfilterclauses.symsolve;

import java.util.HashSet;
import java.util.Set;

public class HashMapStrHmap {

	EntrySH e0;
	EntrySH e1;
	EntrySH e2;
	EntrySH e3;
	EntrySH e4;
	EntrySH e5;
	EntrySH e6;
	EntrySH e7;

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


	EntrySH getTable(int index) {
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

	private void addEntriesToEntrySHSet(Set<EntrySH> es, EntrySH e) {
		EntrySH current = e;
		while (current != null) {
			es.add(current);
			current = current.next;
		}
	}

	public Set<EntrySH> entrySet() {
		Set<EntrySH> es = new HashSet<EntrySH>();
		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			addEntriesToEntrySHSet(es, getTable(i));
		return es;
	}

	private boolean isLL(EntrySH e, HashSet<EntrySH> visited) {
		EntrySH current = e;
		while (current != null) {
			if (!visited.add(current))
				return false;
			current = current.next;
		}
		return true;
	}

	public boolean repOK() {
		HashSet<EntrySH> visited = new HashSet<EntrySH>();

		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			if (!isLL(getTable(i), visited))
				return false;

		return true;
	}

	public static class EntrySH {
		public int key;
		public HashMapStrStr value;
		public int hash;
		public EntrySH next;

		public HashMapStrStr getValue() {
			return value;
		}
	}

}
