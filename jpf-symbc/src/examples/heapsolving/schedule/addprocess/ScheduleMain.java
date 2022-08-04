/*
 * @(#)Schedule.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.schedule.addprocess;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.symbc.TestGen;

import heapsolving.schedule.Schedule;
import heapsolving.schedule.ScheduleHarness;

public class ScheduleMain {

	public static void main(String[] args) {
		int key = SymHeap.makeSymbolicInteger("INPUT_KEY");

		Schedule structure = ScheduleHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.addProcess(key);
			} catch (Exception e) {
			}

			SymHeap.countPath();

            // Property Assertion:
			if (SymHeap.usingIfRepOKStrategy() || SymHeap.usingDriverStrategy())
				assert (structure.repOK());
			else if (SymHeap.usingSymSolveBasedStrategy()) {
				// Given that the addProcess method adds a new node, we need to use a finitization with
				// increased size, "propertyCheckFinSchedule" defined in
				// heapsolving.schedule.symsolve.Schedule
				assert (SymHeap.assertPropertyWithSymSolveUsePropFinitization("repOK", structure));
			}


		}
	}

}
