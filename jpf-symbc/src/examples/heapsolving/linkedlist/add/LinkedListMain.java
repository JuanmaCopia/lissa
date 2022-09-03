/*
 * @(#)LinkedList.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.linkedlist.add;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.symbc.TestGen;

import heapsolving.linkedlist.LinkedList;
import heapsolving.linkedlist.LinkedListHarness;

public class LinkedListMain {

	public static void main(String[] args) {
		int key = SymHeap.makeSymbolicInteger("INPUT_KEY");

		LinkedList structure = LinkedListHarness.getStructure();
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
