
package heapsolving.transportstats.symsolve;

import java.util.HashSet;
import java.util.Set;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

public class TransportStats {


	public TreeMap read_sizes = new TreeMap();
	public TreeMap write_sizes = new TreeMap();

	public long total_reads = 0;
	public long total_writes = 0;

	public TransportStats() {
	}

	public static IFinitization finTransportStats(int nodesNum) {
		IFinitization f = FinitizationFactory.create(TransportStats.class);

		IObjSet treemaps = f.createObjSet(TreeMap.class, 2, true);
		f.set(TransportStats.class, "read_sizes", treemaps);
		f.set(TransportStats.class ,"write_sizes", treemaps);

		IObjSet nodes = f.createObjSet(TreeMap.Entry.class, nodesNum, true);
		f.set(TreeMap.class, "root", nodes);
		f.set(TreeMap.Entry.class, "left", nodes);
		f.set(TreeMap.Entry.class, "right", nodes);
		f.set(TreeMap.Entry.class, "parent", nodes);
		f.set(TreeMap.Entry.class, "color", f.createBooleanSet());

		return f;
	}

	public boolean repOK() {
		HashSet<TreeMap> visited_tm = new HashSet<TreeMap>();
		if (read_sizes != null)
			visited_tm.add(read_sizes);

		if (write_sizes != null && !visited_tm.add(write_sizes))
			return false;

		Set<TreeMap.Entry> visited = new HashSet<TreeMap.Entry>();

		if (read_sizes != null && !read_sizes.repOK(visited))
			return false;
		if (write_sizes != null && !write_sizes.repOK(visited))
			return false;

		return true;
	}

	public boolean areTreesOK() {
		HashSet<TreeMap> visited_tm = new HashSet<TreeMap>();
		if (read_sizes != null)
			visited_tm.add(read_sizes);

		if (write_sizes != null && !visited_tm.add(write_sizes)) {
			return false;
		}

		if (read_sizes != null && !read_sizes.isBinTreeWithParentReferences()) {
			return false;
		}
		if (write_sizes != null && !write_sizes.isBinTreeWithParentReferences()) {
			return false;
		}

		return true;
	}
}
