package gov.nasa.jpf.symbc;

import gov.nasa.jpf.annotation.MJI;
import gov.nasa.jpf.symbc.heap.HeapChoiceGenerator;
import gov.nasa.jpf.symbc.heap.HeapNode;
import gov.nasa.jpf.symbc.heap.Helper;
import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.heap.solving.HeapSolvingInstructionFactory;
import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;
import gov.nasa.jpf.symbc.heap.solving.techniques.DRIVER;
import gov.nasa.jpf.symbc.heap.solving.techniques.IFREPOK;
import gov.nasa.jpf.symbc.heap.solving.techniques.LIHYBRID;
import gov.nasa.jpf.symbc.heap.solving.techniques.LISSA;
import gov.nasa.jpf.symbc.heap.solving.techniques.SolvingStrategy;
import gov.nasa.jpf.symbc.heap.symbolicinput.SymbolicReferenceInput;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.MinMax;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.SymbolicReal;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.NativePeer;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

public class JPF_gov_nasa_jpf_symbc_SymHeap extends NativePeer {

    @MJI
    public static void makeSymbolicImplicitInputThis(MJIEnv env, int objRef, int stringRef, int objvRef) {
        // makes all the fields of obj v symbolic and adds obj v to the symbolic heap to
        // kick off lazy initialization
        if (objvRef == MJIEnv.NULL)
            throw new RuntimeException("## Error: null object");
        // introduce a heap choice generator for the element in the heap
        ThreadInfo ti = env.getVM().getCurrentThread();
        SystemState ss = env.getVM().getSystemState();
        ChoiceGenerator<?> cg;

        if (!ti.isFirstStepInsn()) {
            cg = new HeapChoiceGenerator(1); // new
            ss.setNextChoiceGenerator(cg);
            env.repeatInvocation();
            return; // not used anyways
        }
        // else this is what really returns results

        cg = ss.getChoiceGenerator();
        assert (cg instanceof HeapChoiceGenerator) : "expected HeapChoiceGenerator, got: " + cg;

        // see if there were more inputs added before
        ChoiceGenerator<?> prevHeapCG = cg.getPreviousChoiceGenerator();
        while (!((prevHeapCG == null) || (prevHeapCG instanceof HeapChoiceGenerator))) {
            prevHeapCG = prevHeapCG.getPreviousChoiceGenerator();
        }

        PathCondition pcHeap;
        SymbolicInputHeap symInputHeap;
        if (prevHeapCG == null) {

            pcHeap = new PathCondition();
            symInputHeap = new SymbolicInputHeap();
        } else {
            pcHeap = ((HeapChoiceGenerator) prevHeapCG).getCurrentPCheap();
            symInputHeap = ((HeapChoiceGenerator) prevHeapCG).getCurrentSymInputHeap();
        }

        // set all the fields to be symbolic
        ClassInfo ci = env.getClassInfo(objvRef);
        FieldInfo[] fields = ci.getDeclaredInstanceFields();
        FieldInfo[] staticFields = ci.getDeclaredStaticFields();

        String name = env.getStringObject(stringRef);
        String refChain = name + "[" + objvRef + "]"; // why is the type used here? should use the name of the field
                                                      // instead

        SymbolicInteger newSymRef = new SymbolicInteger(refChain);
        // ElementInfo eiRef = DynamicArea.getHeap().get(objvRef);
        ElementInfo eiRef = VM.getVM().getHeap().getModifiable(objvRef);
        Helper.initializeInstanceFields(fields, eiRef, refChain, symInputHeap);
        Helper.initializeStaticFields(staticFields, ci, ti);

        // create new HeapNode based on above info
        // update associated symbolic input heap

        ClassInfo typeClassInfo = eiRef.getClassInfo();

        HeapNode rootHeapNode = new HeapNode(objvRef, typeClassInfo, newSymRef);
        symInputHeap._add(rootHeapNode);
        SymbolicReferenceInput symRefInput = symInputHeap.getImplicitInputThis();
        symRefInput.setRootHeapNode(rootHeapNode);

        pcHeap._addDet(Comparator.NE, newSymRef, new IntegerConstant(-1));
        ((HeapChoiceGenerator) cg).setCurrentPCheap(pcHeap);
        ((HeapChoiceGenerator) cg).setCurrentSymInputHeap(symInputHeap);

        return;
    }

    @MJI
    public static int getMaxScope(MJIEnv env, int objRef) {
        ConfigParser conf = HeapSolvingInstructionFactory.getConfigParser();
        return Integer.valueOf(conf.finitizationArgs);
    }

    @MJI
    public static boolean usingDriverStrategy(MJIEnv env, int objRef) {
        SolvingStrategy strategy = HeapSolvingInstructionFactory.getSolvingStrategy();
        return strategy instanceof DRIVER;
    }

    @MJI
    public static boolean usingLIHybridStrategy(MJIEnv env, int objRef) {
        SolvingStrategy strategy = HeapSolvingInstructionFactory.getSolvingStrategy();
        return strategy instanceof LIHYBRID;
    }

    @MJI
    public static boolean usingSymSolveBasedStrategy(MJIEnv env, int objRef) {
        SolvingStrategy strategy = HeapSolvingInstructionFactory.getSolvingStrategy();
        if (strategy instanceof LIHYBRID)
        	return false;
        return strategy instanceof LISSA;
    }

    @MJI
    public static boolean usingIfRepOKStrategy(MJIEnv env, int objRef) {
        SolvingStrategy strategy = HeapSolvingInstructionFactory.getSolvingStrategy();
        return strategy instanceof IFREPOK;
    }

    @MJI
    public static void countPath(MJIEnv env, int objRef) {
        VM vm = env.getVM();
        SolvingStrategy strategy = HeapSolvingInstructionFactory.getSolvingStrategy();
        strategy.pathFinished(vm, vm.getCurrentThread());
    }

    @MJI
    public static int makeSymbolicInteger(MJIEnv env, int objRef, int stringRef) {
        String name = env.getStringObject(stringRef);
        env.setReturnAttribute(new SymbolicInteger(name, MinMax.getVarMinInt(name), MinMax.getVarMaxInt(name)));
        return 0;
    }

    @MJI
    public static long makeSymbolicLong(MJIEnv env, int objRef, int stringRef) {
        String name = env.getStringObject(stringRef);
        env.setReturnAttribute(new SymbolicInteger(name, MinMax.getVarMinLong(name), MinMax.getVarMaxLong(name)));
        return 0;
    }

    @MJI
    public static short makeSymbolicShort(MJIEnv env, int objRef, int stringRef) {
        String name = env.getStringObject(stringRef);
        env.setReturnAttribute(new SymbolicInteger(name, MinMax.getVarMinShort(name), MinMax.getVarMaxShort(name)));
        return 0;
    }

    @MJI
    public static byte makeSymbolicByte(MJIEnv env, int objRef, int stringRef) {
        String name = env.getStringObject(stringRef);
        env.setReturnAttribute(new SymbolicInteger(name, MinMax.getVarMinByte(name), MinMax.getVarMaxByte(name)));
        return 0;
    }

    @MJI
    public static char makeSymbolicChar(MJIEnv env, int objRef, int stringRef) {
        String name = env.getStringObject(stringRef);
        env.setReturnAttribute(new SymbolicInteger(name, MinMax.getVarMinChar(name), MinMax.getVarMaxChar(name)));
        return 0;
    }

    @MJI
    public static double makeSymbolicReal(MJIEnv env, int objRef, int stringRef) {
        String name = env.getStringObject(stringRef);
        env.setReturnAttribute(new SymbolicReal(name, MinMax.getVarMinDouble(name), MinMax.getVarMaxDouble(name)));
        return 0.0;
    }

    @MJI
    public static boolean makeSymbolicBoolean(MJIEnv env, int objRef, int stringRef) {
        String name = env.getStringObject(stringRef);
        env.setReturnAttribute(new SymbolicInteger(name, 0, 1));
        return false;
    }

    @MJI
    public static int makeSymbolicString__Ljava_lang_String_2__Ljava_lang_String_2(MJIEnv env, int objRef,
            int stringRef) {
        String name = env.getStringObject(stringRef);
        env.setReturnAttribute(new StringSymbolic(name));
        return env.newString("WWWWW's Birthday is 12-17-77");
    }

}
