/*
 * @(#)TreeSet.java	1.26 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.treeset;

/**
 * This class implements the <tt>Set</tt> interface, backed by a
 * <tt>TreeMap</tt> instance. This class guarantees that the sorted set will be
 * in ascending element order, sorted according to the <i>natural order</i> of
 * the elements (see <tt>Comparable</tt>), or by the comparator provided at set
 * creation time, depending on which constructor is used.
 * <p>
 *
 * This implementation provides guaranteed log(n) time cost for the basic
 * operations (<tt>add</tt>, <tt>remove</tt> and <tt>contains</tt>).
 * <p>
 *
 * Note that the ordering maintained by a set (whether or not an explicit
 * comparator is provided) must be <i>consistent with equals</i> if it is to
 * correctly implement the <tt>Set</tt> interface. (See <tt>Comparable</tt> or
 * <tt>Comparator</tt> for a precise definition of <i>consistent with
 * equals</i>.) This is so because the <tt>Set</tt> interface is defined in
 * terms of the <tt>equals</tt> operation, but a <tt>TreeSet</tt> instance
 * performs all key comparisons using its <tt>compareTo</tt> (or
 * <tt>compare</tt>) method, so two keys that are deemed equal by this method
 * are, from the standpoint of the set, equal. The behavior of a set <i>is</i>
 * well-defined even if its ordering is inconsistent with equals; it just fails
 * to obey the general contract of the <tt>Set</tt> interface.
 * <p>
 *
 * <b>Note that this implementation is not synchronized.</b> If multiple threads
 * access a set concurrently, and at least one of the threads modifies the set,
 * it <i>must</i> be synchronized externally. This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the set. If no such
 * object exists, the set should be "wrapped" using the
 * <tt>Collections.synchronizedSet</tt> method. This is best done at creation
 * time, to prevent accidental unsynchronized access to the set:
 * 
 * <pre>
 *     SortedSet s = Collections.synchronizedSortedSet(new TreeSet(...));
 * </pre>
 * <p>
 *
 * The Iterators returned by this class's <tt>iterator</tt> method are
 * <i>fail-fast</i>: if the set is modified at any time after the iterator is
 * created, in any way except through the iterator's own <tt>remove</tt> method,
 * the iterator will throw a <tt>ConcurrentModificationException</tt>. Thus, in
 * the face of concurrent modification, the iterator fails quickly and cleanly,
 * rather than risking arbitrary, non-deterministic behavior at an undetermined
 * time in the future.
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
 * @author Josh Bloch
 * @version 1.26, 01/23/03
 * @see Collection
 * @see Set
 * @see HashSet
 * @see Comparable
 * @see Comparator
 * @see Collections#synchronizedSortedSet(SortedSet)
 * @see TreeMap
 * @since 1.2
 */
public class TreeSet {

	public transient TreeMap m; // The backing Map

	// Dummy value to associate with an Object in the backing Map
	private static final Object PRESENT = new Object();

	public TreeSet() {
		this.m = new TreeMap();
	}

	/**
	 * Returns the number of elements in this set (its cardinality).
	 *
	 * @return the number of elements in this set (its cardinality).
	 */
	public int size() {
		return m.size();
	}

	/**
	 * Returns <tt>true</tt> if this set contains the specified element.
	 *
	 * @param o the object to be checked for containment in this set.
	 * @return <tt>true</tt> if this set contains the specified element.
	 *
	 * @throws ClassCastException if the specified object cannot be compared with
	 *                            the elements currently in the set.
	 */
	public boolean contains(int o) {
		return m.containsKey(o);
	}

	/**
	 * Adds the specified element to this set if it is not already present.
	 *
	 * @param o element to be added to this set.
	 * @return <tt>true</tt> if the set did not already contain the specified
	 *         element.
	 *
	 * @throws ClassCastException if the specified object cannot be compared with
	 *                            the elements currently in the set.
	 */
	public boolean add(int o) {
		return m.put(o, PRESENT) == null;
	}

	/**
	 * Removes the specified element from this set if it is present.
	 *
	 * @param o object to be removed from this set, if present.
	 * @return <tt>true</tt> if the set contained the specified element.
	 *
	 * @throws ClassCastException if the specified object cannot be compared with
	 *                            the elements currently in the set.
	 */
	public boolean remove(int o) {
		return m.remove(o) == PRESENT;
	}

	/**
	 * Removes all of the elements from this set.
	 */
	public void clear() {
		m.clear();
	}

	/**
	 * Returns the first (lowest) element currently in this sorted set.
	 *
	 * @return the first (lowest) element currently in this sorted set.
	 * @throws NoSuchElementException sorted set is empty.
	 */
	public Object first() {
		return m.firstKey();
	}

	/**
	 * Returns the last (highest) element currently in this sorted set.
	 *
	 * @return the last (highest) element currently in this sorted set.
	 * @throws NoSuchElementException sorted set is empty.
	 */
	public Object last() {
		return m.lastKey();
	}

	public boolean repOK() {
		if (m == null)
			return false;
		return m.repOK();
	}
	
	public boolean isBinTreeWithParentReferences() {
        if (m == null)
            return false;
        return m.isBinTreeWithParentReferences();
    }

}
