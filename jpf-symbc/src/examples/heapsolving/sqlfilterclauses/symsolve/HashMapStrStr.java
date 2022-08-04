package heapsolving.sqlfilterclauses.symsolve;

import java.util.HashSet;
import java.util.Set;

public class HashMapStrStr {

	EntrySS e0;
	EntrySS e1;
	EntrySS e2;
	EntrySS e3;
	EntrySS e4;
	EntrySS e5;
	EntrySS e6;
	EntrySS e7;

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

//	public static IFinitization finHashMapStrObj(int nodesNum) {
//
//		IFinitization f = FinitizationFactory.create(HashMapStrStr .class);
//
//		IObjSet entries = f.createObjSet(EntrySS.class, nodesNum, true);
//
//		f.set("e0", entries, false, false, true);
//		f.set("e1", entries, false, false, true);
//		f.set("e2", entries, false, false, true);
//		f.set("e3", entries, false, false, true);
//		f.set("e4", entries, false, false, true);
//		f.set("e5", entries, false, false, true);
//		f.set("e6", entries, false, false, true);
//		f.set("e7", entries, false, false, true);
//		f.set("e8", entries, false, false, true);
//		f.set("e9", entries, false, false, true);
//		f.set("e10", entries, false, false, true);
//		f.set("e11", entries, false, false, true);
//		f.set("e12", entries, false, false, true);
//		f.set("e13", entries, false, false, true);
//		f.set("e14", entries, false, false, true);
//		f.set("e15", entries, false, false, true);
//
//		f.set("EntrySS.next", entries, false, false, true);
//
//		return f;
//	}

	EntrySS getTable(int index) {
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

	private void addEntriesToEntrySSSet(Set<EntrySS> es, EntrySS e) {
		EntrySS current = e;
		while (current != null) {
			es.add(current);
			current = current.next;
		}
	}

	public Set<EntrySS> entrySet() {
		Set<EntrySS> es = new HashSet<EntrySS>();
		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			addEntriesToEntrySSSet(es, getTable(i));
		return es;
	}

	private boolean isLL(EntrySS e, HashSet<EntrySS> visited) {
		EntrySS current = e;
		while (current != null) {
			if (!visited.add(current))
				return false;
			current = current.next;
		}
		return true;
	}

	public boolean repOK() {
		HashSet<EntrySS> visited = new HashSet<EntrySS>();

		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			if (!isLL(getTable(i), visited))
				return false;

		return true;
	}

	public static class EntrySS {
		public int key;
		public int value;
		public int hash;
		public EntrySS next;

		public int getValue() {
			return value;
		}
	}

}
