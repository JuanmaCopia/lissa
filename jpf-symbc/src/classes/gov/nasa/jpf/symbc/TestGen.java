package gov.nasa.jpf.symbc;

public class TestGen {

    native public static void registerTargetMethod(String string, int i);

    native public static void registerSymbolicIntegerArgument(int symbolicInteger);
    native public static void registerSymbolicStringArgument(String symbolicString);

    native public static void registerConcreteArgument(String name, String declarationCode);

}
