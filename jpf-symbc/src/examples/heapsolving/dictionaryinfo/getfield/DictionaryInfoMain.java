/*
 * @(#)DictionaryInfo.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.dictionaryinfo.getfield;

import gov.nasa.jpf.symbc.SymHeap;
import heapsolving.dictionaryinfo.DictionaryInfo;
import heapsolving.dictionaryinfo.DictionaryInfoHarness;

public class DictionaryInfoMain {

	public static void main(String[] args) {
		int tagNumber = SymHeap.makeSymbolicInteger("INPUT_KEY");

		DictionaryInfo structure = DictionaryInfoHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.getField(tagNumber);
			} catch (Exception e) {
			}

			// Property Assertion:
			if (SymHeap.usingIfRepOKStrategy() || SymHeap.usingDriverStrategy())
				assert (structure.areTreesOK());
			else if (SymHeap.usingSymSolveBasedStrategy()) {
				assert (SymHeap.assertPropertyWithSymSolve("areTreesOK", structure));
			}

			SymHeap.countPath();
		}
	}

}
