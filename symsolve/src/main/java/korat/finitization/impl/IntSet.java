package korat.finitization.impl;

import korat.finitization.IIntSet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksandar Milicevic <aca.milicevic@gmail.com>
 */

// TODO: maybe better NumericTypeSet
public class IntSet extends PrimitiveTypeSet implements IIntSet {

    Map<Integer, Integer> valueIndexMap = new HashMap<Integer, Integer>();

    int min = 0;
    int max = 0;

    IntSet(int min, int diff, int max) {
        super(int.class);
        this.min = min;
        this.max = max;
        addRange(min, diff, max);
    }

    public IntSet(int min, int max) {
        this(min, 1, max);
    }

    IntSet(int value) {
        this(value, 1, value);
    }

    public void addInt(int i) {
        primitives.add(i);
    }

    public void addRange(int min, int diff, int max) {
        int i = min;
        int count = 0;
        while (i <= max) {
            primitives.add(i);
            valueIndexMap.put(i, count);
            i += diff;
            count++;
        }
    }

    public void removeInt(int i) {
        primitives.remove(new Integer(i));
    }

    public int getInt(int index) {
        return (Integer) primitives.get(index);
    }

    public int[] getInts() {
        int[] ints = new int[primitives.size()];
        for (int i = 0; i < primitives.size(); i++)
            ints[i] = getInt(i);

        return ints;
    }

    public Map<Integer, Integer> getValueIndexMap() {
        return valueIndexMap;
    }

//    public int getMin() {
//        if (primitives.size() == 0)
//            throw new RuntimeException("Size of int set is zero");
//
//        int min = Integer.MAX_VALUE;
//        for (int i = 0; i < primitives.size(); i++) {
//            int elem = getInt(i);
//            if (elem < min)
//                min = elem;
//        }
//
//        return min;
//    }
//
//    public int getMax() {
//        if (primitives.size() == 0)
//            throw new RuntimeException("Size of int set is zero");
//
//        int max = Integer.MIN_VALUE;
//        for (int i = 0; i < primitives.size(); i++) {
//            int elem = getInt(i);
//            if (elem > max)
//                max = elem;
//        }
//
//        return max;
//    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

}
