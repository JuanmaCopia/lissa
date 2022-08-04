package heapsolving.dictionaryinfo;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.vm.Verify;


public class DictionaryInfoHarness {


    public static DictionaryInfo getStructure() {
        if (SymHeap.usingDriverStrategy())
            return generateDriverStructure();

        DictionaryInfo structure = new DictionaryInfo(SymHeap.makeSymbolicString("version"));
        structure = (DictionaryInfo) SymHeap.makeSymbolicRefThis("dictionaryinfo_0", structure);

        if (SymHeap.usingIfRepOKStrategy()) {
            if (!structure.repOK())
                return null;
        }

        return structure;
    }

    public static DictionaryInfo generateDriverStructure() {
    	int maxScope = SymHeap.getMaxScope();
		DictionaryInfo structure = new DictionaryInfo(SymHeap.makeSymbolicString("version"));

		int numNodes = Verify.getInt(0, maxScope);
		for (int i = 1; i <= numNodes; i++) {
			FieldInfo fi = new FieldInfo();
			fi.setTagNumber(SymHeap.makeSymbolicInteger("tagNum" + i));
			fi.setName(SymHeap.makeSymbolicString("name" + i));
			structure.addField(fi);
		}
		return structure;
	}

}
