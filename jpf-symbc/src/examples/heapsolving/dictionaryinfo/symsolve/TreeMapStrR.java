
package heapsolving.dictionaryinfo.symsolve;

import java.util.HashSet;
import java.util.LinkedList;

public class TreeMapStrR {

	public transient EntryB root = null;

	public static final boolean RED = false;
	public static final boolean BLACK = true;

	/**
	 * Node in the Tree. Doubles as a means to pass key-value pairs back to user
	 * (see Map.EntryB).
	 */
	public static class EntryB {
		public int key;
		public Object value;
		public EntryB left = null;
		public EntryB right = null;
		public EntryB parent;
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
		HashSet<EntryB> visited = new HashSet<EntryB>();
		LinkedList<EntryB> worklist = new LinkedList<EntryB>();
		visited.add(root);
		worklist.add(root);
		if (root.parent != null)
			return false;

		while (!worklist.isEmpty()) {
			EntryB node = worklist.removeFirst();
			EntryB left = node.left;
			if (left != null) {
				if (!visited.add(left))
					return false;
				if (left.parent != node)
					return false;
				worklist.add(left);
			}
			EntryB right = node.right;
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
		LinkedList<EntryB> worklist = new LinkedList<EntryB>();
		worklist.add(root);
		while (!worklist.isEmpty()) {
			EntryB current = worklist.removeFirst();
			EntryB cl = current.left;
			EntryB cr = current.right;
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
		LinkedList<Pair<EntryB, Integer>> worklist2 = new LinkedList<Pair<EntryB, Integer>>();
		worklist2.add(new Pair<EntryB, Integer>(root, 0));
		while (!worklist2.isEmpty()) {
			Pair<EntryB, Integer> p = worklist2.removeFirst();
			EntryB e = p.first();
			int n = p.second();
			if (e != null && e.color == BLACK)
				n++;
			if (e == null) {
				if (numberOfBlack == -1)
					numberOfBlack = n;
				else if (numberOfBlack != n)
					return false;
			} else {
				worklist2.add(new Pair<EntryB, Integer>(e.left, n));
				worklist2.add(new Pair<EntryB, Integer>(e.right, n));
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