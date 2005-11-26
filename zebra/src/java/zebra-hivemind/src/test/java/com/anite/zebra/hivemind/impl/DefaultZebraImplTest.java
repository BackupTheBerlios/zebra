package com.anite.zebra.hivemind.impl;

import junit.framework.TestCase;

import org.apache.fulcrum.hivemind.RegistryManager;

public class DefaultZebraImplTest extends TestCase {
	public void testInitialiseHiveMind() {
		Zebra zebra = (Zebra) RegistryManager.getInstance().getRegistry().getService("zebra.Zebra",Zebra.class);

		assertNotNull(zebra);
		assertNotNull(zebra.getDefinitionFactory());

	}
}
