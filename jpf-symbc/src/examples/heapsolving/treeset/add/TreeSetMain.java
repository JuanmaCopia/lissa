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

			SymHeap.countPath();
		}
	}

}
