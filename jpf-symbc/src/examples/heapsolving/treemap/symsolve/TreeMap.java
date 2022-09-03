package heapsolving.treemap.symsolve;

import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

public class TreeMap {

	public Entry root;
	public int size = 0;

	public static final boolean RED = false;
	public static final boolean BLACK = true;

	public static IFinitization finTreeMap(int nodesNum) {
		IFinitization f = FinitizationFactory.create(TreeMap.class);
		IObjSet nodes = f.createObjSet(Entry.class, nodesNum, true);
		f.set(TreeMap.class, "root", nodes);
		f.set(TreeMap.class, "size", f.createIntSet(0, nodesNum));
		f.set(Entry.class, "key", f.createIntSet(0, nodesNum - 1));
		f.set(Entry.class, "left", nodes);
		f.set(Entry.class, "right", nodes);
		f.set(Entry.class, "parent", nodes);
		f.set(Entry.class, "color", f.createBooleanSet());
		return f;
	}

	public boolean repOK() {
		if (root != null) {
			if (!isBinTreeWithParentReferences())
				return false;
			if (!isWellColored())
				return false;
		}
		return true;
	}

	public boolean isBinTreeWithParentReferences() {
		if (root == null)
			return true;
		Set<Entry> visited = new HashSet<Entry>();
		LinkedList<Entry> worklist = new LinkedList<Entry>();
		visited.add(root);
		worklist.add(root);
		if (root.parent != null)
			return false;

		while (!worklist.isEmpty()) {
			Entry node = worklist.removeFirst();
			Entry left = node.left;
			if (left != null) {
				if (!visited.add(left))
					return false;
				if (left.parent != node)
					return false;
				worklist.add(left);
			}
			Entry right = node.right;
			if (right != null) {
				if (!visited.add(right))
					return false;
				if (right.parent != node)
					return false;
				worklist.add(right);
			}
		}
		return true;
	}

	public boolean isWellColored() {
		if (root.color != BLACK)
			return false;
		LinkedList<Entry> worklist = new LinkedList<Entry>();
		worklist.add(root);
		while (!worklist.isEmpty()) {
			Entry current = worklist.removeFirst();
			Entry cl = current.left;
			Entry cr = current.right;
			if (current.color == RED) {
				if (cl != null && cl.color == RED)
					return false;
				if (cr != null && cr.color == RED)
					return false;
			}
			if (cl != null)
				worklist.add(cl);
			if (cr != null)
				worklist.add(cr);
		}
		int numberOfBlack = -1;
		LinkedList<Pair<Entry, Integer>> worklist2 = new LinkedList<Pair<Entry, Integer>>();
		worklist2.add(new Pair<Entry, Integer>(root, 0));
		while (!worklist2.isEmpty()) {
			Pair<Entry, Integer> p = worklist2.removeFirst();
			Entry e = p.first();
			int n = p.second();
			if (e != null && e.color == BLACK)
				n++;
			if (e == null) {
				if (numberOfBlack == -1)
					numberOfBlack = n;
				else if (numberOfBlack != n)
					return false;
			} else {
				worklist2.add(new Pair<Entry, Integer>(e.left, n));
				worklist2.add(new Pair<Entry, Integer>(e.right, n));
			}
		}
		return true;
	}

	private class Pair<T, U> {
		private T a;
		private U b;

		public Pair(T a, U b) {
			this.a = a;
			this.b = b;
		}

		public T first() {
			return a;
		}

		public U second() {
			return b;
		}
	}

	public class Entry {

		public int key;
		public Object value;
		public Entry left = null;
		public Entry right = null;
		public Entry parent;
		public boolean color = TreeMap.BLACK;

		/**
		 * Test two values for equality. Differs from o1.equals(o2) only in that it
		 * copes with with <tt>null</tt> o1 properly.
		 */
		private boolean valEquals(Object o1, Object o2) {
			return (o1 == null ? o2 == null : o1.equals(o2));
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
		 * Returns the key.
		 *
		 * @return the key.
		 */
		public int getKey() {
			return key;
		}

		/**
		 * Returns the value associated with the key.
		 *
		 * @return the value associated with the key.
		 */
		public Object getValue() {
			return value;
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

	}
}
