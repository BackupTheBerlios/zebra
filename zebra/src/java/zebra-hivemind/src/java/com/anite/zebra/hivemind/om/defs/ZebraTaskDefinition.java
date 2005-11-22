/*
 * Copyright 2004 Anite - Central Government Division
 * http://www.anite.com/publicsector
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.anite.zebra.hivemind.om.defs;

import java.util.Set;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fulcrum.security.util.PermissionSet;

import com.anite.zebra.ext.definitions.api.IProperties;
import com.anite.zebra.ext.definitions.api.IPropertyGroups;
import com.anite.zebra.ext.definitions.impl.TaskDefinition;

/**
 * A task definition class for Antelope
 * 
 * @author Eric Pugh
 * @author Ben Gidley
 * @hibernate.class
 * @hibernate.cache usage="transactional"
 */
@Entity
public class ZebraTaskDefinition extends TaskDefinition implements IXmlDefinition {
	private final static Log log = LogFactory
			.getLog(ZebraTaskDefinition.class);

	/* General Property's */
	private static final String PROPGROUP_GENERAL = "(General Task Properties)";

	private static final String PROP_SHOWINHISTORY = "ShowInHistory";

	private static final String PROP_STATIC_PERMISSIONS = "Static Permissions";

	private static final String PROP_DYNAMIC_PERMISSIONS = "Dynamic Permissions";

	private static final String PROP_GET_SHOW_IN_TASK_LIST = "ShowInTaskList";

	/* Subprocess properties */
	private static final String PROPGROUP_SUBPROCESS = "SubProcess";

	private static final String PROP_SUBPROCESS_NAME = "Process Name";

	private static final String PROP_PUSH_OUTPUTS = "Push Outputs";

	private static final String PROPGROUP_INPUTS = "(Inputs)";

	private static final String PROPGROUP_OUTPUTS = "(Outputs)";

	/* Screen/Decision Properties */
	private static final String PROPGROUP_SCREEN = "Screen";

	private static final String PROP_SCREEN_NAME = "Screen Name";

	private static final String PROP_AUTO_SHOW = "Auto Show";

	private Long xmlId;

	/*
	 * Methdods from IXmlDefinition (/
	 * 
	 * /** @return Returns the xmlId. @hibernate.property
	 */
	public Long getXmlId() {
		return xmlId;
	}

	/**
	 * @param xmlId
	 *            The xmlId to set.
	 */
	public void setXmlId(Long xmlId) {
		this.xmlId = xmlId;
	}

	/**
	 * Note this is from BOTH TaskDefinition and IXMLDefinition
	 * 
	 * @return Returns the id.
	 * @hibernate.id generator-class="native"
	 */
	@Id(generate=GeneratorType.AUTO)
	public Long getId() {
		return super.getId();
	}

	/* Methods from Task Definition */

	/**
	 * @hibernate.property
	 */
	public String getClassConstruct() {
		return super.getClassConstruct();
	}

	/**
	 * @hibernate.property
	 */
	public String getClassDestruct() {
		return super.getClassDestruct();
	}

	/**
	 * Return the class name to use to instantiate a task
	 * 
	 * @return Returns the className.
	 * @hibernate.property
	 */
	public String getClassName() {
		return super.getClassName();
	}

	/**
	 * @return Returns the name.
	 * @hibernate.property
	 */
	public String getName() {
		return super.getName();
	}

	/**
	 * @return Returns the propertyGroups.
	 * @hibernate.many-to-one column="taskPropertyGroupsId" not-null="false"
	 *                        class="com.anite.zebra.hivemind.om.defs.ZebraPropertyGroups"
	 *                        cascade="save-update"
	 */
	@ManyToOne(targetEntity=ZebraPropertyGroups.class)
	public IPropertyGroups getPropertyGroups() {
		return super.getPropertyGroups();
	}

	/**
	 * @return Returns the routingIn.
	 * @hibernate.set cascade="all" inverse="false"
	 * @hibernate.collection-key column="taskDefRoutingInId"
	 * @hibernate.collection-one-to-many class="com.anite.zebra.hivemind.om.defs.ZebraRoutingDefinition"
	 * @hibernate.collection-cache usage="transactional"
	 */
	@OneToMany(targetEntity = ZebraRoutingDefinition.class)
	@JoinTable(table = @Table(name = "taskDefinitionRoutingIn"), joinColumns = { @JoinColumn(name = "taskDefinitionId") }, inverseJoinColumns = @JoinColumn(name = "routingDefinitionId"))
	public Set getRoutingIn() {
		return super.getRoutingIn();
	}

	/**
	 * @return Returns the routingOut.
	 * @hibernate.set cascade="all" inverse="false"
	 * @hibernate.collection-key column="taskDefRoutingOutId"
	 * @hibernate.collection-one-to-many class="com.anite.zebra.hivemind.om.defs.ZebraRoutingDefinition"
	 * @hibernate.collection-cache usage="transactional"
	 */
	@OneToMany(targetEntity = ZebraRoutingDefinition.class)
	@JoinTable(table = @Table(name = "taskDefinitionRoutingOut"), joinColumns = { @JoinColumn(name = "taskDefinitionId") }, inverseJoinColumns = @JoinColumn(name = "routingDefinitionId"))
	public Set getRoutingOut() {
		return super.getRoutingOut();
	}

	/**
	 * @hibernate.property
	 */
	public boolean getSynchronise() {
		return super.getSynchronise();
	}

	/**
	 * @hibernate.property
	 */
	public boolean isAuto() {
		return super.isAuto();
	}

	/* Custom Additional Methods */

	/* Property Groups */

	@Transient
	public IProperties getGeneralProperties() {
		return getPropertyGroups().getProperties(PROPGROUP_GENERAL);
	}
	@Transient
	public IProperties getScreenProperties() {
		return getPropertyGroups().getProperties(PROPGROUP_SCREEN);
	}
	@Transient
	public IProperties getSubflowProperties() {
		return getPropertyGroups().getProperties(PROPGROUP_SUBPROCESS);
	}
	@Transient
	public IProperties getInputs() {
		return getPropertyGroups().getProperties(PROPGROUP_INPUTS);
	}
	@Transient
	public IProperties getOutputs() {
		return getPropertyGroups().getProperties(PROPGROUP_OUTPUTS);
	}

	/* Property Shortcuts */
	@Transient
	public boolean getShowInHistory() {
		return getGeneralProperties().getBoolean(PROP_SHOWINHISTORY);
	}
	@Transient
	protected String getStaticPermissionsString() {
		return getGeneralProperties().getString(PROP_STATIC_PERMISSIONS);
	}
	@Transient
	protected String getDynamicPermissions() {
		return getGeneralProperties().getString(PROP_DYNAMIC_PERMISSIONS);
	}

	/**
	 * This is used to filter all tasks by task lists Unlike most of these this
	 * defaults to false as it is easier that way!
	 * 
	 * @return
	 */
	@Transient
	public boolean getShowInTaskList() {
		Boolean show = getGeneralProperties().getBooleanAsObj(
				PROP_GET_SHOW_IN_TASK_LIST);
		if (show == null) {
			return true;
		} else {
			return show.booleanValue();
		}
	}
	@Transient
	public String getSubProcessName() {
		return getSubflowProperties().getString(PROP_SUBPROCESS_NAME);
	}
	@Transient
	public boolean getPushOutputs() {
		return getSubflowProperties().getBoolean(PROP_PUSH_OUTPUTS);
	}
	@Transient
	public String getScreenName() {
		return getScreenProperties().getString(PROP_SCREEN_NAME);
	}
	@Transient
	public boolean getAutoShow() {
		return getScreenProperties().getBoolean(PROP_AUTO_SHOW);
	}
	@Transient
	public PermissionSet getStaticPermissions() throws NestableException {
		//@TODO return ZebraHelper.getInstance().getPermissionSet(				this.getStaticPermissionsString());
		return null;
	}

}