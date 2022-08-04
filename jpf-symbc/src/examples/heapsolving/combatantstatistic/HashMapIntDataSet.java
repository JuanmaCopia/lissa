/*
 * @(#)HashMap.java	1.57 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.combatantstatistic;

import java.util.HashSet;
import java.util.Set;

/**
 * Hash table based implementation of the <tt>Map</tt> interface. This
 * implementation provides all of the optional map operations, and permits
 * <tt>null</tt> values and the <tt>null</tt> key. (The <tt>HashMap</tt> class
 * is roughly equivalent to <tt>Hashtable</tt>, except that it is unsynchronized
 * and permits nulls.) This class makes no guarantees as to the order of the
 * map; in particular, it does not guarantee that the order will remain constant
 * over time.
 *
 * <p>
 * This implementation provides constant-time performance for the basic
 * operations (<tt>get</tt> and <tt>put</tt>), assuming the hash function
 * disperses the elements properly among the buckets. Iteration over collection
 * views requires time proportional to the "capacity" of the <tt>HashMap</tt>
 * instance (the number of buckets) plus its size (the number of key-value
 * mappings). Thus, it's very important not to set the initial capacity too high
 * (or the load factor too low) if iteration performance is important.
 *
 * <p>
 * An instance of <tt>HashMap</tt> has two parameters that affect its
 * performance: <i>initial capacity</i> and <i>load factor</i>. The
 * <i>capacity</i> is the number of buckets in the hash table, and the initial
 * capacity is simply the capacity at the time the hash table is created. The
 * <i>load factor</i> is a measure of how full the hash table is allowed to get
 * before its capacity is automatically increased. When the number of entries in
 * the hash table exceeds the product of the load factor and the current
 * capacity, the capacity is roughly doubled by calling the <tt>rehash</tt>
 * method.
 *
 * <p>
 * As a general rule, the default load factor (.75) offers a good tradeoff
 * between time and space costs. Higher values decrease the space overhead but
 * increase the lookup cost (reflected in most of the operations of the
 * <tt>HashMap</tt> class, including <tt>get</tt> and <tt>put</tt>). The
 * expected number of entries in the map and its load factor should be taken
 * into account when setting its initial capacity, so as to minimize the number
 * of <tt>rehash</tt> operations. If the initial capacity is greater than the
 * maximum number of entries divided by the load factor, no <tt>rehash</tt>
 * operations will ever occur.
 *
 * <p>
 * If many mappings are to be stored in a <tt>HashMap</tt> instance, creating it
 * with a sufficiently large capacity will allow the mappings to be stored more
 * efficiently than letting it perform automatic rehashing as needed to grow the
 * table.
 *
 * <p>
 * <b>Note that this implementation is not synchronized.</b> If multiple threads
 * access this map concurrently, and at least one of the threads modifies the
 * map structurally, it <i>must</i> be synchronized externally. (A structural
 * modification is any operation that adds or deletes one or more mappings;
 * merely changing the value associated with a key that an instance already
 * contains is not a structural modification.) This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the map. If no such
 * object exists, the map should be "wrapped" using the
 * <tt>Collections.synchronizedMap</tt> method. This is best done at creation
 * time, to prevent accidental unsynchronized access to the map:
 * 
 * <pre>
 *  Map m = Collections.synchronizedMap(new HashMap(...));
 * </pre>
 *
 * <p>
 * The iterators returned by all of this class's "collection view methods" are
 * <i>fail-fast</i>: if the map is structurally modified at any time after the
 * iterator is created, in any way except through the iterator's own
 * <tt>remove</tt> or <tt>add</tt> methods, the iterator will throw a
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
 *
 * <p>
 * This class is a member of the
 * <a href="{@docRoot}/../guide/collections/index.html"> Java Collections
 * Framework</a>.
 *
 * @author Doug Lea
 * @author Josh Bloch
 * @author Arthur van Hoff
 * @version 1.57, 01/23/03
 * @see Object#hashCode()
 * @see Collection
 * @see Map
 * @see HashMapIntDataSet
 * @see Hashtable
 * @since 1.2
 */
public class HashMapIntDataSet {
	/**
	 * The default initial capacity - MUST be a power of two.
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 8;

	/**
	 * The maximum capacity, used if a higher value is implicitly specified by
	 * either of the constructors with arguments. MUST be a power of two <= 1<<30.
	 */
	static final int MAXIMUM_CAPACITY = 1 << 30;

	/**
	 * The load factor used when none specified in constructor.
	 **/
	static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * The table, resized as necessary. Length MUST Always be a power of two.
	 */
	// transient EntryIDS[] table;

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
	transient int size;

	/**
	 * The next size value at which to resize (capacity * load factor).
	 * 
	 * @serial
	 */
	int threshold;

	/**
	 * The load factor for the hash table.
	 *
	 * @serial
	 */
	final float loadFactor;

	/**
	 * The number of times this HashMap has been structurally modified Structural
	 * modifications are those that change the number of mappings in the HashMap or
	 * otherwise modify its internal structure (e.g., rehash). This field is used to
	 * make iterators on Collection-views of the HashMap fail-fast. (See
	 * ConcurrentModificationException).
	 */
	transient volatile int modCount;

	/**
	 * Constructs an empty <tt>HashMap</tt> with the default initial capacity (16)
	 * and the default load factor (0.75).
	 */
	public HashMapIntDataSet() {
		this.loadFactor = DEFAULT_LOAD_FACTOR;
		threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
//    table = new EntryIDS[DEFAULT_INITIAL_CAPACITY];
		init();
	}

	// internal utilities

	/**
	 * Initialization hook for subclasses. This method is called in all constructors
	 * and pseudo-constructors (clone, readObject) after HashMap has been
	 * initialized but before any entries have been inserted. (In the absence of
	 * this method, readObject would require explicit knowledge of subclasses.)
	 */
	void init() {
	}

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

	void setTable(int index, EntryIDS entry) {
		switch (index) {
		case 0:
			e0 = entry;
			break;
		case 1:
			e1 = entry;
			break;
		case 2:
			e2 = entry;
			break;
		case 3:
			e3 = entry;
			break;
		case 4:
			e4 = entry;
			break;
		case 5:
			e5 = entry;
			break;
		case 6:
			e6 = entry;
			break;
		case 7:
			e7 = entry;
			break;
		default:
			throw new IndexOutOfBoundsException("Index " + index + " is out of bounds!");
		}

	}

	/**
	 * Returns a hash value for the specified object. In addition to the object's
	 * own hashCode, this method applies a "supplemental hash function," which
	 * defends against poor quality hash functions. This is critical because HashMap
	 * uses power-of two length hash tables.
	 * <p>
	 *
	 * The shift distances in this function were chosen as the result of an
	 * automated search over the entire four-dimensional search space.
	 */
	static int hash(int x) {
		return x;
	}

	/**
	 * Check for equality of non-null reference x and possibly-null y.
	 */
	static boolean eq(int x, int y) {
		return x == y;
	}

	/**
	 * Returns index for hash code h.
	 */
	static int indexFor(int h, int length) {
		return h & (length - 1);
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 *
	 * @return the number of key-value mappings in this map.
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 *
	 * @return <tt>true</tt> if this map contains no key-value mappings.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns the value to which the specified key is mapped in this identity hash
	 * map, or <tt>null</tt> if the map contains no mapping for this key. A return
	 * value of <tt>null</tt> does not <i>necessarily</i> indicate that the map
	 * contains no mapping for the key; it is also possible that the map explicitly
	 * maps the key to <tt>null</tt>. The <tt>containsKey</tt> method may be used to
	 * distinguish these two cases.
	 *
	 * @param key the key whose associated value is to be returned.
	 * @return the value to which this map maps the specified key, or <tt>null</tt>
	 *         if the map contains no mapping for this key.
	 * @see #put(Object, Object)
	 */
	public DataSet get(int key) {
		int hash = hash(key);
		int i = indexFor(hash, DEFAULT_INITIAL_CAPACITY);
//    EntryIDS e = table[i];
		EntryIDS e = getTable(i);
		while (true) {
			if (e == null)
				return null;
			if (e.hash == hash && eq(key, e.key))
				return e.value;
			e = e.next;
		}
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the specified key.
	 *
	 * @param key The key whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map contains a mapping for the specified key.
	 */
	public boolean containsKey(int key) {
		int hash = hash(key);
		int i = indexFor(hash, DEFAULT_INITIAL_CAPACITY);
//    EntryIDS e = table[i];
		EntryIDS e = getTable(i);
		while (e != null) {
			if (e.hash == hash && eq(key, e.key))
				return true;
			e = e.next;
		}
		return false;
	}

	/**
	 * Returns the entry associated with the specified key in the HashMap. Returns
	 * null if the HashMap contains no mapping for this key.
	 */
	EntryIDS getEntryIDS(int key) {
		int hash = hash(key);
		int i = indexFor(hash, DEFAULT_INITIAL_CAPACITY);
//    EntryIDS e = table[i];
		EntryIDS e = getTable(i);
		while (e != null && !(e.hash == hash && eq(key, e.key)))
			e = e.next;
		return e;
	}

	/**
	 * Associates the specified value with the specified key in this map. If the map
	 * previously contained a mapping for this key, the old value is replaced.
	 *
	 * @param key   key with which the specified value is to be associated.
	 * @param value value to be associated with the specified key.
	 * @return previous value associated with specified key, or <tt>null</tt> if
	 *         there was no mapping for key. A <tt>null</tt> return can also
	 *         indicate that the HashMap previously associated <tt>null</tt> with
	 *         the specified key.
	 */
	public DataSet put(int key, DataSet value) {
		int hash = hash(key);
		int i = indexFor(hash, DEFAULT_INITIAL_CAPACITY);

		for (EntryIDS e = getTable(i); e != null; e = e.next) {
			if (e.hash == hash && eq(key, e.key)) {
				DataSet oldValue = e.value;
				e.value = value;
				e.recordAccess(this);
				return oldValue;
			}
		}

		modCount++;
		addEntryIDS(hash, key, value, i);
		return null;
	}

	/**
	 * Removes the mapping for this key from this map if present.
	 *
	 * @param key key whose mapping is to be removed from the map.
	 * @return previous value associated with specified key, or <tt>null</tt> if
	 *         there was no mapping for key. A <tt>null</tt> return can also
	 *         indicate that the map previously associated <tt>null</tt> with the
	 *         specified key.
	 */
	public DataSet remove(int key) {
		EntryIDS e = removeEntryIDSForKey(key);
		return (e == null ? null : e.value);
	}

	/**
	 * Removes and returns the entry associated with the specified key in the
	 * HashMap. Returns null if the HashMap contains no mapping for this key.
	 */
	EntryIDS removeEntryIDSForKey(int key) {
		int hash = hash(key);
		int i = indexFor(hash, DEFAULT_INITIAL_CAPACITY);
		EntryIDS prev = getTable(i);
		EntryIDS e = prev;

		while (e != null) {
			EntryIDS next = e.next;
			if (e.hash == hash && eq(key, e.key)) {
				modCount++;
				size--;
				if (prev == e)
					setTable(i, next);
				else
					prev.next = next;
				e.recordRemoval(this);
				return e;
			}
			prev = e;
			e = next;
		}

		return e;
	}

	/**
	 * Special version of remove for EntryIDSSet.
	 */
	EntryIDS removeMapping(Object o) {
		if (!(o instanceof EntryIDS)) {
			return null;
		}

		EntryIDS entry = (EntryIDS) o;
		int k = entry.getKey();
		int hash = hash(k);
		int i = indexFor(hash, DEFAULT_INITIAL_CAPACITY);
		EntryIDS prev = getTable(i);
		EntryIDS e = prev;

		while (e != null) {
			EntryIDS next = e.next;
			if (e.hash == hash && e.equals(entry)) {
				modCount++;
				size--;
				if (prev == e)
					setTable(i, next);
				else
					prev.next = next;
				e.recordRemoval(this);
				return e;
			}
			prev = e;
			e = next;
		}

		return e;
	}

	/**
	 * Removes all mappings from this map.
	 */
	public void clear() {
		modCount++;
		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			setTable(i, null);
		size = 0;
	}

	/**
	 * Returns <tt>true</tt> if this map maps one or more keys to the specified
	 * value.
	 *
	 * @param value value whose presence in this map is to be tested.
	 * @return <tt>true</tt> if this map maps one or more keys to the specified
	 *         value.
	 */
	public boolean containsValue(DataSet value) {
		if (value == null) {
			return containsNullValue();
		}

		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			for (EntryIDS e = getTable(i); e != null; e = e.next)
				if (value.equals(e.value))
					return true;
		return false;
	}

	/**
	 * Special-case code for containsValue with null argument
	 **/
	private boolean containsNullValue() {
		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			for (EntryIDS e = getTable(i); e != null; e = e.next)
				if (e.value == null)
					return true;
		return false;
	}

	public static class EntryIDS {
		final int key;
		DataSet value;
		final int hash;
		EntryIDS next;

		/**
		 * Create new entry.
		 */
		EntryIDS(int h, int k, DataSet v, EntryIDS n) {
			value = v;
			next = n;
			key = k;
			hash = h;
		}

		public int getKey() {
			return key;
		}

		public DataSet getValue() {
			return value;
		}

		public DataSet setValue(DataSet newValue) {
			DataSet oldValue = value;
			value = newValue;
			return oldValue;
		}

		public boolean equals(Object o) {
			if (!(o instanceof EntryIDS))
				return false;
			EntryIDS e = (EntryIDS) o;
			int k1 = getKey();
			int k2 = e.getKey();
			if (k1 == k2) {
				DataSet v1 = getValue();
				DataSet v2 = e.getValue();
				if (v1 == v2 || (v1 != null && v1.equals(v2)))
					return true;
			}
			return false;
		}

//    public int hashCode() {
//      return key ^ (value == null ? 0 : value.hashCode());
//    }

		public String toString() {
			return getKey() + "=" + getValue();
		}

		/**
		 * This method is invoked whenever the value in an entry is overwritten by an
		 * invocation of put(k,v) for a key k that's already in the HashMap.
		 */
		void recordAccess(HashMapIntDataSet m) {
		}

		/**
		 * This method is invoked whenever the entry is removed from the table.
		 */
		void recordRemoval(HashMapIntDataSet m) {
		}
	}

	/**
	 * Add a new entry with the specified key, value and hash code to the specified
	 * bucket. It is the responsibility of this method to resize the table if
	 * appropriate.
	 *
	 * Subclass overrides this to alter the behavior of put method.
	 */
	void addEntryIDS(int hash, int key, DataSet value, int bucketIndex) {
		setTable(bucketIndex, new EntryIDS(hash, key, value, getTable(bucketIndex)));
		// if (size++ >= threshold) resize(2 * DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * Like addEntryIDS except that this version is used when creating entries as
	 * part of Map construction or "pseudo-construction" (cloning, deserialization).
	 * This version needn't worry about resizing the table.
	 *
	 * Subclass overrides this to alter the behavior of HashMap(Map), clone, and
	 * readObject.
	 */
	void createEntryIDS(int hash, int key, DataSet value, int bucketIndex) {
		setTable(bucketIndex, new EntryIDS(hash, key, value, getTable(bucketIndex)));
		size++;
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

	private static final long serialVersionUID = 362498820763181265L;

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

}
