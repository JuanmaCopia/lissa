package gov.nasa.jpf.symbc.heap.symbolicinput;

import gov.nasa.jpf.symbc.string.StringSymbolic;

public class SymbolicStringArgument extends SymbolicArgument {

    public SymbolicStringArgument(StringSymbolic symVar) {
        this.typeName = "String";
        this.symVar = symVar;
        this.argName = symVar.getName();
        this.value = "";
    }

    public StringSymbolic getSymbolicVariable() {
        return (StringSymbolic) this.symVar;
    }

    public String getDeclarationCode() {
        return String.format("%s %s = \"%s\";", this.typeName, this.argName, this.value);
    }

}
