package korat.finitization.impl;

import korat.finitization.IObjSet;
import korat.testing.ITester;
import symsolve.candidates.PredicateChecker;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ObjSet extends FieldDomain implements IObjSet {

    private final Map<Object, Integer> objectToIndex = new HashMap<>();
    Class<?> classOfField;
    int numOfObjects;
    Object[] fieldDomainValues;
    List<Object> objects = new LinkedList<>();
    boolean includesNull;

    public ObjSet(Class<?> classOfField, int numOfObjects, boolean includesNull) {
        super(classOfField);
        this.classOfField = classOfField;
        this.includesNull = includesNull;
        this.numOfObjects = numOfObjects;
    }

    public void initializeFieldDomain(PredicateChecker predicateChecker) {
        allocateObjects(predicateChecker);
        setUpFieldDomain();
        createObjectToIndexMap();
    }

    private void allocateObjects(PredicateChecker predicateChecker) {
        for (int i = 0; i < numOfObjects; i++) {
            Object obj;
            try {
                Constructor<?> constructor = classOfField.getConstructor(ITester.class);
                obj = constructor.newInstance(predicateChecker);
                objects.add(obj);
            } catch (Exception e) {
                String msg = "{0}: object of class {1} cannot be created";
                msg = MessageFormat.format(msg, e.getMessage(), classOfField.getName());
                System.out.println(msg);
                e.printStackTrace();
                throw new RuntimeException(msg, e);
            }
        }
    }

    private void setUpFieldDomain() {
        if (includesNull) {
            LinkedList<Object> objectsCopy = new LinkedList<>(objects);
            objectsCopy.addFirst(null);
            fieldDomainValues = objectsCopy.toArray();
        } else {
            fieldDomainValues = objects.toArray();
        }
    }

    private void createObjectToIndexMap() {
        for (int i = 0; i < fieldDomainValues.length; i++) {
            Object obj = fieldDomainValues[i];
            if (obj != null)
                objectToIndex.put(obj, i);
        }
    }

    public List<Object> getAllInstances() {
        return objects;
    }

    public Object getObject(int index) {
        return fieldDomainValues[index];
        /*return objects.get(index);*/
    }

    public int getIndexInFieldDomain(Object obj) {
        return objectToIndex.get(obj);
    }

    public Object getFirstObject() {
        if (includesNull)
            return objects.get(1);
        return objects.get(0);
    }


    @Override
    public boolean isNullAllowed() {
        return includesNull;
    }


    @Override
    public void replaceFirstObject(Object rootObject) {
        objects.set(0, rootObject);
        if (includesNull)
            fieldDomainValues[1] = rootObject;
        else
            fieldDomainValues[0] = rootObject;
    }

    @Override
    public Class<?> getClassOfField() {
        return classOfField;
    }

    @Override
    public int getNumberOfElements() {
        return fieldDomainValues.length;
    }

    @Override
    public int getNumOfClassDomains() {
        assert (false);
        return 1;
    }

    @Override
    public int getClassDomainIndexFor(int objectIndex) {
        assert (false);
        return 0;
    }

    @Override
    public int getIndexOfFirstObjectInNextClassDomain(int objectIndex) {
        assert (false);
        return 0;
    }

    @Override
    public int getSizeOfClassDomain(int classDomainIndex) {
        assert (false);
        return 0;
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    @Override
    public boolean isArrayType() {
        return false;
    }

}
