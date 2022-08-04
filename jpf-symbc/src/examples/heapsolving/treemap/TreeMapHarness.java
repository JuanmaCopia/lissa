package heapsolving.treemap;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.vm.Verify;

public class TreeMapHarness {

    public static TreeMap getStructure() {
        if (SymHeap.usingDriverStrategy())
            return generateDriverStructure();

        TreeMap structure = new TreeMap();
        structure = (TreeMap) SymHeap.makeSymbolicRefThis("treemap_0", structure);

        if (SymHeap.usingIfRepOKStrategy()) {
            if (!structure.repOK())
                return null;
        }

        return structure;
    }

    private static TreeMap generateDriverStructure() {
        int maxScope = SymHeap.getMaxScope();
        TreeMap tree = new TreeMap();
        int numNodes = Verify.getInt(0, maxScope);
        for (int i = 1; i <= numNodes; i++) {
            tree.put(SymHeap.makeSymbolicInteger("N" + i), new Object());
        }
        return tree;
    }

}
