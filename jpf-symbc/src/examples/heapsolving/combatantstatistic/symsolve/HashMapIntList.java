package heapsolving.combatantstatistic.symsolve;

import java.util.HashSet;
import java.util.Set;

public class HashMapIntList {

	EntryIL e0;
	EntryIL e1;
	EntryIL e2;
	EntryIL e3;
	EntryIL e4;
	EntryIL e5;
	EntryIL e6;
	EntryIL e7;

	/**
	 * The default initial capacity - MUST be a power of two.
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 8;


	EntryIL getTable(int index) {
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

	private void addEntriesToEntryILSet(Set<EntryIL> es, EntryIL e) {
		EntryIL current = e;
		while (current != null) {
			es.add(current);
			current = current.next;
		}
	}

	public Set<EntryIL> entrySet() {
		Set<EntryIL> es = new HashSet<EntryIL>();
		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			addEntriesToEntryILSet(es, getTable(i));
		return es;
	}

	private boolean isLL(EntryIL e, HashSet<EntryIL> visited) {
		EntryIL current = e;
		while (current != null) {
			if (!visited.add(current))
				return false;
			current = current.next;
		}
		return true;
	}

	public boolean repOK() {
		for (int i = 2; i < DEFAULT_INITIAL_CAPACITY; i++)
			if (getTable(i) != null)
				return false;

		HashSet<EntryIL> visited = new HashSet<EntryIL>();
		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++) {
			if (!isLL(getTable(i), visited))
				return false;
		}
		
		for (EntryIL e : visited) {
		    LinkedList list = e.getValue();
		    if (list != null && !list.repOK())
		        return false;
		}

		return true;
	}

	public static class EntryIL {
		public int key;
		public LinkedList value;
		public int hash;
		public EntryIL next;

		public LinkedList getValue() {
			return value;
		}
	}

}
