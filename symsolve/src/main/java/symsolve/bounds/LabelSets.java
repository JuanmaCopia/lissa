package symsolve.bounds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LabelSets {

    Bounds bounds;
    Map<Object, Set<Integer>> labelSetMap = new HashMap<>();
    Map<Object, Integer> referenceCount = new HashMap<>();


    public LabelSets(Bounds bounds) {
        this.bounds = bounds;
    }

    public Set<Integer> get(Object object) {
        return labelSetMap.get(object);
    }

    public void put(Object object, Set<Integer> labelSet) {
        labelSetMap.put(object, labelSet);
        if (referenceCount.containsKey(object)) {
            referenceCount.put(object, referenceCount.get(object) + 1);
        } else {
            referenceCount.put(object, 1);
        }
    }

    public boolean contains(Object object) {
        return labelSetMap.containsKey(object);
    }

    public void remove(Object object) {
        if (referenceCount.containsKey(object)) {
            int refCount = referenceCount.get(object);
            if (refCount > 1)
                referenceCount.put(object, referenceCount.get(object) - 1);
            else {
                labelSetMap.remove(object);
                referenceCount.remove(object);
            }
        }
    }

    public Set<Integer> calculateTargetLabelSet(Object thisObject, String fieldName) {
        Set<Integer> thisLabelSet = labelSetMap.get(thisObject);
        Set<Integer> targetLabelSet = bounds.getTargetLabelSet(thisObject.getClass(), fieldName, thisLabelSet);
        targetLabelSet.remove(0);
        return targetLabelSet;
    }

    public Set<Integer> calculateSetIntersection(Set<Integer> thisLabelSet, Set<Integer> newValueLabelSet) {
        Set<Integer> intersection = new HashSet<>(thisLabelSet);
        intersection.retainAll(newValueLabelSet);
        return intersection;
    }

    public void clear() {
        labelSetMap.clear();
        referenceCount.clear();
    }

    public void increaseRefCount(Object object) {
        referenceCount.put(object, referenceCount.get(object) + 1);
    }
}
