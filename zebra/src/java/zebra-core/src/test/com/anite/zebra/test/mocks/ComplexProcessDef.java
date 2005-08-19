/**
 * @author matt
 * Created on 19-Aug-2005
 */
package com.anite.zebra.test.mocks;

import com.anite.zebra.test.mocks.taskdefs.AutoRunTaskDef;
import com.anite.zebra.test.mocks.taskdefs.JoinTaskDef;
import com.anite.zebra.test.mocks.taskdefs.ManualRunTaskDef;
import com.anite.zebra.test.mocks.taskdefs.MockTaskDef;
import com.anite.zebra.test.mocks.taskdefs.SplitTaskDef;

/**
 * @author matt
 * Created on 19-Aug-2005
 *
 */
public class ComplexProcessDef extends MockProcessDef {
	
	
	
	/**
	 * @author matt
	 * Created on 19-Aug-2005
	 */
	public static final String GOTO = "Goto ";
	public MockTaskDef tdStart;
	public MockTaskDef tdJoin;
	public MockTaskDef tdParallel_1;
	public MockTaskDef tdParallel_2;
	public MockTaskDef tdAlternateEnding;
	public MockTaskDef tdSplit;
	public MockTaskDef tdEnd;

	public ComplexProcessDef(String name) {
		super(name);
		setup();
	}
	
	private void setup() {
		tdStart = new ManualRunTaskDef(this,"Start");
		this.setFirstTask(tdStart);
		
		tdSplit = new SplitTaskDef(this,"Split");
		tdStart.addRoutingOut(tdSplit);
		
		
		tdJoin = new JoinTaskDef(this,"Join");
		tdParallel_1 = new AutoRunTaskDef (this,"Parallel-1");
		tdSplit.addRoutingOut(tdParallel_1);
		tdParallel_1.addRoutingOut(tdJoin);
		
		
		tdParallel_2 = new ManualRunTaskDef (this,"Parallel-2");
		tdSplit.addRoutingOut(tdParallel_2);		
		MockRouting mr1 = tdParallel_2.addRoutingOut(tdJoin);
		mr1.setName(GOTO + tdJoin.getName());
		mr1.setConditionClass(MockRoutingCondition.class.getName());
		MockRouting mr2 = tdParallel_2.addRoutingOut(tdSplit);
		mr2.setName(GOTO + tdSplit.getName());
		mr2.setConditionClass(MockRoutingCondition.class.getName());
		
		tdAlternateEnding = new ManualRunTaskDef(this,"Alternate Ending");
		MockRouting mr3 = tdParallel_2.addRoutingOut(tdAlternateEnding);
		mr3.setName(GOTO + tdAlternateEnding.getName());
		mr3.setConditionClass(MockRoutingCondition.class.getName());
	
		tdEnd = new ManualRunTaskDef(this,"End");
		tdJoin.addRoutingOut(tdEnd);
		
	}

}
