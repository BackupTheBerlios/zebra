/**
 * @author matt
 * Created on 19-Aug-2005
 */
package com.anite.zebra.core;

import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.anite.zebra.core.api.IEngine;
import com.anite.zebra.core.exceptions.DefinitionNotFoundException;
import com.anite.zebra.core.exceptions.StartProcessException;
import com.anite.zebra.core.exceptions.TransitionException;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.test.mocks.ComplexProcessDef;
import com.anite.zebra.test.mocks.MockProcessInstance;
import com.anite.zebra.test.mocks.MockStateFactory;
import com.anite.zebra.test.mocks.MockTaskInstance;



/**
 *
 * Tests whether a complex split / join operation works correctly
 * 
 * @author matt
 * Created on 19-Aug-2005
 *
 */
public class ComplexSplitJoinTest extends TestCase {

	private static Log log = LogFactory.getLog(ComplexSplitJoinTest.class);
	
	public void testAlternateEnding() throws Exception {
		
		ComplexProcessDef pd = new ComplexProcessDef("complexTest1");
		
		
		MockStateFactory msf = new MockStateFactory();
		IEngine engine = new Engine(msf);
		MockProcessInstance processInstance = (MockProcessInstance) engine.createProcess(pd);

		// first transition test - runs the workflow up to the point where it hits the JOIN
		doPart1(pd, engine, processInstance);
		
		/*
		 * now transition the workflow toward the alternate ending
		 */
		MockTaskInstance ti = processInstance.findTask(pd.tdParallel_2,MockTaskInstance.STATE_READY);
		assertNotNull("Failed to find the task to run",ti);
		
		ti.setConditionAction(ComplexProcessDef.GOTO + pd.tdAlternateEnding.getName());
		engine.transitionTask(ti);
		
		/*
		 * check to see if we have the expected number of object in the audit trail
		 * 1 x process
		 * 1 x tdStart (complete)
		 * 1 x tdSplit (complete)
		 * 1 x tdJoin (complete)
		 * 1 x tdParallel-1 (complete)
		 * 1 x tdParallel-2 (complete)
		 * 1 x tdAlternateEnding (ready)
		 * 1 x tdEnd (ready)
		 */
		assertEquals(8,msf.getAuditTrail().size());
		assertEquals(1,msf.countInstances(pd));
		assertEquals(1,msf.countInstances(pd.tdStart, MockTaskInstance.STATE_DELETED));
		assertEquals(1,msf.countInstances(pd.tdSplit, MockTaskInstance.STATE_DELETED));
		assertEquals(1,msf.countInstances(pd.tdJoin, MockTaskInstance.STATE_DELETED));
		assertEquals(1,msf.countInstances(pd.tdParallel_1, MockTaskInstance.STATE_DELETED));
		assertEquals(1,msf.countInstances(pd.tdParallel_2, MockTaskInstance.STATE_DELETED));
		assertEquals(1,msf.countInstances(pd.tdEnd, MockTaskInstance.STATE_READY));
		assertEquals(1,msf.countInstances(pd.tdAlternateEnding, MockTaskInstance.STATE_READY));
	}
	
	public void testLoopingJoin() throws Exception {
		
		
		ComplexProcessDef pd = new ComplexProcessDef("complexTest1");
		
		
		MockStateFactory msf = new MockStateFactory();
		IEngine engine = new Engine(msf);
		MockProcessInstance processInstance = (MockProcessInstance) engine.createProcess(pd);

		// first transition test - runs the workflow up to the point where it hits the JOIN
		doPart1(pd, engine, processInstance);		
		
		MockTaskInstance ti = processInstance.findTask(pd.tdParallel_2,MockTaskInstance.STATE_READY);
		assertNotNull("Failed to find the task to run",ti);
		
		ti.setConditionAction(ComplexProcessDef.GOTO + pd.tdSplit.getName());
		engine.transitionTask(ti);
		
		/*
		 * check to see if we have the expected number of object in the audit trail
		 * 1 x process
		 * 1 x tdStart (complete)
		 * 2 x tdSplit (complete)
		 * 1 x tdJoin (awaiting sync)
		 * 2 x tdParallel-1 (complete)
		 * 2 x tdParallel-2 (1 x complete, 1 x ready)
		 */
		assertEquals(9,msf.getAuditTrail().size());
		assertEquals(1,msf.countInstances(pd));
		assertEquals(1,msf.countInstances(pd.tdStart, MockTaskInstance.STATE_DELETED));
		assertEquals(2,msf.countInstances(pd.tdSplit, MockTaskInstance.STATE_DELETED));
		assertEquals(1,msf.countInstances(pd.tdJoin, MockTaskInstance.STATE_AWAITINGSYNC));
		assertEquals(2,msf.countInstances(pd.tdParallel_1, MockTaskInstance.STATE_DELETED));
		assertEquals(1,msf.countInstances(pd.tdParallel_2, MockTaskInstance.STATE_DELETED));
		assertEquals(1,msf.countInstances(pd.tdParallel_2, MockTaskInstance.STATE_READY));
		
		/*
		 * check that we've got what we expect in the process instance
		 * 1 x tdJoin
		 * 1 x tdParallel2
		 */
		assertEquals(2,processInstance.getTaskInstances().size());
		assertEquals(1,processInstance.countInstances(pd.tdParallel_2,MockTaskInstance.STATE_READY));
		assertEquals(1,processInstance.countInstances(pd.tdJoin,MockTaskInstance.STATE_AWAITINGSYNC));
		
		/*
		 * now transition again, but go to the JOIN
		 */
		ti = processInstance.findTask(pd.tdParallel_2,MockTaskInstance.STATE_READY);
		assertNotNull("Failed to find the task to run",ti);
		ti.setConditionAction(ComplexProcessDef.GOTO + pd.tdJoin.getName());
		engine.transitionTask(ti);
		
		/*
		 * check that we've got what we expect in the processinstance
		 * 1 x tdEnd
		 */
		assertEquals(1,processInstance.getTaskInstances().size());
		assertEquals(1,processInstance.countInstances(pd.tdEnd,MockTaskInstance.STATE_READY));
		
		/*
		 * check that we've still only got one Join task in the audit trail
		 */
		assertEquals(1,msf.countInstances(pd.tdJoin));
		
		/*
		 * now transition again to complete
		 */
		ti = processInstance.findTask(pd.tdEnd,MockTaskInstance.STATE_READY);
		assertNotNull("Failed to find the task to run",ti);
		engine.transitionTask(ti);
		
		/*
		 * check there are no outstanding tasks & the process is complete 
		 */
		assertEquals(0,processInstance.getTaskInstances().size());
		assertTrue(processInstance.getState()==IProcessInstance.STATE_COMPLETE);
		
		/*
		 * check to see if we have the expected number of object in the audit trail
		 * 1 x process
		 * 1 x tdStart (complete)
		 * 2 x tdSplit (complete)
		 * 1 x tdJoin (complete)
		 * 2 x tdParallel-1 (complete)
		 * 2 x tdParallel-2 (complete)
		 * 1 x tdEnd (complete)
		 * 0 x tdAlternateEnding
		 */
		assertEquals(10,msf.getAuditTrail().size());
		assertEquals(1,msf.countInstances(pd));
		assertEquals(1,msf.countInstances(pd.tdStart, MockTaskInstance.STATE_DELETED));
		assertEquals(2,msf.countInstances(pd.tdSplit, MockTaskInstance.STATE_DELETED));
		assertEquals(1,msf.countInstances(pd.tdJoin, MockTaskInstance.STATE_DELETED));
		assertEquals(2,msf.countInstances(pd.tdParallel_1, MockTaskInstance.STATE_DELETED));
		assertEquals(2,msf.countInstances(pd.tdParallel_2, MockTaskInstance.STATE_DELETED));
		assertEquals(1,msf.countInstances(pd.tdEnd, MockTaskInstance.STATE_DELETED));
		assertEquals(0,msf.countInstances(pd.tdAlternateEnding));
		
	}

	/**
	 * @author matt
	 * Created on 19-Aug-2005
	 *
	 * @param pd
	 * @param engine
	 * @param processInstance
	 * @throws DefinitionNotFoundException
	 * @throws StartProcessException
	 * @throws TransitionException
	 */
	private void doPart1(ComplexProcessDef pd, IEngine engine, MockProcessInstance processInstance) throws DefinitionNotFoundException, StartProcessException, TransitionException {
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
		
		MockTaskInstance ti = (MockTaskInstance) taskInstances.iterator().next();
		assertEquals(pd.tdStart, ti.getTaskDefinition());
		log.info("Transitioning Task " + ti.getTaskInstanceId());
		engine.transitionTask(ti);
		
		/* ensure the tasks are the ones we expect
		 * 1 x tdJoin
		 * 1 x tdParallel2
		 */
		assertEquals(2,processInstance.getTaskInstances().size());
		assertEquals(1,processInstance.countInstances(pd.tdParallel_2,MockTaskInstance.STATE_READY));
		assertEquals(1,processInstance.countInstances(pd.tdJoin,MockTaskInstance.STATE_AWAITINGSYNC));
	}
}
