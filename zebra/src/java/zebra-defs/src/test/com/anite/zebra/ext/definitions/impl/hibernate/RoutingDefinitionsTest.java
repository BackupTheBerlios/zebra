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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.hibernate.Session;

import com.anite.zebra.ext.definitions.impl.PropertyElement;
import com.anite.zebra.ext.definitions.impl.PropertyGroups;
import com.anite.zebra.ext.definitions.impl.RoutingDefinition;
import com.anite.zebra.ext.definitions.impl.RoutingDefinitions;
import com.anite.zebra.ext.definitions.impl.TaskDefinition;

public class RoutingDefinitionsTest extends BaseTestCase {

    public void testSimpleSaveRetrieve() throws Exception {
        Session session = getSessionFactory().openSession();
        
        RoutingDefinition rd = new RoutingDefinition();
        rd.setConditionClass("blah");
        rd.setName("test routing def");
        rd.setParallel(true);
        Set set = new HashSet();
        set.add(rd);
        RoutingDefinitions rds = new RoutingDefinitions(set);
                
        TaskDefinition origTD = new TaskDefinition();
        TaskDefinition destTD = new TaskDefinition();
        rd.setDestinationTaskDefinition(destTD);
        rd.setOriginatingTaskDefinition(origTD);
        
        session.save(rd);
        assertNotNull(rd.getId());
        assertNotNull(rd.getOriginatingTaskDefinition().getId());
        assertNotNull(rd.getDestinationTaskDefinition().getId());
        set.add(rd);
        Iterator i = rds.iterator();
        assertTrue(i.hasNext());
        i.next();
        assertFalse(i.hasNext());
        
        PropertyGroups pg = new PropertyGroups();
        pg.addPropertyElement(new PropertyElement("hi","hi","hi"));
        rd.setPropertyGroups(pg);
        session.saveOrUpdate(rd);
        assertNotNull(rd.getPropertyGroups());
        
    }

}