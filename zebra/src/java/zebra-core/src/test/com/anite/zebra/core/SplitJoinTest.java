package com.anite.zebra.core;

import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.anite.zebra.core.api.IEngine;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.core.state.api.IStateObject;
import com.anite.zebra.core.state.api.ITaskInstance;
import com.anite.zebra.test.mocks.MockProcessDef;
import com.anite.zebra.test.mocks.MockProcessInstance;
import com.anite.zebra.test.mocks.MockStateFactory;
import com.anite.zebra.test.mocks.MockTaskInstance;
import com.anite.zebra.test.mocks.taskdefs.AutoRunTaskDef;
import com.anite.zebra.test.mocks.taskdefs.JoinTaskDef;
import com.anite.zebra.test.mocks.taskdefs.ManualRunTaskDef;
import com.anite.zebra.test.mocks.taskdefs.SplitTaskDef;

public class SplitJoinTest extends TestCase {
	private static Log log = LogFactory.getLog(SplitJoinTest.class);
	
    
	public void testSimpleSplitJoin() throws Exception {
		MockProcessDef pd = new MockProcessDef("testSimpleSplitJoin"); 
		ManualRunTaskDef tdStart = new ManualRunTaskDef(pd,"Start");
		pd.setFirstTask(tdStart);
		SplitTaskDef tdSplit = new SplitTaskDef(pd,"Split");
		tdStart.addRoutingOut(tdSplit);
		JoinTaskDef tdJoin = new JoinTaskDef(pd,"Join");
		
		AutoRunTaskDef tdParallel1 = new AutoRunTaskDef (pd,"Parallel-1");
		tdSplit.addRoutingOut(tdParallel1);
		tdParallel1.addRoutingOut(tdJoin);
		AutoRunTaskDef tdParallel2 = new AutoRunTaskDef (pd,"Parallel-2");
		tdSplit.addRoutingOut(tdParallel2);		
		tdParallel2.addRoutingOut(tdJoin);
		
		MockStateFactory msf = new MockStateFactory();
		IEngine engine = new Engine(msf);
		IProcessInstance processInstance = engine.createProcess(pd);

		// tests to see if process is created properly 
		assertEquals(pd, processInstance.getProcessDef());
		assertTrue(processInstance.getProcessInstanceId().longValue() > 0);
		assertEquals(IProcessInstance.STATE_CREATED, processInstance.getState());

		// test to see if we have the expected number of tasks - NONE
		Set taskInstances = processInstance.getTaskInstances();
		assertEquals(0, taskInstances.size());
		log.info("Starting Process");
		engine.startProcess(processInstance);
		taskInstances = processInstance.getTaskInstances();
		// test to see if we have the expected number of tasks - ONE
		assertEquals(1, taskInstances.size());
		
		ITaskInstance ti = (ITaskInstance) taskInstances.iterator().next();
		assertEquals(tdStart, ti.getTaskDefinition());
		log.info("Transitioning Task " + ti.getTaskInstanceId());
		engine.transitionTask(ti);
		// test to see if we have the expected number of tasks - NONE
		taskInstances = processInstance.getTaskInstances();
		assertEquals(0, taskInstances.size());
		/* test to see if the audit log is the expected size -
		 * 1 process
		 * 1 start task
		 * 1 split task
		 * 2 parallel tasks
		 * 1 join task
		 *  = 6 objects
		 */ 
		
		assertEquals(6, msf.getAuditTrail().size());
		// test to see all are marked as "completed" / "deleted" as appropriate
		for (Iterator it = msf.getAuditTrail().iterator();it.hasNext();) {
			IStateObject so = (IStateObject) it.next();
			if (so instanceof MockProcessInstance) {
				MockProcessInstance check = (MockProcessInstance) so;
				assertTrue("State not COMPLETED",check.getState() == MockProcessInstance.STATE_COMPLETE);
			} else if (so instanceof MockTaskInstance) {
				MockTaskInstance check = (MockTaskInstance) so;
				assertTrue("State not DELETED",check.getState() == MockTaskInstance.STATE_DELETED);
			} else {
				fail("Unexpected object " + so);
			}

		}
		
	}
}
