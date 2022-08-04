package symsolve.candidates;

import korat.testing.ITester;
import korat.testing.impl.CannotFindPredicateException;
import korat.testing.impl.CannotInvokePredicateException;
import korat.utils.IIntList;
import symsolve.utils.Helper;

import java.lang.reflect.Method;

public class PredicateChecker implements ITester {

    Class<?> rootClass;
    IIntList accessedIndices;
    boolean isInitialized = false;
    boolean traceStarted;
    Method predicate;


    public void initialize(Class<?> rootClass, String predicateName, IIntList accessedIndices) throws CannotFindPredicateException {
        this.rootClass = rootClass;
        this.accessedIndices = accessedIndices;
        setPredicate(predicateName);
        isInitialized = true;
    }

    public void setPredicate(String predicateName) throws CannotFindPredicateException {
        predicate = Helper.getPredicateMethod(rootClass, predicateName);
    }

    public void setPredicate(Method predicate) throws CannotFindPredicateException {
        this.predicate = predicate;
    }

    public boolean checkPredicate(Object structure) throws CannotInvokePredicateException {
        assert (predicate != null);
        startFieldTrace();
        try {
            return (Boolean) predicate.invoke(structure, (Object[]) null);
        } catch (Exception e) {
            throw new CannotInvokePredicateException(rootClass, predicate.getName(), e.getMessage(), e);
        } finally {
            stopFieldTrace();
        }
    }

    @Override
    public void startFieldTrace() {
        traceStarted = true;
        accessedIndices.clear();
    }

    @Override
    public void continueFieldTrace() {
        traceStarted = true;
    }

    @Override
    public void stopFieldTrace() {
        traceStarted = false;
    }

    @Override
    public void notifyFieldAccess(int accessedFieldIndex) {
        if (!traceStarted)
            return;
        if (accessedFieldIndex != -1)
            accessedIndices.add(accessedFieldIndex);
    }

}
