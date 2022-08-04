package heapsolving.combatantstatistic;

/*
 Copyright (c) 2010 Daniel Raap

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */


/**
 * All the data about one aspect of a participant of a fortbattle for each
 * {@link CombatantSide}.
 * 
 * @author daniel
 */
public class DataSet {
//	private final Map<CombatantSide, List<Integer>> valuesPerSide = new HashMap<CombatantSide, List<Integer>>();

	// CombatantSide enum replaced by an equivalent integer representation: 0
	// (CombatantSide.ATTACKER) or 1 (CombatantSide.DEFENDER)
	// the domain of the map is 0 (CombatantSide.ATTACKER) or 1
	// (CombatantSide.DEFENDER)
	private final HashMapIntList valuesPerSide = new HashMapIntList();

	/**
	 * initialize a new DataSet
	 */
	public DataSet() {
		valuesPerSide.put(0, new LinkedList());
		valuesPerSide.put(1, new LinkedList());
	}

	/**
	 * Add an additional value to this set of data
	 * 
	 * @param side
	 * @param value not <code>null</code>!
	 */
	public void addData(int side, int value) {
		if (side < 0 || side > 1)
			throw new IllegalArgumentException("wrong side!");

		valuesPerSide.get(side).add(value);
	}

	public boolean repOK() {
		if (valuesPerSide == null)
			return false;
		return valuesPerSide.repOK();
	}

	/**
	 * use the given operation to calculate an aggregated value for this dataset
	 * 
	 * @param side
	 * @param type
	 * @return
	 */
//	double aggregate(final Integer side, final DataAggregationType type) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		final List<Integer> data = getSideData(side);
//		return type.aggregate(data);
//	}

	/**
	 * @param side
	 * @return
	 */
//	List<Integer> getSideData(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		final List<Integer> data;
//		if (side == null) {
//			data = getAll();
//		} else {
//			data = valuesPerSide.get(side);
//		}
//		return data;
//	}

	/**
	 * @return the values of all sides
	 */
//	private List<Integer> getAll() {
//		final List<Integer> all = new ArrayList<Integer>();
//		for (final List<Integer> side : valuesPerSide.values()) {
//			all.addAll(side);
//		}
//		return all;
//	}

	/**
	 * @param value to be counted
	 * @param side
	 * @return the number of entries in this data set with the given value for the
	 *         given side
	 */
//	int countOfValuesWith(final Integer value, final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		final List<Integer> data = getSideData(side);
//		int count = 0;
//		for (final Integer v : data) {
//			if (v.equals(value)) {
//				count++;
//			}
//		}
//		return count;
//	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass() + " values A: " + valuesPerSide.get(0);
	}
}
