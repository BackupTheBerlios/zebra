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

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;

import com.anite.zebra.core.exceptions.DefinitionNotFoundException;
import com.anite.zebra.core.exceptions.RunTaskException;
import com.anite.zebra.core.factory.exceptions.StateFailureException;
import com.anite.zebra.core.state.api.ITaskInstance;
import com.anite.zebra.core.state.api.ITransaction;
import com.anite.zebra.hivemind.impl.Zebra;
import com.anite.zebra.hivemind.om.defs.ZebraTaskDefinition;
import com.anite.zebra.hivemind.om.state.ZebraTaskInstance;

public class QuartzServiceTaskAction extends ZebraTaskAction {

    public static final Log log = LogFactory.getLog(QuartzServiceTaskAction.class);

    public static final String TASK_INSTANCE_ID_KEY = "taskInstanceId";
    
    public static final String JOB_NAME = "QuartzServiceTaskAction";

    private static final long SECONDS_DELAY = 1000L;

    private static final long MINUTES_DELAY = 60L * 1000L;

    private static final long HOURS_DELAY = 60L * 60L * 1000L;

    private static final long DAYS_DELAY = 24L * 60L * 60L * 1000L;

    private Scheduler scheduler;

    private Zebra zebra;

    public Zebra getZebra() {
        return zebra;
    }

    public void setZebra(Zebra zebra) {
        this.zebra = zebra;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * When this task is run, it waits until the scheduled time and then runs
     * the QuartzJob. This job is given the taskInstanceId and is responsible
     * for tranistioning the task when the trigger is fired.
     */
    public void runTask(ZebraTaskInstance taskInstance) throws RunTaskException {
        if (StringUtils.isEmpty(taskInstance.getOutcome())) {
            try {

                /*
                 * Create your Scheduled job
                 */
            	JobDetail jobDetail = scheduler.getJobDetail(JOB_NAME, null);
            	if (jobDetail == null) {
            		jobDetail = new JobDetail(taskInstance.getTaskInstanceId().toString(), null,
                            ScheduledTaskTransitionJob.class);
            	}

            	/*
                 * get the poll interval from taskInstace.getTaskDefinition
                 * (downcast to ZebraTaskDefinition) get property.
                 */
                ZebraTaskDefinition taskDef = (ZebraTaskDefinition) taskInstance.getTaskDefinition();
                String wait = taskDef.getGeneralProperties().getString("Wait");
                log.info("Wait is : " + wait);
                log.info("Delay is :" + new Long(delay(wait)).toString());
                /*
                 * Wait supports delay times in : - 
                 * 	S	Seconds ( Default )
                 *  M	Minutes
                 *  H	Hours
                 *  D	Days
                 *   
                 */

                SimpleTrigger trigger = new SimpleTrigger();
                trigger.setStartTime(new Date(new Date().getTime() + delay(wait)));
                trigger.setName(taskInstance.getTaskInstanceId().toString());
                trigger.getJobDataMap().put(TASK_INSTANCE_ID_KEY, taskInstance.getTaskInstanceId());
                scheduler.scheduleJob(jobDetail, trigger);
                /*
                 * Don't set the state unlike every other task action as the state
                 * will be set when the trigger fires.
                 */

                // Need to make sure that state (which is STATE_RUNNING) is saved in the DB
                // This is a bug in the w/f engine as it hsould save it for us
                taskInstance.setState(ITaskInstance.STATE_RUNNING);
                ITransaction t = zebra.getStateFactory().beginTransaction();                
                zebra.getStateFactory().saveObject(taskInstance);
                t.commit();
                log.info("Save task in state:" + taskInstance.getState());

            } catch (SchedulerException se) {
                throw new RunTaskException(se);
            } catch (StateFailureException e) {
                throw new RunTaskException(e);
            } catch (DefinitionNotFoundException e) {
                throw new RunTaskException(e);            }
        } else {
            // Trigger has fired
            taskInstance.setState(ITaskInstance.STATE_AWAITINGCOMPLETE);
        }

    }

    private long delay(String wait) throws IllegalArgumentException {
        long delay = 0;
        String sd = null;
        String period = "S";
        wait.trim().toUpperCase();
        if (wait.startsWith("S")) {
            sd = wait.substring(1);
        }
        if (wait.endsWith("S")) {
            sd = wait.substring(0, wait.length() - 1);
        }
        if (wait.startsWith("M")) {
            sd = wait.substring(1);
            period = "M";
        }
        if (wait.endsWith("M")) {
            sd = wait.substring(0, wait.length() - 1);
            period = "M";
        }
        if (wait.startsWith("H")) {
            sd = wait.substring(1);
            period = "H";
        }
        if (wait.endsWith("H")) {
            sd = wait.substring(0, wait.length() - 1);
            period = "H";
        }
        if (wait.startsWith("D")) {
            sd = wait.substring(1);
            period = "D";
        }
        if (wait.endsWith("D")) {
            sd = wait.substring(0, wait.length() - 1);
            period = "D";
        }
        /*
         * wait does not contain S,M,H or D
         */
        if ((wait.indexOf("S") == -1) && (wait.indexOf("M") == -1) && (wait.indexOf("H") == -1)
                && (wait.indexOf("D") == -1)) {
            sd = wait;
        }
        try {
            delay = Long.parseLong(sd);
        } catch (NumberFormatException pe) {
            throw new IllegalArgumentException("Wait string should be an integer followed by S, M, H or D. : " + sd);
        }
        if (period.equals("S")) {
            return delay * SECONDS_DELAY;
        }
        if (period.equals("M")) {
            return delay * MINUTES_DELAY;
        }
        if (period.equals("H")) {
            return delay * HOURS_DELAY;
        }
        if (period.equals("D")) {
            return delay * DAYS_DELAY;
        }
        return delay * SECONDS_DELAY;
    }

}
