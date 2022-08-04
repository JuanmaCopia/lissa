/*
 * @(#)LinkedList.java	1.46 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.template.symsolve;

import java.util.HashSet;
import java.util.Set;

public class LinkedList {

	public Entry header;

	public static class Entry {
		public Object element;
		public Entry next;
		public Entry previous;
	}

	public boolean repOK() {
		if (header == null)
			return false;

		Set<Entry> visited = new HashSet<Entry>();
		visited.add(header);
		Entry current = header;

		while (true) {
			Entry next = current.next;
			if (next == null)
				return false;
			if (next.previous != current)
				return false;
			current = next;
			if (!visited.add(next))
				break;
		}
		if (current != header)
			return false;

		return true;
	}

}
