/*
 * @(#)TreeMap.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.treemap.containskey;

import gov.nasa.jpf.symbc.SymHeap;
import heapsolving.treemap.TreeMap;
import heapsolving.treemap.TreeMapHarness;

public class TreeMapMain {

	public static void main(String[] args) {
		int key = SymHeap.makeSymbolicInteger("INPUT_KEY");

		TreeMap structure = TreeMapHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.containsKey(key);
			} catch (Exception e) {
			}

			SymHeap.countPath();

            // Property Assertion:
			if (SymHeap.usingIfRepOKStrategy() || SymHeap.usingDriverStrategy())
				assert (structure.isBinTreeWithParentReferences());
			else if (SymHeap.usingSymSolveBasedStrategy()) {
				assert (SymHeap.assertPropertyWithSymSolve("isBinTreeWithParentReferences", structure));
			}


		}
	}

}
