
package heapsolving.dictionaryinfo.symsolve;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;


public class DictionaryInfo {


	public TreeMap fieldsByTagNumber;

	public TreeMapStrR fieldsByName;


	public static IFinitization finDictionaryInfo(int nodesNum) {
		IFinitization f = FinitizationFactory.create(DictionaryInfo.class);
		IObjSet t1 = f.createObjSet(TreeMap.class, 1, true);
		IObjSet t2 = f.createObjSet(TreeMapStrR.class, 1, true);

		f.set(DictionaryInfo.class, "fieldsByTagNumber", t1);
		f.set(DictionaryInfo.class, "fieldsByName", t2);

		IObjSet entries = f.createObjSet(TreeMap.Entry.class, nodesNum, true);

		f.set(TreeMap.class, "root", entries);
		f.set(TreeMap.Entry.class, "left", entries);
		f.set(TreeMap.Entry.class, "right", entries);
		f.set(TreeMap.Entry.class, "parent", entries);
		f.set(TreeMap.Entry.class, "color", f.createBooleanSet());

		IObjSet entriesB = f.createObjSet(TreeMapStrR.EntryB.class, nodesNum, true);
		f.set(TreeMapStrR.class, "root", entriesB);
		f.set(TreeMapStrR.EntryB.class, "left", entriesB);
		f.set(TreeMapStrR.EntryB.class, "right", entriesB);
		f.set(TreeMapStrR.EntryB.class, "parent", entriesB);
		f.set(TreeMapStrR.EntryB.class, "color", f.createBooleanSet());

		return f;
	}

	public boolean repOK() {
		if (fieldsByTagNumber == null)
			return false;
		if (fieldsByName == null)
			return false;
		return fieldsByTagNumber.repOK() && fieldsByName.repOK();
	}

	public boolean areTreesOK() {
		if (fieldsByTagNumber == null)
			return false;
		if (fieldsByName == null)
			return false;
		if (!fieldsByTagNumber.isBinTreeWithParentReferences()) {
			return false;
		}
		if (!fieldsByName.isBinTreeWithParentReferences()) {
			return false;
		}
		return true;
	}

}
