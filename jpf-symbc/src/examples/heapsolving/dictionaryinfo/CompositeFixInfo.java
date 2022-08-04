/**
 * InfoDao.java 10:49:24 PM Apr 21, 2008
 * 
 * <PRE>
 * Copyright (c) 2008, Jan Amoyo
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 'AS IS';
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * </PRE>
 */

package heapsolving.dictionaryinfo;

import java.util.List;

/**
 * An interface representing an item in the FIX specification (field, component,
 * message) that holds multiple items.
 * 
 * @author jramoyo
 */
public interface CompositeFixInfo extends FixInfo {

	/**
	 * Returns a component given an id
	 * 
	 * @param id - a component id
	 * @return the component given an id
	 */
	public ComponentInfo getComponent(int id);

	/**
	 * Returns a component given a name
	 * 
	 * @param name - a component name;
	 * @return the component given a name
	 */
	public ComponentInfo getComponent(String name);

	/**
	 * Returns the components
	 * 
	 * @return the components
	 */
	public List<ComponentInfo> getComponents();

	/**
	 * Adds a component
	 * 
	 * @param component - a component
	 * @param position  - a position
	 */
	public void addComponent(ComponentInfo component, double position);

	/**
	 * Returns a field given a tagNumber
	 * 
	 * @param tagNumber - a tagNumber
	 * @return a field given a tagNumber
	 */
	public FieldInfo getField(int tagNumber);

	/**
	 * Returns a field given a name
	 * 
	 * @param name - a name
	 * @return a field given a name
	 */
	public FieldInfo getField(String name);

	/**
	 * Returns the fields
	 * 
	 * @return the fields
	 */
	public List<FieldInfo> getFields();

	/**
	 * Adds a field
	 * 
	 * @param field    - a field
	 * @param position - a position
	 */
	public void addField(FieldInfo field, double position);

	/**
	 * Replace the field by a group. This is used by FPL parsers which cannot tell
	 * if a field is a group just from Fields.xml
	 * 
	 * @param field - a field
	 * @param group - a group
	 */
	public void replaceAsGroup(FieldInfo field, GroupInfo group);

	/**
	 * Returns the items in this CompositeInfo
	 * 
	 * @return the items in this CompositeInfo
	 */
	public List<FixInfo> getItems();

}
