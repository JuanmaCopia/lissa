
package heapsolving.combatantstatistic.symsolve;

import java.util.HashSet;
import java.util.Set;

public class LinkedList {

	public Entry header = new Entry();
	public int size = 0;


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

	public class Entry {
		public int element;
		public Entry next;
		public Entry previous;

		Entry() {
		}

		Entry(int element, Entry next, Entry previous) {
			this.element = element;
			this.next = next;
			this.previous = previous;
		}
	}

}
