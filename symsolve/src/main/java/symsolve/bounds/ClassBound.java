package symsolve.bounds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


public class ClassBound {

    private transient final Class<?> cls;
    Map<String, FieldBound> fieldBoundMap = new HashMap<>();


    public ClassBound(Class<?> cls, List<String> fieldNames) {
        this.cls = cls;
        initializeFieldBoundMap(fieldNames);
    }

    private void initializeFieldBoundMap(List<String> fieldNames) {
        if (fieldNames != null) {
            for (String fieldName : fieldNames) {
                if (!fieldBoundMap.containsKey(fieldName))
                    fieldBoundMap.put(fieldName, new FieldBound(fieldName, this));
            }
        }
    }

    public void addBound(String fieldName, int ownerObjectID, int fieldValue) {
        FieldBound fieldBound = fieldBoundMap.get(fieldName);
        fieldBound.addBound(ownerObjectID, fieldValue);
    }

    public FieldBound getFieldBounds(String fieldName) {
        if (!fieldBoundMap.containsKey(fieldName))
            throw new NoSuchElementException(String.format("The field %s is not in bounds of class %s", fieldName, cls.getName()));
        return fieldBoundMap.get(fieldName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, FieldBound> e : fieldBoundMap.entrySet()) {
            sb.append(String.format("\tField: %s: \n", e.getKey()));
            sb.append(e.getValue().toString());
        }
        return sb.toString();
    }

}
