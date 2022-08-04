package symsolve.candidates.traversals;

import symsolve.candidates.traversals.visitors.CandidateVisitor;

public interface CandidateTraversal {

    void traverse(int[] vector, CandidateVisitor visitor);

}
