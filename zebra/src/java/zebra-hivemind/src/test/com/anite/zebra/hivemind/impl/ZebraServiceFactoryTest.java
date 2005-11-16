package com.anite.zebra.hivemind.impl;

import junit.framework.TestCase;

import org.apache.fulcrum.hivemind.RegistryManager;

public class ZebraServiceFactoryTest extends TestCase {
	public void testGetFromRegistry() {
		ZebraServiceFactory zebraServiceFactory = (ZebraServiceFactory) RegistryManager
				.getInstance().getRegistry().getService(
						"zebra.zebraServiceFactory", ZebraServiceFactory.class);
		assertNotNull(zebraServiceFactory);
	}

	public void testCreation() {
		ZebraServiceFactory zebraServiceFactory = (ZebraServiceFactory) RegistryManager
				.getInstance().getRegistry().getService(
						"zebra.zebraServiceFactory", ZebraServiceFactory.class);

		Zebra zebra = (Zebra) zebraServiceFactory.createCoreServiceImplementation(null);
		assertNotNull(zebra);
	}
}
