package examples.treemap;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.Finitization;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TreeMap {

    public static final boolean RED = false;
    public static final boolean BLACK = true;
    public Entry root;
    public int size = 0;

    public static IFinitization finTreeMap(int nodesNum) {
        IFinitization f = new Finitization(TreeMap.class);
        IObjSet nodes = f.createObjSet(Entry.class, nodesNum, true);
        f.set(TreeMap.class, "root", nodes);
        f.set(TreeMap.class, "size", f.createIntSet(0, nodesNum));
        f.set(Entry.class, "key", f.createIntSet(0, nodesNum - 1));
        f.set(Entry.class, "left", nodes);
        f.set(Entry.class, "right", nodes);
        f.set(Entry.class, "parent", nodes);
        f.set(Entry.class, "color", f.createBooleanSet());
        return f;
    }

    public static IFinitization propertyCheckFinTreeMap(int nodesNum) {
        return finTreeMap(nodesNum + 1);
    }

    public boolean repOK() {
        if (root == null)
            return true;
        if (!isBinTreeWithParentReferences())
            return false;
        if (!isWellColored())
            return false;
        return isSorted();
    }

    public boolean repOKStructure() {
        if (root == null)
            return true;
        Set<Entry> visited = new HashSet<Entry>();
        LinkedList<Entry> worklist = new LinkedList<Entry>();
        visited.add(root);
        worklist.add(root);
        if (root.parent != null)
            return false;

        while (!worklist.isEmpty()) {
            Entry node = worklist.removeFirst();
            Entry left = node.left;
            if (left != null) {
                if (!visited.add(left))
                    return false;
                if (left.parent != node)
                    return false;
                worklist.add(left);
            }
            Entry right = node.right;
            if (right != null) {
                if (!visited.add(right))
                    return false;
                if (right.parent != node)
                    return false;
                worklist.add(right);
            }
        }
        return visited.size() == size;
    }

    public boolean isBinTreeWithParentReferences() {
        Set<Entry> visited = new HashSet<Entry>();
        LinkedList<Entry> worklist = new LinkedList<Entry>();
        visited.add(root);
        worklist.add(root);
        if (root.parent != null)
            return false;

        while (!worklist.isEmpty()) {
            Entry node = worklist.removeFirst();
            Entry left = node.left;
            if (left != null) {
                if (!visited.add(left))
                    return false;
                if (left.parent != node)
                    return false;
                worklist.add(left);
            }
            Entry right = node.right;
            if (right != null) {
                if (!visited.add(right))
                    return false;
                if (right.parent != node)
                    return false;
                worklist.add(right);
            }
        }
        return visited.size() == size;
    }

    public boolean isWellColored() {
        if (root.color != BLACK)
            return false;
        LinkedList<Entry> worklist = new LinkedList<Entry>();
        worklist.add(root);
        while (!worklist.isEmpty()) {
            Entry current = worklist.removeFirst();
            Entry cl = current.left;
            Entry cr = current.right;
            if (current.color == RED) {
                if (cl != null && cl.color == RED)
                    return false;
                if (cr != null && cr.color == RED)
                    return false;
            }
            if (cl != null)
                worklist.add(cl);
            if (cr != null)
                worklist.add(cr);
        }
        int numberOfBlack = -1;
        LinkedList<Pair<Entry, Integer>> worklist2 = new LinkedList<Pair<Entry, Integer>>();
        worklist2.add(new Pair<Entry, Integer>(root, 0));
        while (!worklist2.isEmpty()) {
            Pair<Entry, Integer> p = worklist2.removeFirst();
            Entry e = p.first();
            int n = p.second();
            if (e != null && e.color == BLACK)
                n++;
            if (e == null) {
                if (numberOfBlack == -1)
                    numberOfBlack = n;
                else if (numberOfBlack != n)
                    return false;
            } else {
                worklist2.add(new Pair<Entry, Integer>(e.left, n));
                worklist2.add(new Pair<Entry, Integer>(e.right, n));
            }
        }
        return true;
    }

    private boolean isSorted() {
        return isSorted(root, null, null);
    }

    private boolean isSorted(Entry n, Integer min, Integer max) {
        if ((min != null && n.key <= (min)) || (max != null && n.key >= (max)))
            return false;

        if (n.left != null)
            if (!isSorted(n.left, min, n.key))
                return false;
        if (n.right != null)
            return isSorted(n.right, n.key, max);
        return true;
    }

    public boolean shouldFailProperty() {
        if (root != null)
            return root.color == RED;
        return false;
    }

    private class Pair<T, U> {
        private final T a;
        private final U b;

        public Pair(T a, U b) {
            this.a = a;
            this.b = b;
        }

        public T first() {
            return a;
        }

        public U second() {
            return b;
        }
    }

}
