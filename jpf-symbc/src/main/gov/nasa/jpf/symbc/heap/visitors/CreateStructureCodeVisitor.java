package gov.nasa.jpf.symbc.heap.visitors;

import java.util.HashMap;

import gov.nasa.jpf.symbc.heap.SymHeapHelper;
import gov.nasa.jpf.symbc.heap.solving.canonicalizer.TestCaseGenerator;
import gov.nasa.jpf.symbc.heap.solving.canonicalizer.VectorField;
import gov.nasa.jpf.symbc.heap.solving.canonicalizer.VectorStructure;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.FieldInfo;

public class CreateStructureCodeVisitor implements SymbolicInputHeapVisitor {

    private PathCondition pathCondition;

    HashMap<ClassInfo, Integer> maxIdMap = new HashMap<ClassInfo, Integer>();

    HashMap<String, String> varValues = new HashMap<String, String>();

    VectorStructure vector;

    protected String currentOwnerObjClassName;
    String currentOwnerObjVarName;

    protected String currentFieldName;
    protected String currentFieldAccessString;
    protected String currentFieldClassName;

    String fieldStringValue;

    public CreateStructureCodeVisitor(VectorStructure vector, PathCondition pathCondition) {
        this.vector = vector;
        this.pathCondition = pathCondition;
    }

    @Override
    public void visitedNullReferenceField() {
        this.fieldStringValue = "null";
    }

    @Override
    public void visitedNewReferenceField(int id) {
        this.fieldStringValue = TestCaseGenerator.createVariableName(this.currentFieldClassName, id - 1);
    }

    @Override
    public void visitedExistentReferenceField(int id) {
        this.fieldStringValue = TestCaseGenerator.createVariableName(this.currentFieldClassName, id - 1);
    }

    @Override
    public void visitedSymbolicReferenceField() {
        this.fieldStringValue = "null";
        //assert (false); // TODO: Just for debug purposes, remove this line
    }

    @Override
    public void visitedSymbolicIntegerField(FieldInfo field, SymbolicInteger symbolicInteger) {
        this.fieldStringValue = SymHeapHelper.getSolution(symbolicInteger, this.pathCondition).toString();
    }

    @Override
    public void visitedSymbolicBooleanField(FieldInfo field, SymbolicInteger symbolicBoolean) {
        int value = SymHeapHelper.getSolution(symbolicBoolean, this.pathCondition);
        Boolean booleanvalue =  (value != 0);
        this.fieldStringValue = booleanvalue.toString();
    }

    @Override
    public void visitedSymbolicLongField(FieldInfo fi, SymbolicInteger symbolicLong) {
        this.fieldStringValue = SymHeapHelper.getSolution(symbolicLong, this.pathCondition).toString();
    }

    @Override
    public void visitedSymbolicStringField(FieldInfo fi, StringSymbolic symbolicString) {
    }

    @Override
    public void setCurrentOwner(ClassInfo ownerObjectClass, int currentObjID) {
        this.currentOwnerObjClassName = ownerObjectClass.getSimpleName();
        this.currentOwnerObjVarName = TestCaseGenerator.createVariableName(currentOwnerObjClassName, currentObjID);
    }

    @Override
    public void setCurrentField(ClassInfo fieldClass, FieldInfo field) {
        this.currentFieldClassName = fieldClass.getSimpleName();
        this.currentFieldName = field.getName();
        this.currentFieldAccessString = TestCaseGenerator.createFieldAccessString(this.currentOwnerObjVarName,
                this.currentFieldName);
    }

    @Override
    public void resetCurrentOwner() {
        this.currentOwnerObjClassName = null;
        this.currentOwnerObjVarName = null;
    }

    @Override
    public void resetCurrentField() {
        varValues.put(this.currentFieldAccessString, this.fieldStringValue);
        this.currentFieldName = null;
        this.currentFieldAccessString = null;
        this.currentFieldClassName = null;
        this.fieldStringValue = null;
    }

    @Override
    public boolean isIgnoredField() {
        String fieldSignature = VectorField.createFieldSignature(currentOwnerObjClassName, currentFieldName, this.currentFieldClassName);
        return !vector.isTrackedField(fieldSignature);
    }

    @Override
    public void setMaxIdMap(HashMap<ClassInfo, Integer> maxIdMap) {
        this.maxIdMap = maxIdMap;
    }

    public HashMap<ClassInfo, Integer> getMaxIdMap() {
        return this.maxIdMap;
    }

    public HashMap<String, String> getVarValues() {
        return this.varValues;
    }

}
