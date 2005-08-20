/*
 * Copyright 2005 Anite - Central Government Division
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
package com.anite.zebra.test.mocks.taskdefs;
import java.util.HashSet;
import java.util.Set;

import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.test.mocks.MockProcessDef;
import com.anite.zebra.test.mocks.MockRouting;
import com.anite.zebra.test.mocks.MockTaskDefinitions;

/**
 * @author Matthew Norrishew norris
 * Created on 19-Aug-2005
 */
public class MockTaskDef implements ITaskDefinition {

	private static long idCounter = 1;
	private Long id;
	private boolean auto;
	private String className;
	private boolean synchronised;
	private Set routingOut = new HashSet();
	private Set routingIn = new HashSet();
	private String classConstruct;
	private String classDestruct;
	private String name;
	private MockProcessDef processDef;
	
	public MockTaskDef(MockProcessDef pd, String taskName) {
		this.processDef = pd;
		MockTaskDefinitions mtd = (MockTaskDefinitions) pd.getTaskDefs();
		this.id = new Long(idCounter++);
		this.name = taskName;
		mtd.add(this);
	}
	
	public Long getId() {
		return id;
	}

	public boolean isAuto() {
		return auto;
	}

	public String getClassName() {
		return className;
	}

	public boolean isSynchronised() {
		return synchronised;
	}

	public Set getRoutingOut() {
		return routingOut;
	}

	public Set getRoutingIn() {
		return routingIn;
	}

	public String getClassConstruct() {
		return classConstruct;
	}

	public String getClassDestruct() {
		return classDestruct;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public void setClassConstruct(String classConstruct) {
		this.classConstruct = classConstruct;
	}

	public void setClassDestruct(String classDestruct) {
		this.classDestruct = classDestruct;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setSynchronised(boolean synchronised) {
		this.synchronised = synchronised;
	}

	public String getName() {
		return this.name;
	}
	public MockProcessDef getProcessDef(){
		return this.processDef;
	}
	
	public MockRouting addRoutingOut(MockTaskDef destination) {
		MockRouting mr = new MockRouting(this,destination);
		return mr;
	}

	public String toString() {
		return "MOCK-DEF-ID "+ this.id + "[" + this.name + "]";
	}
	
	
}
