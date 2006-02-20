/*
 * Copyright 2004, 2005 Anite 
 *    http://www.anite.com/publicsector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anite.zebra.hivemind.taskAction;

import org.apache.fulcrum.hivemind.RegistryManager;
import org.hibernate.Session;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.anite.zebra.core.exceptions.TransitionException;
import com.anite.zebra.core.state.api.ITaskInstance;
import com.anite.zebra.hivemind.impl.Zebra;
import com.anite.zebra.hivemind.om.state.ZebraTaskInstance;
import com.anite.zebra.hivemind.util.RegistryHelper;

public class ScheduledTaskTransitionJob implements Job {

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap data = context.getMergedJobDataMap();
		Long taskInstanceId = (Long) data
				.get(QuartzServiceTaskAction.TASK_INSTANCE_ID_KEY);

		Session session = RegistryHelper.getInstance().getSession();

        
        
		ZebraTaskInstance task = (ZebraTaskInstance) session.load(
				ZebraTaskInstance.class, taskInstanceId);
		task.setOutcome("Done");        
        task.setState(ITaskInstance.STATE_AWAITINGCOMPLETE);
		Zebra zebra = RegistryHelper.getInstance().getZebra();
       
		try {
            zebra.transitionTask(task);
		} catch (TransitionException te) {
			throw new JobExecutionException(te);
		} 
		
		// Tell hivemind to recycle thread        
        RegistryManager.getInstance().getRegistry().cleanupThread();

	}

}
