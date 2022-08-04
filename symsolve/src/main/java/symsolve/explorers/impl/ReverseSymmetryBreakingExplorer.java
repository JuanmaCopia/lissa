package symsolve.explorers.impl;

import korat.finitization.impl.StateSpace;
import korat.utils.IIntList;

public class ReverseSymmetryBreakingExplorer extends AbstractVectorStateSpaceExplorer {

    private final boolean[] initializedFields;


    public ReverseSymmetryBreakingExplorer(StateSpace stateSpace, IIntList accessedIndices, IIntList changedFields) {
        super(stateSpace, accessedIndices, changedFields);
        initializedFields = new boolean[vectorSize];
    }

    @Override
    boolean setNextValue() {
        if (isCurrentFieldPrimitive)
            return setNextPrimitiveTypeValue();

        return setNextReferenceTypeValue();
    }

    @Override
    void backtrack() {
        candidateVector[currentIndex] = 0;
        setCurrentFieldAsNotInitialized();
    }

    @Override
    public void resetExplorerState() {
        for (int i = 0; i < vectorSize; i++) {
            changedFields.add(i);
            initializedFields[i] = false;
        }
    }

    protected boolean setNextPrimitiveTypeValue() {
        if (lastPrimitiveValueReached())
            return false;
        candidateVector[currentIndex]++;
        return true;
    }

    protected boolean setNextReferenceTypeValue() {
        if (!isCurrentFieldInitialized()) {
            initializeCurrentField();
            candidateVector[currentIndex] = getFirstValue();
            return true;
        }

        if (!lastReferenceValueReached()) {
            candidateVector[currentIndex]--;
            return true;
        }
        return false;
    }

    private boolean isCurrentFieldInitialized() {
        return initializedFields[currentIndex];
    }

    private void initializeCurrentField() {
        initializedFields[currentIndex] = true;
    }

    private void setCurrentFieldAsNotInitialized() {
        initializedFields[currentIndex] = false;
    }

    private boolean lastReferenceValueReached() {
        return currentValue <= 1;
    }

    private boolean lastPrimitiveValueReached() {
        return currentValue >= maxFieldDomainValue;
    }

    private int getFirstValue() {
        int maxIndexInVector = getMaxInstanceInVector(currentFieldDomain);
        if (maxIndexInVector >= maxFieldDomainValue) {
            return maxIndexInVector;
        }
        return maxIndexInVector + 1;
    }

}
