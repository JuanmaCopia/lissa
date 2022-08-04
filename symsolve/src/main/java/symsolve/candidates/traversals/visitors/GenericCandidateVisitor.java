package symsolve.candidates.traversals.visitors;

public class GenericCandidateVisitor implements CandidateVisitor {

    protected Class<?> rootClass;
    protected Object rootObject;
    protected int rootID;

    protected Class<?> currentOwnerClass;
    protected Object currentOwnerObject;
    protected String currentOwnerClassName;
    protected int currentOwnerID;

    protected String currentFieldName;
    protected int currentFieldIndexInVector;


    @Override
    public void setRoot(Object rootObject, int rootID) {
        this.rootObject = rootObject;
        rootClass = rootObject.getClass();
        this.rootID = rootID;
    }

    @Override
    public void setCurrentOwner(Object currentOwnerObject, int currentOwnerID) {
        currentOwnerClass = currentOwnerObject.getClass();
        currentOwnerClassName = currentOwnerClass.getName();
        this.currentOwnerObject = currentOwnerObject;
        this.currentOwnerID = currentOwnerID;
    }

    @Override
    public void setCurrentField(String fieldName, int fieldIndexInVector) {
        currentFieldName = fieldName;
        currentFieldIndexInVector = fieldIndexInVector;
    }

    @Override
    public void accessedVisitedReferenceField(Object fieldObject, int fieldObjectID) {
    }

    @Override
    public void accessedNullReferenceField(int fieldObjectID) {
    }

    @Override
    public void accessedNewReferenceField(Object fieldObject, int fieldObjectID) {
    }

    @Override
    public void accessedPrimitiveField(int fieldObjectID) {
    }

    @Override
    public boolean isTraversalAborted() {
        return false;
    }

}
