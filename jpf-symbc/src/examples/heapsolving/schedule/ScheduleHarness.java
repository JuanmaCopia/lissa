package heapsolving.schedule;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.vm.Verify;

public class ScheduleHarness {

    public static Schedule getStructure() {
        if (SymHeap.usingDriverStrategy())
            return generateDriverStructure();

        Schedule structure = new Schedule();
        structure = (Schedule) SymHeap.makeSymbolicRefThis("schedule_0", structure);

        if (SymHeap.usingIfRepOKStrategy()) {
            if (!structure.repOK())
                return null;
        }

        return structure;
    }

    private static Schedule generateDriverStructure() {
        Schedule structure = new Schedule();
        int numAddedProcesses = addNonDeterministicAmountOfProcesses(structure);
        blockNonDeterministicAmountOfProcesses(structure, numAddedProcesses);
        return structure;
    }
    
    private static int addNonDeterministicAmountOfProcesses(Schedule structure) {
        int numNodes = Verify.getInt(0, SymHeap.getMaxScope());
        for (int i = 1; i <= numNodes; i++) {
            try {
                structure.addProcess(SymHeap.makeSymbolicInteger("N" + i));
            } catch (Exception e) {
            }
        }
        return numNodes;
    }
    
    private static void blockNonDeterministicAmountOfProcesses(Schedule structure, int numAddedProcesses) {
        int numBlocks = Verify.getInt(0, numAddedProcesses);
        for (int i = 1; i <= numBlocks; i++) {
            try {
                structure.blockProcess();
            } catch (Exception e) {
            }
        }
    }

}
