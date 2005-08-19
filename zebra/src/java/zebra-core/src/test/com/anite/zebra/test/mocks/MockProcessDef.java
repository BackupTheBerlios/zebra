package com.anite.zebra.test.mocks;
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
import com.anite.zebra.core.definitions.api.IProcessDefinition;
import com.anite.zebra.core.definitions.api.IRoutingDefinitions;
import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.core.definitions.api.ITaskDefinitions;


/**
 * @author Eric Pugh
 *
 * Mock object for testing.
 */
public class MockProcessDef implements IProcessDefinition {

	
	
	private Long id;
	private String name;
	private MockRoutingDefs routingDefs;

	public MockProcessDef(String name) {
		this.name = name;
		routingDefs = new MockRoutingDefs(this);
	}
	public IRoutingDefinitions getRoutingDefs() {
		return routingDefs;
	}
	public MockRoutingDefs getMockRoutingDefs() {
		return routingDefs;
	}
	private ITaskDefinitions taskDefs = new MockTaskDefinitions(this);
	private ITaskDefinition firstTask = null;

	private String classConstruct = null;
	private String classDestruct = null;
	private Long version;
		
	public Long getVersion() {
		return this.version;
	}
	
	public void setVersion(Long version) {
		this.version = version;
	}
	
	public void setFirstTask(ITaskDefinition taskDef) {
		firstTask = taskDef;
	}

	public String getClassConstruct() {
		return this.classConstruct;
	}

	public void setClassConstruct(String className){
		this.classConstruct = className;
	}
	
	public String getClassDestruct() {
		return this.classDestruct;
	}
	
	public void setClassDestruct(String className){
		this.classDestruct = className;
	}
	
	public ITaskDefinition getFirstTask() {
		return firstTask;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ITaskDefinitions getTaskDefs() {
		return taskDefs;
	}




}
