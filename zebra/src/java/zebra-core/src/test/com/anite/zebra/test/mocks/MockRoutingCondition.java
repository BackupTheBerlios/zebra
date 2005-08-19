/**
 * @author matt
 * Created on 19-Aug-2005
 */
package com.anite.zebra.test.mocks;

import com.anite.zebra.core.api.IConditionAction;
import com.anite.zebra.core.definitions.api.IRoutingDefinition;
import com.anite.zebra.core.exceptions.RunRoutingException;
import com.anite.zebra.core.state.api.ITaskInstance;

/**
 * @author matt
 * Created on 19-Aug-2005
 *
 */
public class MockRoutingCondition implements IConditionAction {

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.api.IConditionAction#runCondition(com.anite.zebra.core.definitions.api.IRoutingDefinition, com.anite.zebra.core.state.api.ITaskInstance)
	 */
	public boolean runCondition(IRoutingDefinition routingDef,
			ITaskInstance taskInstance) throws RunRoutingException {
		MockTaskInstance ti = (MockTaskInstance) taskInstance;
		return routingDef.getName().equals(ti.getConditionAction());
	}

}
