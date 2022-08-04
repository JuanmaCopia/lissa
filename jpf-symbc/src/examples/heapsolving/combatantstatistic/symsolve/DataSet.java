package heapsolving.combatantstatistic.symsolve;

public class DataSet {

	private final HashMapIntList valuesPerSide = new HashMapIntList();

	public boolean repOK() {
		if (valuesPerSide == null)
			return false;
		return valuesPerSide.repOK();
	}

}
