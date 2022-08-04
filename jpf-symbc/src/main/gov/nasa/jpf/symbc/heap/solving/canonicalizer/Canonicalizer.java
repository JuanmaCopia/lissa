package gov.nasa.jpf.symbc.heap.solving.canonicalizer;

import gov.nasa.jpf.symbc.heap.Helper;
import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.heap.symbolicinput.SymbolicReferenceInput;
import gov.nasa.jpf.symbc.heap.visitors.OutputVisitor;
import gov.nasa.jpf.symbc.heap.visitors.ReferenceFieldOnlyVisitor;
import gov.nasa.jpf.symbc.heap.visitors.SymbolicOutputHeapVisitor;
import korat.finitization.impl.CVElem;
import symsolve.vector.SymSolveVector;


public class Canonicalizer {

	
    VectorStructure vector;
    VectorStructure specialPropertyCheckVector;
    

    public Canonicalizer(CVElem[] structureList, CVElem[] propertyCheckStructureList) {
        vector = new VectorStructure(structureList);
        if (propertyCheckStructureList != null)
        	specialPropertyCheckVector = new VectorStructure(propertyCheckStructureList);
    }

    public SymSolveVector createVector(SymbolicInputHeap symInputHeap) {
    	vector.resetVector();
        SymbolicReferenceInput symRefInput = symInputHeap.getImplicitInputThis();
        ReferenceFieldOnlyVisitor visitor = new ReferenceFieldOnlyVisitor(vector);
        symRefInput.acceptBFS(visitor);
        return new SymSolveVector(vector.getVectorAsIntArray(), vector.getFixedIndices());
    }

    public SymSolveVector createPropertyCheckVector(int rootIndex, boolean usePropertyFinitization) {
    	if (usePropertyFinitization) {
    		if (specialPropertyCheckVector != null)
    			return createOutputVector(rootIndex, specialPropertyCheckVector);
    		else
    			throw new RuntimeException("ERROR: special finitization not found");
    	}
    	else
    		return createOutputVector(rootIndex, vector);
    }
    
    private SymSolveVector createOutputVector(int rootIndex, VectorStructure vector) {
    	vector.resetVector();
        SymbolicOutputHeapVisitor visitor = new OutputVisitor(vector);
        Helper.acceptBFS(rootIndex, visitor);
        return new SymSolveVector(vector.getVectorAsIntArray(), vector.getFixedIndices());
    }

    public VectorStructure getVector() {
        return vector;
    }

}
