/*
 * Copyright 2004 Anite - Central Government Division
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

package com.anite.zebra.hivemind.om.state;

import com.anite.zebra.core.state.api.IFOE;
import com.anite.zebra.core.state.api.IProcessInstance;

/**
 * @hibernate.class
 * @hibernate.cache usage="transactional"
 * @author Ben.Gidley
 */
public class ZebraFOE implements IFOE {

    private ZebraProcessInstance processInstance;
    private Integer antelopeFoeID;
    
 
    /**
     * @param processInstance
     */
    public ZebraFOE(IProcessInstance processInstance) {
        this.processInstance = (ZebraProcessInstance) processInstance;
        this.processInstance.getFOEs().add(this);
    }

    /**
     * @hibernate.many-to-one column="processInstanceId" not-null="true"
     *                        class="com.anite.antelope.zebra.om.AntelopeProcessInstance"
     *                        cascade="all"
     */
    public IProcessInstance getProcessInstance() {
        return processInstance;
    }

    

    /**
     * @param processInstance The processInstance to set.
     */
    public void setProcessInstance(IProcessInstance processInstance) {
        this.processInstance = (ZebraProcessInstance) processInstance;
    }
    /**
     * @hibernate.id generator-class="native"
     * @return Returns the antelopeFoeID.
     */
    public Integer getAntelopeFoeID() {
        return antelopeFoeID;
    }

    /**
     * @param antelopeFoeID The antelopeFoeID to set.
     */
    public void setAntelopeFoeID(Integer antelopeFoeID) {
        this.antelopeFoeID = antelopeFoeID;
    }

    

}