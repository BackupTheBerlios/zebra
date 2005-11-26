/*
 * Copyright 2004, 2005 Anite - Central Government Division
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
package com.anite.zebra.hivemind.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.exception.NestableException;
import org.apache.fulcrum.security.entity.User;
import org.hibernate.HibernateException;

import com.anite.zebra.core.exceptions.StartProcessException;
import com.anite.zebra.core.exceptions.TransitionException;
import com.anite.zebra.hivemind.om.defs.ZebraProcessDefinition;
import com.anite.zebra.hivemind.om.state.ZebraProcessInstance;
import com.anite.zebra.hivemind.om.state.ZebraTaskInstance;

public class Zebra {
	
	private ZebraDefinitionFactory zebraDefinitionFactory;

	public Map<String, ZebraProcessDefinition> getAllProcessDefinitions() {		
		return this.zebraDefinitionFactory.getAllProcessDefinitionsByName();
	}

	/**
	 * Start a process in a paused state by name
	 * @param processName
	 * @return
	 */
	public ZebraProcessInstance createProcessPaused(String processName) {
		return createProcessPaused(this.getAllProcessDefinitions().get(processName));
		
	}

	public ZebraProcessInstance createProcessPaused(ZebraProcessDefinition process) {
		// TODO
		return null;
	}

	public List<ZebraTaskInstance> getTaskList(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ZebraTaskInstance> getOnlyOwnedTaskList(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ZebraTaskInstance> getOnlyDelegatedTaskList(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public void startProcess(ZebraProcessInstance processInstance) throws StartProcessException {
		// TODO Auto-generated method stub

	}

	public void transitionTask(ZebraTaskInstance taskInstance) throws TransitionException {
		// TODO Auto-generated method stub

	}

	/**
	 * Kill this process and all tasks within it. This does NOT kill the parent
	 * process but will kill child processes
	 * 
	 * The application is expected to handle security over who can kill a
	 * process it is NOT enforced here
	 * 
	 * If this is a child process the subflow step will be marked complete
	 * 
	 * TODO talk to Matt about moving this into the core AZebraProcessInstance of the engine TODO
	 * write a unit test for this
	 * 
	 * @throws NestableException
	 * @throws HibernateException
	 * @throws PersistenceException
	 * @throws ComponentException
	 * 
	 */
	public void killProcess(ZebraProcessInstance processInstance) {
		// IStateFactory stateFactory = ZebraHelper.getInstance()
		// .getStateFactory();
		//
		// List processesToKill = this.getRunningChildProcesses();
		// processesToKill.add(this);
		//
		// ITransaction t = stateFactory.beginTransaction();
		//
		// for (Iterator iter = processesToKill.iterator(); iter.hasNext();) {
		// ZebraProcessInstance process = (ZebraProcessInstance) iter.next();
		//
		// Set tasks = process.getTaskInstances();
		// for (Iterator iterator = tasks.iterator(); iterator.hasNext();) {
		// AntelopeTaskInstance task = (AntelopeTaskInstance) iterator
		// .next();
		//
		// task.setState(AntelopeTaskInstance.KILLED);
		// task.setTaskOwner(UserLocator.getLoggedInUser());
		// stateFactory.saveObject(task);
		//
		// // This will create history automatically and will remove itself
		// // from the set
		// stateFactory.deleteObject(task);
		// process.setState(AntelopeTaskInstance.KILLED);
		// stateFactory.saveObject(process);
		// }
		//
		// }
		// t.commit();
		//
		// // Only destroy this process if there is a parent to force a subflow
		// // return
		// // - no need to do the child tree - as they are all killed
		// if (this.getParentProcessInstance() != null) {
		// ProcessDestruct destructor = new ProcessDestruct();
		// destructor.processDestruct(this);
		// }
	}

	/**
	 * Gets the Zebra Definition Factory.
	 * 
	 * Most of the key mehthods used in a normal application are availabe on this facade.
	 * @return
	 */
	public ZebraDefinitionFactory getZebraDefinitionFactory() {
		return this.zebraDefinitionFactory;
	}

	public void setZebraDefinitionFactory(
			ZebraDefinitionFactory zebraDefinitionFactory) {
		this.zebraDefinitionFactory = zebraDefinitionFactory;
	}

}
