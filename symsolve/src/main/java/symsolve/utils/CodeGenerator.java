package symsolve.utils;

import korat.finitization.impl.*;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class CodeGenerator {


    HashMap<Class<?>, Integer> maxIdMap = new HashMap<Class<?>, Integer>();
    HashMap<String, String> fieldVariableValues = new HashMap<String, String>();

    StateSpace stateSpace;

    Class<?> rootClass;


    public CodeGenerator(StateSpace stateSpace, Class<?> rootClass) {
        this.stateSpace = stateSpace;
        this.rootClass = rootClass;
    }

    public String generateStructureCode(int[] vector) {
        calculateFieldValues(vector);
        String result = createCodeOfObjectDefinitions();
        result += createCodeOfFieldAssignations();
        return result;
    }

    private void calculateFieldValues(int[] vector) {
        this.fieldVariableValues.clear();
        this.maxIdMap.clear();
        this.maxIdMap.put(rootClass, 0);
        CVElem[] structureList = stateSpace.getStructureList();
        Object root = stateSpace.getRootObject();
        IdentityHashMap<Object, Integer> idMap = new IdentityHashMap<Object, Integer>();
        idMap.put(root, 0);
        LinkedList<Object> worklist = new LinkedList<Object>();
        worklist.add(root);

        while (!worklist.isEmpty()) {
            Object currentObject = worklist.removeFirst();
            int objId = idMap.get(currentObject);
            Class<? extends Object> currentClass = currentObject.getClass();
            String currentClassName = currentClass.getSimpleName();
            String objectVariableName = createVariableName(currentClassName, objId);

//            System.out.println("\nCurrent object:" + objectVariableName);

            int[] fieldIndices = stateSpace.getFieldIndicesFor(currentObject);

            for (int i : fieldIndices) {
                CVElem elem = structureList[i];
                int fieldDomainIndex = vector[i];
                FieldDomain fieldDomain = elem.getFieldDomain();
                Class<?> clsOfField = fieldDomain.getClassOfField();


                String fieldTypeName = clsOfField.getSimpleName();
                String stringFieldValue = "";

                String fieldName = elem.getFieldName();
                String fieldVariableName = createFieldAccessString(objectVariableName, fieldName);

//                System.out.println("\tVisiting:" + fieldVariableName);

                if (!fieldDomain.isPrimitiveType() && !fieldDomain.isArrayType()) {

                    ObjSet set = (ObjSet) fieldDomain;
                    Object fieldValue = set.getObject(fieldDomainIndex);

                    if (fieldValue == null) {
//                        System.out.println("\t\tAlready visited object: " + stringFieldValue);
                        stringFieldValue = "null";
                    } else if (idMap.containsKey(fieldValue)) {
                        int id = idMap.get(fieldValue);
                        stringFieldValue = createVariableName(fieldTypeName, id);
//                        System.out.println("\t\tAlready visited object: " + stringFieldValue);
                    } else {
                        int id = 0;
                        if (this.maxIdMap.containsKey(clsOfField))
                            id = this.maxIdMap.get(clsOfField) + 1;
                        this.maxIdMap.put(clsOfField, id);
                        idMap.put(fieldValue, id);

                        stringFieldValue = createVariableName(fieldTypeName, id);
//                        System.out.println("\t\tNew visited object: " + stringFieldValue);

                        worklist.add(fieldValue);
                    }

                } else {
                    stringFieldValue = getStrigValueOfPrimitiveField(fieldDomain, fieldDomainIndex);
                }

                fieldVariableValues.put(fieldVariableName, stringFieldValue);
            }
        }
    }

    private String createCodeOfObjectDefinitions() {
        String result = "";
        for (Entry<Class<?>, Integer> entry : maxIdMap.entrySet()) {
            Class<?> cls = entry.getKey();
            Integer maxId = entry.getValue();
            result += createCodeDefinitionsForClass(cls, maxId);
        }
        return result;
    }

    private String createCodeOfFieldAssignations() {
        String result = "";
        for (Entry<String, String> entry : fieldVariableValues.entrySet()) {
            String field = entry.getKey();
            String fieldValue = entry.getValue();
            result += String.format("%s = %s;\n", field, fieldValue);
        }
        return result;
    }

    private String createVariableName(String className, int id) {
        return className.toLowerCase() + "_" + id;
    }

    private String createFieldAccessString(String ownerObject, String fieldName) {
        return ownerObject + "." + fieldName;
    }

    private String getStrigValueOfPrimitiveField(FieldDomain fieldDomain, int fieldDomainIndex) {
        Class<?> clsOfField = fieldDomain.getClassOfField();
        if (clsOfField == int.class) {

            IntSet set = (IntSet) fieldDomain;
            int value = set.getInt(fieldDomainIndex);
            return Integer.toString(value);

        } else if (clsOfField == boolean.class) {

            BooleanSet set = (BooleanSet) fieldDomain;
            boolean value = set.getBoolean(fieldDomainIndex);
            return Boolean.toString(value);

        } else if (clsOfField == String.class) {

            StringSet set = (StringSet) fieldDomain;
            String value = set.getString(fieldDomainIndex);
            return String.format("\"%s\"", value);

        } else if (clsOfField == byte.class) {

            ByteSet set = (ByteSet) fieldDomain;
            byte value = set.getByte(fieldDomainIndex);
            return Byte.toString(value);

        } else if (clsOfField == double.class) {

            DoubleSet set = (DoubleSet) fieldDomain;
            double value = set.getDouble(fieldDomainIndex);
            return Double.toString(value);

        } else if (clsOfField == float.class) {

            FloatSet set = (FloatSet) fieldDomain;
            float value = set.getFloat(fieldDomainIndex);
            return Float.toString(value);

        } else if (clsOfField == long.class) {

            LongSet set = (LongSet) fieldDomain;
            long value = set.getLong(fieldDomainIndex);
            return Long.toString(value);

        } else if (clsOfField == short.class) {

            ShortSet set = (ShortSet) fieldDomain;
            short value = set.getShort(fieldDomainIndex);
            return Short.toString(value);

        } else {
            assert (false);
        }
        return null;
    }

    private String createCodeDefinitionsForClass(Class<?> cls, Integer maxId) {
        String result = "";
        String className = cls.getSimpleName();
        for (int i = 0; i <= maxId; i++) {
            result += createCodeOfConstructorCall(className, i);
        }
        return result;
    }

    private String createCodeOfConstructorCall(String className, int id) {
        String variableName = createVariableName(className, id);
        return String.format("%s %s = new %s();\n", className, variableName, className);
    }

}
