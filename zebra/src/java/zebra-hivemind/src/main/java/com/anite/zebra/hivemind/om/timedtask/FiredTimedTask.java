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
package com.anite.zebra.hivemind.om.timedtask;

import java.util.Date;

import javax.persistence.Entity;

import com.anite.zebra.hivemind.om.state.ZebraTaskInstanceHistory;

@Entity
public class FiredTimedTask extends TimedTask {

    private Date startTime;

    private Date endTime;

    private ZebraTaskInstanceHistory zebraTaskInstanceHistory;

    private boolean failed;

    private String exceptionText;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getExceptionText() {
        return exceptionText;
    }

    public void setExceptionText(String exceptionText) {
        this.exceptionText = exceptionText;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public ZebraTaskInstanceHistory getZebraTaskInstanceHistory() {
        return zebraTaskInstanceHistory;
    }

    public void setZebraTaskInstanceHistory(ZebraTaskInstanceHistory zebraTaskInstanceHistory) {
        this.zebraTaskInstanceHistory = zebraTaskInstanceHistory;
    }
    
    public FiredTimedTask() {
        
    }
    
    public FiredTimedTask(TimedTask timedTask) {
        // set timed task properties to the fired time task
    }

}
