/*
 * @(#)TreeMap.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.treemap.get;

import gov.nasa.jpf.symbc.SymHeap;

import heapsolving.treemap.TreeMap;
import heapsolving.treemap.TreeMapHarness;

public class TreeMapMain {

	public static void main(String[] args) {
		int key = SymHeap.makeSymbolicInteger("INPUT_KEY");

		TreeMap structure = TreeMapHarness.getStructure();
		if (structure != null) {
			try {
				structure.get(key); // Call to method under analysis
			} catch (Exception e) {
			}

			SymHeap.countPath();
		}
	}

}
