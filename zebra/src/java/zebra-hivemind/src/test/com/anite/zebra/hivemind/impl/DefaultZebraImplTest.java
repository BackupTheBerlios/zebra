package com.anite.zebra.hivemind.impl;

import com.anite.zebra.hivemind.BaseCase;

public class DefaultZebraImplTest extends BaseCase {
	public void testInitialiseHiveMind() {
		Zebra zebra = (Zebra) registry.getService("zebra.Zebra",Zebra.class);

		assertNotNull(zebra);

	}
}
