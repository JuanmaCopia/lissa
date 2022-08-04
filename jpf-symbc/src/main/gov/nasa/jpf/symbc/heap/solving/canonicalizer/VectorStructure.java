package gov.nasa.jpf.symbc.heap.solving.canonicalizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import gov.nasa.jpf.symbc.numeric.Expression;
import korat.finitization.impl.CVElem;

public class VectorStructure {

    VectorField[] vector;

    HashSet<Integer> fixedIndices = new HashSet<Integer>();

    HashMap<String, Integer> currentIndexMap = new HashMap<String, Integer>();

    HashMap<String, Integer> indexMap = new HashMap<String, Integer>();

    private HashSet<String> fieldSignatures = new HashSet<String>();

    //private HashMap<Integer, Expression> primitiveIndices = new HashMap<Integer, Expression>();

    public VectorStructure(CVElem[] structureList) {
        this.vector = new VectorField[structureList.length];
        for (int i = 0; i < structureList.length; i++) {
            CVElem elem = structureList[i];
            VectorField vectorField = new VectorField(elem, i);
            vector[i] = vectorField;
            String ownerClassName = vectorField.getOwnerClassName();
            if (!indexMap.containsKey(ownerClassName))
                indexMap.put(ownerClassName, i);
            this.currentIndexMap = new HashMap<String, Integer>(this.indexMap);
            this.fieldSignatures.add(vectorField.getFieldSignature());
        }
    }

    public void setFieldAsConcrete(String ownerClassName, String fieldName, int value) {
        VectorField vectorField = retriveVectorField(ownerClassName, fieldName);
        vectorField.setValue(value);
        fixedIndices.add(vectorField.getIndexInVector());
    }

    public void setReferenceFieldAsSymbolic(String ownerClassName, String fieldName) {
        retriveVectorField(ownerClassName, fieldName);
    }

    public void setPrimitiveFieldAsSymbolic(String ownerClassName, String fieldName, Expression exp) {
        retriveVectorField(ownerClassName, fieldName);
        //int index = vectorField.getIndexInVector();
        //primitiveIndices.put(index, exp);
    }

    private VectorField retriveVectorField(String ownerClassName, String fieldName) {
        int vectorIndex = currentIndexMap.get(ownerClassName);
        VectorField vectorField = vector[vectorIndex];
        // if (!vectorField.matchesField(ownerClassName, fieldName)) {
        // 	System.out.println(String.format("%s.%s does not match vector field: %s.%s", ownerClassName, fieldName, vectorField.getOwnerClassName(), vectorField.getFieldName()));
        // }
        assert (vectorField.matchesField(ownerClassName, fieldName));
        currentIndexMap.put(ownerClassName, vectorIndex + 1);
        return vectorField;
    }

    public boolean isTrackedField(String signature) {
        boolean result = fieldSignatures.contains(signature);

        // if (!result) {
        // 	System.out.println(String.format("Signature %s not tracked. Tracked signatures: \n", signature));
        // 	System.out.println(fieldSignatures.toString());
        // }

        return result;
    }

    public void resetVector() {
        resetVectorValues();
        clearIndices();
        resetCurrentIndexMap();
    }

    private void resetCurrentIndexMap() {
        this.currentIndexMap = new HashMap<String, Integer>(this.indexMap);
    }

    private void resetVectorValues() {
        for (VectorField vectorField : this.vector) {
            vectorField.setAsDefaultValue();
        }
    }

    private void clearIndices() {
        this.fixedIndices.clear();
        //this.primitiveIndices.clear();
    }

    public VectorField[] getVector() {
        return this.vector;
    }

    public VectorField getField(int index) {
        return this.vector[index];
    }

    public int[] getVectorAsIntArray() {
        int[] array = new int[this.vector.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = this.vector[i].getValue();
        }
        return array;
    }

    public Set<Integer> getFixedIndices() {
        return this.fixedIndices;
    }
//
//    public HashMap<Integer, Expression> getPrimitiveIndices() {
//        return this.primitiveIndices;
//    }

    public Set<String> getFieldSignatures() {
        return this.fieldSignatures;
    }

}
