package heapsolving.transportstats;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.vm.Verify;

public class TransportStatsHarness {

	public static TransportStats getStructure() {
		if (SymHeap.usingDriverStrategy())
			return generateDriverStructure();

		TransportStats structure = new TransportStats();
		structure = (TransportStats) SymHeap.makeSymbolicRefThis("transportstats_0", structure);

		if (SymHeap.usingIfRepOKStrategy()) {
			if (!structure.repOK())
				return null;
		}

		return structure;
	}

	private static TransportStats generateDriverStructure() {
		int maxScope = SymHeap.getMaxScope();
		TransportStats ts = new TransportStats();
		int numNodes = Verify.getInt(0, maxScope);

		int numNodest1 = Verify.getInt(0, numNodes);
		for (int i = 1; i <= numNodest1; i++) {
			ts.bytesRead(SymHeap.makeSymbolicInteger("R" + i));
		}

		int numNodest2 = numNodes - numNodest1;
		for (int i = 1; i <= numNodest2; i++) {
			ts.bytesWritten(SymHeap.makeSymbolicInteger("W" + i));
		}

		return ts;
	}

}
