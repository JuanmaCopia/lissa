/*
 * @(#)TreeSet.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.treeset.add;

import gov.nasa.jpf.symbc.SymHeap;

import heapsolving.treeset.TreeSet;
import heapsolving.treeset.TreeSetHarness;

public class TreeSetMain {

	public static void main(String[] args) {
		int key = SymHeap.makeSymbolicInteger("INPUT_KEY");

		TreeSet structure = TreeSetHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.add(key);
			} catch (Exception e) {
			}

			// Property Assertion:
			if (SymHeap.usingIfRepOKStrategy() || SymHeap.usingDriverStrategy())
				assert (structure.isBinTreeWithParentReferences());
			else if (SymHeap.usingSymSolveBasedStrategy()) {
				// Given that the add method adds a new node, we need to use a finitization with
				// increased size, "propertyCheckFinTreeSet" defined in
				// heapsolving.treeset.symsolve.TreeSet
				assert (SymHeap.assertPropertyWithSymSolveUsePropFinitization("isBinTreeWithParentReferences",
						structure));
			}

			SymHeap.countPath();
		}
	}

}
