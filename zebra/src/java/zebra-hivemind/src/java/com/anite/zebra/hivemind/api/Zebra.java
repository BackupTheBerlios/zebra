package com.anite.zebra.hivemind.api;

import java.util.List;
import java.util.Map;

import com.anite.zebra.core.exceptions.DefinitionNotFoundException;
import com.anite.zebra.core.exceptions.RunTaskException;
import com.anite.zebra.core.exceptions.StartProcessException;
import com.anite.zebra.core.exceptions.TaskConstructException;
import com.anite.zebra.core.exceptions.TransitionException;
import com.anite.zebra.core.factory.exceptions.ClassInstantiationException;
import com.anite.zebra.core.factory.exceptions.StateFailureException;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.core.state.api.ITaskInstance;

/**
 * The Core Zebra Facade Interface for Hivemind
 * 
 * All objects and common functions required should be available from here. All
 * implementation is handled via Hivemind - therefore each application can
 * implement it however it wants.
 * 
 * This replaces classes like ZebraHelper and the Avalon Helpers seen in the
 * other implementations of Zebra.
 * 
 * @author Ben Gidley
 */
public interface Zebra<PD, TD, PI, TI> {

	/**
	 * returns all process definitions as a map
	 * 
	 * @return
	 */
	public Map getAllProcessDefinitions();

	/**
	 * returns a process definition by its name
	 * 
	 * @param processName
	 *            process Name
	 * @throws DefinitionNotFoundException
	 *             definition not found exception
	 */
	public PD getProcessDefinitions() throws DefinitionNotFoundException;

	/**
	 * Create a process in a paused state
	 * @param processName
	 * @return
	 */
	public PI createProcessPaused(String processName);

	/**
	 * Get the passed users task list include all owned and delegated tasks
	 * @param user
	 * @return
	 */
	public List<TI> getTaskList(User user);

	/**
	 * Get the passed users tasks list but only directly owned tasks
	 * @param user
	 * @return
	 */
	public List<TI> getOnlyOwnedTaskList(User user);

	/**
	 * Get the passed users tasks list but only delegated tasks
	 * @param user
	 * @return
	 */
	public List<TI> getOnlyDelegatedTaskList(User user);

	/**
	 * Start the process
	 * @param processInstance
	 * @throws StartProcessException
	 */
	public void startProcess(PI processInstance) throws StartProcessException;

	/**
	 * Transition the passed task
	 * @param taskInstance
	 * @throws TransitionException
	 */
	public void transitionTask(TI taskInstance) throws TransitionException;
}
