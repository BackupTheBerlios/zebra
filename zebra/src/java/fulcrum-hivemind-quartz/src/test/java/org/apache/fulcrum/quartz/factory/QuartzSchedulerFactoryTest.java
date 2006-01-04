package org.apache.fulcrum.quartz.factory;

import org.apache.fulcrum.hivemind.RegistryManager;
import org.apache.hivemind.ServiceImplementationFactory;
import org.quartz.Scheduler;

import junit.framework.TestCase;

public class QuartzSchedulerFactoryTest extends TestCase {

	public void testInitializeScheduler() {
		ServiceImplementationFactory quartzSchedulerFactory = (ServiceImplementationFactory) RegistryManager
				.getInstance().getRegistry().getService(
						"fulcrum.quartz.QuartzSchedulerFactory",
						ServiceImplementationFactory.class);
		assertNotNull(quartzSchedulerFactory);

		Scheduler scheduler = (Scheduler) RegistryManager.getInstance()
				.getRegistry().getService("fulcrum.quartz.Scheduler",
						Scheduler.class);
		assertNotNull(scheduler);
	}
}
