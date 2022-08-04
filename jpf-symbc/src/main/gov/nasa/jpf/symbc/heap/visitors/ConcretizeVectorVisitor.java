package gov.nasa.jpf.symbc.heap.visitors;

import java.util.HashMap;
import java.util.Map;

import gov.nasa.jpf.symbc.heap.SymHeapHelper;
import gov.nasa.jpf.symbc.heap.solving.canonicalizer.VectorStructure;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.vm.FieldInfo;

public class ConcretizeVectorVisitor extends ReferenceFieldOnlyVisitor {

    private PathCondition pathCondition;

    Map<String, Object> primitiveVariableSolutions = new HashMap<String, Object>();

    public ConcretizeVectorVisitor(VectorStructure vector, PathCondition pathCondition) {
        super(vector);
        this.pathCondition = pathCondition;
    }

    @Override
    public void visitedSymbolicIntegerField(FieldInfo field, SymbolicInteger symbolicInteger) {
        setIntegerFieldValueInVector(symbolicInteger);
    }

    @Override
    public void visitedSymbolicBooleanField(FieldInfo field, SymbolicInteger symbolicBoolean) {
        setIntegerFieldValueInVector(symbolicBoolean);
    }

    private void setIntegerFieldValueInVector(SymbolicInteger symbolicInteger) {
        int value = SymHeapHelper.getSolution(symbolicInteger, this.pathCondition);
        this.vector.setFieldAsConcrete(currentOwnerObjClassName, currentFieldName, value);
    }

//    private Set<Expression> getSymbolicVariables() {
//        Set<Expression> symbolicVariables = null;
//        if (pathCondition != null) {
//            CollectVariableVisitor visitor = new CollectVariableVisitor();
//            pathCondition.accept(visitor);
//            symbolicVariables = visitor.getVariables();
//        }
//        return symbolicVariables;
//    }

}
