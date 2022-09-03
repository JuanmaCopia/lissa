/*
 * @(#)Template.java	1.56 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package heapsolving.template.getparameter;

import gov.nasa.jpf.symbc.SymHeap;
import heapsolving.template.Template;
import heapsolving.template.TemplateHarness;


public class TemplateMain {

	public static void main(String[] args) {
		String key = SymHeap.makeSymbolicString("INPUT_KEY");

		Template structure = TemplateHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.getParameter(key);
			} catch (Exception e) {
			}

			SymHeap.countPath();
		}
	}

}
