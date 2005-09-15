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

import com.anite.zebra.ext.definitions.api.IProcessVersion;
import com.anite.zebra.ext.definitions.impl.ProcessDefinition;
import com.anite.zebra.ext.definitions.impl.ProcessVersions;
import com.anite.zebra.ext.definitions.impl.PropertyGroups;
import com.anite.zebra.ext.definitions.impl.TaskDefinition;

public class ProcessVersionsTest extends BaseTestCase {

    public void testSimpleSaveRetrieve() throws Exception {
        Session session = getSessionFactory().openSession();
        ProcessVersions pv = new ProcessVersions();
        ProcessDefinition processDef = new ProcessDefinition();
        processDef.setClassConstruct("java.lang.String");
        processDef.setClassDestruct("java.lang.Integer");
        
        processDef.setVersion(new Long(1));
        processDef.setPropertyGroups(new PropertyGroups());

        TaskDefinition firstTask = new TaskDefinition();
        firstTask.setAuto(Boolean.TRUE);
        firstTask.setClassName("my.class");
        firstTask.setName("firstTask");
        processDef.setFirstTask(firstTask);
        pv.addProcessVersion(processDef);
        session.save(processDef);
        assertNotNull(((ProcessVersions)processDef.getProcessVersions()).getId());
        assertNotNull(processDef.getId());
        assertNotNull(firstTask.getId());
        assertTrue(processDef.getId().longValue() > 0);
        session.flush();
        
        assertTrue(processDef instanceof IProcessVersion);
        
        pv = new ProcessVersions();
        pv.setName("name");
        pv.addProcessVersion(processDef);
        session.save(pv);
        assertNotNull(pv.getId());
        assertEquals(1,pv.getProcessVersions().size());
        assertEquals(processDef, pv.getLatestProcessVersion());
        assertEquals(processDef.getProcessVersions(),pv);
        session.flush();
        
       

    }
}