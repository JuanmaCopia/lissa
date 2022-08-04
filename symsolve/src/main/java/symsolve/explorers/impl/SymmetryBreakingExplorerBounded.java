package symsolve.explorers.impl;

import korat.finitization.IObjSet;
import korat.finitization.impl.ObjSet;
import korat.finitization.impl.StateSpace;
import korat.utils.IIntList;
import symsolve.bounds.Bounds;
import symsolve.bounds.LabelSets;
import symsolve.bounds.visitors.BoundChecker;
import symsolve.bounds.visitors.LabelSetCalculator;
import symsolve.vector.SymSolveVector;

import java.util.Set;

public class SymmetryBreakingExplorerBounded extends SymmetryBreakingExplorer {

    LabelSetCalculator labelSetCalculator;
    LabelSets labelSets;
    Bounds bounds;


    public SymmetryBreakingExplorerBounded(StateSpace stateSpace, IIntList accessedIndices, IIntList changedFields, Bounds bounds) {
        super(stateSpace, accessedIndices, changedFields);
        this.bounds = bounds;
        labelSets = new LabelSets(bounds);
        labelSetCalculator = new LabelSetCalculator(stateSpace, bounds);
    }

    @Override
    public boolean canBeDeterminedUnsat(SymSolveVector vector) {
        BoundChecker boundChecker = new BoundChecker(stateSpace, bounds);
        return !boundChecker.isVectorInBounds(vector);
    }

    @Override
    public void initialize(SymSolveVector vector) {
        super.initialize(vector);
        labelSets = labelSetCalculator.collectLabelSetsForVector(vector.getConcreteVector());
    }

    @Override
    boolean setNextValue() {
        if (currentValue >= maxFieldDomainValue)
            return false;

        Set<Integer> targetLabelSet = labelSets.calculateTargetLabelSet(currentFieldOwner, currentFieldName);
        if (targetLabelSet.isEmpty())
            return false;

        if (isCurrentFieldPrimitive)
            return setNextValueForPrimitiveType(targetLabelSet);
        return setNextValueForReferenceType(targetLabelSet);
    }

    @Override
    void backtrack() {
        if (!isCurrentFieldPrimitive)
            removeCurrentValueFromLabelSets();
        candidateVector[currentIndex] = 0;
        maxInstances[currentIndex] = -1;
    }

    private boolean isNewValueInBounds(int newValue, Set<Integer> targetLabelSet) {
        Object newValueObject = ((IObjSet) currentFieldDomain).getObject(newValue);
        if (isNewObject(newValueObject)) {
            labelSets.put(newValueObject, targetLabelSet);
            return true;
        }
        Set<Integer> newValueLabelSet = labelSets.get(newValueObject);
        Set<Integer> intersection = labelSets.calculateSetIntersection(targetLabelSet, newValueLabelSet);
        if (intersection.isEmpty())
            return false;

        labelSets.increaseRefCount(newValueObject);
        return true;
    }

    private boolean setNextValueForPrimitiveType(Set<Integer> targetLabelSet) {
        int nextValue = currentValue;
        while (nextValue < maxFieldDomainValue) {
            nextValue++;
            if (targetLabelSet.contains(nextValue)) {
                candidateVector[currentIndex] = nextValue;
                return true;
            }
        }
        return false;
    }

    private boolean setNextValueForReferenceType(Set<Integer> targetLabelSet) {
        if (maxInstances[currentIndex] == -1)
            maxInstances[currentIndex] = getMaxInstanceInVector(currentFieldDomain);
        
        int nextValue = currentValue;
        while (nextValue <= maxInstances[currentIndex] && nextValue < maxFieldDomainValue) {
            nextValue++;
            if (isNewValueInBounds(nextValue, targetLabelSet)) {
                candidateVector[currentIndex] = nextValue;
                return true;
            }
        }
        return false;
    }

    private void removeCurrentValueFromLabelSets() {
        IObjSet objSet = (ObjSet) currentFieldDomain;
        labelSets.remove(objSet.getObject(currentValue));
    }

    private boolean isNewObject(Object object) {
        return !labelSets.contains(object);
    }

}
