package gov.nasa.jpf.symbc.heap.symbolicinput;

public class ConcreteArgument extends Argument {
    
    String declarationCode;
    
    public ConcreteArgument(String name, String declarationCode) {
        this.argName = name;
        this.declarationCode = declarationCode;
    }

    @Override
    public boolean isSymbolic() {
        return false;
    }
    
    public String getDeclarationCode() {
        return this.declarationCode;
    }


}
