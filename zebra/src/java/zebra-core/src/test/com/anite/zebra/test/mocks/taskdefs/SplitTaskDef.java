/**
 * @author matt
 * Created on 19-Aug-2005
 */
package com.anite.zebra.test.mocks.taskdefs;

import com.anite.zebra.test.mocks.MockProcessDef;
import com.anite.zebra.test.mocks.MockRouting;

/**
 * @author matt
 * Created on 19-Aug-2005
 *
 */
public class SplitTaskDef extends AutoRunTaskDef {

	/**
	 * @author matt
	 * Created on 19-Aug-2005
	 *
	 * @param pd
	 */
	public SplitTaskDef(MockProcessDef pd,String taskName) {
		super(pd,taskName);
	}

	public MockRouting addRoutingOut(MockTaskDef destination) {
		MockRouting mr = super.addRoutingOut(destination);
		mr.setParallel(true);
		return mr;
	}
}
