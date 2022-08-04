package gov.nasa.jpf.symbc.heap.solving.config;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.symbc.heap.solving.techniques.SolvingStrategyEnum;

import symsolve.explorers.impl.SymmetryBreakStrategy;

public class ConfigParser {

    private static final String TARGET_METHOD_CONFIG = "method";
    private static final String TARGET_CLASS_CONFIG = "target";
    private static final String SYMSOLVE_CLASS_CONFIG = "heapsolving.symsolve.class";
    private static final String SYMSOLVE_PREDICATE_CONFIG = "heapsolving.symsolve.predicate";
    private static final String SYMSOLVE_FINITIZATION_ARGS_CONFIG = "symbolic.scope";
    private static final String HEAP_SOLVING_TECHNIQUE_CONFIG = "heapsolving.strategy";
    private static final String HEAP_GETFIELD_LIMIT_CONFIG = "heap.getFieldLimit";

    public static final String DEFAULT_PREDICATE_NAME = "repOK";

    private static final String RESULTS_FILE_POSFIX = "-results.csv";
    
    public static final String OUTPUT_DIR = "output";
    public static final String STATISTICS_DIR = String.format("%s/%s", OUTPUT_DIR, "results");
    public static final String TESTCASE_DIR = String.format("%s/%s", OUTPUT_DIR, "testcases");

    private static final int DEFAULT_GETFIELD_LIMIT = 2000;

    public Config conf;

    public String targetMethodName;
    public String targetClassName;
    public String symSolveClassName;
    public String symSolveSimpleClassName;
    public String finitizationArgs;
    public String resultsFileName;
    public String predicateName;
    public SolvingStrategyEnum solvingStrategy;
    public SymmetryBreakStrategy symmetryBreakingStrategy = SymmetryBreakStrategy.SYMMETRY_BREAK;
    public int getFieldLimit;

    public ConfigParser(Config conf) {
        this.conf = conf;
        this.targetMethodName = getConfigValueString(TARGET_METHOD_CONFIG, "METHOD-NOT-SET");
        this.targetClassName = getConfigValueString(TARGET_CLASS_CONFIG);
        this.symSolveClassName = getConfigValueString(SYMSOLVE_CLASS_CONFIG);
        this.symSolveSimpleClassName = calculateSimpleClassName(this.symSolveClassName);
        this.finitizationArgs = getConfigValueString(SYMSOLVE_FINITIZATION_ARGS_CONFIG);
        this.solvingStrategy = getSolvingHeapSolvingTechnique();
        this.getFieldLimit = conf.getInt(HEAP_GETFIELD_LIMIT_CONFIG, DEFAULT_GETFIELD_LIMIT);
        String resFileName = this.symSolveSimpleClassName + RESULTS_FILE_POSFIX;
        this.resultsFileName = String.format("%s/%s", STATISTICS_DIR, resFileName);
        this.predicateName = getConfigValueString(SYMSOLVE_PREDICATE_CONFIG, DEFAULT_PREDICATE_NAME);
    }

    public String getConfigValueString(String settingName) {
        String value = conf.getString(settingName, "");
        if (value.isEmpty())
            throw new MissingConfigurationValueException(settingName);
        return value.trim();
    }
    
    public String getConfigValueString(String settingName, String defaultValue) {
        return conf.getString(settingName, defaultValue).trim();
    }

    public SolvingStrategyEnum getSolvingHeapSolvingTechnique() {
        String techniqueName = getConfigValueString(HEAP_SOLVING_TECHNIQUE_CONFIG);
        return SolvingStrategyEnum.valueOf(techniqueName.toUpperCase());
    }

    private String calculateSimpleClassName(String fullyQualifiedClassName) {
        String[] cnsplit = fullyQualifiedClassName.split("\\.");
        if (cnsplit.length == 0)
            throw new InvalidConfigurationValueException(
                    "the value of " + SYMSOLVE_CLASS_CONFIG + " is not well formed");
        return cnsplit[cnsplit.length - 1];
    }

}
