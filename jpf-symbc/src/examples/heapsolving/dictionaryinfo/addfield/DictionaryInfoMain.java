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

			SymHeap.countPath();
		}
	}

}
