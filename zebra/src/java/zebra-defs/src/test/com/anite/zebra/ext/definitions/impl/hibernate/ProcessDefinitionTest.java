/*
 * Copyright 2004/2005 Anite - Enforcement & Security
 * http://www.anite.com/publicsector
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.anite.zebra.ext.definitions.impl.hibernate;

import net.sf.hibernate.Session;

import com.anite.zebra.ext.definitions.impl.ProcessDefinition;
import com.anite.zebra.ext.definitions.impl.ProcessVersions;
import com.anite.zebra.ext.definitions.impl.PropertyElement;
import com.anite.zebra.ext.definitions.impl.PropertyGroups;
import com.anite.zebra.ext.definitions.impl.RoutingDefinition;
import com.anite.zebra.ext.definitions.impl.TaskDefinition;

public class ProcessDefinitionTest extends BaseTestCase {

    public void testSimpleSaveRetrieve() throws Exception {
        Session session = getSessionFactory().openSession();
        ProcessVersions pv = new ProcessVersions();
        pv.setName("name");
        
        ProcessDefinition processDef = new ProcessDefinition();
        processDef.setClassConstruct("java.lang.String");
        processDef.setClassDestruct("java.lang.Integer");
      //  processDef.setName("name");
        processDef.setVersion(new Long(1));
        

        TaskDefinition firstTask = new TaskDefinition();
        firstTask.setAuto(Boolean.TRUE);
        firstTask.setClassName("my.class");
        firstTask.setName("firstTask");
        processDef.setFirstTask(firstTask);
        processDef.setPropertyGroups(new PropertyGroups());
        //try {
        pv.addProcessVersion(processDef);
        session.save(pv);
        //fail("Shouldn't be able to save without providing a PropertyGroup to use.");
        //}
        //catch (PropertyValueException pve){
        //    assertTrue(pve.getMessage().indexOf("not-null")>-1);
        //}
        
       // Transaction t = session.beginTransaction();
        session.save(processDef);
       // t.commit();
        assertNotNull(processDef.getId());
        assertNotNull(firstTask.getId());
        assertTrue(processDef.getId().longValue() > 0);
        session.flush();
        //session = getSessionFactory().openSession();
        // evict our test object so that the session.load actually
        // goes back to the database!
        session.evict(processDef);
        ProcessDefinition processDef2 = (ProcessDefinition) session.load(ProcessDefinition.class, processDef.getId());
        assertEquals(processDef.getId(), processDef2.getId());
        assertEquals("name", processDef2.getName());
        assertEquals("name", processDef2.getProcessVersions().getName());
        assertNotNull(processDef2.getFirstTask());
        assertTrue(processDef2.getFirstTask().getId().longValue() > 0);

        PropertyGroups pg = new PropertyGroups();
        pg.addPropertyElement(new PropertyElement("group2", "key2", "value2"));
        processDef2.setPropertyGroups(pg);
        assertNull(pg.getId());
        session.saveOrUpdate(processDef2);
        session.saveOrUpdate(processDef2.getPropertyGroups());
        assertTrue(processDef2.getPropertyGroups() instanceof PropertyGroups);
        assertNotNull(((PropertyGroups) processDef2.getPropertyGroups()).getId());

        PropertyGroups pg2 = new PropertyGroups();
        pg2.addPropertyElement(new PropertyElement("group3", "key3", "value2"));
        firstTask = (TaskDefinition) processDef2.getFirstTask();
        assertNull("If none has been set, then none should be there",firstTask.getPropertyGroups());

        
        ((TaskDefinition) (processDef2.getFirstTask())).setPropertyGroups(pg2);
        assertNotNull(firstTask.getPropertyGroups());
        assertNull(pg2.getId());
        session.saveOrUpdate(processDef2);
        session.saveOrUpdate(pg2);
        assertNotNull(pg2.getId());
        assertNotNull(processDef2.getTaskDefs());
        session.saveOrUpdate(firstTask);
        assertNotNull(processDef2.getTaskDefs().getTaskDef(firstTask.getId()));

        TaskDefinition td2 = new TaskDefinition();
        processDef2.getTaskDefinitions().add(td2);
        assertNull(td2.getId());
        session.saveOrUpdate(td2);
        assertNotNull(td2.getId());
        assertNotNull(processDef2.getTaskDefs().getTaskDef(td2.getId()));

        RoutingDefinition rd = new RoutingDefinition();

        TaskDefinition origTD = new TaskDefinition();
        TaskDefinition destTD = new TaskDefinition();
        rd.setDestinationTaskDefinition(destTD);
        rd.setOriginatingTaskDefinition(origTD);

        session.saveOrUpdate(rd);
        assertNotNull(rd.getOriginatingTaskDefinition().getId());
        assertNotNull(rd.getDestinationTaskDefinition().getId());
        processDef2.getRoutingDefinitions().add(rd);
        session.save(processDef2);
        assertNotNull(rd.getId());
        
        RoutingDefinition rd2 = new RoutingDefinition();
        td2.getRoutingIn().add(rd);
        session.saveOrUpdate(td2);
        session.saveOrUpdate(rd2);
       // session.flush();
       // assertNotNull(rd2.getId());

    }
}