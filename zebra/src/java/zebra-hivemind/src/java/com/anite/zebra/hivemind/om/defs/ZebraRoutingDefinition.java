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


package com.anite.zebra.hivemind.om.defs;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.ext.definitions.api.IPropertyGroups;
import com.anite.zebra.ext.definitions.impl.RoutingDefinition;

/**
 * A Zebra Routing Definition with Hibernate Tags and XML Serialization support   
 * @author Eric Pugh
 * @author Ben GIdley
 * @hibernate.class
 * @hibernate.cache usage="transactional"
 */
@Entity
public class ZebraRoutingDefinition extends RoutingDefinition {

    private Long xmlId; 

    /**
     * @return Returns the xmlId.
     * @hibernate.property
     */
    public Long getXmlId() {
        return this.xmlId;
    }
    /**
     * @param xmlId The xmlId to set.
     */
    public void setXmlId(Long xmlId) {
        this.xmlId = xmlId;
    }
    /**
     * @hibernate.id generator-class="native"
     */
    @Id(generate=GeneratorType.AUTO)
    public Long getId() {
        return super.getId();
    }
    /**
     * @hibernate.property
     */
    public String getName() {
        return super.getName();
    }
    
    /**
     * @hibernate.many-to-one column="origTaskDefId" not-null="false"
     *                        class="com.anite.zebra.hivemind.om.defs.ZebraTaskDefinition"
     *                        cascade="all" 
     */
    @ManyToOne(targetEntity=ZebraTaskDefinition.class)
    @JoinColumn(name="originatingTaskDefinitionId")
    public ITaskDefinition getOriginatingTaskDefinition() {
        return super.getOriginatingTaskDefinition();
    }
    /**
     * @hibernate.property
     */
    public boolean getParallel() {
        return super.getParallel();
    }
    /**
     * @return Returns the propertyGroups.
     * @@hibernate.many-to-one column="propertyGroupsId" not-null="false"
     *                        class="com.anite.zebra.hivemind.om.defs.ZebraPropertyGroups"
     *                        cascade="save-update"
     */
    @ManyToOne(targetEntity=ZebraPropertyGroups.class)
    
    public IPropertyGroups getPropertyGroups() {
        return super.getPropertyGroups();
    }
    
    /**
     * @hibernate.property
     */
    public String getConditionClass() {
        return super.getConditionClass();
    }
    
    /**
     * @hibernate.many-to-one column="destTaskDefId" not-null="false"
     *                        class="com.anite.zebra.hivemind.om.defs.ZebraTaskDefinition"
     *                        cascade="all" 
     */ 
    @ManyToOne(targetEntity=ZebraTaskDefinition.class)
    @JoinColumn(name="desitationTaskDefinitionId")
    public ITaskDefinition getDestinationTaskDefinition() {
        return super.getDestinationTaskDefinition();
    }    
}
