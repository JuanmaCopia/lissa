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

import java.util.SortedMap;

/**
 * StaticStatistics are data which is immediatly available at the begining of
 * the battle. It contains statistics about the inital and ending state.
 * 
 * @author daniel
 */
public interface StaticStatistics {
	/**
	 * @param side if null the sum of all will be returned
	 * @return the number of playeres for the given side, or all players if no side
	 *         is given
	 */
	int numberOfPlayers(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the number of players who survived the battle for the given side, or
	 *         all players if no side is given
	 */
	int survivedPlayers(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the number of playeres with the class adventurer for the given side,
	 *         or all players if no side is given
	 */
	int numberOfAdventurers(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the number of playeres with the class duelant for the given side, or
	 *         all players if no side is given
	 */
	int numberOfDuelants(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the number of playeres without a class for the given side, or all
	 *         players if no side is given
	 */
	int numberOfGreenhorns(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the number of playeres with the class soldier for the given side, or
	 *         all players if no side is given
	 */
	int numberOfSoldiers(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the number of playeres with the class worker for the given side, or
	 *         all players if no side is given
	 */
	int numberOfWorkers(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the average level of a combatant for the given side
	 */
	double averageLevel(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the average amount of health of an player at the beginning of the
	 *         battle
	 */
	double initialHealthPerPlayer(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the sum of health of all players of the given side at the beginning
	 *         of the battle
	 */
	int initialHealthWholeSide(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the average amount of health of an player at the end of the battle
	 */
	int endHealthPerPlayer(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the sum of health of all players of the given side at the end of the
	 *         battle
	 */
	int endHealthWholeSide(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the average maximum health of a player of the given side at the end
	 *         of the battle
	 */
	int maxPossibleHealthPerPlayer(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the sum of maximum health of all players of the given side at the end
	 *         of the battle
	 */
	int maxPossibleHealthWholeSide(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the average damage of a weapon for the given side
	 */
	double averageWeaponDamage(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the average damage done to an enemy with one hit by a single
	 *         combatant of the given side
	 */
	double averageHitDamage(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the average damage dealt with one hit by one player of the given side
	 */
	double causedDamagePerPlayer(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the sum of all damage dealt by all players of the given side
	 */
	int causedDamageWholeSide(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the number of shots fired by the given side
	 */
	int shotsFiredWholeSide(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the number of shots fired by a player of the given side
	 */
	double shotsFiredPerPlayer(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the number of shots which hit an enemy for the given side
	 */
	int hitsWholeSide(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the average number of shots of a player which hit an enemy for the
	 *         given side
	 */
	double hitsPerPlayer(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the average amount of damage taken by a player for the given side
	 */
	double takenDamagePerPlayer(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the total amount of damage taken by a player for the given side
	 */
	int takenDamageWholeSide(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return number of rounds a player of the given side survived
	 */
	double averageLifetime(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return the average number of dodges for a player of the given side
	 */
	double dodgesPerPlayer(Integer side);

	/**
	 * @param side if null the sum of all will be returned
	 * @return number of dodges for the whole side
	 */
	int dodgesWholeSide(Integer side);

	/**
	 * Creates a mapping of health ranges to the number of players. The first value
	 * gives the upper limit of a players health. The second is the number of
	 * players. <example>Players have 500, 600, 1300, 4000 LP<br>
	 * Result will be: 1000->2, 2000->1, 3000->0, 4000->1</example>
	 * 
	 * @param side
	 * @return a map of max healt points and the number of players within this range
	 */
	SortedMap<Number, Number> healthDistribution(Integer side);

	/**
	 * Creates a mapping of health ranges to the relative number of players. The
	 * first value gives the upper limit of a players health. The second is the
	 * percentage of participants. <example>Players have 500, 600, 1300, 4000 LP<br>
	 * Result will be: 1000->0.5, 2000->0.25, 3000->0, 4000->0.25</example>
	 * 
	 * @param side
	 * @return a map of max healt points and the percentage of players within this
	 *         range
	 */
	SortedMap<Number, Double> healthDistributionRelative(Integer side);
}
