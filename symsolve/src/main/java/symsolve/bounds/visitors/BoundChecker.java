package symsolve.bounds.visitors;

import korat.finitization.impl.StateSpace;
import symsolve.bounds.Bounds;
import symsolve.bounds.LabelSets;
import symsolve.candidates.traversals.BFSCandidateTraversal;
import symsolve.candidates.traversals.CandidateTraversal;
import symsolve.candidates.traversals.visitors.GenericCandidateVisitor;
import symsolve.vector.SymSolveVector;

import java.util.HashSet;
import java.util.Set;

public class BoundChecker extends GenericCandidateVisitor {

    LabelSets labelSets;
    StateSpace stateSpace;
    Set<Integer> fixedIndices;
    boolean hasFieldOutOfBounds = false;


    public BoundChecker(StateSpace stateSpace, Bounds bounds) {
        this.stateSpace = stateSpace;
        this.labelSets = new LabelSets(bounds);
    }

    @Override
    public void setRoot(Object rootObject, int rootID) {
        Set<Integer> rootLabelSet = new HashSet<>();
        rootLabelSet.add(rootID);
        labelSets.put(rootObject, rootLabelSet);
    }

    @Override
    public void accessedNewReferenceField(Object fieldObject, int fieldObjectID) {
        Set<Integer> targetLabelSet = labelSets.calculateTargetLabelSet(currentOwnerObject, currentFieldName);
        if (targetLabelSet.isEmpty() && fixedIndices.contains(currentFieldIndexInVector))
            hasFieldOutOfBounds = true;
        else
            labelSets.put(fieldObject, targetLabelSet);
    }

    @Override
    public void accessedVisitedReferenceField(Object fieldObject, int fieldObjectID) {
        if (fixedIndices.contains(currentFieldIndexInVector)) {
            Set<Integer> targetLabelSet = labelSets.calculateTargetLabelSet(currentOwnerObject, currentFieldName);
            if (targetLabelSet.isEmpty()) {
                hasFieldOutOfBounds = true;
                return;
            }

            Set<Integer> fieldValueLabelSet = labelSets.get(fieldObject);
            Set<Integer> intersection = labelSets.calculateSetIntersection(targetLabelSet, fieldValueLabelSet);
            if (intersection.isEmpty())
                hasFieldOutOfBounds = true;
        }
    }

    public boolean isVectorInBounds(SymSolveVector vector) {
        labelSets.clear();
        hasFieldOutOfBounds = false;
        fixedIndices = vector.getFixedIndices();
        CandidateTraversal traversal = new BFSCandidateTraversal(stateSpace);
        traversal.traverse(vector.getConcreteVector(), this);
        return !hasFieldOutOfBounds;
    }

    @Override
    public boolean isTraversalAborted() {
        return hasFieldOutOfBounds;
    }

}
