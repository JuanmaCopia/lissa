package symsolve;

import korat.testing.impl.CannotInvokePredicateException;
import symsolve.bounds.BoundCalculator;
import symsolve.bounds.Bounds;
import symsolve.config.BoundCalculatorConfig;

public class SymSolveBoundCalculator {

    private BoundCalculator boundCalculator;


    public SymSolveBoundCalculator(String className, String finitizationArgs) {
        this(new BoundCalculatorConfig(className, finitizationArgs));
    }

    public SymSolveBoundCalculator(BoundCalculatorConfig config) {
        try {
            boundCalculator = new BoundCalculator(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bounds calculateBounds() {
        Bounds bounds = null;
        try {
            bounds = boundCalculator.calculateBounds();
        } catch (CannotInvokePredicateException e) {
            e.printStackTrace(System.err);
        }
        return bounds;
    }

}
