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
package com.anite.zebra.hivemind.job;

import org.apache.fulcrum.hivemind.RegistryManager;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.anite.zebra.hivemind.api.TimedTaskRunner;
import com.anite.zebra.hivemind.impl.TimedTaskRunnerImpl;
import com.anite.zebra.hivemind.manager.TimeManager;
import com.anite.zebra.hivemind.om.timedtask.Time;

/**
 * Execute a 'times' jobs
 * 
 * This pays no attention to what the actual time is as this could be recovered 
 * or delayed.
 * @author ben.gidley
 *
 */
public class TimedTaskRunnerJob implements Job {

	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		int hour = jobDataMap.getInt(TimedTaskRunnerImpl.HOUR);
		int minute = jobDataMap.getInt(TimedTaskRunnerImpl.MINUTE);

		TimedTaskRunner timedTaskRunner = (TimedTaskRunner) RegistryManager
				.getInstance().getRegistry().getService(TimedTaskRunner.class);

		TimeManager timeManager = (TimeManager) RegistryManager.getInstance()
				.getRegistry().getService(TimeManager.class);

		Time time = timeManager.createOrFetchTime(hour, minute);
		timedTaskRunner.runTasksForTime(time);

	}

}
