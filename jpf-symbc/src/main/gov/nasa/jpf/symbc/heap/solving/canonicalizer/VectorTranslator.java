package gov.nasa.jpf.symbc.heap.solving.canonicalizer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import korat.finitization.impl.FieldDomain;
import korat.finitization.impl.IntSet;
import symsolve.vector.SymSolveVector;

public class VectorTranslator {

    Set<Integer> integerFieldIndices = new HashSet<Integer>();
    
    VectorStructure vector;

    public VectorTranslator(VectorStructure vector) {
        this.vector = vector;
        for (VectorField vectorField : vector.getVector()) {
            if (vectorField.getClassOfField().equals(int.class)) {
                integerFieldIndices.add(vectorField.getIndexInVector());
            }
        }
    }

    public SymSolveVector translateToSymSolveVector() {
        int[] arrayVector = vector.getVectorAsIntArray();
        Set<Integer> fixedIndices = vector.getFixedIndices();

        for (Integer index : this.integerFieldIndices) {
            if (fixedIndices.contains(index)) {
                VectorField field = vector.getField(index);
                arrayVector[index] = convertValueToIndex(field);
            }
        }
        return new SymSolveVector(arrayVector, fixedIndices);
    }
    
    public SymSolveVector translateToSymSolveVector(int[] toTranslate) {
        int[] arrayVector = toTranslate.clone();
        Set<Integer> fixedIndices = vector.getFixedIndices();

        for (Integer index : this.integerFieldIndices) {
            if (fixedIndices.contains(index)) {
                VectorField field = vector.getField(index);
                arrayVector[index] = convertValueToIndex(field);
            }
        }
        return new SymSolveVector(arrayVector, fixedIndices);
    }
    
    public int convertValueToIndex(VectorField field) {
        FieldDomain domain = field.getFieldDomain();
        assert(domain instanceof IntSet);
        IntSet intSet = (IntSet) domain;
        Map<Integer, Integer> valuesToIndex = intSet.getValueIndexMap();
        Integer newValue = valuesToIndex.get(field.getValue());
        if (newValue == null) {
            System.out.println(String.format("Index %d is out of range for field %s", field.getValue(), field.getFieldName()));
            assert(false);
        }
        return newValue;
    }
    
    public int[] translateFromSymSolveVector(int[] symSolveVector) {
        int[] translatedVector = symSolveVector.clone();
        for (Integer index : this.integerFieldIndices) {
            VectorField field = vector.getField(index);
            translatedVector[index] = convertIndexToValue(field, symSolveVector[index]);
        }
        return translatedVector;
    }
    
    public int convertIndexToValue(VectorField field, int index) {
        FieldDomain domain = field.getFieldDomain();
        assert(domain instanceof IntSet);
        IntSet intSet = (IntSet) domain;
        return intSet.getInt(index);
    }

}
