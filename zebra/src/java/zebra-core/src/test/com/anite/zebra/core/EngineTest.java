package com.anite.zebra.core;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.anite.zebra.core.api.IEngine;
import com.anite.zebra.core.factory.api.IStateFactory;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.core.state.api.ITaskInstance;
import com.anite.zebra.test.mocks.MockProcessDef;
import com.anite.zebra.test.mocks.MockStateFactory;
import com.anite.zebra.test.mocks.taskdefs.AutoRunTaskDef;
import com.anite.zebra.test.mocks.taskdefs.ManualRunTaskDef;

/**
 * @author Matthew.Norris
 */
public class EngineTest extends TestCase {

	public void testWorkflowWithAutoSteps() throws Exception {

		MockProcessDef processDef = new MockProcessDef("");

		AutoRunTaskDef taskDef = new AutoRunTaskDef(processDef, "");

		processDef.setFirstTask(taskDef);

		IStateFactory msf = new MockStateFactory();
		IEngine engine = new Engine(msf);
		IProcessInstance processInstance = engine.createProcess(processDef);
		assertEquals(processDef, processInstance.getProcessDef());
		assertTrue(processInstance.getProcessInstanceId().longValue() > 0);
		assertEquals(IProcessInstance.STATE_CREATED, processInstance.getState());
		Set taskInstances = processInstance.getTaskInstances();
		assertEquals(0, taskInstances.size());
		engine.startProcess(processInstance);
		assertEquals(0, processInstance.getTaskInstances().size());
		assertEquals(IProcessInstance.STATE_COMPLETE, processInstance
				.getState());

	}

	public void testWorkflowWithManualSteps() throws Exception {

		MockProcessDef processDef = new MockProcessDef("");
		
		ManualRunTaskDef taskDef = new ManualRunTaskDef(processDef,"");

		processDef.setFirstTask(taskDef);

		IStateFactory msf = new MockStateFactory();
		IEngine engine = new Engine(msf);
		IProcessInstance processInstance = engine.createProcess(processDef);
		assertEquals(processDef, processInstance.getProcessDef());
		assertTrue(processInstance.getProcessInstanceId().longValue() > 0);
		assertEquals(IProcessInstance.STATE_CREATED, processInstance.getState());
		Set taskInstances = processInstance.getTaskInstances();
		assertEquals(0, taskInstances.size());
		engine.startProcess(processInstance);
		assertEquals(1, processInstance.getTaskInstances().size());
		assertEquals(IProcessInstance.STATE_RUNNING, processInstance.getState());
		while (processInstance.getTaskInstances().size() > 0) {
			List tasks = new ArrayList();
			for (Iterator i = processInstance.getTaskInstances().iterator(); i
					.hasNext();) {
				ITaskInstance taskInstance = (ITaskInstance) i.next();
				if (taskInstance.getState() == ITaskInstance.STATE_READY) {
					tasks.add(taskInstance);
				}
			}
			if (tasks.size() == 0) {
				break;
			}
			for (Iterator i = tasks.iterator(); i.hasNext();) {
				ITaskInstance taskInstance = (ITaskInstance) i.next();
				engine.transitionTask(taskInstance);
			}
		}
		assertEquals(IProcessInstance.STATE_COMPLETE, processInstance.getState());
		assertEquals(0, processInstance.getTaskInstances().size());

	}
}