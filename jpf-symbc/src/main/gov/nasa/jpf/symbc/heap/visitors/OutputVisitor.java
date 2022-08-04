package gov.nasa.jpf.symbc.heap.visitors;

import gov.nasa.jpf.symbc.heap.solving.canonicalizer.VectorStructure;

import java.util.HashMap;

import gov.nasa.jpf.symbc.heap.solving.canonicalizer.VectorField;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.FieldInfo;

public class OutputVisitor implements SymbolicOutputHeapVisitor {

    protected String currentOwnerObjClassName;
    protected String currentFieldName;
    protected String currentFieldClassName;
    protected VectorStructure vector;
    

    public OutputVisitor(VectorStructure vector) {
        this.vector = vector;
    }

    @Override
    public void setCurrentOwner(ClassInfo ownerObjectClass, int currentObjID) {
        this.currentOwnerObjClassName = ownerObjectClass.getSimpleName();
    }

    @Override
    public void setCurrentField(ClassInfo fieldClass, FieldInfo field) {
        this.currentFieldName = field.getName();
        this.currentFieldClassName = fieldClass.getSimpleName();
    }

    @Override
    public boolean isIgnoredField() {
        String fieldSignature = VectorField.createFieldSignature(currentOwnerObjClassName, currentFieldName, this.currentFieldClassName);
        return !vector.isTrackedField(fieldSignature);
    }

    @Override
    public void resetCurrentField() {
        this.currentFieldName = null;
    }

    @Override
    public void resetCurrentOwner() {
        this.currentOwnerObjClassName = null;
    }

    @Override
    public void visitedNullReferenceField() {
        this.vector.setFieldAsConcrete(currentOwnerObjClassName, currentFieldName, VectorField.DEFAULT_VALUE);
    }

    @Override
    public void visitedNewReferenceField(int id) {
        this.vector.setFieldAsConcrete(currentOwnerObjClassName, currentFieldName, id);
    }

    @Override
    public void visitedExistentReferenceField(int id) {
        this.vector.setFieldAsConcrete(currentOwnerObjClassName, currentFieldName, id);
    }

    @Override
    public void visitedSymbolicReferenceField() {
        this.vector.setReferenceFieldAsSymbolic(currentOwnerObjClassName, currentFieldName);
    }


    @Override
    public void setMaxIdMap(HashMap<ClassInfo, Integer> maxIdMap) {
    }

	@Override
	public void visitedSymbolicPrimitiveField(FieldInfo fi) {
		// TODO Auto-generated method stub
		vector.setPrimitiveFieldAsSymbolic(currentOwnerObjClassName, currentFieldName, null);
		
	}

}
