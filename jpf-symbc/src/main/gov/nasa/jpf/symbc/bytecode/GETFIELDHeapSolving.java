/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * Symbolic Pathfinder (jpf-symbc) is licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.nasa.jpf.symbc.bytecode;

import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.heap.HeapChoiceGenerator;
import gov.nasa.jpf.symbc.heap.HeapNode;
import gov.nasa.jpf.symbc.heap.Helper;
import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.heap.solving.HeapSolvingInstructionFactory;
import gov.nasa.jpf.symbc.heap.solving.techniques.SolvingStrategy;
import gov.nasa.jpf.symbc.heap.symbolicinput.SymbolicReferenceInput;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.string.StringExpression;
import gov.nasa.jpf.symbc.string.SymbolicStringBuilder;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;

public class GETFIELDHeapSolving extends gov.nasa.jpf.jvm.bytecode.GETFIELD {
    public GETFIELDHeapSolving(String fieldName, String clsName, String fieldDescriptor) {
        super(fieldName, clsName, fieldDescriptor);
    }

    // private int numNewRefs = 0; // # of new reference objects to account for
    // polymorphism -- work of Neha Rungta -- needs to be updated

    boolean abstractClass = false;

    @Override
    public Instruction execute(ThreadInfo ti) {
        HeapNode[] prevSymRefs = null; // previously initialized objects of same type: candidates for lazy init
        int numSymRefs = 0; // # of prev. initialized objects
        ChoiceGenerator<?> prevHeapCG = null;

        // ================ Modification Begin ================ //
        SolvingStrategy heapSolvingStrategy = HeapSolvingInstructionFactory.getSolvingStrategy();
        if (!heapSolvingStrategy.isLazyInitializationBased())
            return super.execute(ti);
        // ================ Modification End ================ //

        StackFrame frame = ti.getModifiableTopFrame();
        int objRef = frame.peek(); // don't pop yet, we might re-enter
        lastThis = objRef;
        if (objRef == MJIEnv.NULL) {
            return ti.createAndThrowException("java.lang.NullPointerException",
                    "referencing field '" + fname + "' on null object");
        }

        ElementInfo ei = ti.getModifiableElementInfo(objRef); // getModifiableElementInfoWithUpdatedSharedness(objRef);
                                                              // POR broken
        FieldInfo fi = getFieldInfo();
        if (fi == null) {
            return ti.createAndThrowException("java.lang.NoSuchFieldError",
                    "referencing field '" + fname + "' in " + ei);
        }

        // System.out.println("\nFIELD: " + ei.toString() + "." + fi.getName());

        Object attr = ei.getFieldAttr(fi);
        // check if the field is of ref type & it is symbolic (i.e. it has an attribute)
        // if it is we need to do lazy initialization

        // ================ Modification Begin ================ //
        if (fi.isReference() && attr == null) {
            if (heapSolvingStrategy.reachedGETFIELDLimit(objRef)) {
                ti.getVM().getSystemState().setIgnored(true); // Backtrack
                return this;
            }
        }

        ClassInfo typeClassInfo = fi.getTypeClassInfo(); // use this instead of fullType
        String fieldSimpleClassName = typeClassInfo.getSimpleName();

        // if (fi.isReference()) {
        //     if (!heapSolvingStrategy.isClassInBounds(fieldSimpleClassName)) {
        //         System.err.println("LOG: Class not found in bounds:" + fieldSimpleClassName);
        //         System.err.println("LOG: Field Name: " + fi.getName());
        //     }
        //     else {
        //         System.out.println("LOG: Class found: " + fieldSimpleClassName);
        //     }
        // }

        if (!fi.isReference() || attr == null || attr instanceof StringExpression
                || attr instanceof SymbolicStringBuilder || !heapSolvingStrategy.isClassInBounds(fieldSimpleClassName)

        ) {
            return super.execute(ti);
        }
        // ================ Modification End ================ //

        // Lazy initialization:

        int currentChoice;
        ChoiceGenerator<?> thisHeapCG;

        if (!ti.isFirstStepInsn()) {
            prevSymRefs = null;
            numSymRefs = 0;

            prevHeapCG = ti.getVM().getSystemState().getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
            // to check if this still works in the case of cascaded choices...

            if (prevHeapCG != null) {
                // determine # of candidates for lazy initialization
                SymbolicInputHeap symInputHeap = ((HeapChoiceGenerator) prevHeapCG).getCurrentSymInputHeap();
                prevSymRefs = symInputHeap.getNodesOfType(typeClassInfo);
                numSymRefs = prevSymRefs.length;
            }
            int increment = 2;
            if (typeClassInfo.isAbstract()) {
                abstractClass = true;
                increment = 1; // only null
            }

            thisHeapCG = new HeapChoiceGenerator(numSymRefs + increment); // +null,new
            ti.getVM().getSystemState().setNextChoiceGenerator(thisHeapCG);
            // ti.reExecuteInstruction();
            if (SymbolicInstructionFactory.debugMode)
                System.out.println("# heap cg registered: " + thisHeapCG);
            return this;

        } else { // this is what really returns results
            // here we can have 2 choice generators: thread and heappc at the same time?

            frame.pop(); // Ok, now we can remove the object ref from the stack

            thisHeapCG = ti.getVM().getSystemState().getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
            assert (thisHeapCG != null
                    && thisHeapCG instanceof HeapChoiceGenerator) : "expected HeapChoiceGenerator, got: " + thisHeapCG;
            currentChoice = ((HeapChoiceGenerator) thisHeapCG).getNextChoice();
        }

        PathCondition pcHeap; // this pc contains only the constraints on the heap
        SymbolicInputHeap symInputHeap;

        // depending on the currentChoice, we set the current field to an object that
        // was already created
        // 0 .. numymRefs -1, or to null or to a new object of the respective type,
        // where we set all its
        // fields to be symbolic

        prevHeapCG = thisHeapCG.getPreviousChoiceGeneratorOfType(HeapChoiceGenerator.class);

        if (prevHeapCG == null) {
            pcHeap = new PathCondition();
            symInputHeap = new SymbolicInputHeap();
        } else {
            pcHeap = ((HeapChoiceGenerator) prevHeapCG).getCurrentPCheap();
            symInputHeap = ((HeapChoiceGenerator) prevHeapCG).getCurrentSymInputHeap();
        }

        assert pcHeap != null;
        assert symInputHeap != null;

        prevSymRefs = symInputHeap.getNodesOfType(typeClassInfo);
        numSymRefs = prevSymRefs.length;

        SymbolicReferenceInput symRefInput = symInputHeap.getImplicitInputThis();

        int daIndex = 0; // index into JPF's dynamic area
        if (currentChoice < numSymRefs) { // lazy initialization using a previously lazily initialized object
            HeapNode candidateNode = prevSymRefs[currentChoice];
            // here we should update pcHeap with the constraint attr == candidateNode.sym_v
            pcHeap._addDet(Comparator.EQ, (SymbolicInteger) attr, candidateNode.getSymbolic());
            daIndex = candidateNode.getIndex();
            // ================ Modification Begin ================ //
            symRefInput.addReferenceField(objRef, fi.getName(), daIndex);
            // ================ Modification End ================ //
        } else if (currentChoice == numSymRefs) { // null object
            pcHeap._addDet(Comparator.EQ, (SymbolicInteger) attr, new IntegerConstant(-1));
            daIndex = MJIEnv.NULL;

            // ================ Modification Begin ================ //
            symRefInput.addReferenceField(objRef, fi.getName(), SymbolicReferenceInput.NULL);
            // ================ Modification End ================ //
        } else if (currentChoice == (numSymRefs + 1) && !abstractClass) {
            // creates a new object with all fields symbolic and adds the object to
            // SymbolicHeap

            // ================ Modification Begin ================ //
            Integer bound = heapSolvingStrategy.getBoundForClass(fieldSimpleClassName);
            // int scope = SymSolveListener.getScope();
            // System.out.println("Class " + fieldSimpleClassName + " : " + bound + " ==? "
            // + scope);
            // Integer bound = SymSolveListener.getScope();

            // backtrack if the max bound of nodes has been reached
            if (numSymRefs == bound) {
                ti.getVM().getSystemState().setIgnored(true);
                return this;
            }
            // ================ Modification End ================ //

            daIndex = Helper.addNewHeapNode(typeClassInfo, ti, attr, pcHeap, symInputHeap, numSymRefs, prevSymRefs,
                    ei.isShared());

            // ================ Modification Begin ================ //
            symRefInput.addReferenceField(objRef, fi.getName(), daIndex);
            // ================ Modification End ================ //
        } else {
            System.err.println("subtyping not handled");
        }

        ei.setReferenceField(fi, daIndex);
        ei.setFieldAttr(fi, null);

        frame.pushRef(daIndex);

        ((HeapChoiceGenerator) thisHeapCG).setCurrentPCheap(pcHeap);
        ((HeapChoiceGenerator) thisHeapCG).setCurrentSymInputHeap(symInputHeap);
        if (SymbolicInstructionFactory.debugMode)
            System.out.println("GETFIELD pcHeap: " + pcHeap);

        // ================ Modification End ================ //
        if (!heapSolvingStrategy.checkHeapSatisfiability(ti, symInputHeap)) {
            ti.getVM().getSystemState().setIgnored(true); // Backtrack
            return this;
        }
        // ================ Modification End ================ //
        return getNext(ti);
    }
}
