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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.anite.zebra.ext.definitions.api.IProcessVersion;
import com.anite.zebra.ext.definitions.impl.ProcessVersions;

/**
 * Extends Process Versions to make XDoclet read tags
 * 
 * @author Eric Pugh
 * @author Ben Gidley
 */
@Entity
public class ZebraProcessVersions extends ProcessVersions {

	public String getName() {
		return super.getName();
	}

	@Id(generate = GeneratorType.AUTO)
	public Long getId() {
		return super.getId();
	}

	@OneToMany(targetEntity = ZebraProcessDefinition.class, cascade=CascadeType.ALL)
	@JoinTable(table = @Table(name = "processVersionProcesses"), joinColumns = { @JoinColumn(name = "processVersionId") }, inverseJoinColumns = @JoinColumn(name = "processDefinitionId"))
	public Set getProcessVersions() {
		return super.getProcessVersions();
	}

	public void addProcessVersion(IProcessVersion processVersion) {
		super.addProcessVersion(processVersion);
		if (processVersion instanceof ZebraProcessDefinition) {
			((ZebraProcessDefinition) processVersion).setVersions(this);
		}
	}

}
