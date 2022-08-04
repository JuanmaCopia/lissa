package symsolve.config;

import symsolve.explorers.impl.SymmetryBreakStrategy;

public abstract class SymSolveConfig {

    static final String DEFAULT_PREDICATE_NAME = "repOK";
    static final SymmetryBreakStrategy DEFAULT_SBREAK_STRATEGY = SymmetryBreakStrategy.SYMMETRY_BREAK;

    String fullClassName;
    String[] finitizationArgs;
    String finitizationName;
    String propertyFinitizationName;
    String predicateName;
    SymmetryBreakStrategy symmetryBreakStrategy;


    public String[] getFinitizationArgs() {
        return finitizationArgs;
    }

    public String getFinitizationName() {
        return finitizationName;
    }

    public String getPropertyCheckFinitizationName() {
        return propertyFinitizationName;
    }

    public String getFullyQualifiedClassName() {
        return fullClassName;
    }

    public String getPredicateName() {
        return predicateName;
    }

    public SymmetryBreakStrategy getSymmetryBreakStretegy() {
        return symmetryBreakStrategy;
    }

    protected String calculateFinitizationName(String fullClassName) {
        String[] cs = fullClassName.split("\\.");
        return "fin" + cs[cs.length - 1];
    }

    protected String calculatePropertyFinitizationName(String fullClassName) {
        String[] cs = fullClassName.split("\\.");
        return "propertyCheckFin" + cs[cs.length - 1];
    }
}
