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
 * A statistic for combatants
 * 
 * TODO use separate classes for calculating and storing the statistics, so
 * statistics i.e. can be serialized
 * 
 * @author daniel
 */

//Replaced enums:
//CombatantSide.ATTACKER = 0
//CombatantSide.DEFENDER = 1

//public enum CombatantStatType {
//flagholdcount, 0
//hitcount, 1
//totalcauseddamage, 2
//takendamage, 3
//dodgecount, 4
//misscount, 5
//takenhits, 6
//finishedhp, 7
//starthp, 8
//maxhp, 9
//weaponmindmg, 10
//weaponmaxdmg, 11
//diedwhen, 12
//charlevel, 13
//charclass; 14
//}

public class CombatantStatistic {
	private final int numberOfRounds;
//	private final Map<CombatantStatType, DataSet> allData = new HashMap<CombatantStatType, DataSet>();
	// Replaced enum CombatantStatType for an equivalent integer representation
	// (between 0 and 14).
	private final HashMapIntDataSet allData = new HashMapIntDataSet();

	/**
	 * create statistics for the given number of rounds.
	 * 
	 * @param numberOfRounds
	 */
	public CombatantStatistic(final int numberOfRounds) {
		this.numberOfRounds = numberOfRounds;
	}

	/**
	 * @param type  what kind of data is given as value?
	 * @param side  at which the combatant of the value fights
	 * @param value
	 */
	public void addData(int type, int side, int value) {
		if (side < 0 || side > 1)
			throw new IllegalArgumentException("wrong side!");
		if (type < 0 || type > 14)
			throw new IllegalArgumentException("wrong type!");

		ensureTypExists(type);
		int storedValue;
		// fix class, because in the data it starts at -1 but we start with 0
		if (type == 14) {
			storedValue = value + 1;
		} else {
			storedValue = value;
		}
		allData.get(type).addData(side, storedValue);
	}

	/**
	 * @param type
	 */
	public void ensureTypExists(int type) {
		if (type < 0 || type > 14)
			throw new IllegalArgumentException("wrong type!");

		if (!allData.containsKey(type)) {
			allData.put(type, new DataSet());
		}
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

//	private double aggregate(final Integer stat, final Integer side, final DataAggregationType function) {
//		if (stat < 0 || stat > 14)
//			throw new IllegalArgumentException("wrong type!");
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		// some data may not be present in old logs (ie. since 1.29 charlevel
//		// and charclass were added)
//		if (!allData.containsKey(stat)) {
//			return 0;
//		}
//		final DataSet statData = allData.get(stat);
//		assert statData != null : "data must be present!";
//		return statData.aggregate(side, function);
//	}

//	private double getAverage(final Integer stat, final Integer side) {
//		if (stat < 0 || stat > 14)
//			throw new IllegalArgumentException("wrong type!");
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return aggregate(stat, side, DataAggregationType.AVERAGE);
//	}
//
//	private double getSum(final Integer stat, final Integer side) {
//		if (stat < 0 || stat > 14)
//			throw new IllegalArgumentException("wrong type!");
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return aggregate(stat, side, DataAggregationType.SUM);
//	}

//	private double getCount(final Integer stat, final Integer side) {
//		if (stat < 0 || stat > 14)
//			throw new IllegalArgumentException("wrong type!");
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return aggregate(stat, side, DataAggregationType.AMOUNT);
//	}
//
//	private double getPositiveAverage(final Integer stat, final Integer side) {
//		if (stat < 0 || stat > 14)
//			throw new IllegalArgumentException("wrong type!");
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return aggregate(stat, side, DataAggregationType.POSITIVE_AVERAGE);
//	}
//
//	private double getPositiveSum(final Integer stat, final Integer side) {
//		if (stat < 0 || stat > 14)
//			throw new IllegalArgumentException("wrong type!");
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return aggregate(stat, side, DataAggregationType.POSITIVE_SUM);
//	}
//
//	private double getPositiveCount(final Integer stat, final Integer side) {
//		if (stat < 0 || stat > 14)
//			throw new IllegalArgumentException("wrong type!");
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return aggregate(stat, side, DataAggregationType.POSITIVE_AMOUNT);
//	}

	/**
	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#numberOfPlayers(de.outstare.fortbattleplayer.model.Integer)
	 */
//	public int numberOfPlayers(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		// just a stat that is available for all combatants
//		return (int) Math.round(getCount(8, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#survivedPlayers(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int survivedPlayers(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getPositiveCount(7, side));
//	}

	/**
	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#initialHealthPerPlayer(de.outstare.fortbattleplayer.model.Integer)
	 */
//	public double initialHealthPerPlayer(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getAverage(8, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#averageWeaponDamage(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public double averageWeaponDamage(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		final DataSet allMinDamage = allData.get(10);
//		final DataSet allMaxDamage = allData.get(11);
//		if (allMinDamage == null || allMaxDamage == null) {
//			// TODO logging: LOG.warn("no weapon damage data available!");
//			// no weapon data available
//			return 0;
//		}
//		// use double precision for calculating average
//		final double avgMinDamage = allMinDamage.aggregate(side, DataAggregationType.AVERAGE);
//		final double avgMaxDamage = allMaxDamage.aggregate(side, DataAggregationType.AVERAGE);
//		return (avgMinDamage + avgMaxDamage) / 2.0;
//	}

	/**
	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#causedDamagePerPlayer(de.outstare.fortbattleplayer.model.Integer)
	 */
//	public double causedDamagePerPlayer(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return getAverage(2, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#shotsFiredWholeSide(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int shotsFiredWholeSide(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getSum(1, side) + getSum(5, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#hitsWholeSide(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int hitsWholeSide(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getSum(1, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#averageLifetime(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public double averageLifetime(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		final int totalPlayers = (int) getCount(12, side);
//		if (totalPlayers == 0) {
//			return 0.0;
//		}
//		final int diedPlayers = (int) getPositiveCount(12, side);
//		final int survivedPlayers = totalPlayers - diedPlayers;
//		final double diedInRound = getPositiveAverage(12, side);
//
//		return (diedPlayers * diedInRound + survivedPlayers * numberOfRounds) / totalPlayers;
//	}

//	private int numberOfClass(final CharacterClass charClass, final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		final DataSet data = allData.get(14);
//		// only available since v1.29
//		if (data == null) {
//			return 0;
//		}
//		final Integer value = Integer.valueOf(charClass.ordinal());
//		return data.countOfValuesWith(value, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#numberOfAdventurers(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int numberOfAdventurers(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return numberOfClass(CharacterClass.ADVENTURER, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#numberOfDuelants(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int numberOfDuelants(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return numberOfClass(CharacterClass.DUELANT, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#numberOfGreenhorns(Integer)
//	 */
//	public int numberOfGreenhorns(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return numberOfClass(CharacterClass.GREENHORN, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#numberOfSoldiers(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int numberOfSoldiers(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return numberOfClass(CharacterClass.SOLDIER, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#numberOfWorkers(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int numberOfWorkers(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return numberOfClass(CharacterClass.WORKER, side);
//	}

	/**
	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#averageLevel(de.outstare.fortbattleplayer.model.Integer)
	 */
//	public double averageLevel(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return getAverage(13, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#initialHealthWholeSide(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int initialHealthWholeSide(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getSum(8, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#endHealthPerPlayer(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int endHealthPerPlayer(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getPositiveAverage(7, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#endHealthWholeSide(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int endHealthWholeSide(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getPositiveSum(7, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#maxPossibleHealthPerPlayer(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int maxPossibleHealthPerPlayer(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getAverage(9, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#maxPossibleHealthWholeSide(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int maxPossibleHealthWholeSide(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getSum(9, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#causedDamageWholeSide(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int causedDamageWholeSide(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getSum(2, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#shotsFiredPerPlayer(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public double shotsFiredPerPlayer(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return getAverage(1, side) + getAverage(5, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#hitsPerPlayer(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public double hitsPerPlayer(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return getAverage(1, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#takenDamagePerPlayer(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public double takenDamagePerPlayer(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return getAverage(3, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#takenDamageWholeSide(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int takenDamageWholeSide(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getSum(3, side));
//	}

	/**
	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#averageHitDamage(de.outstare.fortbattleplayer.model.Integer)
	 */
//	public double averageHitDamage(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		// don't count the misses
//		final double teamHits = hitsWholeSide(side);
//		if (teamHits == 0) {
//			return 0;
//		}
//		final double teamDamage = causedDamageWholeSide(side);
//		final double totalCalculated = teamDamage / teamHits;
//		return totalCalculated;
//	}

	/**
	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#dodgesPerPlayer(de.outstare.fortbattleplayer.model.Integer)
	 */
//	public double dodgesPerPlayer(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return getAverage(4, side);
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#dodgesWholeSide(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public int dodgesWholeSide(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		return (int) Math.round(getSum(4, side));
//	}
//
//	/**
//	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#healthDistribution(de.outstare.fortbattleplayer.model.Integer)
//	 */
//	public SortedMap<Number, Number> healthDistribution(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		// initialize map
//		final SortedMap<Number, Number> result = new TreeMap<Number, Number>();
//		final int maxHP = 14000;
//		final int step = 1000;
//		for (int limit = step; limit <= maxHP; limit += step) {
//			result.put(Integer.valueOf(limit), new AtomicInteger());
//		}
//		// fill with values
//		final Integer stat = 8;
//		if (allData.containsKey(stat)) {
//			final DataSet statData = allData.get(stat);
//			final List<Integer> data = statData.getSideData(side);
//			for (final Integer value : data) {
//				Number key = getUpperLimit(value.intValue(), step);
//				if (!result.containsKey(key)) {
//					// out of range, put it to the last value
//					key = result.lastKey();
//				}
//				((AtomicInteger) result.get(key)).incrementAndGet();
//			}
//		}
//		return result;
//	}

	/**
	 * @see de.outstare.fortbattleplayer.statistics.StaticStatistics#healthDistributionRelative(de.outstare.fortbattleplayer.model.Integer)
	 */
//	public SortedMap<Number, Double> healthDistributionRelative(final Integer side) {
//		if (side < 0 || side > 1)
//			throw new IllegalArgumentException("wrong side!");
//		
//		final SortedMap<Number, Number> distribution = healthDistribution(side);
//		final TreeMap<Number, Double> result = new TreeMap<Number, Double>();
//		final double totalPlayers = numberOfPlayers(side);
//		for (final Entry<Number, Number> mapping : distribution.entrySet()) {
//			final double currentPlayers = mapping.getValue().doubleValue();
//			final Double percentage = Double.valueOf(currentPlayers / totalPlayers * 100.0);
//			result.put(mapping.getKey(), percentage);
//		}
//		return result;
//	}

	// /**
	// * @param value
	// * @param step
	// * @return
	// */
//	private Number getUpperLimit(final int value, final int step) {
//		int limit = 0;
//		while (value > limit) {
//			limit += step;
//		}
//		return Integer.valueOf(limit);
//	}

//	@Override
//	public int numberOfAdventurers(Integer side) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int numberOfDuelants(Integer side) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int numberOfGreenhorns(Integer side) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int numberOfSoldiers(Integer side) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int numberOfWorkers(Integer side) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
}
