/*
 * @(#)DictionaryInfo.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.dictionaryinfo.addfield;

import gov.nasa.jpf.symbc.SymHeap;

import heapsolving.dictionaryinfo.DictionaryInfo;
import heapsolving.dictionaryinfo.DictionaryInfoHarness;
import heapsolving.dictionaryinfo.FieldInfo;

public class DictionaryInfoMain {

	public static void main(String[] args) {
		int tagNumber = SymHeap.makeSymbolicInteger("tagNumber");
		String name = SymHeap.makeSymbolicString("fieldName");
		FieldInfo field = new FieldInfo();
		field.setName(name);
		field.setTagNumber(tagNumber);

		DictionaryInfo structure = DictionaryInfoHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.addField(field);
			} catch (Exception e) {
			}

			// Property Assertion:
			if (SymHeap.usingIfRepOKStrategy() || SymHeap.usingDriverStrategy())
				assert (structure.areTreesOK());
			else if (SymHeap.usingSymSolveBasedStrategy()) {
				// Given that the addField method increases the structure size, we need to use a
				// finitization with increased size, "propertyCheckFinTreeMap" defined in
				// heapsolving.dictionaryinfo.symsolve.DictionaryInfo
				assert (SymHeap.assertPropertyWithSymSolveUsePropFinitization("areTreesOK", structure));
			}

			SymHeap.countPath();
		}
	}

}
