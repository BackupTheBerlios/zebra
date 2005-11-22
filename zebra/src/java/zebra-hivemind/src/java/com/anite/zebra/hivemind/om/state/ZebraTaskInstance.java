package com.anite.zebra.hivemind.om.state;

import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.core.exceptions.DefinitionNotFoundException;
import com.anite.zebra.core.state.api.IFOE;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.ext.state.hibernate.HibernateTaskInstance;

/**
 * Zebra Task Instance Implementation
 * @author ben.gidley
 * @hibernate.class
 */
public class ZebraTaskInstance extends HibernateTaskInstance {

	/**
	 * @hibernate.property
	 */
	@Override
	public IFOE getFOE() {
		// TODO Auto-generated method stub
		return super.getFOE();
	}

	@Override
	public IProcessInstance getProcessInstance() {
		// TODO Auto-generated method stub
		return super.getProcessInstance();
	}

	@Override
	public long getState() {
		// TODO Auto-generated method stub
		return super.getState();
	}

	@Override
	public ITaskDefinition getTaskDefinition() throws DefinitionNotFoundException {
		// TODO Auto-generated method stub
		return super.getTaskDefinition();
	}

	@Override
	public Long getTaskInstanceId() {
		// TODO Auto-generated method stub
		return super.getTaskInstanceId();
	}

}
