/**
 * FieldInfo.java 9:05:06 PM Apr 21, 2008
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
import java.util.List;
import java.util.TreeMap;

/**
 * Represents a Field as defined by the FIX specification.
 * 
 * @author jramoyo
 */
public class FieldInfo implements FixInfo {

	private int tagNumber;

	public String name;

	private String dataType;

	private String description;

	private String abbreviation;

	private String overrideXmlName;

	private String baseCategory;

	private String baseCategoryXmlName;

	private String unionDataType;

	private String usesEnumFromTag;

	private String comments;

	private int length;

	private boolean isNotRequiredXml;

	private String deprecatingVersion;

	private List<ComponentInfo> requiringComponents;

	private TreeMap<String, ValueInfo> validValues;

	/**
	 * Returns the tagNumber
	 * 
	 * @return the tagNumber
	 */
	public int getTagNumber() {
		return tagNumber;
	}

	/**
	 * Modifies the tagNumber
	 * 
	 * @param tagNumber - the tagNumber to set
	 */
	public void setTagNumber(int tagNumber) {
		this.tagNumber = tagNumber;
	}

	/**
	 * Returns the fieldName
	 * 
	 * @return the fieldName
	 */
	public String getName() {
		return name;
	}

	/**
	 * Modifies the fieldName
	 * 
	 * @param name - the fieldName to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the dataType
	 * 
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Modifies the dataType
	 * 
	 * @param dataType - the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * Returns the description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Modifies the description
	 * 
	 * @param description - the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the comments
	 * 
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Modifies the comments
	 * 
	 * @param comments - the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Returns the abbreviation
	 * 
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Modifies the abbreviation
	 * 
	 * @param abbreviation - the abbreviation to set
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Returns the overrideXmlName
	 * 
	 * @return the overrideXmlName
	 */
	public String getOverrideXmlName() {
		return overrideXmlName;
	}

	/**
	 * Modifies the overrideXmlName
	 * 
	 * @param overrideXmlName - the overrideXmlName to set
	 */
	public void setOverrideXmlName(String overrideXmlName) {
		this.overrideXmlName = overrideXmlName;
	}

	/**
	 * Returns the baseCategory
	 * 
	 * @return the baseCategory
	 */
	public String getBaseCategory() {
		return baseCategory;
	}

	/**
	 * Modifies the baseCategory
	 * 
	 * @param baseCategory - the baseCategory to set
	 */
	public void setBaseCategory(String baseCategory) {
		this.baseCategory = baseCategory;
	}

	/**
	 * Returns the baseCategoryXmlName
	 * 
	 * @return the baseCategoryXmlName
	 */
	public String getBaseCategoryXmlName() {
		return baseCategoryXmlName;
	}

	/**
	 * Modifies the baseCategoryXmlName
	 * 
	 * @param baseCategoryXmlName - the baseCategoryXmlName to set
	 */
	public void setBaseCategoryXmlName(String baseCategoryXmlName) {
		this.baseCategoryXmlName = baseCategoryXmlName;
	}

	/**
	 * Returns the unionDataType
	 * 
	 * @return the unionDataType
	 */
	public String getUnionDataType() {
		return unionDataType;
	}

	/**
	 * Modifies the unionDataType
	 * 
	 * @param unionDataType - the unionDataType to set
	 */
	public void setUnionDataType(String unionDataType) {
		this.unionDataType = unionDataType;
	}

	/**
	 * Returns the usesEnumFromTag
	 * 
	 * @return the usesEnumFromTag
	 */
	public String getUsesEnumFromTag() {
		return usesEnumFromTag;
	}

	/**
	 * Modifies the usesEnumFromTag
	 * 
	 * @param usesEnumFromTag - the usesEnumFromTag to set
	 */
	public void setUsesEnumFromTag(String usesEnumFromTag) {
		this.usesEnumFromTag = usesEnumFromTag;
	}

	/**
	 * Returns the length
	 * 
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Modifies the length
	 * 
	 * @param length - the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Returns the isNotRequiredXml
	 * 
	 * @return the isNotRequiredXml
	 */
	public boolean isNotRequiredXml() {
		return isNotRequiredXml;
	}

	/**
	 * Modifies the isNotRequiredXml
	 * 
	 * @param isNotRequiredXml - the isNotRequiredXml to set
	 */
	public void setNotRequiredXml(boolean isNotRequiredXml) {
		this.isNotRequiredXml = isNotRequiredXml;
	}

	/**
	 * Returns the deprecatingVersion
	 * 
	 * @return the deprecatingVersion
	 */
	public String getDeprecatingVersion() {
		return deprecatingVersion;
	}

	/**
	 * Modifies the deprecatingVersion
	 * 
	 * @param deprecatingVersion - the deprecatingVersion to set
	 */
	public void setDeprecatingVersion(String deprecatingVersion) {
		this.deprecatingVersion = deprecatingVersion;
	}

	/**
	 * Returns the requiringComponents
	 * 
	 * @return the requiringComponents
	 */
	public List<ComponentInfo> getRequiringComponents() {
		return requiringComponents;
	}

	/**
	 * Add a requiringComponent
	 * 
	 * @param component - a requiringComponent
	 */
	public void addRequiringComponent(ComponentInfo component) {
		if (requiringComponents == null) {
			requiringComponents = new ArrayList<ComponentInfo>();
		}
		requiringComponents.add(component);
	}

	/**
	 * Modifies the requiringComponents
	 * 
	 * @param requiringComponents - the requiringComponent to set
	 */
	protected void setRequiringComponents(List<ComponentInfo> requiringComponents) {
		this.requiringComponents = requiringComponents;
	}

	/**
	 * Returns whether this field is required in the specified component
	 * 
	 * @param component - a component
	 * @return whether this field is required in the specified component
	 */
	public boolean isRequiredInComponent(ComponentInfo component) {
		if (requiringComponents != null) {
			return requiringComponents.contains(component);
		} else {
			return false;
		}
	}

	/**
	 * Returns the validValues
	 * 
	 * @return the validValues
	 */
	public List<ValueInfo> getValidValues() {
		if (validValues != null) {
			return new ArrayList<ValueInfo>(validValues.values());
		} else {
			return null;
		}
	}

	/**
	 * Adds a value
	 * 
	 * @param value - a value
	 */
	public void addValidValue(ValueInfo value) {
		if (validValues == null) {
			validValues = new TreeMap<String, ValueInfo>();
		}
		validValues.put(value.getValue(), value);
	}

	/**
	 * Returns whether a given value is valid
	 * 
	 * @param value - a value
	 * @return whether a given value is valid
	 */
	public boolean isValidValue(String value) {
		return validValues.keySet().contains(value);
	}

	/**
	 * Modifies the validValues
	 * 
	 * @param validValues - the validValues to set
	 */
	protected void setValidValues(TreeMap<String, ValueInfo> validValues) {
		this.validValues = validValues;
	}

	/**
	 * Returns the validValues Map
	 * 
	 * @return the validValues Map
	 */
	protected TreeMap<String, ValueInfo> getValidValuesMap() {
		return validValues;
	}

}
