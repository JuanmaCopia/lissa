package korat.finitization.impl;

import korat.finitization.*;
import korat.loading.InstrumentingClassLoader;
import korat.utils.RandomStrings;
import symsolve.candidates.PredicateChecker;

import java.util.*;


public class Finitization implements IFinitization {

    private static final ClassLoader classLoader = new InstrumentingClassLoader();
    private final Class<?> rootClass;
    private final Map<Class<?>, Map<String, IFieldDomain>> domainsMap = new HashMap<>();
    private final Set<ObjSet> objSets = new LinkedHashSet<>();
    private final List<CVElem> vectorDescriptor = new ArrayList<>();
    private final StateSpace stateSpace = new StateSpace();
    private final Map<String, IntSet> integerFieldsMinMax = new HashMap<>();
    boolean isInitialized = false;
    ObjSet rootObjectSet;
    Object rootObject;


    public Finitization(Class<?> rootClass) {
        this.rootClass = rootClass;
        rootObjectSet = new ObjSet(rootClass, 1, false);
        objSets.add(rootObjectSet);
        domainsMap.put(rootClass, new LinkedHashMap<>());
    }

    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    public StateSpace getStateSpace() {
        assert (isInitialized);
        return stateSpace;
    }

    public void initialize(PredicateChecker predicateChecker) {
        initializeObjectSets(predicateChecker);
        rootObject = rootObjectSet.getFirstObject();

        addObjectsToVectorDescriptor();
        createStateSpace();
        isInitialized = true;
    }

    private void createStateSpace() {
        stateSpace.setStructureList(vectorDescriptor.toArray(new CVElem[0]));
        stateSpace.setRootObject(rootObject);
        stateSpace.setRootObjectSet(rootObjectSet);
        stateSpace.initialize();
    }

    private void addObjectsToVectorDescriptor() {
        for (ObjSet objectSet : objSets) {
            for (Object obj : objectSet.getAllInstances()) {
                addFieldsToVectorDescriptor(obj);
            }
        }
    }

    private void initializeObjectSets(PredicateChecker predicateChecker) {
        for (ObjSet objectSet : objSets) {
            objectSet.initializeFieldDomain(predicateChecker);
        }
    }

    private void addFieldsToVectorDescriptor(Object obj) {
        Class<?> cls = obj.getClass();
        Map<String, IFieldDomain> fieldsMap = domainsMap.get(cls);
        if (fieldsMap != null) {
            for (Map.Entry<String, IFieldDomain> e : fieldsMap.entrySet()) {
                String fieldName = e.getKey();
                IFieldDomain fd = e.getValue();
                FieldDomain fieldDomain = (FieldDomain) e.getValue();
                CVElem elem = new CVElem(obj, fieldName, fieldDomain, stateSpace);
                vectorDescriptor.add(elem);
            }
        }
    }

    public IFieldDomain getFieldDomain(Class<?> cls, String fieldName) {
        Map<String, IFieldDomain> fieldsMap = domainsMap.get(cls);
        if (fieldsMap == null)
            return null;
        return fieldsMap.get(fieldName);
    }

    public Map<String, IntSet> getIntegerFieldsMinMaxMap() {
        return integerFieldsMinMax;
    }

    @Override
    public Class<?> getRootClass() {
        return rootClass;
    }

    @Override
    public IIntSet createIntSet(int min, int diff, int max) {
        return new IntSet(min, diff, max);
    }

    @Override
    public IIntSet createIntSet(int min, int max) {
        return new IntSet(min, max);
    }

    @Override
    public IIntSet createIntSet(int singleValue) {
        return new IntSet(singleValue);
    }

    @Override
    public IBooleanSet createBooleanSet() {
        return new BooleanSet();
    }

    @Override
    public IByteSet createByteSet(byte min, byte diff, byte max) {
        return new ByteSet(min, diff, max);
    }

    @Override
    public IByteSet createByteSet(byte min, byte max) {
        return new ByteSet(min, max);
    }

    @Override
    public IByteSet createByteSet(byte singleValue) {
        return new ByteSet(singleValue);
    }

    @Override
    public IShortSet createShortSet(short min, short diff, short max) {
        return new ShortSet(min, diff, max);
    }

    @Override
    public IShortSet createShortSet(short min, short max) {
        return new ShortSet(min, max);
    }

    @Override
    public IShortSet createShortSet(short singleValue) {
        return new ShortSet(singleValue);
    }

    @Override
    public ILongSet createLongSet(long min, long diff, long max) {
        return new LongSet(min, diff, max);
    }

    @Override
    public ILongSet createLongSet(long min, long max) {
        return new LongSet(min, max);
    }

    @Override
    public ILongSet createLongSet(long singleValue) {
        return new LongSet(singleValue);
    }

    @Override
    public IDoubleSet createDoubleSet(double min, double diff, double max) {
        return new DoubleSet(min, diff, max);
    }

    @Override
    public IDoubleSet createDoubleSet(double min, double max) {
        return new DoubleSet(min, max);
    }

    @Override
    public IDoubleSet createDoubleSet(double singleValue) {
        return new DoubleSet(singleValue);
    }

    @Override
    public IFloatSet createFloatSet(float min, float diff, float max) {
        return new FloatSet(min, diff, max);
    }

    @Override
    public IFloatSet createFloatSet(float min, float max) {
        return new FloatSet(min, max);
    }

    @Override
    public IFloatSet createFloatSet(float singleValue) {
        return new FloatSet(singleValue);
    }

    @Override
    public StringSet createStringSet(Set<String> set) {
        return new StringSet(set);
    }

    @Override
    public StringSet createRandomStringSet(int setSize, int minLength, int maxLength) {
        return new StringSet(RandomStrings.generateRandomStringSet(setSize, minLength, maxLength));
    }

    @Override
    public IObjSet createObjSet(Class<?> fieldBaseClass, int numOfInstances, boolean includeNull) {
        return new ObjSet(fieldBaseClass, numOfInstances, includeNull);
    }

    public void set(Class<?> cls, String fieldName, IFieldDomain fieldDomain) {
        if (!domainsMap.containsKey(cls))
            domainsMap.put(cls, new LinkedHashMap<>());

        Map<String, IFieldDomain> fieldsMap = domainsMap.get(cls);

        fieldsMap.put(fieldName, fieldDomain);

        if (fieldDomain instanceof IntSet) {
            integerFieldsMinMax.put(createFieldName(cls, fieldName), (IntSet) fieldDomain);
        } else if (fieldDomain instanceof ObjSet) {
            objSets.add((ObjSet) fieldDomain);
        }
    }

    private String createFieldName(Class<?> cls, String fieldName) {
        return cls.getSimpleName() + "." + fieldName;
    }

    @Override
    public Object getRootObject() {
        return stateSpace.getRootObject();
    }

    public Set<Class<?>> getClasses() {
        return domainsMap.keySet();
    }

    public List<String> getFieldNames(Class<?> cls) {
        Map<String, IFieldDomain> fieldDomains = domainsMap.get(cls);
        if (fieldDomains == null)
            return new ArrayList<>();

        Set<String> fieldNames = fieldDomains.keySet();
        return new ArrayList<>(fieldNames);
    }

    public void includeRootObjectInObjectSet(ObjSet objSet) {
        objSet.replaceFirstObject(rootObject);
    }

    public HashMap<String, Integer> getScopes() {
        HashMap<String, Integer> scopes = new HashMap<>();
        CVElem[] structureList = stateSpace.getStructureList();
        for (CVElem cvElem : structureList) {
            FieldDomain fieldDomain = cvElem.getFieldDomain();
            if (!fieldDomain.isPrimitiveType()) {
                String classSimpleName = fieldDomain.getClassOfField().getSimpleName();
                if (!scopes.containsKey(classSimpleName)) {
                    int bound = fieldDomain.getNumberOfElements();
                    if (((ObjSet) fieldDomain).isNullAllowed())
                        bound--;
                    scopes.put(classSimpleName, bound);
                }
            }
        }
        return scopes;
    }

}
