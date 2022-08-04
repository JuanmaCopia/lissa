/*
 * @(#)TransportStats.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.transportstats.byteswritten;

import gov.nasa.jpf.symbc.SymHeap;

import heapsolving.transportstats.TransportStats;
import heapsolving.transportstats.TransportStatsHarness;

public class TransportStatsMain {

	public static void main(String[] args) {
		int key = SymHeap.makeSymbolicInteger("INPUT_KEY");

		TransportStats structure = TransportStatsHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.bytesWritten(key);
			} catch (Exception e) {
			}

			SymHeap.countPath();

            // Property Assertion:
			if (SymHeap.usingIfRepOKStrategy() || SymHeap.usingDriverStrategy())
				assert (structure.areTreesOK());
			else if (SymHeap.usingSymSolveBasedStrategy()) {
				// Given that the bytesWritten method adds a new node, we need to use a finitization with
				// increased size, "propertyCheckFinTransportStats" defined in heapsolving.transportstats.symsolve.TransportStats
				assert (SymHeap.assertPropertyWithSymSolveUsePropFinitization("areTreesOK", structure));
			}


		}
	}

}
