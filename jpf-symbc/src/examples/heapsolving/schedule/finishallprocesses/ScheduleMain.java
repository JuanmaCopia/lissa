/*
 * @(#)Schedule.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.schedule.finishallprocesses;

import gov.nasa.jpf.symbc.SymHeap;
import gov.nasa.jpf.symbc.TestGen;

import heapsolving.schedule.Schedule;
import heapsolving.schedule.ScheduleHarness;

public class ScheduleMain {

	public static void main(String[] args) {

		Schedule structure = ScheduleHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.finishAllProcesses();
			} catch (Exception e) {
			}

			SymHeap.countPath();
		}
	}

}
