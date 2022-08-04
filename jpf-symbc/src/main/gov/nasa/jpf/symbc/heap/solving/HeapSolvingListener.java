package gov.nasa.jpf.symbc.heap.solving;

import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.heap.solving.techniques.SolvingStrategy;

/**
 *
 */
public class HeapSolvingListener extends PropertyListenerAdapter implements PublisherExtension {

    SolvingStrategy heapSolvingStrategy;

    @Override
    public void searchStarted(Search search) {
        this.heapSolvingStrategy = HeapSolvingInstructionFactory.getSolvingStrategy();
        this.heapSolvingStrategy.searchStarted();
    }

    @Override
    public void searchFinished(Search search) {
        this.heapSolvingStrategy.searchFinished();
    }

}
