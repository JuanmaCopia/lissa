package symsolve.candidates.traversals;

import korat.finitization.impl.CVElem;
import korat.finitization.impl.FieldDomain;
import korat.finitization.impl.ObjSet;
import korat.finitization.impl.StateSpace;
import symsolve.candidates.traversals.visitors.CandidateVisitor;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;

public class BFSCandidateTraversal implements CandidateTraversal {

    Map<Class<?>, Integer> maxIdMap = new HashMap<>();
    Map<Object, Integer> idMap = new IdentityHashMap<>();
    LinkedList<Object> queue = new LinkedList<>();

    StateSpace stateSpace;

    Object rootObject;
    int[] traversedVector;
    CandidateVisitor visitor;


    public BFSCandidateTraversal(StateSpace stateSpace) {
        if (stateSpace == null)
            throw new IllegalArgumentException();
        this.stateSpace = stateSpace;
        rootObject = stateSpace.getRootObject();
    }

    public void traverse(int[] traversedVector, CandidateVisitor visitor) {
        initializeTraverserState(traversedVector, visitor);
        handleRoot();

        while (!allNodesHaveBeenExplored() && !visitor.isTraversalAborted()) {
            Object currentNode = getNextNode();
            handleNode(currentNode);
            loopThroughFieldsOfNode(currentNode);
        }
    }

    protected void initializeTraverserState(int[] traversedVector, CandidateVisitor visitor) {
        this.traversedVector = traversedVector;
        this.visitor = visitor;

        maxIdMap.clear();
        idMap.clear();
        queue.clear();

        maxIdMap.put(rootObject.getClass(), 0);
        idMap.put(rootObject, 0);
        queue.add(rootObject);
    }

    protected void handleRoot() {
        visitor.setRoot(rootObject, idMap.get(rootObject)); // root ObjSet does not include null, so we don't add 1 to the ID
    }

    protected boolean allNodesHaveBeenExplored() {
        return queue.isEmpty();
    }

    protected void handleNode(Object node) {
        int currentNodeID = idMap.get(node);
        if (node != rootObject)
            visitor.setCurrentOwner(node, currentNodeID + 1);
        else
            visitor.setCurrentOwner(node, currentNodeID);
    }

    protected Object getNextNode() {
        return queue.removeFirst();
    }

    protected void loopThroughFieldsOfNode(Object node) {
        int[] fieldIndices = stateSpace.getFieldIndicesFor(node);
        CVElem[] structureList = stateSpace.getStructureList();

        for (int i : fieldIndices) {

            if (visitor.isTraversalAborted())
                break;

            int indexInFieldDomain = traversedVector[i];
            CVElem elem = structureList[i];
            FieldDomain fieldDomain = elem.getFieldDomain();
            String fieldName = elem.getFieldName();

            visitor.setCurrentField(fieldName, i);

            if (fieldDomain.isPrimitiveType()) {
                handlePrimitiveField(fieldName, indexInFieldDomain);
            } else {
                Object fieldObject = ((ObjSet) fieldDomain).getObject(indexInFieldDomain);
                handleReferenceField(fieldObject, fieldName, indexInFieldDomain);
            }
        }
    }

    protected void handlePrimitiveField(String fieldName, int indexInFieldDomain) {
        visitor.accessedPrimitiveField(indexInFieldDomain);
    }

    protected void handleReferenceField(Object fieldObject, String fieldName, int indexInFieldDomain) {
        if (fieldObject == null) {
            handleNullReferenceField(fieldName, indexInFieldDomain);
        } else if (idMap.containsKey(fieldObject)) {
            handleAlreadyVisitedReferenceField(fieldName, fieldObject);
        } else {
            handleNewReferenceField(fieldName, fieldObject);
        }
    }

    protected void handleNullReferenceField(String fieldName, int indexInFieldDomain) {
        visitor.accessedNullReferenceField(indexInFieldDomain);
    }

    protected void handleAlreadyVisitedReferenceField(String fieldName, Object fieldObject) {
        int fieldObjectID = idMap.get(fieldObject) + 1;
        visitor.accessedVisitedReferenceField(fieldObject, fieldObjectID);
    }

    protected void handleNewReferenceField(String fieldName, Object fieldObject) {
        int fieldObjectID = 0;
        Class<?> clsOfField = fieldObject.getClass();
        if (maxIdMap.containsKey(clsOfField))
            fieldObjectID = maxIdMap.get(clsOfField) + 1;
        maxIdMap.put(clsOfField, fieldObjectID);
        idMap.put(fieldObject, fieldObjectID);

        fieldObjectID = fieldObjectID + 1;
        visitor.accessedNewReferenceField(fieldObject, fieldObjectID);

        addNodeToQueue(fieldObject);
    }

    protected void addNodeToQueue(Object node) {
        queue.add(node);
    }

}
