package heapsolving.combatantstatistic;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.vm.Verify;

public class CombatantStatisticHarness {

    public static CombatantStatistic getStructure() {
        if (SymHeap.usingDriverStrategy())
            return generateDriverStructure();

        CombatantStatistic structure = new CombatantStatistic(SymHeap.makeSymbolicInteger("numRounds"));
        structure = (CombatantStatistic) SymHeap.makeSymbolicRefThis("combatantstatistic_0", structure);

        if (SymHeap.usingIfRepOKStrategy()) {
            if (!structure.repOK())
                return null;
        }

        return structure;
    }

    private static CombatantStatistic generateDriverStructure() {
        int maxScope = SymHeap.getMaxScope();
        CombatantStatistic structure = new CombatantStatistic(SymHeap.makeSymbolicInteger("numRounds"));
        int numNodes = Verify.getInt(0, maxScope);
        for (int i = 1; i <= numNodes; i++) {
            try {
            	int type = SymHeap.makeSymbolicInteger("type" + i);
            	int side = SymHeap.makeSymbolicInteger("side" + i);
            	int value = SymHeap.makeSymbolicInteger("value" + i);
            	SymHeap.assume(side >= 0 && side <= 1);
                SymHeap.assume(type >= 0 && type <= 14);
                structure.addData(type, side, value);
            } catch (Exception e) {
            }
        }
        return structure;
    }

}
