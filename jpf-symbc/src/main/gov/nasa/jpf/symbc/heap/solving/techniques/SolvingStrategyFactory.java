package gov.nasa.jpf.symbc.heap.solving.techniques;

import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;
import symsolve.explorers.impl.SymmetryBreakStrategy;


public class SolvingStrategyFactory {

    public static SolvingStrategy makeSymbolicHeapSolvingTechnique(ConfigParser config) {
        switch (config.solvingStrategy) {
            case LISSA:
                return new LISSA(config);
            case LISSAM:
                return new LISSAM(config);
            case LIHYBRID:
                return new LIHYBRID(config);
            case DRIVER:
                return new DRIVER(config);
            case LISSANOSB:
                config.symmetryBreakingStrategy = SymmetryBreakStrategy.NO_SYMMETRY_BREAK;
                return new LISSA(config);
            case IFREPOK:
                return new IFREPOK(config);
            default:
                throw new IllegalArgumentException("Invalid symbolic heap solving technique");
        }
    }

}
