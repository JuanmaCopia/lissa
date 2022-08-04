package korat.finitization;

public interface IObjSet extends IFieldDomain {


    /**
     * Is null allowed or not.
     *
     * @return is null allowed
     * @see #isNullAllowed()
     */
    boolean isNullAllowed();


    void replaceFirstObject(Object rootObject);

    Object getObject(int index);
}
