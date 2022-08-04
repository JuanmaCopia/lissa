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

package gov.nasa.jpf.symbc.heap.solving;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.bytecode.GETFIELDHeapSolving;
import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;
import gov.nasa.jpf.symbc.heap.solving.techniques.SolvingStrategy;
import gov.nasa.jpf.symbc.heap.solving.techniques.SolvingStrategyFactory;
import gov.nasa.jpf.symbc.heap.symbolicinput.Argument;
import gov.nasa.jpf.symbc.heap.symbolicinput.TargetMethod;
import gov.nasa.jpf.vm.Instruction;

public class HeapSolvingInstructionFactory extends SymbolicInstructionFactory {

    @Override
    public Instruction getfield(String fieldName, String clsName, String fieldDescriptor) {
        Instruction getFieldInstruction;
        if (filter.isPassing(ci))
            getFieldInstruction = new GETFIELDHeapSolving(fieldName, clsName, fieldDescriptor);
        else
            getFieldInstruction = super.getfield(fieldName, clsName, fieldDescriptor);
        return getFieldInstruction;
    }

    static ConfigParser configParser;

    static SolvingStrategy solvingStrategy;
    
    static TargetMethod targetMethod;
    
    public static boolean doingHeapSolving = false;

    public HeapSolvingInstructionFactory(Config conf) {
        super(conf);
        doingHeapSolving = true;
        configParser = new ConfigParser(conf);
        solvingStrategy = SolvingStrategyFactory.makeSymbolicHeapSolvingTechnique(configParser);
    }
    
    public static void setTargetMethod(String name, int numArgs) {
        targetMethod = new TargetMethod(configParser.symSolveSimpleClassName, name, numArgs);
    }
    
    public static void setArgument(Argument arg, int index) {
        targetMethod.setArgument(arg, index);
    }
    
    public static void setArgument(Argument arg) {
        targetMethod.setArgument(arg);
    }
    
    public static TargetMethod getTargetMethod() {
        return targetMethod;
    }

    public static SolvingStrategy getSolvingStrategy() {
        return solvingStrategy;
    }

    public static ConfigParser getConfigParser() {
        return configParser;
    }
    

}
