/*
 * @(#)HashMap.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.combatantstatistic.ensuretypexists;

import gov.nasa.jpf.symbc.SymHeap;

import heapsolving.combatantstatistic.CombatantStatistic;
import heapsolving.combatantstatistic.CombatantStatisticHarness;

public class CombatantStatisticMain {

	public static void main(String[] args) {
		int type = SymHeap.makeSymbolicInteger("type");

		CombatantStatistic structure = CombatantStatisticHarness.getStructure();
		if (structure != null) {
			try {
				SymHeap.assume(type >= 0 && type <= 14);
				// Call to method under analysis
				structure.ensureTypExists(type);
			} catch (Exception e) {
			}

			// Property Assertion:
			if (SymHeap.usingIfRepOKStrategy() || SymHeap.usingDriverStrategy())
				assert (structure.repOK());
			else if (SymHeap.usingSymSolveBasedStrategy()) {
				// Given that the put method adds a new node, we need to use a finitization with
				// increased size, "propertyCheckFinHashMap" defined in
				// heapsolving.hashmap.symsolve.HashMap
				assert (SymHeap.assertPropertyWithSymSolveUsePropFinitization("repOK", structure));
			}

			SymHeap.countPath();
		}
	}

}
