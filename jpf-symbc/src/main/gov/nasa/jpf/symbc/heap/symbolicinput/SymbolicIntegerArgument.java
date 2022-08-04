package gov.nasa.jpf.symbc.heap.symbolicinput;

import gov.nasa.jpf.symbc.numeric.SymbolicInteger;

public class SymbolicIntegerArgument extends SymbolicArgument {

    public SymbolicIntegerArgument(SymbolicInteger symVar) {
        this.typeName = "Integer";
        this.symVar = symVar;
        this.argName = symVar.getName();
        this.value = "";
    }

    public SymbolicInteger getSymbolicVariable() {
        return (SymbolicInteger) this.symVar;
    }
    
    public String getDeclarationCode() {
        return String.format("%s %s = %s;", this.typeName, this.argName, this.value);
    }

}
