/**
 * GroupInfo.java 9:20:12 PM Apr 21, 2008
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Represents a group as defined by the FIX specification
 * 
 * @author jramoyo
 */
public class GroupInfo extends FieldInfo implements CompositeFixInfo {

	// Default collection
	private TreeMap<Double, FixInfo> itemsByPosition;

	private HashMap<FixInfo, Double> itemsByContent;

	// Default collection
	private TreeMap<Integer, FieldInfo> fieldsByTagNumber;

	private TreeMap<String, FieldInfo> fieldsByName;

	// Default collection
	private TreeMap<String, ComponentInfo> componentsByName;

	private TreeMap<Integer, ComponentInfo> componentsById;

	/**
	 * Creates a new GroupInfo
	 * 
	 * @param field - a field to populate from
	 */
	public GroupInfo(FieldInfo field) {
		setTagNumber(field.getTagNumber());
		setName(field.getName());
		setDataType(field.getDataType());
		setDescription(getDescription());
		setAbbreviation(field.getAbbreviation());
		setOverrideXmlName(field.getOverrideXmlName());
		setBaseCategory(field.getBaseCategory());
		setBaseCategoryXmlName(field.getBaseCategoryXmlName());
		setUnionDataType(field.getUnionDataType());
		setUsesEnumFromTag(field.getUsesEnumFromTag());
		setComments(field.getComments());
		setLength(field.getLength());
		setDeprecatingVersion(field.getDeprecatingVersion());
		setRequiringComponents(field.getRequiringComponents());
		setValidValues(field.getValidValuesMap());
	}

	/**
	 * @see org.fixsuite.message.info.CompositeFixInfo#getFields()
	 */
	public List<FieldInfo> getFields() {
		if (fieldsByTagNumber != null) {
			return new ArrayList<FieldInfo>(fieldsByTagNumber.values());
		}
		return null;
	}

	/**
	 * @see org.fixsuite.message.info.CompositeFixInfo#getField(int)
	 */
	public FieldInfo getField(int tagNumber) {
		if (fieldsByTagNumber != null) {
			return fieldsByTagNumber.get(tagNumber);
		}
		return null;
	}

	/**
	 * @see org.fixsuite.message.info.CompositeFixInfo#getField(java.lang.String)
	 */
	public FieldInfo getField(String name) {
		if (fieldsByName != null) {
			return fieldsByName.get(name);
		}
		return null;
	}

	/**
	 * @see org.fixsuite.message.info.CompositeFixInfo#addField(org.fixsuite.message.info.FieldInfo,
	 *      double)
	 */
	public void addField(FieldInfo field, double position) {
		if (fieldsByTagNumber == null) {
			fieldsByTagNumber = new TreeMap<Integer, FieldInfo>();
			fieldsByName = new TreeMap<String, FieldInfo>();
		}
		fieldsByTagNumber.put(field.getTagNumber(), field);
		fieldsByName.put(field.getName(), field);
		addItem(field, position);
	}

	/**
	 * @see org.fixsuite.message.info.CompositeFixInfo#getComponents()
	 */
	public List<ComponentInfo> getComponents() {
		if (componentsByName != null) {
			return new ArrayList<ComponentInfo>(componentsByName.values());
		}
		return null;
	}

	/**
	 * @see org.fixsuite.message.info.CompositeFixInfo#getComponent(int)
	 */
	public ComponentInfo getComponent(int id) {
		if (componentsById != null) {
			return componentsById.get(id);
		}
		return null;
	}

	/**
	 * @see org.fixsuite.message.info.CompositeFixInfo#getComponent(java.lang.String)
	 */
	public ComponentInfo getComponent(String name) {
		if (componentsByName != null) {
			return componentsByName.get(name);
		}
		return null;
	}

	/**
	 * @see org.fixsuite.message.info.CompositeFixInfo#addComponent(org.fixsuite.message.info.ComponentInfo,
	 *      double)
	 */
	public void addComponent(ComponentInfo component, double position) {
		if (componentsByName == null) {
			componentsByName = new TreeMap<String, ComponentInfo>();
			componentsById = new TreeMap<Integer, ComponentInfo>();
		}
		componentsByName.put(component.getName(), component);
		componentsById.put(component.getId(), component);
		addItem(component, position);
	}

	/**
	 * @see org.fixsuite.message.info.CompositeFixInfo#getItems()
	 */
	public List<FixInfo> getItems() {
		if (itemsByPosition != null) {
			return new ArrayList<FixInfo>(itemsByPosition.values());
		}
		return null;
	}

	/**
	 * @see org.fixsuite.message.info.CompositeFixInfo#replaceAsGroup(org.fixsuite.message.info.FieldInfo,
	 *      org.fixsuite.message.info.GroupInfo)
	 */
	public void replaceAsGroup(FieldInfo field, GroupInfo group) {
		fieldsByTagNumber.put(field.getTagNumber(), group);
		fieldsByName.put(field.getName(), group);
		double position = itemsByContent.get(field);
		itemsByContent.remove(field);
		itemsByContent.put(group, position);
		itemsByPosition.put(position, group);
	}

	private void addItem(FixInfo item, double position) {
		if (itemsByPosition == null) {
			itemsByPosition = new TreeMap<Double, FixInfo>();
			itemsByContent = new HashMap<FixInfo, Double>();
		}
		itemsByPosition.put(position, item);
		itemsByContent.put(item, position);
	}

}
