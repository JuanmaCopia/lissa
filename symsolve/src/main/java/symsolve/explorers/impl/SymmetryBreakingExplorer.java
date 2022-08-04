package symsolve.explorers.impl;

import korat.finitization.impl.StateSpace;
import korat.utils.IIntList;

public class SymmetryBreakingExplorer extends AbstractVectorStateSpaceExplorer {

    protected int[] maxInstances;


    public SymmetryBreakingExplorer(StateSpace stateSpace, IIntList accessedIndices, IIntList changedFields) {
        super(stateSpace, accessedIndices, changedFields);
        maxInstances = new int[vectorSize];
    }

    @Override
    boolean setNextValue() {
        if (currentValue >= maxFieldDomainValue)
            return false;

        if (isCurrentFieldPrimitive) {
            candidateVector[currentIndex]++;
            return true;
        }
        return setNextReferenceTypeValue();
    }

    @Override
    void backtrack() {
        candidateVector[currentIndex] = 0;
        maxInstances[currentIndex] = -1;
    }

    @Override
    void resetExplorerState() {
        for (int i = 0; i < vectorSize; i++) {
            changedFields.add(i);
            maxInstances[i] = -1;
        }
    }

    protected boolean setNextReferenceTypeValue() {
        if (maxInstances[currentIndex] == -1)
            maxInstances[currentIndex] = getMaxInstanceInVector(currentFieldDomain);

        if (currentValue <= maxInstances[currentIndex]) {
            candidateVector[currentIndex]++;
            return true;
        }
        return false;
    }

}
