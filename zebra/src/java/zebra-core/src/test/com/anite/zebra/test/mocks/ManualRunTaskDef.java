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
import java.util.HashSet;
import java.util.Set;

import com.anite.zebra.core.definitions.api.IProcessDefinition;
import com.anite.zebra.core.definitions.api.ITaskDefinition;


/**
 * @author Eric Pugh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ManualRunTaskDef implements ITaskDefinition {
	private String name;
	private Long id;
	private String processName;
	private boolean synchronise;
	private boolean auto;
	private String className;
	private String classDestruct = null;
	private String classConstruct = null;
	private Set routingOut = new HashSet();
	private Set routingIn = new HashSet();
	private IProcessDefinition parent;
	
	
	/**
	 * 
	 */
	public ManualRunTaskDef() {
		this.id = new Long(2);
		this.name="ManualRunTaskDef";
		this.processName="Process2";
		this.auto=false;
	}
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return Returns the processName.
	 */
	public String getProcessName() {
		return processName;
	}
	/**
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anite.zebra.impl.AbstractTaskDef#getProcessDef()
	 */
	public IProcessDefinition getProcessDef() {
		return parent;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anite.zebra.impl.AbstractTaskDef#getRoutingOut()
	 */
	public Set getRoutingOut() {
		return routingOut;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anite.zebra.api.definitions.InterfaceTaskDef#getRoutingIn()
	 */
	public Set getRoutingIn() {
		return routingIn;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anite.zebra.api.definitions.InterfaceTaskDef#getClassConstruct()
	 */
	public String getClassConstruct() {
		return this.classConstruct;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anite.zebra.api.definitions.InterfaceTaskDef#getClassDestruct()
	 */
	public String getClassDestruct() {
		return this.classDestruct;
	}
	/**
	 * @param pd
	 */
	public void setParent(IProcessDefinition pd) {
		this.parent = pd;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anite.zebra.api.definitions.ITaskDefintions#isAuto()
	 */
	/**
	 * @inheritDoc
	 */
	public boolean isAuto() {
		return this.auto;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anite.zebra.api.definitions.ITaskDefintions#isSynchronised()
	 */
	/**
	 * @inheritDoc
	 */
	public boolean isSynchronised() {
		return this.synchronise;
	}

}
