package heapsolving.treeset;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.vm.Verify;

public class TreeSetHarness {

    public static TreeSet getStructure() {
        if (SymHeap.usingDriverStrategy())
            return generateDriverStructure();

        TreeSet structure = new TreeSet();
        structure = (TreeSet) SymHeap.makeSymbolicRefThis("treeset_0", structure);

        if (SymHeap.usingIfRepOKStrategy()) {
            if (!structure.repOK())
                return null;
        }

        return structure;
    }

    private static TreeSet generateDriverStructure() {
        int maxScope = SymHeap.getMaxScope();
        TreeSet tree = new TreeSet();
        int numNodes = Verify.getInt(0, maxScope);
        for (int i = 1; i <= numNodes; i++) {
            tree.add(SymHeap.makeSymbolicInteger("N" + i));
        }
        return tree;
    }

}
