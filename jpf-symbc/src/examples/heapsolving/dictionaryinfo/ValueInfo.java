/**
 * ValueInfo.java 9:05:42 PM Apr 21, 2008
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

/**
 * Represents a Pre-defined value as defined by the FIX specification.
 * 
 * @author jramoyo
 */
public class ValueInfo {

	private String value;

	private String description;

	private String group;

	private String deprecatingVersion;

	/**
	 * Returns the value
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Modifies the value
	 * 
	 * @param value - the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
	 * Returns the group
	 * 
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Modifies the group
	 * 
	 * @param group - the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
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

}
