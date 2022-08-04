package symsolve.candidates.traversals.visitors;

public interface CandidateVisitor {

    void setRoot(Object rootObject, int rootID);

    void setCurrentOwner(Object currentOwnerObject, int currentOwnerID);

    void setCurrentField(String fieldName, int fieldIndexInVector);

    void accessedVisitedReferenceField(Object fieldObject, int fieldObjectID);

    void accessedNullReferenceField(int fieldObjectID);

    void accessedNewReferenceField(Object fieldObject, int fieldObjectID);

    void accessedPrimitiveField(int fieldObjectID);

    boolean isTraversalAborted();

}
