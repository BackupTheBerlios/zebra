package com.anite.zebra.hivemind.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.exception.NestableException;
import org.apache.fulcrum.security.entity.User;
import org.hibernate.HibernateException;

import com.anite.zebra.core.exceptions.DefinitionNotFoundException;
import com.anite.zebra.core.exceptions.StartProcessException;
import com.anite.zebra.core.exceptions.TransitionException;

public class Zebra<PD, TD, PI, TI>{

	public Map getAllProcessDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	public PD getProcessDefinitions() throws DefinitionNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public PI createProcessPaused(String processName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TI> getTaskList(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TI> getOnlyOwnedTaskList(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TI> getOnlyDelegatedTaskList(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public void startProcess(PI processInstance)
			throws StartProcessException {
		// TODO Auto-generated method stub

	}

	public void transitionTask(TI taskInstance) throws TransitionException {
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
	 * TODO talk to Matt about moving this into the core API of the engine TODO
	 * write a unit test for this
	 * 
	 * @throws NestableException
	 * @throws HibernateException
	 * @throws PersistenceException
	 * @throws ComponentException
	 * 
	 */
	public void killProcess(PI processInstance) throws HibernateException,
			NestableException {
//		IStateFactory stateFactory = ZebraHelper.getInstance()
//				.getStateFactory();
//
//		List processesToKill = this.getRunningChildProcesses();
//		processesToKill.add(this);
//
//		ITransaction t = stateFactory.beginTransaction();
//
//		for (Iterator iter = processesToKill.iterator(); iter.hasNext();) {
//			ZebraProcessInstance process = (ZebraProcessInstance) iter.next();
//
//			Set tasks = process.getTaskInstances();
//			for (Iterator iterator = tasks.iterator(); iterator.hasNext();) {
//				AntelopeTaskInstance task = (AntelopeTaskInstance) iterator
//						.next();
//
//				task.setState(AntelopeTaskInstance.KILLED);
//				task.setTaskOwner(UserLocator.getLoggedInUser());
//				stateFactory.saveObject(task);
//
//				// This will create history automatically and will remove itself
//				// from the set
//				stateFactory.deleteObject(task);
//				process.setState(AntelopeTaskInstance.KILLED);
//				stateFactory.saveObject(process);
//			}
//
//		}
//		t.commit();
//
//		// Only destroy this process if there is a parent to force a subflow
//		// return
//		// - no need to do the child tree - as they are all killed
//		if (this.getParentProcessInstance() != null) {
//			ProcessDestruct destructor = new ProcessDestruct();
//			destructor.processDestruct(this);
//		}
	}
	

}
