package gov.nasa.jpf.symbc.heap.symbolicinput;

public abstract class Argument {
    
    String typeName;
    String argName;
    String value;
    
    public abstract boolean isSymbolic();
    
    public abstract String getDeclarationCode();
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getName() {
        return this.argName;
    }
    
    public String getStringValue() {
        return this.value;
    }
    
}
