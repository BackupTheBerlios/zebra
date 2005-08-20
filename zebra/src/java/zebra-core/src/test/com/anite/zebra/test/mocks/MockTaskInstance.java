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
import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.core.exceptions.DefinitionNotFoundException;
import com.anite.zebra.core.state.api.IFOE;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.core.state.api.ITaskInstance;

/**
 * @author Eric Pugh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MockTaskInstance implements ITaskInstance {

	public static final long STATE_DELETED = -100;
	private static long taskInstanceCounter = 0; 
	private Long taskInstanceId = null;
	private IProcessInstance processInstance;
	private IFOE foe;
	private ITaskDefinition taskDef;
	private long state;
	private String conditionAction;
	/**
	 * 
	 */
	private MockTaskInstance() {
		taskInstanceId = new Long(taskInstanceCounter++);
	}
	
	public MockTaskInstance(ITaskDefinition taskDef,
			IProcessInstance processInstance, IFOE foe){		
		this();
		this.taskDef = taskDef;
		this.processInstance = processInstance;
		this.foe=foe;		
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.state.api.ITaskInstance#getProcessInstance()
	 */
	public IProcessInstance getProcessInstance() {
		return processInstance;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.state.api.ITaskInstance#getFOE()
	 */
	public IFOE getFOE() {
		return foe;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.state.api.ITaskInstance#getTaskDef()
	 */
	public ITaskDefinition getTaskDefinition() throws DefinitionNotFoundException {
		return taskDef;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.state.api.ITaskInstance#getTaskInstanceId()
	 */
	public Long getTaskInstanceId() {
		return taskInstanceId;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.state.api.ITaskInstance#getState()
	 */
	public long getState() {
		return state;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.state.api.ITaskInstance#setState(long)
	 */
	public void setState(long state) {
		this.state = state;

	}

	public String toString() {
		return "MOCK-TI-ID " + this.taskInstanceId + " ["  + this.taskDef + "]";
	}

	/**
	 * @author Matthew Norris
	 * Created on 19-Aug-2005
	 *
	 * @return
	 */
	public String getConditionAction() {
		return conditionAction;
	}

	public void setConditionAction(String conditionAction) {
		this.conditionAction = conditionAction;
	}
}
