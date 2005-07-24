package com.anite.zebra.hivemind.api;

import java.util.Map;

import com.anite.zebra.core.exceptions.DefinitionNotFoundException;
import com.anite.zebra.ext.definitions.impl.ProcessDefinition;
import com.anite.zebra.ext.definitions.impl.TaskDefinition;

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
public interface Zebra {

	/**
	 * returns all process definitions as a map
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
	public ProcessDefinition getProcessDefinition(String processName)
			throws DefinitionNotFoundException;

	public TaskDefinition getTaskDefinition(String taskName)
			throws DefinitionNotFoundException;
}
