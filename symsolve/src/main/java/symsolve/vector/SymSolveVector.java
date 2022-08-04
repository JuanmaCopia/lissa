package symsolve.vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that holds the representations of partially symbolic vectors.
 *
 * @author Juan Manuel Copia
 */
public class SymSolveVector {

    public static final int SYMBOLIC = -1;
    public static final int NULL = 0;
    int size;
    int[] concreteVector;
    int[] partialVector;
    Set<Integer> fixedIndices = new HashSet<Integer>();

    /**
     * Creates a vector a SymKoratVector instance holding the vector
     * representations.
     *
     * @param concreteVector The concrete vector representation (Symbolic fields
     *                       represented with 0).
     * @param fixedIndices   The set of Fixed indices in this vector.
     */
    public SymSolveVector(int[] concreteVector, Set<Integer> fixedIndices) {
        this.size = concreteVector.length;
        this.concreteVector = concreteVector;
        this.fixedIndices = fixedIndices;
        this.partialVector = createPartialVector();
    }

    public SymSolveVector(int vectorSize) {
        this.size = vectorSize;
        this.concreteVector = new int[vectorSize];
        this.partialVector = createPartialVector();
    }

    /**
     * Creates a vector a SymKoratVector instance by calculating the different
     * vector representations from a string partial representation (symbolic fields
     * represented by -1).
     *
     * @param vector the string representation of the partial vector.
     */
    public SymSolveVector(String vector) {
        String[] vectorValues = vector.split(",");
        this.size = vectorValues.length;

        this.concreteVector = new int[size];
        this.partialVector = new int[size];
        for (int i = 0; i < size; i++) {
            int elem = Integer.parseInt(vectorValues[i].trim());
            this.partialVector[i] = elem;
            if (elem == SymSolveVector.SYMBOLIC) {
                this.concreteVector[i] = SymSolveVector.NULL;
            } else {
                this.concreteVector[i] = elem;
                this.fixedIndices.add(i);
            }
        }
    }

    private int[] createPartialVector() {
        int[] partialVector = new int[this.size];
        for (int i = 0; i < this.size; i++) {
            if (this.fixedIndices.contains(i))
                partialVector[i] = this.concreteVector[i];
            else
                partialVector[i] = SYMBOLIC;
        }
        return partialVector;
    }

    /**
     * Returns the partial vector representation of this vector. i.e. symbolic
     * values are encoded with -1.
     *
     * @return A clone of the partial vector representation.
     */
    public int[] getPartialVector() {
        return this.partialVector.clone();
    }

    /**
     * Returns the concrete vector representation of this vector. i.e. symbolic
     * values are encoded with 0.
     *
     * @return A clone of the concrete vector representation.
     */
    public int[] getConcreteVector() {
        return this.concreteVector.clone();
    }

    public void setConcreteVector(int[] newConcreteVector) {
        this.concreteVector = newConcreteVector;
    }

    /**
     * Returns the set of Fixed indices in this vector.
     *
     * @return A clone of the fixed indices of this vector.
     */
    public Set<Integer> getFixedIndices() {
        return new HashSet<Integer>(this.fixedIndices);
    }

    public boolean isSymbolicIndex(int index) {
        return !this.fixedIndices.contains(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PartialVector: " + Arrays.toString(this.partialVector));
        sb.append("\nConcreteVector: " + Arrays.toString(this.concreteVector));
        sb.append("\nFixed Indices: " + this.fixedIndices.toString());
        return sb.toString();
    }
}
