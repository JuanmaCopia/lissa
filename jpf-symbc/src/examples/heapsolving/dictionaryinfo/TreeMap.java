/*
 * @(#)TreeMap.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.dictionaryinfo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Red-Black tree based implementation of the <tt>SortedMap</tt> interface. This
 * class guarantees that the map will be in ascending key order, sorted
 * according to the <i>natural order</i> for the key's class (see
 * <tt>Comparable</tt>), or by the comparator provided at creation time,
 * depending on which constructor is used.
 * <p>
 *
 * This implementation provides guaranteed log(n) time cost for the
 * <tt>containsKey</tt>, <tt>get</tt>, <tt>put</tt> and <tt>remove</tt>
 * operations. Algorithms are adaptations of those in Cormen, Leiserson, and
 * Rivest's <I>Introduction to Algorithms</I>.
 * <p>
 *
 * Note that the ordering maintained by a sorted map (whether or not an explicit
 * comparator is provided) must be <i>consistent with equals</i> if this sorted
 * map is to correctly implement the <tt>Map</tt> interface. (See
 * <tt>Comparable</tt> or <tt>Comparator</tt> for a precise definition of
 * <i>consistent with equals</i>.) This is so because the <tt>Map</tt> interface
 * is defined in terms of the equals operation, but a map performs all key
 * comparisons using its <tt>compareTo</tt> (or <tt>compare</tt>) method, so two
 * keys that are deemed equal by this method are, from the standpoint of the
 * sorted map, equal. The behavior of a sorted map <i>is</i> well-defined even
 * if its ordering is inconsistent with equals; it just fails to obey the
 * general contract of the <tt>Map</tt> interface.
 * <p>
 *
 * <b>Note that this implementation is not synchronized.</b> If multiple threads
 * access a map concurrently, and at least one of the threads modifies the map
 * structurally, it <i>must</i> be synchronized externally. (A structural
 * modification is any operation that adds or deletes one or more mappings;
 * merely changing the value associated with an existing key is not a structural
 * modification.) This is typically accomplished by synchronizing on some object
 * that naturally encapsulates the map. If no such object exists, the map should
 * be "wrapped" using the <tt>Collections.synchronizedMap</tt> method. This is
 * best done at creation time, to prevent accidental unsynchronized access to
 * the map:
 *
 * <pre>
 *     Map m = Collections.synchronizedMap(new TreeMap(...));
 * </pre>
 * <p>
 *
 * The iterators returned by all of this class's "collection view methods" are
 * <i>fail-fast</i>: if the map is structurally modified at any time after the
 * iterator is created, in any way except through the iterator's own
 * <tt>remove</tt> or <tt>add</tt> methods, the iterator throws a
 * <tt>ConcurrentModificationException</tt>. Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than risking
 * arbitrary, non-deterministic behavior at an undetermined time in the future.
 *
 * <p>
 * Note that the fail-fast behavior of an iterator cannot be guaranteed as it
 * is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification. Fail-fast iterators throw
 * <tt>ConcurrentModificationException</tt> on a best-effort basis. Therefore,
 * it would be wrong to write a program that depended on this exception for its
 * correctness: <i>the fail-fast behavior of iterators should be used only to
 * detect bugs.</i>
 * <p>
 *
 * This class is a member of the
 * <a href="{@docRoot}/../guide/collections/index.html"> Java Collections
 * Framework</a>.
 *
 * @author Josh Bloch and Doug Lea
 * @version 1.56, 01/23/03
 * @see Map
 * @see HashMap
 * @see Hashtable
 * @see Comparable
 * @see Comparator
 * @see Collection
 * @see Collections#synchronizedMap(Map)
 * @since 1.2
 */
public class TreeMap {

	public transient Entry root = null;

	/**
	 * The number of entries in the tree
	 */
	public transient int size = 0;

	private void incrementSize() {
		size++;
	}

	private void decrementSize() {
		size--;
	}

	/**
	 * Constructs a new, empty map, sorted according to the keys' natural order. All
	 * keys inserted into the map must implement the <tt>Comparable</tt> interface.
	 * Furthermore, all such keys must be <i>mutually comparable</i>:
	 * <tt>k1.compareTo(k2)</tt> must not throw a ClassCastException for any
	 * elements <tt>k1</tt> and <tt>k2</tt> in the map. If the user attempts to put
	 * a key into the map that violates this constraint (for example, the user
	 * attempts to put a string key into a map whose keys are integers), the
	 * <tt>put(Object key, Object
	 * value)</tt> call will throw a <tt>ClassCastException</tt>.
	 *
	 * @see Comparable
	 */
	public TreeMap() {
	}

	// Query Operations

	/**
	 * Returns the number of key-value mappings in this map.
	 *
	 * @return the number of key-value mappings in this map.
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the specified key.
	 *
	 * @param key key whose presence in this map is to be tested.
	 *
	 * @return <tt>true</tt> if this map contains a mapping for the specified key.
	 * @throws ClassCastException   if the key cannot be compared with the keys
	 *                              currently in the map.
	 * @throws NullPointerException key is <tt>null</tt> and this map uses natural
	 *                              ordering, or its comparator does not tolerate
	 *                              <tt>null</tt> keys.
	 */
	public boolean containsKey(int key) {
		return getEntry(key) != null;
	}

	/**
	 * Returns <tt>true</tt> if this map maps one or more keys to the specified
	 * value. More formally, returns <tt>true</tt> if and only if this map contains
	 * at least one mapping to a value <tt>v</tt> such that
	 * <tt>(value==null ? v==null : value.equals(v))</tt>. This operation will
	 * probably require time linear in the Map size for most implementations of Map.
	 *
	 * @param value value whose presence in this Map is to be tested.
	 * @return <tt>true</tt> if a mapping to <tt>value</tt> exists; <tt>false</tt>
	 *         otherwise.
	 * @since 1.2
	 */
	public boolean containsValue(Object value) {
		return (root == null ? false : (value == null ? valueSearchNull(root) : valueSearchNonNull(root, value)));
	}

	private boolean valueSearchNull(Entry n) {
		if (n.value == null) {
			return true;
		}

		// Check left and right subtrees for value
		return (n.left != null && valueSearchNull(n.left)) || (n.right != null && valueSearchNull(n.right));
	}

	private boolean valueSearchNonNull(Entry n, Object value) {
		// Check this node for the value
		if (value.equals(n.value)) {
			return true;
		}

		// Check left and right subtrees for value
		return (n.left != null && valueSearchNonNull(n.left, value))
				|| (n.right != null && valueSearchNonNull(n.right, value));
	}

	/**
	 * Returns the value to which this map maps the specified key. Returns
	 * <tt>null</tt> if the map contains no mapping for this key. A return value of
	 * <tt>null</tt> does not <i>necessarily</i> indicate that the map contains no
	 * mapping for the key; it's also possible that the map explicitly maps the key
	 * to <tt>null</tt>. The <tt>containsKey</tt> operation may be used to
	 * distinguish these two cases.
	 *
	 * @param key key whose associated value is to be returned.
	 * @return the value to which this map maps the specified key, or <tt>null</tt>
	 *         if the map contains no mapping for the key.
	 * @throws ClassCastException   key cannot be compared with the keys currently
	 *                              in the map.
	 * @throws NullPointerException key is <tt>null</tt> and this map uses natural
	 *                              ordering, or its comparator does not tolerate
	 *                              <tt>null</tt> keys.
	 *
	 * @see #containsKey(Object)
	 */
	public Object get(int key) {
		Entry p = getEntry(key);
		return (p == null ? null : p.value);
	}

	/**
	 * Returns the first (lowest) key currently in this sorted map.
	 *
	 * @return the first (lowest) key currently in this sorted map.
	 * @throws NoSuchElementException Map is empty.
	 */
	public int firstKey() {
		return key(firstEntry());
	}

	/**
	 * Returns the last (highest) key currently in this sorted map.
	 *
	 * @return the last (highest) key currently in this sorted map.
	 * @throws NoSuchElementException Map is empty.
	 */
	public int lastKey() {
		return key(lastEntry());
	}

	/**
	 * Returns this map's entry for the given key, or <tt>null</tt> if the map does
	 * not contain an entry for the key.
	 *
	 * @return this map's entry for the given key, or <tt>null</tt> if the map does
	 *         not contain an entry for the key.
	 * @throws ClassCastException   if the key cannot be compared with the keys
	 *                              currently in the map.
	 * @throws NullPointerException key is <tt>null</tt> and this map uses natural
	 *                              order, or its comparator does not tolerate *
	 *                              <tt>null</tt> keys.
	 */
	private Entry getEntry(int key) {
		Entry p = root;
		while (p != null) {
			int cmp = compare(key, p.key);
			if (cmp == 0)
				return p;
			else if (cmp < 0)
				p = p.left;
			else
				p = p.right;
		}
		return null;
	}

	/**
	 * Returns the key corresonding to the specified Entry. Throw
	 * NoSuchElementException if the Entry is <tt>null</tt>.
	 */
	private static int key(Entry e) {
		if (e == null)
			throw new NoSuchElementException();
		return e.key;
	}

	/**
	 * Associates the specified value with the specified key in this map. If the map
	 * previously contained a mapping for this key, the old value is replaced.
	 *
	 * @param key   key with which the specified value is to be associated.
	 * @param value value to be associated with the specified key.
	 *
	 * @return previous value associated with specified key, or <tt>null</tt> if
	 *         there was no mapping for key. A <tt>null</tt> return can also
	 *         indicate that the map previously associated <tt>null</tt> with the
	 *         specified key.
	 * @throws ClassCastException   key cannot be compared with the keys currently
	 *                              in the map.
	 * @throws NullPointerException key is <tt>null</tt> and this map uses natural
	 *                              order, or its comparator does not tolerate
	 *                              <tt>null</tt> keys.
	 */
	public Object put(int key, Object value) {
		Entry t = root;

		if (t == null) {
			incrementSize();
			root = new Entry(key, value, null);
			return null;
		}

		while (true) {
			int cmp = compare(key, t.key);
			if (cmp == 0) {
				return t.setValue(value);
			} else if (cmp < 0) {
				if (t.left != null) {
					t = t.left;
				} else {
					incrementSize();
					t.left = new Entry(key, value, t);
					fixAfterInsertion(t.left);
					return null;
				}
			} else { // cmp > 0
				if (t.right != null) {
					t = t.right;
				} else {
					incrementSize();
					t.right = new Entry(key, value, t);
					fixAfterInsertion(t.right);
					return null;
				}
			}
		}
	}

	/**
	 * Removes the mapping for this key from this TreeMap if present.
	 *
	 * @param key key for which mapping should be removed
	 * @return previous value associated with specified key, or <tt>null</tt> if
	 *         there was no mapping for key. A <tt>null</tt> return can also
	 *         indicate that the map previously associated <tt>null</tt> with the
	 *         specified key.
	 *
	 * @throws ClassCastException   key cannot be compared with the keys currently
	 *                              in the map.
	 * @throws NullPointerException key is <tt>null</tt> and this map uses natural
	 *                              order, or its comparator does not tolerate
	 *                              <tt>null</tt> keys.
	 */
	public Object remove(int key) {
		Entry p = getEntry(key);
		if (p == null) {
			return null;
		}

		Object oldValue = p.value;
		deleteEntry(p);
		return oldValue;
	}

	/**
	 * Removes all mappings from this TreeMap.
	 */
	public void clear() {
		size = 0;
		root = null;
	}

	/**
	 * Compares two keys using the correct comparison method for this TreeMap.
	 */
	private int compare(int k1, int k2) {
		if (k1 < k2)
			return -1;
		else if (k1 > k2)
			return 1;
		return 0;
	}

	/**
	 * Test two values for equality. Differs from o1.equals(o2) only in that it
	 * copes with with <tt>null</tt> o1 properly.
	 */
	private static boolean valEquals(Object o1, Object o2) {
		return (o1 == null ? o2 == null : o1.equals(o2));
	}

	private static final boolean RED = false;
	private static final boolean BLACK = true;

	/**
	 * Node in the Tree. Doubles as a means to pass key-value pairs back to user
	 * (see Map.Entry).
	 */
	static class Entry {
		int key;
		Object value;
		Entry left = null;
		Entry right = null;
		Entry parent;
		boolean color = BLACK;

		/**
		 * Make a new cell with given key, value, and parent, and with <tt>null</tt>
		 * child links, and BLACK color.
		 */
		Entry(int key, Object value, Entry parent) {
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

	/**
	 * Returns the first Entry in the TreeMap (according to the TreeMap's key-sort
	 * function). Returns null if the TreeMap is empty.
	 */
	private Entry firstEntry() {
		Entry p = root;
		if (p != null)
			while (p.left != null)
				p = p.left;
		return p;
	}

	/**
	 * Returns the last Entry in the TreeMap (according to the TreeMap's key-sort
	 * function). Returns null if the TreeMap is empty.
	 */
	private Entry lastEntry() {
		Entry p = root;
		if (p != null)
			while (p.right != null)
				p = p.right;
		return p;
	}

	/**
	 * Returns the successor of the specified Entry, or null if no such.
	 */
	private Entry successor(Entry t) {
		if (t == null)
			return null;
		else if (t.right != null) {
			Entry p = t.right;
			while (p.left != null)
				p = p.left;
			return p;
		} else {
			Entry p = t.parent;
			Entry ch = t;
			while (p != null && ch == p.right) {
				ch = p;
				p = p.parent;
			}
			return p;
		}
	}

	/**
	 * Balancing operations.
	 *
	 * Implementations of rebalancings during insertion and deletion are slightly
	 * different than the CLR version. Rather than using dummy nilnodes, we use a
	 * set of accessors that deal properly with null. They are used to avoid
	 * messiness surrounding nullness checks in the main algorithms.
	 */
	private static boolean colorOf(Entry p) {
		return (p == null ? BLACK : p.color);
	}

	private static Entry parentOf(Entry p) {
		return (p == null ? null : p.parent);
	}

	private static void setColor(Entry p, boolean c) {
		if (p != null)
			p.color = c;
	}

	private static Entry leftOf(Entry p) {
		return (p == null) ? null : p.left;
	}

	private static Entry rightOf(Entry p) {
		return (p == null) ? null : p.right;
	}

	/** From CLR **/
	private void rotateLeft(Entry p) {
		Entry r = p.right;
		p.right = r.left;
		if (r.left != null)
			r.left.parent = p;
		r.parent = p.parent;
		if (p.parent == null)
			root = r;
		else if (p.parent.left == p)
			p.parent.left = r;
		else
			p.parent.right = r;
		r.left = p;
		p.parent = r;
	}

	/** From CLR **/
	private void rotateRight(Entry p) {
		Entry l = p.left;
		p.left = l.right;
		if (l.right != null)
			l.right.parent = p;
		l.parent = p.parent;
		if (p.parent == null)
			root = l;
		else if (p.parent.right == p)
			p.parent.right = l;
		else
			p.parent.left = l;
		l.right = p;
		p.parent = l;
	}

	/** From CLR **/
	private void fixAfterInsertion(Entry x) {
		x.color = RED;

		while (x != null && x != root && x.parent.color == RED) {
			if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
				Entry y = rightOf(parentOf(parentOf(x)));
				if (colorOf(y) == RED) {
					setColor(parentOf(x), BLACK);
					setColor(y, BLACK);
					setColor(parentOf(parentOf(x)), RED);
					x = parentOf(parentOf(x));
				} else {
					if (x == rightOf(parentOf(x))) {
						x = parentOf(x);
						rotateLeft(x);
					}
					setColor(parentOf(x), BLACK);
					setColor(parentOf(parentOf(x)), RED);
					if (parentOf(parentOf(x)) != null)
						rotateRight(parentOf(parentOf(x)));
				}
			} else {
				Entry y = leftOf(parentOf(parentOf(x)));
				if (colorOf(y) == RED) {
					setColor(parentOf(x), BLACK);
					setColor(y, BLACK);
					setColor(parentOf(parentOf(x)), RED);
					x = parentOf(parentOf(x));
				} else {
					if (x == leftOf(parentOf(x))) {
						x = parentOf(x);
						rotateRight(x);
					}
					setColor(parentOf(x), BLACK);
					setColor(parentOf(parentOf(x)), RED);
					if (parentOf(parentOf(x)) != null)
						rotateLeft(parentOf(parentOf(x)));
				}
			}
		}
		root.color = BLACK;
	}

	/**
	 * Delete node p, and then rebalance the tree.
	 */
	private void deleteEntry(Entry p) {
		decrementSize();

		// If strictly internal, copy successor's element to p and then make p
		// point to successor.
		if (p.left != null && p.right != null) {
			Entry s = successor(p);
			p.key = s.key;
			p.value = s.value;
			p = s;
		} // p has 2 children

		// Start fixup at replacement node, if it exists.
		Entry replacement = (p.left != null ? p.left : p.right);

		if (replacement != null) {
			// Link replacement to parent
			replacement.parent = p.parent;
			if (p.parent == null)
				root = replacement;
			else if (p == p.parent.left)
				p.parent.left = replacement;
			else
				p.parent.right = replacement;

			// Null out links so they are OK to use by fixAfterDeletion.
			p.left = p.right = p.parent = null;

			// Fix replacement
			if (p.color == BLACK)
				fixAfterDeletion(replacement);
		} else if (p.parent == null) { // return if we are the only node.
			root = null;
		} else { // No children. Use self as phantom replacement and unlink.
			if (p.color == BLACK) {
				fixAfterDeletion(p);
			}

			if (p.parent != null) {
				if (p == p.parent.left)
					p.parent.left = null;
				else if (p == p.parent.right)
					p.parent.right = null;
				p.parent = null;
			}
		}
	}

	/** From CLR **/
	private void fixAfterDeletion(Entry x) {
		while (x != root && colorOf(x) == BLACK) {
			if (x == leftOf(parentOf(x))) {
				Entry sib = rightOf(parentOf(x));

				if (colorOf(sib) == RED) {
					setColor(sib, BLACK);
					setColor(parentOf(x), RED);
					rotateLeft(parentOf(x));
					sib = rightOf(parentOf(x));
				}

				if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
					setColor(sib, RED);
					x = parentOf(x);
				} else {
					if (colorOf(rightOf(sib)) == BLACK) {
						setColor(leftOf(sib), BLACK);
						setColor(sib, RED);
						rotateRight(sib);
						sib = rightOf(parentOf(x));
					}
					setColor(sib, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(rightOf(sib), BLACK);
					rotateLeft(parentOf(x));
					x = root;
				}
			} else { // symmetric
				Entry sib = leftOf(parentOf(x));

				if (colorOf(sib) == RED) {
					setColor(sib, BLACK);
					setColor(parentOf(x), RED);
					rotateRight(parentOf(x));
					sib = leftOf(parentOf(x));
				}

				if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
					setColor(sib, RED);
					x = parentOf(x);
				} else {
					if (colorOf(leftOf(sib)) == BLACK) {
						setColor(rightOf(sib), BLACK);
						setColor(sib, RED);
						rotateLeft(sib);
						sib = leftOf(parentOf(x));
					}
					setColor(sib, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(leftOf(sib), BLACK);
					rotateRight(parentOf(x));
					x = root;
				}
			}
		}

		setColor(x, BLACK);
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
