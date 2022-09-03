package heapsolving.combatantstatistic.symsolve;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

public class CombatantStatistic {

	private final HashMapIntDataSet allData = new HashMapIntDataSet();

	public static IFinitization finCombatantStatistic(int nodesNum) {
		IFinitization f = FinitizationFactory.create(CombatantStatistic.class);

		IObjSet hashmap = f.createObjSet(HashMapIntDataSet.class, 1, true);
		f.set(CombatantStatistic.class, "allData", hashmap);

		IObjSet entries = f.createObjSet(HashMapIntDataSet.EntryIDS.class, nodesNum, true);
		f.set(HashMapIntDataSet.class, "e0", entries);
		f.set(HashMapIntDataSet.class, "e1", entries);
		f.set(HashMapIntDataSet.class, "e2", entries);
		f.set(HashMapIntDataSet.class, "e3", entries);
		f.set(HashMapIntDataSet.class, "e4", entries);
		f.set(HashMapIntDataSet.class, "e5", entries);
		f.set(HashMapIntDataSet.class, "e6", entries);
		f.set(HashMapIntDataSet.class, "e7", entries);

		IObjSet datasets = f.createObjSet(DataSet.class, nodesNum, true);
		f.set(HashMapIntDataSet.EntryIDS.class, "value", datasets);
		f.set(HashMapIntDataSet.EntryIDS.class, "next", entries);

		IObjSet hashmap2 = f.createObjSet(HashMapIntList.class, nodesNum, true);
		f.set(DataSet.class, "valuesPerSide", hashmap2);

		IObjSet entries2 = f.createObjSet(HashMapIntList.EntryIL.class, nodesNum, true);
		f.set(HashMapIntList.class, "e0", entries2);
		f.set(HashMapIntList.class, "e1", entries2);
		f.set(HashMapIntList.class, "e2", entries2);
		f.set(HashMapIntList.class, "e3", entries2);
		f.set(HashMapIntList.class, "e4", entries2);
		f.set(HashMapIntList.class, "e5", entries2);
		f.set(HashMapIntList.class, "e6", entries2);
		f.set(HashMapIntList.class, "e7", entries2);
		
		IObjSet list = f.createObjSet(LinkedList.class, nodesNum, true);
		f.set(HashMapIntList.EntryIL.class, "value", list);
		f.set(HashMapIntList.EntryIL.class, "next", entries2);
		
		IObjSet listNodes = f.createObjSet(LinkedList.Entry.class, nodesNum * nodesNum, true);
		f.set(LinkedList.class, "header", listNodes);
		f.set(LinkedList.Entry.class, "next", listNodes);
		f.set(LinkedList.Entry.class, "previous", listNodes);

		return f;
	}

	public boolean repOK() {
		if (allData == null)
			return false;

		if (!allData.repOK())
			return false;

		DataSet h;
		for (HashMapIntDataSet.EntryIDS e : allData.entrySet()) {
			h = e.getValue();
			if (h != null && !h.repOK())
				return false;
		}

		return true;
	}

}
