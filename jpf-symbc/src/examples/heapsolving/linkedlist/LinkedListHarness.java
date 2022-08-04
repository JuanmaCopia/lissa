package heapsolving.linkedlist;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.vm.Verify;

public class LinkedListHarness {

    public static LinkedList getStructure() {
        if (SymHeap.usingDriverStrategy())
            return generateDriverStructure();

        LinkedList structure = new LinkedList();
        structure = (LinkedList) SymHeap.makeSymbolicRefThis("linkedlist_0", structure);

        if (SymHeap.usingIfRepOKStrategy()) {
            if (!structure.repOK())
                return null;
        }

        return structure;
    }

    private static LinkedList generateDriverStructure() {
        int maxScope = SymHeap.getMaxScope();
        LinkedList structure = new LinkedList();
        int numNodes = Verify.getInt(0, maxScope);
        for (int i = 1; i <= numNodes; i++) {
            structure.add(SymHeap.makeSymbolicInteger("Int" + i));
        }
        return structure;
    }

}
