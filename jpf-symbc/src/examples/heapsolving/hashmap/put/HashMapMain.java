/*
 * @(#)HashMap.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.hashmap.put;

import gov.nasa.jpf.symbc.SymHeap;

import heapsolving.hashmap.HashMap;
import heapsolving.hashmap.HashMapHarness;

public class HashMapMain {

	public static void main(String[] args) {
		int key = SymHeap.makeSymbolicInteger("INPUT_KEY");
		Object value = new Object();

		HashMap structure = HashMapHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.put(key, value);
			} catch (Exception e) {
			}

			SymHeap.countPath();
		}
	}

}
