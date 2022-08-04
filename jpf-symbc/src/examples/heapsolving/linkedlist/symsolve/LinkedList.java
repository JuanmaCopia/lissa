
package heapsolving.linkedlist.symsolve;

import java.util.HashSet;
import java.util.Set;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

public class LinkedList {

	public Entry header = new Entry();
	public int size = 0;

	public static IFinitization finLinkedList(int nodesNum) {
		IFinitization f = FinitizationFactory.create(LinkedList.class);
		IObjSet nodes = f.createObjSet(Entry.class, nodesNum, true);
		f.set(LinkedList.class, "header", nodes);
		f.set(LinkedList.class, "size", f.createIntSet(0, nodesNum));
		f.set(Entry.class, "element", f.createIntSet(0));
		f.set(Entry.class, "next", nodes);
		f.set(Entry.class, "previous", nodes);
		return f;
	}

	public static IFinitization propertyCheckFinLinkedList(int nodesNum) {
		return finLinkedList(nodesNum + 1);
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
