package korat.finitization;

import korat.finitization.impl.StringSet;

import java.util.Set;

/**
 * The <code>IFinitization</code> interface is used to set up the bounds
 * for the search.  The <code>Finitization</code> class uses these bounds
 * to build the state space for search.
 *
 * @author korat.team
 */
public interface IFinitization {

    /**
     * Returns <code>Class</code> object of the finitized class
     *
     * @return <code>Class</code> object for root class that is being finitized
     */
    Class<?> getRootClass();


    /**
     * Creates <code>IIntSet</code> according to the given parameters.
     *
     * @param min  minimal element of this set of integers (included)
     * @param diff step, the difference between to elements
     * @param max  maximal element of this set of integers (included)
     * @return created <code>IIntSet</code>
     * @see #createIntSet(int)
     * @see #createIntSet(int, int)
     */
    IIntSet createIntSet(int min, int diff, int max);

    /**
     * Creates <code>IIntSet</code> with the given <code>min</code> and
     * <code>max</code> parameters. The difference between two elements is by
     * default 1. Equivalent of <code>createIntSet(min, 1, max)</code>.
     *
     * @param min minimal element of this set of integers (included)
     * @param max maximal element of this set of integers (included)
     * @return created <code>IIntSet</code>
     * @see #createIntSet(int)
     * @see #createIntSet(int, int, int)
     */
    IIntSet createIntSet(int min, int max);

    /**
     * Creates <code>IIntSet</code> with the single value that is given as the
     * method parameter. Equivalent of
     * <code>createIntSet(value, 1, value)</code>.
     *
     * @param singleValue single value that will be added to <code>IIntSet</code>
     * @return created <code>IIntSet</code>
     * @see #createIntSet(int, int)
     * @see #createIntSet(int, int, int)
     */
    IIntSet createIntSet(int singleValue);

    /**
     * Creates new <code>IBooleanSet</code>
     *
     * @return newly created <code>IBooleanSet</code>
     */
    IBooleanSet createBooleanSet();

    /**
     * @param min  - minimum value to be included in the set
     * @param diff - difference (step) between two consecutive values in the set
     * @param max  - maximum value to be included in the set
     * @return newly created <code>IByteSet</code>
     */
    IByteSet createByteSet(byte min, byte diff, byte max);

    /**
     * Here the step (differecte) between two consecutive values in the set
     * defaults to 1.
     *
     * @param min - minimum value to be included in the set
     * @param max - maximum value to be included in the set
     * @return newly created <code>IByteSet</code>
     */
    IByteSet createByteSet(byte min, byte max);

    /**
     * Creates <code>IByteSet</code> with the single value that is given as
     * the method parameter. Equivalent of
     * <code>createByteSet(value, 1, value)</code>.
     *
     * @param singleValue single value that will be added to <code>IByteSet</code>
     * @return created <code>IByteSet</code>
     * @see #createByteSet(byte, byte)
     * @see #createByteSet(byte, byte, byte)
     */
    IByteSet createByteSet(byte singleValue);

    /**
     * @param min  - minimum value to be included in the set
     * @param diff - difference (step) between two consecutive values in the set
     * @param max  - maximum value to be included in the set
     * @return newly created <code>IShortSet</code>
     */
    IShortSet createShortSet(short min, short diff, short max);

    /**
     * Here the step (differecte) between two consecutive values in the set
     * defaults to 1.
     *
     * @param min - minimum value to be included in the set
     * @param max - maximum value to be included in the set
     * @return newly created <code>IShortSet</code>
     */
    IShortSet createShortSet(short min, short max);

    /**
     * Creates <code>IShortSet</code> with the single value that is given as
     * the method parameter. Equivalent of
     * <code>createShortSet(value, 1, value)</code>.
     *
     * @param singleValue single value that will be added to <code>IShortSet</code>
     * @return created <code>IShortSet</code>
     * @see #createShortSet(short, short)
     * @see #createShortSet(short, short, short)
     */
    IShortSet createShortSet(short singleValue);

    /**
     * @param min  - minimum value to be included in the set
     * @param diff - difference (step) between two consecutive values in the set
     * @param max  - maximum value to be included in the set
     * @return newly created <code>ILongSet</code>
     */
    ILongSet createLongSet(long min, long diff, long max);

    /**
     * Here the step (differecte) between two consecutive values in the set
     * defaults to 1.
     *
     * @param min - minimum value to be included in the set
     * @param max - maximum value to be included in the set
     * @return newly created <code>ILongSet</code>
     */
    ILongSet createLongSet(long min, long max);

    /**
     * Creates <code>ILongSet</code> with the single value that is given as
     * the method parameter. Equivalent of
     * <code>createLongSet(value, 1, value)</code>.
     *
     * @param singleValue single value that will be added to <code>ILongSet</code>
     * @return created <code>ILongSet</code>
     * @see #createLongSet(long, long)
     * @see #createLongSet(long, long, long)
     */
    ILongSet createLongSet(long singleValue);

    /**
     * @param min  - minimum value to be included in the set
     * @param diff - difference (step) between two consecutive values in the set
     * @param max  - maximum value to be included in the set
     * @return newly created <code>IDoubleSet</code>
     */
    IDoubleSet createDoubleSet(double min, double diff, double max);

    /**
     * Here the step (differecte) between two consecutive values in the set
     * defaults to 1.
     *
     * @param min - minimum value to be included in the set
     * @param max - maximum value to be included in the set
     * @return newly created <code>IDoubleSet</code>
     */
    IDoubleSet createDoubleSet(double min, double max);

    /**
     * Creates <code>IDoubleSet</code> with the single value that is given as
     * the method parameter. Equivalent of
     * <code>createDoubleSet(value, 1, value)</code>.
     *
     * @param singleValue single value that will be added to <code>IDoubleSet</code>
     * @return created <code>IDoubletSet</code>
     * @see #createDoubleSet(double, double)
     * @see #createDoubleSet(double, double, double)
     */
    IDoubleSet createDoubleSet(double singleValue);

    /**
     * Here the step (differecte) between two consecutive values in the set
     * defaults to 1.
     *
     * @param min - minimum value to be included in the set
     * @param max - maximum value to be included in the set
     * @return newly created <code>IFloatSet</code>
     */
    IFloatSet createFloatSet(float min, float diff, float max);

    /**
     * Here the step (differecte) between two consecutive values in the set
     * defaults to 1.
     *
     * @param min - minimum value to be included in the set
     * @param max - maximum value to be included in the set
     * @return newly created <code>IFloatSet</code>
     */
    IFloatSet createFloatSet(float min, float max);

    /**
     * Creates <code>IFloatSet</code> with the single value that is given as
     * the method parameter. Equivalent of
     * <code>createFloatSet(value, 1, value)</code>.
     *
     * @param singleValue single value that will be added to <code>IFloatSet</code>
     * @return created <code>IFloatSet</code>
     * @see #createFloatSet(float, float)
     * @see #createFloatSet(float, float, float)
     */
    IFloatSet createFloatSet(float singleValue);

    StringSet createStringSet(Set<String> set);

    StringSet createRandomStringSet(int setSize, int minLength, int maxLength);


    /**
     * Creates <code>IObjSet</code> and automatically creates given number of
     * instances of the same class
     *
     * <p>
     * Equivalent to <br>
     *
     * <pre>
     * IClassDomain c = f.createClassDomain(fieldBaseClass, numOfInstances);
     * IObjSet toReturn = f.createObjSet(fieldBaseClass, includeNull);
     * toReturn.addClassDomain(c);
     * return toReturn;
     * </pre>
     *
     * @param fieldBaseClass -
     *                       class of the field
     * @param numOfInstances -
     *                       instances of the <code>fieldBaseClass</code> that are to be
     *                       created in the class domain of <code>fieldBaseClass</code>
     * @param includeNull    -
     *                       whether to include null in the <code>IObjSet</code>
     * @return newly created <code>IObjSet</code>
     */
    IObjSet createObjSet(Class<?> fieldBaseClass, int numOfInstances, boolean includeNull);


    /**
     * Assigns <code>fieldDomain</code> to the given field (<code>fieldName</code>)
     * of the given class (<code>cls</code>).<br/>
     *
     * <b><u>Note</u></b> that you cannot assign field domain to the field of
     * some class if the class domain (ObjSet) for that class has not been
     * created before (e.g. with the call
     * <code>createObjSet(cls, numOfInstances)</code>)
     *
     * @param cls         -
     *                    <code>Class</code> object of the class
     * @param fieldName   -
     *                    field name of the given class
     * @param fieldDomain -
     *                    domain of the field
     */
    void set(Class<?> cls, String fieldName, IFieldDomain fieldDomain);


    Object getRootObject();
}
