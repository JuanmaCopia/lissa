package gov.nasa.jpf.symbc.heap;

import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

public class SymHeapHelper {
    
    public static SymbolicInputHeap getSymbolicInputHeap() {
        HeapChoiceGenerator heapCG = VM.getVM().getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
        return heapCG.getCurrentSymInputHeap();
    }

    public static SymbolicInputHeap getSymbolicInputHeap(VM vm) {
        HeapChoiceGenerator heapCG = vm.getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
        return heapCG.getCurrentSymInputHeap();
    }

    public static ThreadInfo getCurrentThread() {
        return ThreadInfo.getCurrentThread();
    }
    
    public static PathCondition getPathCondition() {
        return PathCondition.getPC(VM.getVM());
    }

    public static PathCondition getPathCondition(VM vm) {
        return PathCondition.getPC(vm);
    }
    
    public static Integer getSolution(SymbolicInteger symbolicInteger, PathCondition pathCondition) {
      int solution = 0;
      if (pathCondition != null) {
          if (!PathCondition.flagSolved)
              pathCondition.solveOld();
          long val = symbolicInteger.solution();
          if (val != SymbolicInteger.UNDEFINED)
              solution = (int) val;
      }
      return solution;
  }

}
