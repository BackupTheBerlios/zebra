/*
 * Copyright 2004 Anite - Central Government Division
 *    http://www.anite.com/publicsector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anite.zebra.hivemind.om.state;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.anite.zebra.core.exceptions.DefinitionNotFoundException;

/**
 * This represents a currently running task Instance.
 * 
 * The primary addition is a the propertySet for runtime properties. These are
 * disposted on when this moves into the history
 * 
 * @author Ben.Gidley
 */
@Entity
public class ZebraTaskInstance extends AbstractZebraTaskInstance {

	public static final long KILLED = 66;

	/**
	 * The property set catch all for anything at all It will be emptied when
	 * the history item is constructed
	 */
	private Map<String, ZebraPropertySetEntry> propertySet = new HashMap<String, ZebraPropertySetEntry>();

	/**
	 * Default Constructor
	 */
	public ZebraTaskInstance() {
		super();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param taskInstance
	 * @throws DefinitionNotFoundException
	 */
	public ZebraTaskInstance(AbstractZebraTaskInstance taskInstance) {
		super(taskInstance);

	}

	/**
	 * The property set. This is a catch all container for run time property
	 * information. If you make this too big (>64k from WFMC recommendations) it
	 * will slow the system down and potentially cause interoperability issues.
	 * Please store any sizable data elsewhere.
	 * 
	 * @return
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Map getPropertySet() {
		return this.propertySet;
	}

	/**
	 * @param propertySetEntries
	 *            The propertySetEntries to set.
	 */
	public void setPropertySet(
			Map<String, ZebraPropertySetEntry> propertySetEntries) {
		this.propertySet = propertySetEntries;
	}
}
