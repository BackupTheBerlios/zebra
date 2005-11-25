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

package com.anite.zebra.hivemind.om.defs;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.exception.NestableException;
import org.apache.fulcrum.hivemind.RegistryManager;
import org.apache.fulcrum.security.util.PermissionSet;

import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.ext.definitions.api.IProcessVersions;
import com.anite.zebra.ext.definitions.api.IProperties;
import com.anite.zebra.ext.definitions.api.IPropertyGroups;
import com.anite.zebra.ext.definitions.impl.ProcessDefinition;
import com.anite.zebra.hivemind.impl.ZebraSecurity;

/**
 * This class is used to provide a concrete ProcessDefinition for Hibernate and
 * to store constants/convience functions to access properties and property
 * groups.
 * 
 * @author Eric Pugh
 * @author Ben Gidley
 * 
 */
@Entity
public class ZebraProcessDefinition extends ProcessDefinition {

	/* #com.anite.antelope.zebra.om.AntelopePropertyGroups Dependency_Link */
	/* Constants for Property Groups */
	private static final String PROPGROUP_VISIBILITY = "Visibility";

	private static final String PROPGROUP_INPUTS = "(Inputs)";

	private static final String PROPGROUP_OUTPUTS = "(Outputs)";

	/* Constants for visibility properties */
	private static final String PROP_DISPLAYNAME = "Display Name";

	private static final String PROP_DEBUG_FLOW = "DeubgFlow";

	/* Constants for security properties */
	private static final String PROPGROUP_SECURITY = "Security";

	private static final String PROP_START_PERMISSIONS = "Process Start Permissions";

	private static final String PROP_DYNAMIC_PERMISSIONS = "Dynamic Permissions";

	/* Overidden functions to force XDoclet to read Hibernate tags */

	public String getClassConstruct() {
		return super.getClassConstruct();
	}

	public String getClassDestruct() {
		return super.getClassDestruct();
	}

	@ManyToOne(cascade = { CascadeType.ALL }, targetEntity = ZebraTaskDefinition.class)
	@JoinColumn(name = "firstTaskDefId")
	public ITaskDefinition getFirstTask() {
		return super.getFirstTask();
	}

	@Id(generate = GeneratorType.AUTO)
	public Long getId() {
		return super.getId();
	}

	@ManyToOne(cascade = { CascadeType.ALL }, targetEntity = ZebraPropertyGroups.class)
	@JoinColumn(name = "propertyGroupsId")
	public IPropertyGroups getPropertyGroups() {
		return super.getPropertyGroups();
	}

	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = ZebraProcessVersions.class)
	@JoinColumn(name = "versionId")
	public IProcessVersions getProcessVersions() {
		return super.getProcessVersions();
	}

	/**
	 * @param versions
	 *            The versions to set.
	 */
	public void setVersions(IProcessVersions versions) {
		super.setProcessVersions(versions);
	}

	@OneToMany(targetEntity = ZebraRoutingDefinition.class, cascade=CascadeType.ALL)
	@JoinTable(table = @Table(name = "processDefinitionRoutings"), joinColumns = { @JoinColumn(name = "processDefinitionId") }, inverseJoinColumns = @JoinColumn(name = "routingDefinitionId"))
	public Set getRoutingDefinitions() {
		return super.getRoutingDefinitions();
	}

	@OneToMany(targetEntity = ZebraTaskDefinition.class, cascade=CascadeType.ALL)
	@JoinTable(table = @Table(name = "processTaskDefinitions"), joinColumns = { @JoinColumn(name = "processDefinitionId") }, inverseJoinColumns = @JoinColumn(name = "taskDefinitionId"))
	public Set getTaskDefinitions() {
		return super.getTaskDefinitions();
	}

	public Long getVersion() {
		return super.getVersion();
	}

	@Transient
	/* Custom Helper method to get property groups */
	private IProperties getVisibilityProperties() {
		return getPropertyGroups().getProperties(PROPGROUP_VISIBILITY);
	}

	@Transient
	public IProperties getInputs() {
		return getPropertyGroups().getProperties(PROPGROUP_INPUTS);
	}

	@Transient
	public IProperties getOutputs() {
		return getPropertyGroups().getProperties(PROPGROUP_OUTPUTS);
	}

	@Transient
	public IProperties getSecurityProperties() {
		return getPropertyGroups().getProperties(PROPGROUP_SECURITY);
	}

	/* Custom Helper methods to quickly get visibility properties */
	@Transient
	public String getDisplayName() {
		String displayName = getVisibilityProperties().getString(
				PROP_DISPLAYNAME);
		if (displayName == null) {
			displayName = getName();
		}
		return displayName;

	}

	@Transient
	public boolean getDebugFlow() {
		return getVisibilityProperties().getBoolean(PROP_DEBUG_FLOW);
	}

	/* Helpers to get security properties */
	@Transient
	public String getDynamicPermissions() {
		return this.getSecurityProperties().getString(PROP_DYNAMIC_PERMISSIONS);
	}

	@Transient
	private String getStartPermissionsText() {
		return this.getSecurityProperties().getString(PROP_START_PERMISSIONS);
	}

	@Transient
	public PermissionSet getStartPermissions() throws NestableException {
		ZebraSecurity security = (ZebraSecurity) RegistryManager.getInstance()
				.getRegistry().getService("zebra.ZebraSecurity",
						ZebraSecurity.class);
		return security.getPermissionSet(this.getStartPermissionsText());

	}

}