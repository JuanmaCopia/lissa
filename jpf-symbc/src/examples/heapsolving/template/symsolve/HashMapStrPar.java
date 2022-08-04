/*
 * @(#)HashMap.java	1.57 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.template.symsolve;

import java.util.HashSet;
import java.util.Set;

public class HashMapStrPar {
	/**
	 * The default initial capacity - MUST be a power of two.
	 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;

	public EntrySP e0;
	public EntrySP e1;
	public EntrySP e2;
	public EntrySP e3;
	public EntrySP e4;
	public EntrySP e5;
	public EntrySP e6;
	public EntrySP e7;
	public EntrySP e8;
	public EntrySP e9;
	public EntrySP e10;
	public EntrySP e11;
	public EntrySP e12;
	public EntrySP e13;
	public EntrySP e14;
	public EntrySP e15;

	EntrySP getTable(int index) {
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
		case 8:
			return e8;
		case 9:
			return e9;
		case 10:
			return e10;
		case 11:
			return e11;
		case 12:
			return e12;
		case 13:
			return e13;
		case 14:
			return e14;
		case 15:
			return e15;
		default:
			throw new IndexOutOfBoundsException("Index " + index + " is out of bounds!");
		}
	}

	public static class EntrySP {
		public String key;
		public int value;
		public int hash;
		public EntrySP next;

		public int getValue() {
			return value;
		}
	}

	private void addEntriesToEntrySet(Set<EntrySP> es, EntrySP e) {
		EntrySP current = e;
		while (current != null) {
			es.add(current);
			current = current.next;
		}
	}

	public Set<EntrySP> entrySet() {
		Set<EntrySP> es = new HashSet<EntrySP>();
		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			addEntriesToEntrySet(es, getTable(i));
		return es;
	}

	public boolean isLL(EntrySP e, HashSet<EntrySP> visited) {
		EntrySP current = e;
		while (current != null) {
			if (!visited.add(current))
				return false;
			current = current.next;
		}
		return true;
	}

	public boolean repOK() {
		HashSet<EntrySP> visited = new HashSet<EntrySP>();

		for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++)
			if (!isLL(getTable(i), visited))
				return false;

		return true;
	}

}
