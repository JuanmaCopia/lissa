package gov.nasa.jpf.symbc;

import gov.nasa.jpf.annotation.MJI;
import gov.nasa.jpf.symbc.heap.solving.HeapSolvingInstructionFactory;
import gov.nasa.jpf.symbc.heap.symbolicinput.ConcreteArgument;
import gov.nasa.jpf.symbc.heap.symbolicinput.SymbolicIntegerArgument;
import gov.nasa.jpf.symbc.heap.symbolicinput.SymbolicStringArgument;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.NativePeer;


public class JPF_gov_nasa_jpf_symbc_TestGen extends NativePeer {


    @MJI
    public static void registerConcreteArgument(MJIEnv env, int objRef, int nameRef, int codeRef) {
        String name = env.getStringObject(nameRef);
        String code = env.getStringObject(codeRef);

        ConcreteArgument arg = new ConcreteArgument(name, code);
        HeapSolvingInstructionFactory.setArgument(arg);

//        System.out.println(String.format("Registered concrete input: %s ", name));
    }

    @MJI
    public static void registerSymbolicIntegerArgument(MJIEnv env, int objRef, int symInt) {
        Object[] attrs = env.getArgAttributes();
        assert (attrs != null);
        assert (attrs[0] instanceof SymbolicInteger);
        SymbolicInteger symVar = (SymbolicInteger) attrs[0];
        assert (symVar != null);

        SymbolicIntegerArgument arg = new SymbolicIntegerArgument(symVar);
        HeapSolvingInstructionFactory.setArgument(arg);

//        System.out.println(String.format("Registered Symbolic Integer input: %s ", symVar.getName()));
    }
    
    @MJI
    public static void registerSymbolicStringArgument(MJIEnv env, int objRef, int symStringRef) {
        Object[] attrs = env.getArgAttributes();
        assert (attrs != null);
        assert (attrs[0] instanceof StringSymbolic);
        StringSymbolic symVar = (StringSymbolic) attrs[0];
        assert (symVar != null);

        SymbolicStringArgument arg = new SymbolicStringArgument(symVar);
        HeapSolvingInstructionFactory.setArgument(arg);

//        System.out.println(String.format("Registered Symbolic Integer input: %s ", symVar.getName()));
    }

    @MJI
    public static void registerTargetMethod(MJIEnv env, int objRef, int stringRef, int numArgs) {
        String methodName = env.getStringObject(stringRef);
        HeapSolvingInstructionFactory.setTargetMethod(methodName, numArgs);
//        System.out.println("Registered Target Method: " + methodName);
//        System.out.println("Number of Arguments: " + numArgs);
    }

}
