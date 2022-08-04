package heapsolving.template;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.vm.Verify;


public class TemplateHarness {

    public static Template getStructure() {
        if (SymHeap.usingDriverStrategy())
            return generateDriverStructure();

        Template structure = new Template(SymHeap.makeSymbolicString("name"));
        structure = (Template) SymHeap.makeSymbolicRefThis("treemap_0", structure);

        if (SymHeap.usingIfRepOKStrategy()) {
            if (!structure.repOK())
                return null;
        }

        return structure;
    }
    
    private static Template generateDriverStructure() {
    	int maxScope = SymHeap.getMaxScope();
		Template t = new Template(SymHeap.makeSymbolicString("t"));
		int numNodes = Verify.getInt(0, maxScope);
		for (int i = 1; i <= numNodes; i++) {
			Parameter p = new Parameter();
			p.setName(SymHeap.makeSymbolicString("pname" + i));
			p.setIndex(SymHeap.makeSymbolicInteger("pindex" + i));
			p.setRow(SymHeap.makeSymbolicInteger("prow" + i));
			p.setColumn(SymHeap.makeSymbolicInteger("pcol" + i));
			t.addParameter(p);
		}
		return t;
	}

}
