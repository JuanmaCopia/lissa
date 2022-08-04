package symsolve;

import symsolve.bounds.Bounds;
import symsolve.bounds.Helper;
import symsolve.config.BoundCalculatorConfig;
import symsolve.explorers.impl.SymmetryBreakStrategy;
import symsolve.utils.Utils;

import java.io.File;

public class CalculateBoundsMain {

    private static final String BOUNDS_FOLDER = "output/bounds/";

    private static void generateBounds(String className, String scope) {
        Bounds bounds = calculateBounds(className, scope);

        String jsonStringBounds = Helper.boundsToJson(bounds);
        Bounds recoveredBounds = Helper.boundsFromJson(jsonStringBounds);
        assert (bounds.equals(recoveredBounds));

        storeBoundsInDisk(createBoundsFileName(className, scope, "txt"), bounds.toString());
        storeBoundsInDisk(createBoundsFileName(className, scope, "json"), jsonStringBounds);
    }

    private static Bounds calculateBounds(String className, String scope) {
        System.out.printf("\nCalculating bounds for %s, Scope = %s ...\n%n", className, scope);
        BoundCalculatorConfig config = new BoundCalculatorConfig(className, scope, SymmetryBreakStrategy.SYMMETRY_BREAK_REVERSE);
        SymSolveBoundCalculator symSolveBoundCalculator = new SymSolveBoundCalculator(config);
        return symSolveBoundCalculator.calculateBounds();
    }

    private static void storeBoundsInDisk(String fullPathFileName, String boundsString) {
        File file = Utils.createFileAndFolders(fullPathFileName, true);
        Utils.appendToFile(file, boundsString);
    }

    private static String createBoundsFileName(String className, String scope, String extension) {
        return String.format("%s%s.%s.%s", BOUNDS_FOLDER, className, scope, extension);
    }

    public static void main(String[] args) {
        generateBounds("examples.treemap.TreeMap", "5");
        generateBounds("examples.hashmap.HashMap", "2");
        generateBounds("examples.schedule.Schedule", "4");
    }
}
