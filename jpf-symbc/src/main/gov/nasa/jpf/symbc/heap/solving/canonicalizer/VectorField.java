package gov.nasa.jpf.symbc.heap.solving.canonicalizer;

import korat.finitization.impl.CVElem;
import korat.finitization.impl.FieldDomain;

public class VectorField {

    int value;
    String name;
    String ownerClassName;
    String signature;
    CVElem elem;
    int indexInVector;
    FieldDomain domain;

    public static final int SYMBOLIC = -1;
    public static final int DEFAULT_VALUE = 0;

    public static String createFieldSignature(String ownerClassName, String fieldName, String fieldClassName) {
        return String.format("%s.%s(%s)", ownerClassName, fieldName, fieldClassName);
    }

    public VectorField(CVElem elem, int indexInVector) {
        this.elem = elem;
        this.name = elem.getFieldName();
        this.ownerClassName = elem.getObj().getClass().getSimpleName();
        this.value = DEFAULT_VALUE;
        this.indexInVector = indexInVector;
        this.domain = this.elem.getFieldDomain();
        
        String fieldClassName = this.domain.getClassOfField().getSimpleName();
        this.signature = createFieldSignature(this.ownerClassName, this.name, fieldClassName);
    }

    public int getValue() {
        return this.value;
    }

    public int getIndexInVector() {
        return this.indexInVector;
    }

    public String getFieldName() {
        return this.name;
    }

    public String getOwnerClassName() {
        return this.ownerClassName;
    }

    public String getFieldSignature() {
        return this.signature;
    }

    public FieldDomain getFieldDomain() {
        return this.domain;
    }

    public Class<?> getClassOfField() {
        return this.elem.getFieldDomain().getClassOfField();
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean matchesField(String ownerClassName, String name) {
        return ownerClassName.equals(this.ownerClassName) && name.equals(this.name);
    }

    public void setAsDefaultValue() {
        value = DEFAULT_VALUE;
    }

}
