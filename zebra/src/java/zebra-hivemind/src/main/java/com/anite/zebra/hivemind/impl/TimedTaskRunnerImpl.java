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
package com.anite.zebra.hivemind.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.fulcrum.hivemind.RegistryManager;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerUtils;

import com.anite.zebra.core.exceptions.TransitionException;
import com.anite.zebra.core.state.api.ITaskInstance;
import com.anite.zebra.hivemind.api.TimedTaskRunner;
import com.anite.zebra.hivemind.job.TimedTaskRunnerJob;
import com.anite.zebra.hivemind.manager.FiredTimedTaskManager;
import com.anite.zebra.hivemind.manager.TimeManager;
import com.anite.zebra.hivemind.manager.TimedTaskManager;
import com.anite.zebra.hivemind.om.state.ZebraTaskInstance;
import com.anite.zebra.hivemind.om.timedtask.FiredTimedTask;
import com.anite.zebra.hivemind.om.timedtask.Time;
import com.anite.zebra.hivemind.om.timedtask.TimedTask;

/**
 * Runs the tasks.
 * 
 * The manages all interactions with the OM Managers and Quartz.
 * 
 * Quartz is injected into the service
 * 
 * @author Mike Jones
 * @author Ben GIdley
 * 
 */
public class TimedTaskRunnerImpl implements TimedTaskRunner {

	static public final String MINUTE = "minute";

	static public final String HOUR = "hour";

	private static final String TIME_TASK_RUNNER = "TimeTaskRunner";

	private TimedTaskManager timedTaskManager;

	private FiredTimedTaskManager firedTimedTaskManager;

	private TimeManager timeManager;

	private Log log;

	private Zebra zebra;

	private Scheduler scheduler;

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setZebra(Zebra zebra) {
		this.zebra = zebra;
	}

	/*
	 * method for injection
	 */
	public void setFiredTimedTaskManager(
			FiredTimedTaskManager firedTimedTaskManager) {
		this.firedTimedTaskManager = firedTimedTaskManager;
	}

	/*
	 * Method for injection
	 */
	public void setLog(Log log) {
		this.log = log;
	}

	/*
	 * Method for injection
	 */
	public void setTimedTaskManager(TimedTaskManager timedTaskManager) {
		this.timedTaskManager = timedTaskManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anite.zebra.hivemind.api.TimedTaskRunner#runTasksForTime(com.anite.zebra.hivemind.om.timedtask.Time)
	 */
	public void runTasksForTime(Time time) {

		List<TimedTask> timedTasks = timedTaskManager.getTasksForTime(time);

		for (TimedTask timedTask : timedTasks) {

			runTask(timedTask);

		}

	}

	public void scheduleTimedTask(ZebraTaskInstance zti, int hours, int mins) {

		TimedTask timedTask = new TimedTask();
		Time time = getTimeManager().createOrFetchTime(hours, mins);
		timedTask.setZebraTaskInstanceId(zti.getTaskInstanceId());
		timedTask.setTime(time);
		getTimedTaskManager().saveOrUpdate(timedTask);

		// Now verify there is a quartz job for requested time.
		try {
			JobDetail jobDetail = scheduler.getJobDetail(time.getJobName(),
					TIME_TASK_RUNNER);

			if (jobDetail == null){
				createJobDetail(time);
			}

		} catch (SchedulerException e) {
			log.error(e);
		}

	}

	/**
	 * Create the job in quartz for the passed time.
	 * @param time
	 * @throws SchedulerException
	 */
	private void createJobDetail(Time time) throws SchedulerException {
		JobDetail jobDetail = new JobDetail();
		jobDetail.setName(time.getJobName());
		jobDetail.setDescription("Time Task Runner Job");
		jobDetail.setDurability(false);
		jobDetail.setGroup(TIME_TASK_RUNNER);
		jobDetail.setJobClass(TimedTaskRunnerJob.class);
		jobDetail.setRequestsRecovery(true);
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(HOUR, time.getHour());
		jobDataMap.put(MINUTE, time.getMinute());
		jobDetail.setJobDataMap(jobDataMap);

		scheduler.scheduleJob(jobDetail, TriggerUtils.makeDailyTrigger(time.getJobName(), time
				.getHour(), time.getMinute()));
	}

	/**
	 * Runt the timed task by retrieving the associated zebra task instance and
	 * transtioningit it
	 * 
	 * @param timedTask
	 */
	protected void runTask(TimedTask timedTask) {

		FiredTimedTask firedTimedTask = new FiredTimedTask(timedTask);

		ZebraTaskInstance zti = zebra.getStateFactory().loadTaskInstance(
				timedTask.getZebraTaskInstanceId());

		zti.setOutcome("Done");
		zti.setState(ITaskInstance.STATE_AWAITINGCOMPLETE);

		try {
			firedTimedTask.setZebraTaskInstanceId(zti.getTaskInstanceId());
			firedTimedTask.setStartTime(new Date());
			zebra.transitionTask(zti);
			firedTimedTask.setFailed(false);
			firedTimedTask.setEndTime(new Date());			
		} catch (TransitionException e) {
			log.error(e);
			firedTimedTask.setExceptionText("Failed to transition task: "
					+ e.getMessage());
			firedTimedTask.setFailed(true);
		} catch (Throwable e) {
			log.error(e);
			firedTimedTask.setExceptionText("Throwable: " + e.getMessage());
			firedTimedTask.setFailed(true);
		} finally {
			firedTimedTaskManager.saveOrUpdate(firedTimedTask);
			timedTaskManager.delete(timedTask);
			RegistryManager.getInstance().getRegistry().cleanupThread();
		}

	}

	public TimeManager getTimeManager() {
		return timeManager;
	}

	public void setTimeManager(TimeManager timeManager) {
		this.timeManager = timeManager;
	}

	public FiredTimedTaskManager getFiredTimedTaskManager() {
		return firedTimedTaskManager;
	}

	public TimedTaskManager getTimedTaskManager() {
		return timedTaskManager;
	}

}
