package gov.nasa.jpf.symbc.heap.visitors;

import java.util.HashMap;

import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.FieldInfo;

public interface SymbolicInputHeapVisitor {

    public void visitedSymbolicReferenceField();

    public void visitedNullReferenceField();

    public void visitedNewReferenceField(int id);

    public void visitedExistentReferenceField(int id);

    public void visitedSymbolicBooleanField(FieldInfo fi, SymbolicInteger symbolicBoolean);

    public void visitedSymbolicIntegerField(FieldInfo fi, SymbolicInteger symbolicInteger);

    public void visitedSymbolicStringField(FieldInfo fi, StringSymbolic symbolicString);

    public void visitedSymbolicLongField(FieldInfo fi, SymbolicInteger symbolicLong);

    public void setCurrentField(ClassInfo fieldClass, FieldInfo field);

    public void setCurrentOwner(ClassInfo ownerObjectClass, int currentObjID);

    public void resetCurrentField();

    public void resetCurrentOwner();

    public boolean isIgnoredField();

    public void setMaxIdMap(HashMap<ClassInfo, Integer> maxIdMap);

}
