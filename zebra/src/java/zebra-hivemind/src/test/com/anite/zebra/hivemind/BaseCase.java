/*
 * Created on 21-Apr-2005
 */
package com.anite.zebra.hivemind;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fulcrum.hivemind.RegistryManager;
import org.apache.hivemind.Registry;

/**
 * @author chris
 */
public abstract class BaseCase extends TestCase {

	protected static Log log = LogFactory.getLog(BaseCase.class);

	protected static Registry registry;

	public void setUp() throws Exception {
		if (registry == null) {
			registry = RegistryManager.getInstance().getRegistry();
		}
	}

	protected void tearDown() throws Exception {
		registry.cleanupThread();
	}

	public Registry getRegistry(){
		return registry;
	}
	
}
