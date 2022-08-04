package symsolve.explorers.impl;

import korat.finitization.impl.CVElem;
import korat.finitization.impl.FieldDomain;
import korat.finitization.impl.StateSpace;
import korat.utils.IIntList;
import korat.utils.IntListAI;
import symsolve.explorers.VectorStateSpaceExplorer;
import symsolve.vector.SymSolveVector;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractVectorStateSpaceExplorer implements VectorStateSpaceExplorer {

    protected StateSpace stateSpace;

    protected int[] candidateVector;
    protected int vectorSize;

    protected IIntList accessedIndices;
    protected IIntList changedFields;
    protected IIntList fixedIndices;

    protected int currentIndex;
    protected int currentValue;

    protected FieldDomain currentFieldDomain;
    protected int maxFieldDomainValue;

    protected Object currentFieldOwner;
    protected String currentFieldName;
    protected boolean isCurrentFieldPrimitive;

    protected Map<FieldDomain, Integer> maxFixedInstancePerReferenceFieldDomain = new IdentityHashMap<>();


    public AbstractVectorStateSpaceExplorer(StateSpace stateSpace, IIntList accessedIndices, IIntList changedFields) {
        this.stateSpace = stateSpace;
        this.accessedIndices = accessedIndices;
        this.changedFields = changedFields;
        vectorSize = accessedIndices.getLength();
        fixedIndices = new IntListAI(vectorSize);
    }

    @Override
    public int[] getNextCandidate() {
        changedFields.clear();
        while (!accessedIndices.isEmpty()) {
            int lastAccessedIndex = accessedIndices.removeLast();
            if (!fixedIndices.contains(lastAccessedIndex)) {
                setCurrentField(lastAccessedIndex);
                changedFields.add(lastAccessedIndex);
                if (setNextValue())
                    return candidateVector;
                backtrack();
            }
        }
        return null;
    }

    private void setCurrentField(int lastAccessedIndex) {
        currentIndex = lastAccessedIndex;
        currentValue = candidateVector[lastAccessedIndex];
        currentFieldDomain = stateSpace.getFieldDomain(lastAccessedIndex);
        maxFieldDomainValue = currentFieldDomain.getNumberOfElements() - 1;
        CVElem cvElem = stateSpace.getCVElem(currentIndex);
        currentFieldOwner = cvElem.getObj();
        currentFieldName = cvElem.getFieldName();
        isCurrentFieldPrimitive = currentFieldDomain.isPrimitiveType();
    }

    abstract boolean setNextValue();

    abstract void backtrack();

    @Override
    public void initialize(SymSolveVector vector) {
        setCandidateVector(vector);
        calculateMaxFixedInstancePerReferenceFieldDomain(vector.getFixedIndices());
        resetExplorerState();
    }

    private void setCandidateVector(SymSolveVector vector) {
        this.candidateVector = vector.getConcreteVector();
        if (vectorSize != this.candidateVector.length)
            throw new IllegalArgumentException(String.format("Wrong vector size! Expected: %d, but got: %d", vectorSize, this.candidateVector.length));
    }

    void resetExplorerState() {
        for (int i = 0; i < vectorSize; i++)
            changedFields.add(i);
    }

    private void calculateMaxFixedInstancePerReferenceFieldDomain(Set<Integer> fixedInd) {
        maxFixedInstancePerReferenceFieldDomain.clear();
        fixedIndices.clear();
        for (Integer fixedIndex : fixedInd) {
            fixedIndices.add(fixedIndex);
            FieldDomain fieldDomain = stateSpace.getFieldDomain(fixedIndex);
            if (!fieldDomain.isPrimitiveType()) {
                int value = candidateVector[fixedIndex];
                Integer currentMaxFDInstance = maxFixedInstancePerReferenceFieldDomain.get(fieldDomain);
                if (currentMaxFDInstance == null || value > currentMaxFDInstance)
                    maxFixedInstancePerReferenceFieldDomain.put(fieldDomain, value);
            }
        }
    }

    protected int getMaxInstanceInVector(FieldDomain fieldDomain) {
        Integer maxInstance = this.maxFixedInstancePerReferenceFieldDomain.get(fieldDomain);
        if (maxInstance == null)
            maxInstance = 0;
        for (int i = 0; i < accessedIndices.numberOfElements(); i++) {
            int index = accessedIndices.get(i);
            if (!this.fixedIndices.contains(index) && stateSpace.getFieldDomain(index) == fieldDomain) {
                int value = candidateVector[index];
                if (value > maxInstance)
                    maxInstance = value;
            }
        }
        return maxInstance;
    }

    @Override
    public boolean canBeDeterminedUnsat(SymSolveVector vector) {
        return false;
    }

    @Override
    public int[] getCandidateVector() {
        return candidateVector;
    }


}
