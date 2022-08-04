package gov.nasa.jpf.symbc.heap.solving.config;

public class MissingConfigurationValueException extends RuntimeException {
    
    public MissingConfigurationValueException(String configValueName) {
        super(configValueName + " not set in configuration file");
    }

}
