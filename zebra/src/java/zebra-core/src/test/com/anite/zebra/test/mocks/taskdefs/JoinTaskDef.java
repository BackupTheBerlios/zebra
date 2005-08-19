/**
 * @author matt
 * Created on 19-Aug-2005
 */
package com.anite.zebra.test.mocks.taskdefs;

import com.anite.zebra.test.mocks.MockProcessDef;

/**
 * @author matt
 * Created on 19-Aug-2005
 *
 */
public class JoinTaskDef extends AutoRunTaskDef {

	/**
	 * @author matt
	 * Created on 19-Aug-2005
	 *
	 * @param pd
	 * @param taskName
	 */
	public JoinTaskDef(MockProcessDef pd, String taskName) {
		super(pd, taskName);
		this.setSynchronised(true);
	}

}
