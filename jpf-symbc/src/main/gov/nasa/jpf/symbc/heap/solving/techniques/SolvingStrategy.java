package gov.nasa.jpf.symbc.heap.solving.techniques;

import java.util.LinkedList;

import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;
import gov.nasa.jpf.symbc.heap.solving.utils.Utils;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

public abstract class SolvingStrategy {


    long totalTime = 0;
    ConfigParser config;
    public int pathCount = 0;
    public int invalidPaths = 0;
    public int cacheHits = 0;


    public abstract boolean checkHeapSatisfiability(ThreadInfo ti, SymbolicInputHeap symInputHeap);

    public void searchStarted() {
        if (!Utils.fileExist(config.resultsFileName)) {
            Utils.createFileAndFolders(config.resultsFileName, false);
            Utils.appendToFile(config.resultsFileName, getFileHeader());
        }
        startTimeCount();
    }

    public void searchFinished() {
        stopTimeCount();
        printReport();
        writeDataToFile(createStringData());
    }

    protected void printReport() {
        String separator = "--------";
        String header = String.format("\n %s  %s Finished for %s.%s Scope %s  %s\n", separator,
                config.solvingStrategy.name(), config.symSolveSimpleClassName, config.targetMethodName,
                config.finitizationArgs, separator);
        System.out.println(header);
        System.out.println("    ExecutedPaths:  " + pathCount);
        System.out.println("    InvalidPaths:   " + invalidPaths);
        System.out.println("    TotalTime:      " + totalTime / 1000 + " s.");
        System.out.println("    SolvingTime:    " + getSolvingTime() / 1000 + " s.");
        System.out.println("\n ------------------------------------------------------------------");
    }

    protected String createStringData() {
        LinkedList<String> results = new LinkedList<String>();
        results.add(this.config.targetMethodName);
        results.add(this.config.solvingStrategy.name());
        results.add(this.config.finitizationArgs);
        results.add(Long.toString(this.totalTime / 1000));
        results.add(Long.toString(getSolvingTime() / 1000));
        results.add(Integer.toString(pathCount));
        results.add(Integer.toString(invalidPaths));
        results.add(Integer.toString(this.cacheHits));
        String resultsData = results.toString();
        return resultsData.substring(1, resultsData.length() - 1);
    }

    protected void writeDataToFile(String data) {
        Utils.appendToFile(config.resultsFileName, data);
    }

    protected String getFileHeader() {
        return "Method,Technique,Scope,TotalTime,SolvingTime,ExecutedPaths,InvalidPaths,CacheHits";
    }

    public SolvingStrategyEnum getStrategyEnum() {
        return config.solvingStrategy;
    }

    public void pathFinished(VM vm, ThreadInfo terminatedThread) {
        countPath();
    }

    protected void countPath() {
        pathCount++;
    }

    private void startTimeCount() {
        this.totalTime = System.currentTimeMillis();
    }

    private void stopTimeCount() {
        this.totalTime = (System.currentTimeMillis() - this.totalTime);
    }

    public boolean isLazyInitializationBased() {
        return true;
    }

    public boolean reachedGETFIELDLimit(int objRef) {
        return false;
    }
    public long getSolvingTime() {
    	return 0;
    }

    public Integer getBoundForClass(String fieldSimpleClassName) {
        return null;
    }

    public boolean isClassInBounds(String fieldSimpleClassName) {
        if (isLazyInitializationBased()) {
            return getBoundForClass(fieldSimpleClassName) != null;
        }
        return false;
    }

}
