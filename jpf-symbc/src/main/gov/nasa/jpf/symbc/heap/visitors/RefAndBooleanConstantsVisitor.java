package gov.nasa.jpf.symbc.heap.visitors;

import java.util.HashMap;

import gov.nasa.jpf.symbc.heap.solving.canonicalizer.VectorStructure;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.visitors.CollectIntegerConstantsVisitor;
import gov.nasa.jpf.vm.FieldInfo;

public class RefAndBooleanConstantsVisitor extends ReferenceFieldOnlyVisitor {

    private PathCondition pathCondition;

    HashMap<Expression, Integer> nonEqualityConstants = new HashMap<Expression, Integer>();
    
    HashMap<Expression, Integer> equalityConstants = new HashMap<Expression, Integer>();

    public RefAndBooleanConstantsVisitor(VectorStructure vector, PathCondition pathCondition) {
        super(vector);
        this.pathCondition = pathCondition;
        setConstants();
    }

    @Override
    public void visitedSymbolicBooleanField(FieldInfo fi, SymbolicInteger symbolicBoolean) {
        Integer value = null;
        if (equalityConstants.containsKey(symbolicBoolean))
            value = equalityConstants.get(symbolicBoolean);
        else if (nonEqualityConstants.containsKey(symbolicBoolean))
            value = nonEqualityConstants.get(symbolicBoolean);
        
        if (value != null)
            vector.setFieldAsConcrete(currentOwnerObjClassName, currentFieldName, value);
        else
            vector.setPrimitiveFieldAsSymbolic(currentOwnerObjClassName, currentFieldName, symbolicBoolean);
    }
    
    @Override
    public void visitedSymbolicIntegerField(FieldInfo fi, SymbolicInteger symbolicInteger) {
        if (equalityConstants.containsKey(symbolicInteger)) {
            Integer value = equalityConstants.get(symbolicInteger);
            vector.setFieldAsConcrete(currentOwnerObjClassName, currentFieldName, value);
        }
        else {
            vector.setPrimitiveFieldAsSymbolic(currentOwnerObjClassName, currentFieldName, symbolicInteger);
        }
    }

    private void setConstants() {
        if (pathCondition != null) {
            CollectIntegerConstantsVisitor visitor = new CollectIntegerConstantsVisitor();
            pathCondition.accept(visitor);
            this.equalityConstants = visitor.getEqulityConstants();
            this.nonEqualityConstants = visitor.getNonEqulityConstants();
        }
    }
    
}
