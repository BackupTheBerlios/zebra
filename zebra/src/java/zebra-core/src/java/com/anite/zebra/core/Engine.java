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
package com.anite.zebra.core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.anite.zebra.core.api.IConditionAction;
import com.anite.zebra.core.api.IEngine;
import com.anite.zebra.core.api.IProcessConstruct;
import com.anite.zebra.core.api.IProcessDestruct;
import com.anite.zebra.core.api.ITaskAction;
import com.anite.zebra.core.api.ITaskConstruct;
import com.anite.zebra.core.definitions.api.IProcessDefinition;
import com.anite.zebra.core.definitions.api.IRoutingDefinition;
import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.core.exceptions.CreateProcessException;
import com.anite.zebra.core.exceptions.DefinitionNotFoundException;
import com.anite.zebra.core.exceptions.LockException;
import com.anite.zebra.core.exceptions.RunTaskException;
import com.anite.zebra.core.exceptions.StartProcessException;
import com.anite.zebra.core.exceptions.TransitionException;
import com.anite.zebra.core.factory.api.IClassFactory;
import com.anite.zebra.core.factory.api.IStateFactory;
import com.anite.zebra.core.factory.exceptions.CreateObjectException;
import com.anite.zebra.core.factory.exceptions.StateFailureException;
import com.anite.zebra.core.state.api.IFOE;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.core.state.api.ITaskInstance;
import com.anite.zebra.core.state.api.ITransaction;
import com.anite.zebra.core.util.DefaultClassFactory;
import com.anite.zebra.core.util.TaskSync;
/**
 * 
 * This is the main class that controls the execution of each process instance.
 * 
 * @author Matthew.Norris
 */
public class Engine implements IEngine {
	private static Log log = LogFactory.getLog(Engine.class);
	private IStateFactory stateFactory;
	private TaskSync taskSync = new TaskSync();
	private IClassFactory classFactory;
	/**
	 * In order for the engine to work it must be instanced with a valid states
	 * factory.
	 * As no ClassFactory is supplied the DefaultClassFactory will be used.
	 * 
	 * @param statesFactory
	 */
	public Engine(IStateFactory stateFactory) {
		this.stateFactory = stateFactory;
		// no class factory, so use default implementation
		this.classFactory = new DefaultClassFactory();
	}

	/**
	 * Alternate constructor that allows both a StateFactory and a ClassFactory 
	 * to be specified.
	 * 
	 * @param stateFactory
	 * @param classFactory
	 *
	 * @author Matthew.Norris
	 * Created on Aug 21, 2005
	 */
	public Engine(IStateFactory stateFactory, IClassFactory classFactory) {
		this.stateFactory = stateFactory;
		this.classFactory = classFactory;
	}
	
	/* (non-Javadoc)
	 * @see com.anite.zebra.core.api.IEngine#transitionTask(com.anite.zebra.core.state.api.ITaskInstance)
	 */
	public void transitionTask(ITaskInstance taskInstance)
			throws TransitionException {
		Stack taskStack = new Stack();
		taskStack.push(taskInstance);
		IProcessInstance currentProcess = taskInstance.getProcessInstance();
		while (!taskStack.empty()) {
			// get the task from the Stack
			ITaskInstance currentTask = (ITaskInstance) taskStack.pop();
			Map createdTasks;
			try {
				createdTasks = transitionTaskFromStack(currentTask);
			} catch (Exception e) {
				String emsg = "Problem encountered transitioning task from Stack";
				log.error(emsg, e);
				throw new TransitionException(e);
			}
			for (Iterator it = createdTasks.values().iterator(); it.hasNext();) {
				ITaskInstance newTask = (ITaskInstance) it.next();
				ITaskDefinition td;
				try {
					td = newTask.getTaskDefinition();
				} catch (DefinitionNotFoundException e) {
					String emsg = "Failed to access the Task Definition";
					log.error(emsg, e);
					throw new TransitionException(emsg, e);
				}
				if (td.isAuto()) {
					/*
					 * is an Auto task, so add to the stack for processing check
					 * to see if task is present in stack before adding it
					 */
					if (!taskStack.contains(newTask)) {
						if (log.isInfoEnabled()) {
							log.info("Added task to TaskStack - "
									+ newTask);
						}
						taskStack.push(newTask);
					} else {
						if (log.isInfoEnabled()) {
							log
									.info("transitionTask - task already exists in stack "
											+ newTask);
						}
					}
				}
			}
		}
		if (currentProcess.getTaskInstances().size() == 0) {
			// mark process complete
			doProcessDestruct(currentProcess);
		}
	}
	/**
	 * called when there are no more tasks on a process (ie it is completed)
	 * 
	 * @param ipi
	 * @throws TransitionException
	 */
	private void doProcessDestruct(IProcessInstance ipi)
			throws TransitionException {
		try {
			// call destructor class on process
			if (ipi.getProcessDef().getClassDestruct() != null) {
				ipi.setState(IProcessInstance.STATE_COMPLETING);
				ITransaction t = stateFactory.beginTransaction();
				stateFactory.saveObject(ipi);
				t.commit();
				IProcessDestruct ipd = classFactory.getProcessDestruct(ipi.getProcessDef().getClassDestruct());
				ipd.processDestruct(ipi);
			}
			// and mark as complete
			ipi.setState(IProcessInstance.STATE_COMPLETE);
			ITransaction t = stateFactory.beginTransaction();
			stateFactory.saveObject(ipi);
			t.commit();
		} catch (Exception e) {
			String emsg = "Failed to complete process "
					+ ipi;
			log.error(emsg, e);
			throw new TransitionException(emsg, e);
		}
	}
	/**
	 * @param currentTask
	 * @return a map containing new TaskInstances created as part of this
	 *         transition
	 * @throws TransitionException
	 */
	private Map transitionTaskFromStack(ITaskInstance currentTask)
			throws TransitionException, DefinitionNotFoundException {
		if (log.isInfoEnabled()) {
			log.info("transitionTask is initialising for TaskInstance "
					+ currentTask);
		}
		if (currentTask.getState() == ITaskInstance.STATE_INITIALISING) {
			// run the constructor
			try {
				doTaskConstruct(currentTask);
			} catch (Exception e) {
				String emsg = "Error during construction of Task "
						+ currentTask;
				log.error(emsg, e);
				throw new TransitionException(emsg, e);
			}
		} else {
			runTask(currentTask);
		}
		// check the state of the task now it has been run
		if (currentTask.getState() != ITaskInstance.STATE_AWAITINGCOMPLETE) {
			// task has some state other than "completed" so exit
			if (log.isInfoEnabled()) {
				log
						.info("transitionTask - task will not be transitioned as it has a state of "
								+ currentTask.getState()
								+ " - GUID "
								+ currentTask.getTaskInstanceId());
			}
			return new HashMap();
		}
		// do transition - get outbound routing, check for locks, etc
		// make a note of any TaskDef that is SyncLocked by this TaskDef -
		// SyncLocked tasks can be run once this task has completed
		ITaskDefinition taskDef = currentTask.getTaskDefinition();
		List createList = runRouting(taskDef, currentTask);
		if (createList.size() == 0 && taskDef.getRoutingOut().size() > 0) {
			// routing exists, but none ran.
			try {
				ITransaction t = stateFactory.beginTransaction();
				currentTask.setState(ITaskInstance.STATE_ERRORROUTING);
				stateFactory.saveObject(currentTask);
				t.commit();
			} catch (Exception e) {
				log.error(e);
				throw new TransitionException(e);
			}
			throw new TransitionException("Routing exists for TaskInstance "
					+ currentTask + " but none ran!");
		}
		/*
		 * we now have a list of tasks to create. Before we start creating them,
		 * we need to LOCK the ProcessInstance from changes by other Engine
		 * instances. As this can be a little costly (especially when plugged
		 * into a DB states provider) there should probably be a config option
		 * to specify whether multiple engines are in use to either
		 * enable/disable this behaviour.
		 */
		Map createdTasks = new HashMap();
		IProcessInstance processInstance = currentTask.getProcessInstance();
		try {
            stateFactory.acquireLock(processInstance,this);
        } catch (LockException e) {
            String emsg = "Failed to aquire an exclusive lock on the Process Instance";
            log.error(emsg,e);
            throw new TransitionException(emsg,e);
        }
		ITransaction t;
		try {
			t = stateFactory.beginTransaction();
		} catch (Exception e) {
			String emsg = "Failure to create states transaction before creating new tasks";
			log.error(emsg, e);
			throw new TransitionException(emsg, e);
		}
		IFOE foeSerial = null;
		for (Iterator it = createList.iterator(); it.hasNext();) {
			ITaskInstance newTaskInstance;
			IRoutingDefinition routingDef = (IRoutingDefinition) it.next();
			ITaskDefinition newTaskDef = routingDef.getDestinationTaskDefinition();
			try {
				IFOE foe;
				if (routingDef.getParallel()) {
					/*
					 * a parallel routing always creates a new FOE
					 */
					foe = createFOE(processInstance);
				} else {
					/*
					 * Serial routing always re-uses an existing FOE
					 */
					if (foeSerial == null) {
						if (taskDef.isSynchronised()) {
							/*
							 * As we sync'd, there may be multiple FOEs that are
							 * combining, so therefore we need to create a new
							 * FOE for execution to continue down.
							 */
							foeSerial = createFOE(processInstance);
						} else {
							/*
							 * use the FOE of the current task...
							 */
							foeSerial = currentTask.getFOE();
						}
					}
					foe = foeSerial;
				}
				newTaskInstance = createTask(newTaskDef, processInstance, foe);
			} catch (Exception e) {
				// pokemon stylee... catch 'em all
				String emsg = "Failed to create new task Instance for task definition "
						+ newTaskDef;
				log.error(emsg, e);
				throw new TransitionException(emsg, e);
			}
			/*
			 * createTask may return an existing task instance in the case of a
			 * sync task that is being hit by multiple routings, so we check
			 * before we add it to the list of unique tasks created
			 */
			if (!createdTasks.containsKey(newTaskInstance.getTaskInstanceId())) {
				createdTasks.put(newTaskInstance.getTaskInstanceId(),
						newTaskInstance);
			}
		}
		/*
		 * we need to see if there is an existing SyncTaskInstance that was
		 * waiting for this task to complete This check is only necessary when
		 * the route taken by the task removes its lock on the synctask, but
		 * does not route toward the synctask (escape route)
		 */
		// get the list of sync tasks that this task can block
		Map syncList = taskSync.getPotentialTaskLocks(taskDef);
		// iterate over the processInstance's list of tasks, looking for a Sync
		// Task that is blocked by the current task
		for (Iterator it = currentTask.getProcessInstance().getTaskInstances()
				.iterator(); it.hasNext();) {
			ITaskInstance checkTask = (ITaskInstance) it.next();
			if (syncList.containsKey(checkTask.getTaskDefinition().getId())) {
				// sync task found, so add to the create list
				if (!createdTasks.containsKey(checkTask.getTaskInstanceId())) {
					if (log.isInfoEnabled()) {
						log
								.info("adding SyncTask "
										+ checkTask
										+ " that may now be runnable due to completion of taskInstance "
										+ currentTask);
					}
					createdTasks.put(checkTask.getTaskInstanceId(), checkTask);
				}
			}
		}
		// remove completed task
		try {
			currentTask.setState(ITaskInstance.STATE_COMPLETE);
			stateFactory.saveObject(currentTask);
			stateFactory.deleteObject(currentTask);
			t.commit();
		} catch (Exception e) {
			String emsg = "Failed to commit new tasks and finalise task completion";
			log.error(emsg, e);
			throw new TransitionException(emsg, e);
		}
		try {
            // unlock processInstance
            stateFactory.releaseLock(processInstance, this);
        } catch (LockException e) {
            String emsg = "Failed to release the exclusive lock on the Process Instance";
			log.error(emsg, e);
			throw new TransitionException(emsg, e);
        }
		return createdTasks;
	}

	/**
	 * 
	 * Calls down to the States engine to request a new FOE object
	 * 
	 * @param processInstance
	 * @return new FOE object
	 */
	private IFOE createFOE(IProcessInstance processInstance)
			throws CreateObjectException {
		return stateFactory.createFOE(processInstance);
	}
	/**
	 * Runs all Routings on the specified taskInstance
	 * 
	 * @param taskDef
	 * @return a hashtable containing all the routing definitions that spawned a
	 *         task
	 * @throws TransitionException
	 */
	private List runRouting(ITaskDefinition taskDef, ITaskInstance taskInstance)
			throws TransitionException {
		Set routingDefs = taskDef.getRoutingOut();
		boolean doneSerialRouting = false;
		List createList = new ArrayList();
		for (Iterator it = routingDefs.iterator(); it.hasNext();) {
			IRoutingDefinition rd = (IRoutingDefinition) it.next();
			boolean doRouting = false;
			boolean addTask = false;
			/*
			 * Need to process conditional routing seperately to parallel
			 * routing as their behaviours are different.
			 * 
			 * If we've already had a serial routing whose condition passed, we
			 * dont want to run another.
			 * 
			 * All parallel routings are run, even if we've had one that has
			 * already passed it's condition.
			 *  
			 */
			if (!rd.getParallel() && !doneSerialRouting) {
				doRouting = true;
			} else if (rd.getParallel()) {
				// always do all parallel routings
				doRouting = true;
			}
			if (doRouting) {
				// do this routing
				String className = rd.getConditionClass();
				if (className != null) {
					IConditionAction ica;
					try {
						// instance the condition class
						ica = classFactory.getConditionAction(className);
						addTask = ica.runCondition(rd, taskInstance);
					} catch (Exception e) {
						throw new TransitionException(
								"Failed to run RoutingDef " + rd.getId(), e);
					}
				} else {
					// class not specified, so we assume "true"
					addTask = true;
				}
			}
			if (addTask) {
				createList.add(rd);
				if (!rd.getParallel()) {
					/*
					 * routing is serial, so make a note of this so we dont run
					 * any more serial routings
					 */
					doneSerialRouting = true;
				}
			}
		}
		return createList;
	}
	/**
	 * runs the specified taskInstance
	 * 
	 * @param taskInstance
	 * @throws TransitionException
	 */
	private void runTask(ITaskInstance taskInstance)
			throws TransitionException, DefinitionNotFoundException {
		if (log.isInfoEnabled()) {
			log
					.info("Running TaskInstance "
							+ taskInstance);
		}
		if (taskInstance.getState() == ITaskInstance.STATE_AWAITINGCOMPLETE) {
			if (log.isInfoEnabled()) {
				log.info("Task is marked as \""
						+ ITaskInstance.STATE_AWAITINGCOMPLETE
						+ "\" so no further transitions needed");
			}
			return;
		}
		ITaskDefinition taskDef = taskInstance.getTaskDefinition();
		if (taskDef.isSynchronised()) {
			// check for blocks
			if (taskSync.isTaskBlocked(taskInstance)) {
				if (log.isInfoEnabled()) {
					log.info("Task " + taskInstance
							+ " is blocked and will not be run");
				}
				return;
			}
			// task not locked, so set the state to "ready"
			if (log.isInfoEnabled()) {
				log.info("Task " + taskInstance
						+ " is NOT blocked and will be run");
			}
			taskInstance.setState(ITaskInstance.STATE_READY);
		}
		if (taskInstance.getState() == ITaskInstance.STATE_AWAITINGINITIALISATION) {
			try {
				doTaskConstruct(taskInstance);
			} catch (Exception e) {
				String emsg = "Failure to initialise Task "
						+ taskInstance;
				log.error(emsg, e);
				throw new TransitionException(emsg, e);
			}
		}
		if (taskInstance.getState() != ITaskInstance.STATE_READY) {
			throw new TransitionException("Task State is not set to '"
					+ ITaskInstance.STATE_READY
					+ "' and cannot be transitioned");
		}
		String runClass = taskDef.getClassName();
		// check to see if task has a class associated with it
		if (runClass != null) {
			if (log.isInfoEnabled()) {
				log.info("Attempting to instance TaskAction class " + runClass);
			}
			// create instance of the class specified in ClassName
			ITaskAction taskClass;
			try {
				ITransaction t = stateFactory.beginTransaction();
				taskInstance.setState(ITaskInstance.STATE_RUNNING);
				stateFactory.saveObject(taskInstance);
				t.commit();
				taskClass = classFactory.getTaskAction(runClass);
			} catch (Exception e) {
				// fail this transistion if we cant instantiate the task action
				log.error("Failed to instantiate Task Action " + runClass, e);
				throw new TransitionException(e);
			}
			try {
				// fail this transistion if the task action generates an
				// exception
				taskClass.runTask(taskInstance);
			} catch (RunTaskException e) {
				log.error("Problem running task " + runClass, e);
				throw new TransitionException("Problem running Task Action "
						+ runClass, e);
			}
		} else {
			// arbitrarily set the status of the task to "complete"
			try {
				ITransaction t = stateFactory.beginTransaction();
				taskInstance.setState(ITaskInstance.STATE_AWAITINGCOMPLETE);
				stateFactory.saveObject(taskInstance);
				t.commit();
			} catch (StateFailureException e) {
				String emsg = "Failed to set task completion";
				log.error(emsg, e);
				throw new TransitionException(emsg, e);
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.anite.zebra.core.api.IEngine#createProcess(com.anite.zebra.core.definitions.api.IProcessDefinition)
	 */
	public IProcessInstance createProcess(IProcessDefinition processDef)
			throws CreateProcessException {
		IProcessInstance process;
		ITransaction t = null;
		try {
			t = stateFactory.beginTransaction();
			process = stateFactory.createProcessInstance(processDef);
			process.setState(IProcessInstance.STATE_CREATED);
			stateFactory.saveObject(process);
			t.commit();
		} catch (Exception e) {
			String emsg = "createProcessPaused failed to create the ProcessDef "
					+ processDef;
			log.error(emsg, e);
			throw new CreateProcessException(emsg, e);
		}
		return process;
	}
	/**
	 * runs the constructor on a Task Instance
	 * @param iti Task Instance to run constructor class for 
	 * @throws Exception
	 */
	private void doTaskConstruct(ITaskInstance iti) throws Exception {
	    ITaskDefinition taskDef = iti.getTaskDefinition();
		if (taskDef.getClassConstruct() == null) {
			return;
		}
		if (iti.getState() != ITaskInstance.STATE_AWAITINGINITIALISATION) {
			return;
		}
		try {
			ITransaction t = stateFactory.beginTransaction();
			iti.setState(ITaskInstance.STATE_INITIALISING);
			stateFactory.saveObject(iti);
			t.commit();
			ITaskConstruct itc = classFactory.getTaskConstruct(iti.getTaskDefinition().getClassConstruct());
			itc.taskConstruct(iti);
			t = stateFactory.beginTransaction();
			iti.setState(ITaskInstance.STATE_READY);
			stateFactory.saveObject(iti);
			t.commit();
		} catch (Exception e) {
			String emsg = "Failed to run class constructor for task "
					+ iti;
			log.error(emsg, e);
			throw new Exception(emsg, e);
		}
	}
	/**
	 * run constructor for specified process instance
	 * 
	 * @param ipi
	 * @throws Exception
	 */
	private void doProcessConstruct(IProcessInstance ipi) throws Exception {
		try {
			IProcessConstruct ipc = classFactory.getProcessConstruct(
					ipi.getProcessDef().getClassConstruct());
			ipc.processConstruct(ipi);
		} catch (Exception e) {
			String emsg = "Failed to run class constructor \""
					+ ipi.getProcessDef().getClassConstruct()
					+ "\" for process " + ipi;
			log.error(emsg, e);
			throw new Exception(emsg, e);
		}
	}
	/**
	 * creates a new TaskInstance on the specified ProcessInstance and FOE
	 * 
	 * @param td Task Definition for the Task Instance
	 * @param pi Process Instance the task is attached to
	 * @param foe Flow of Execution to initiate the task on
	 * @return the newly TaskInstance to be passed to the transitionTask() method 
	 * @throws CreateObjectException
	 * @throws StateFailureException
	 */
	private ITaskInstance createTask(ITaskDefinition td, IProcessInstance pi, IFOE foe)
			throws CreateObjectException, StateFailureException,
			DefinitionNotFoundException {
		
		if (foe == null || pi == null || td == null) {
			throw new CreateObjectException(
					"Missing required parameters to create a Task Instance - TD: " + td + ", PI: " + pi + ", FOE:" + foe);
		}
		/*
		 * TODO should probably add another check here to ensure that task def
		 * comes from process instance's process def (i.e. protect against potential
		 * workflow definition corruption)
		 */
		if (td.isSynchronised()) {
			// task is a syncTask - we need to ensure that there is only
			// TaskInstance for this definition
			for (Iterator it = pi.getTaskInstances().iterator(); it.hasNext();) {
				ITaskInstance checkTask = (ITaskInstance) it.next();
				if (checkTask.getTaskDefinition().getId().longValue() == td
						.getId().longValue()) {
					// found this task already, so just return it
					if (log.isInfoEnabled()) {
						log
								.info("Instance of Sync Task already exists; returning this instance");
					}
					return checkTask;
				}
			}
		}
		/*
		 * instance of sync task not found or was not a sync task, so we create
		 * the new one
		 */
		if (log.isInfoEnabled()) {
			log.info("Creating new instance of TaskDef " + td);
		}
		ITaskInstance task = stateFactory.createTaskInstance(td, pi, foe);
		// check to see if task has an initialiser - if not, ensure status is
		// set optimally
		if (td.isSynchronised()) {
			task.setState(ITaskInstance.STATE_AWAITINGSYNC);
		} else if (td.getClassConstruct() != null) {
			task.setState(ITaskInstance.STATE_AWAITINGINITIALISATION);
		} else {
			task.setState(ITaskInstance.STATE_READY);
		}
		stateFactory.saveObject(task);
		return task;
	}
	
	/* (non-Javadoc)
	 * @see com.anite.zebra.core.api.IEngine#startProcess(com.anite.zebra.core.state.api.IProcessInstance)
	 */
	public void startProcess(IProcessInstance processInstance)
			throws StartProcessException {
		if (processInstance.getState() != IProcessInstance.STATE_CREATED) {
			throw new StartProcessException("Process "
					+ processInstance + " State is not "
					+ IProcessInstance.STATE_CREATED
					+ " and therefore the process cannot be started");
		}
		IProcessDefinition processDef;
		try {
			processDef = processInstance.getProcessDef();
		} catch (Exception e) {
			String emsg = "Problem accessing process definition";
			log.error(emsg, e);
			throw new StartProcessException(e);
		}
		ITransaction t = null;
		try {
			t = stateFactory.beginTransaction();
			if (processDef.getClassConstruct() != null) {
				processInstance.setState(IProcessInstance.STATE_INITIALISING);
				stateFactory.saveObject(processInstance);
				t.commit();
				doProcessConstruct(processInstance);
				t = stateFactory.beginTransaction();
			}
			processInstance.setState(IProcessInstance.STATE_RUNNING);
			stateFactory.saveObject(processInstance);
			// create first task
			ITaskDefinition td = processDef.getFirstTask();
			IFOE foe = createFOE(processInstance);
			ITaskInstance task = createTask(td, processInstance, foe);
			t.commit();
			if (td.isAuto()) {
				if (log.isInfoEnabled()) {
					log.info("createProcess transitioning first task "
							+ task);
				}
				transitionTask(task);
			}
		} catch (Exception e) {
			String emsg = "startProcess failed to start the process "
					+ processInstance;
			log.error(emsg, e);
			throw new StartProcessException(emsg, e);
		}
	}

}