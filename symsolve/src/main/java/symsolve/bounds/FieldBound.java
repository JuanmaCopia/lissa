package symsolve.bounds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FieldBound {

    private transient final String fieldName;
    private transient final ClassBound belongingClassBound;
    Map<Integer, Set<Integer>> fieldBounds = new HashMap<>();


    public FieldBound(String fieldName, ClassBound belongingClassBound) {
        this.fieldName = fieldName;
        this.belongingClassBound = belongingClassBound;
    }

    public void addBound(int ownerObjectID, int fieldValue) {
        if (!fieldBounds.containsKey(ownerObjectID))
            fieldBounds.put(ownerObjectID, new HashSet<>());
        Set<Integer> fieldValues = fieldBounds.get(ownerObjectID);
        fieldValues.add(fieldValue);
    }

    public Set<Integer> getTargetLabelSet(Set<Integer> thisLabelSet) {
        Set<Integer> allowedValues = new HashSet<>();
        for (Integer label : thisLabelSet) {
            allowedValues.addAll(fieldBounds.get(label));
        }
        return allowedValues;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Set<Integer>> e : fieldBounds.entrySet()) {
            sb.append(String.format("\t\t%d : %s\n", e.getKey(), e.getValue().toString()));
        }
        return sb.toString();
    }

}
