/*
 * @(#)SQLFilterClauses.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.sqlfilterclauses.put;

import gov.nasa.jpf.symbc.SymHeap;
import heapsolving.sqlfilterclauses.SQLFilterClauses;
import heapsolving.sqlfilterclauses.SQLFilterClausesHarness;

public class SQLFilterClausesMain {

	public static void main(String[] args) {
		String clauseName = SymHeap.makeSymbolicString("INPUT_KEY");
		String tableName = SymHeap.makeSymbolicString("INPUT_KEY2");
		String clauseInformation = SymHeap.makeSymbolicString("INPUT_KEY3");

		SQLFilterClauses structure = SQLFilterClausesHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.put(clauseName, tableName, clauseInformation);
			} catch (Exception e) {
			}

			SymHeap.countPath();
		}
	}

}
