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

package com.anite.zebra.core.api;

import com.anite.zebra.core.definitions.api.IProcessDefinition;
import com.anite.zebra.core.exceptions.CreateProcessException;
import com.anite.zebra.core.exceptions.StartProcessException;
import com.anite.zebra.core.exceptions.TransitionException;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.core.state.api.ITaskInstance;

/**
 * @author Matthew.Norris
 */
public interface IEngine {
	/**
	 * transitions the specified task
	 * @param taskInstance
	 * @throws TransitionException
	 */
	public void transitionTask(ITaskInstance taskInstance) throws TransitionException;
	/**
	 * creates the a ProcessInstance of the specified process definition.  This ProcessInstance
	 * does actually do anything.  To start the process, call startProcess.  
	 * <p/><b>This functions the 
	 * same as createProcessPaused in CTMS did.</b>
	 * @param processDef
	 * @return
	 * @throws CreateProcessException
	 */
	public IProcessInstance createProcess(IProcessDefinition processDef) throws CreateProcessException;
	
	/**
	 * starts a paused process Instance
	 * @param processInstance
	 */
	public void startProcess(IProcessInstance processInstance) throws StartProcessException;
		
}
