package gov.nasa.jpf.symbc.heap.solving.canonicalizer;

import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.heap.symbolicinput.SymbolicReferenceInput;
import gov.nasa.jpf.symbc.heap.visitors.ReferenceFieldOnlyVisitor;
import korat.finitization.impl.CVElem;
import symsolve.vector.SymSolveVector;


public class Canonicalizer {

	
    VectorStructure vector;
    

    public Canonicalizer(CVElem[] structureList) {
        vector = new VectorStructure(structureList);
    }

    public SymSolveVector createVector(SymbolicInputHeap symInputHeap) {
    	vector.resetVector();
        SymbolicReferenceInput symRefInput = symInputHeap.getImplicitInputThis();
        ReferenceFieldOnlyVisitor visitor = new ReferenceFieldOnlyVisitor(vector);
        symRefInput.acceptBFS(visitor);
        return new SymSolveVector(vector.getVectorAsIntArray(), vector.getFixedIndices());
    }

    public VectorStructure getVector() {
        return vector;
    }

}
