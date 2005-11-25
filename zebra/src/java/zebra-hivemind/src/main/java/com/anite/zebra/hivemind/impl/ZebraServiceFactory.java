package com.anite.zebra.hivemind.impl;

import org.apache.hivemind.ServiceImplementationFactory;
import org.apache.hivemind.ServiceImplementationFactoryParameters;

import com.anite.zebra.ext.definitions.impl.ProcessDefinition;
import com.anite.zebra.ext.definitions.impl.TaskDefinition;
import com.anite.zebra.ext.state.hibernate.HibernateProcessInstance;
import com.anite.zebra.ext.state.hibernate.HibernateTaskInstance;

/**
 * A factory service for Hivemind to create dynamically the generic Zebra
 * service
 * 
 * @author ben.gidley
 * 
 */
public class ZebraServiceFactory implements ServiceImplementationFactory {

	/**
	 * Create the Zebra object based on the set classes If they aren't set this
	 * will not work.
	 */
	public Object createCoreServiceImplementation(
			ServiceImplementationFactoryParameters factoryParameters) {

		Zebra zebra = new Zebra<TaskDefinition, ProcessDefinition, HibernateTaskInstance, HibernateProcessInstance>();

		return zebra;
	}

}
