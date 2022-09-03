package heapsolving.treeset.symsolve;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

public class TreeSet {

    public transient TreeMap m; // The backing Map

    public static IFinitization finTreeSet(int nodesNum) {
        IFinitization f = FinitizationFactory.create(TreeSet.class);
        IObjSet treemap = f.createObjSet(TreeMap.class, 1, true);
        f.set(TreeSet.class, "m", treemap);
        IObjSet nodes = f.createObjSet(TreeMap.Entry.class, nodesNum, true);
        f.set(TreeMap.class, "root", nodes);
        f.set(TreeMap.class, "size", f.createIntSet(0, nodesNum));
        f.set(TreeMap.Entry.class, "key", f.createIntSet(0, nodesNum - 1));
        f.set(TreeMap.Entry.class, "left", nodes);
        f.set(TreeMap.Entry.class, "right", nodes);
        f.set(TreeMap.Entry.class, "parent", nodes);
        f.set(TreeMap.Entry.class, "color", f.createBooleanSet());
        return f;
    }

    public boolean repOK() {
        if (m == null)
            return false;
        return m.repOK();
    }
    
    public boolean isBinTreeWithParentReferences() {
        if (m == null)
            return false;
        return m.isBinTreeWithParentReferences();
    }

}
