package korat.finitization.impl;

import korat.finitization.IFieldDomain;

/**
 * @author Aleksandar Milicevic <aca.milicevic@gmail.com>
 */
public abstract class FieldDomain implements IFieldDomain {

    protected Class<?> classOfField;

    /**
     * Used for type checking when assigning class domains for this field
     *
     * @param classOfField -
     *                     type of the field
     */
    FieldDomain(Class<?> classOfField) {
        this.classOfField = classOfField;
    }

    /**
     * @param classOfFieldName -
     *                         fully qualified name of the Class
     * @throws ClassNotFoundException -
     *                                if classOfFieldName is not valid
     */
    FieldDomain(String classOfFieldName) throws ClassNotFoundException {
        this(Class.forName(classOfFieldName, false,
                Finitization.getClassLoader()));
    }

    protected boolean checkClassDomainIndex(int index) {
        return index >= 0 && index < getNumOfClassDomains();
    }

    protected boolean checkObjectIndex(int index) {
        return index >= 0 && index < getNumberOfElements();
    }

    public Class<?> getClassOfField() {
        return classOfField;
    }

    /**
     * Number of all elements in this field domain. These are all elements that
     * can be assigned to a field associated with this field domain in the
     * process of generating test cases.
     *
     * @return overall number of elements in field domain
     */
    public abstract int getNumberOfElements();

    /**
     * Number of class domains contained in this field domain.
     * <p>
     * <p/> Primitive types should return <code>1</code>.
     *
     * @return number of class domains in this field domain
     */
    public abstract int getNumOfClassDomains();

    /**
     * Given the index of object in this field domain, returns index of that
     * object in its class domain.
     * <p>
     * <p/>For primitive types it's the same as the input
     * parameter <code>objectIndex</code> because primitive type field domains
     * conceptually contain exactly one class domain.
     *
     * @param objectIndex -
     *                    index of object in the field domain
     * @return index of object in its class domain
     */
    public abstract int getClassDomainIndexFor(int objectIndex);

    public abstract int getIndexOfFirstObjectInNextClassDomain(int objectIndex);

    /**
     * Number of object in the class domain which is at a
     * <code>classDomainIndex</code> position in the list of all class domains
     * within this field domain.
     * <p>
     * <p/> Primitive types should return -1 if
     * <code>classDomainIndex</code> is different than 0, or
     * <code>getNumberOfElements()</code> else.
     *
     * @param classDomainIndex -
     *                         class domain index
     * @return number of objects in domain with index
     * <code>classDomainIndex</code>
     * @see #getNumberOfElements() <br/>
     */
    public abstract int getSizeOfClassDomain(int classDomainIndex);

}
