
package heapsolving.dictionaryinfo.symsolve;

import java.util.HashSet;
import java.util.LinkedList;

public class TreeMap {

	public transient Entry root = null;

	public static final boolean RED = false;
	public static final boolean BLACK = true;

	/**
	 * Node in the Tree. Doubles as a means to pass key-value pairs back to user
	 * (see Map.Entry).
	 */
	public static class Entry {
		public int key;
		public Object value;
		public Entry left = null;
		public Entry right = null;
		public Entry parent;
		public boolean color = BLACK;

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
		HashSet<Entry> visited = new HashSet<Entry>();
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

}
