package gov.nasa.jpf.symbc.heap.symbolicinput;

import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.lang3.tuple.ImmutablePair;

import gov.nasa.jpf.symbc.heap.HeapNode;
import gov.nasa.jpf.symbc.heap.visitors.SymbolicInputHeapVisitor;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

public class SymbolicReferenceInput {

    public static final int NULL = -1;
    public static final int SYMBOLIC = -2;

    private HeapNode rootHeapNode;

    private HashMap<ImmutablePair<Integer, String>, Integer> fieldToReferenceIndex;

    private HashMap<ImmutablePair<Integer, String>, Expression> fieldToPrimitiveTypeExpression;

    public SymbolicReferenceInput() {
        this.fieldToReferenceIndex = new HashMap<ImmutablePair<Integer, String>, Integer>();
        this.fieldToPrimitiveTypeExpression = new HashMap<ImmutablePair<Integer, String>, Expression>();
    }

    public SymbolicReferenceInput(HashMap<ImmutablePair<Integer, String>, Integer> fieldToRefIndex,
            HashMap<ImmutablePair<Integer, String>, Expression> fieldToPrimExpr, HeapNode rootHeapNode) {
        this.fieldToReferenceIndex = new HashMap<ImmutablePair<Integer, String>, Integer>(fieldToRefIndex);
        this.fieldToPrimitiveTypeExpression = new HashMap<ImmutablePair<Integer, String>, Expression>(fieldToPrimExpr);
        this.rootHeapNode = rootHeapNode;
    }

    public SymbolicReferenceInput makeShallowCopy() {
        return new SymbolicReferenceInput(this.fieldToReferenceIndex, this.fieldToPrimitiveTypeExpression,
                this.rootHeapNode);
    }

    public Integer getReferenceField(Integer ownerIndex, FieldInfo field) {
        String fieldName = field.getName();
        ImmutablePair<Integer, String> fieldDescriptor = new ImmutablePair<Integer, String>(ownerIndex, fieldName);
        return fieldToReferenceIndex.get(fieldDescriptor);
    }

    public void addReferenceField(Integer ownerIndex, String fieldName, Integer valueIndex) {
        ImmutablePair<Integer, String> fieldDescriptor = new ImmutablePair<Integer, String>(ownerIndex, fieldName);
        fieldToReferenceIndex.put(fieldDescriptor, valueIndex);
    }

    public Expression getPrimitiveSymbolicField(Integer ObjIndex, FieldInfo field) {
        String fieldName = field.getName();
        ImmutablePair<Integer, String> fieldDescriptor = new ImmutablePair<Integer, String>(ObjIndex, fieldName);
        return fieldToPrimitiveTypeExpression.get(fieldDescriptor);
    }

    public void addPrimitiveSymbolicField(Integer ObjIndex, String fieldName, Expression symbolicVar) {
        ImmutablePair<Integer, String> fieldDescriptor = new ImmutablePair<Integer, String>(ObjIndex, fieldName);
        fieldToPrimitiveTypeExpression.put(fieldDescriptor, symbolicVar);
    }

    public void setRootHeapNode(HeapNode rootHeapNode) {
        this.rootHeapNode = rootHeapNode;
    }

    public HeapNode getRootHeapNode() {
        return this.rootHeapNode;
    }

    public void acceptBFS(SymbolicInputHeapVisitor visitor) {
        HashMap<Integer, Integer> idMap = new HashMap<Integer, Integer>();
        HashMap<ClassInfo, Integer> maxIdMap = new HashMap<ClassInfo, Integer>();

        ThreadInfo ti = VM.getVM().getCurrentThread();
        ClassInfo rootClass = this.rootHeapNode.getType();
        Integer rootIndex = this.rootHeapNode.getIndex();

        idMap.put(rootIndex, 0);
        maxIdMap.put(rootClass, 0);

        LinkedList<Integer> worklist = new LinkedList<Integer>();
        worklist.add(rootIndex);

        while (!worklist.isEmpty()) {
            int currentObjRef = worklist.removeFirst();
            int currentObjID = idMap.get(currentObjRef);
            ElementInfo eiRef = ti.getElementInfo(currentObjRef);
            ClassInfo ownerObjectClass = eiRef.getClassInfo();

            visitor.setCurrentOwner(ownerObjectClass, currentObjID);

            FieldInfo[] instanceFields = ownerObjectClass.getDeclaredInstanceFields();
            for (int i = 0; i < instanceFields.length; i++) {
                FieldInfo field = instanceFields[i];
                ClassInfo fieldClass = field.getTypeClassInfo();

                visitor.setCurrentField(fieldClass, field);

                // System.out.println("BFS: class of current obj: " + ownerObjectClass.getSimpleName());
                // System.out.println("BFS: current field: " + field.getName());
                // System.out.println("BFS: current type: " + fieldClass.getSimpleName());
                // System.out.println("\n");

                if (visitor.isIgnoredField()) {
                    // System.out.println("Ignored field: " + field.getName());
                    // System.out.println("type: " + fieldClass.getSimpleName());
                    continue;
                }

                if (field.isReference() && !field.getType().equals("java.lang.String")) {

                    Integer fieldIndex = getReferenceField(currentObjRef, field);
                    if (fieldIndex == SYMBOLIC) {
                        visitor.visitedSymbolicReferenceField();
                    } else if (fieldIndex == NULL) {
                        visitor.visitedNullReferenceField();
                    } else if (idMap.containsKey(fieldIndex)) { // previously visited object
                        visitor.visitedExistentReferenceField(idMap.get(fieldIndex) + 1);
                    } else { // first time visited
                        int id = 0;
                        if (maxIdMap.containsKey(fieldClass))
                            id = maxIdMap.get(fieldClass) + 1;

                        idMap.put(fieldIndex, id);
                        maxIdMap.put(fieldClass, id);
                        visitor.visitedNewReferenceField(id + 1);
                        worklist.add(fieldIndex);
                    }
                } else {
                    Expression symbolicPrimitive = getPrimitiveSymbolicField(currentObjRef, field);
                    if (symbolicPrimitive instanceof SymbolicInteger) {
                        SymbolicInteger symbolicInteger = (SymbolicInteger) symbolicPrimitive;
                        if (field.isBooleanField()) {
                            visitor.visitedSymbolicBooleanField(field, symbolicInteger);
                        } else if (field.isLongField()) {
                            visitor.visitedSymbolicLongField(field, symbolicInteger);
                        } else if (field.isIntField()) {
                            visitor.visitedSymbolicIntegerField(field, symbolicInteger);
                        } else {
                            assert (false); // ERROR!
                        }
                    } else if (symbolicPrimitive instanceof StringSymbolic) {
                        visitor.visitedSymbolicStringField(field, (StringSymbolic) symbolicPrimitive);
                    } else {
                        assert (false); // ERROR!
                    }
                }
                visitor.resetCurrentField();
            }
            visitor.resetCurrentOwner();
        }
        visitor.setMaxIdMap(maxIdMap);
    }

}
