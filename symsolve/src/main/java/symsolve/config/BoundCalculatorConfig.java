package symsolve.config;

import symsolve.explorers.impl.SymmetryBreakStrategy;

public class BoundCalculatorConfig extends SymSolveConfig {

    public BoundCalculatorConfig(String fullClassName, String finitizationArgs, SymmetryBreakStrategy symmetryBreakingStrategy, String predicateName) {
        this.fullClassName = fullClassName;
        this.finitizationArgs = finitizationArgs.split(",");
        this.finitizationName = calculateFinitizationName(fullClassName);
        this.symmetryBreakStrategy = symmetryBreakingStrategy;
        this.predicateName = predicateName;
    }

    public BoundCalculatorConfig(String fullClassName, String finitizationArgs, SymmetryBreakStrategy symmetryBreakingStrategy) {
        this(fullClassName, finitizationArgs, symmetryBreakingStrategy, DEFAULT_PREDICATE_NAME);
    }

    public BoundCalculatorConfig(String fullClassName, String finitizationArgs) {
        this(fullClassName, finitizationArgs, DEFAULT_SBREAK_STRATEGY, DEFAULT_PREDICATE_NAME);
    }

}
