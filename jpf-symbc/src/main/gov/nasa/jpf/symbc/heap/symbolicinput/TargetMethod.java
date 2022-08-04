package gov.nasa.jpf.symbc.heap.symbolicinput;

import java.util.ArrayList;
import java.util.List;

public class TargetMethod {

    String ownerClassName;
    String name;
    Argument[] args;
    int numberOfArgs;
    int currentArgIndex = 0;

    public TargetMethod(String ownerClassName, String name, Argument[] args) {
        this.ownerClassName = ownerClassName;
        this.name = name;
        this.args = args;
        this.numberOfArgs = args.length;
    }

    public TargetMethod(String ownerClassName, String methodName, int numberOfArgs) {
        this(ownerClassName, methodName, new Argument[numberOfArgs]);
    }

    public TargetMethod makeShallowCopy() {
        return new TargetMethod(this.ownerClassName, this.name, this.args);
    }

    public void setArgument(Argument arg, int index) {
        this.args[index] = arg;
    }

    public void setArgument(Argument arg) {
        this.args[this.currentArgIndex] = arg;
        this.currentArgIndex++;
    }

    public List<SymbolicArgument> getSymbolicArguments() {
        List<SymbolicArgument> symArgs = new ArrayList<SymbolicArgument>();
        for (int i = 0; i < this.numberOfArgs; i++) {
            Argument arg = this.args[i];
            if (arg instanceof SymbolicArgument)
                symArgs.add((SymbolicArgument) arg);
        }
        return symArgs;
    }

    public Argument getArgument(int index) {
        return this.args[index];
    }
    
    public Argument[] getArguments() {
        return this.args;
    }

    public int getNumberOfArguments() {
        return this.numberOfArgs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return this.ownerClassName;
    }

    public String getName() {
        return this.name;
    }

}
