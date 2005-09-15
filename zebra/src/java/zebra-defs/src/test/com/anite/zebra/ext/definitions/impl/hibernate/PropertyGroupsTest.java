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

import com.anite.zebra.ext.definitions.api.IProperties;
import com.anite.zebra.ext.definitions.impl.PropertyElement;
import com.anite.zebra.ext.definitions.impl.PropertyGroups;

public class PropertyGroupsTest extends BaseTestCase {

    public void testSimpleSaveRetrieve() throws Exception {
        Session session = getSessionFactory().openSession();
        PropertyGroups pg = new PropertyGroups();
        PropertyElement pe = new PropertyElement();
        pe.setGroup("group");
        pe.setKey("name");
        pe.setValue("value");
        pg.addPropertyElement(pe);
        IProperties properties = pg.getProperties("group");
        assertEquals("group",properties.getName());
        assertEquals("value",properties.getString("name"));
        assertNull(pg.getId());
        assertNull(pe.getId());
        session.save(pg);
        session.save(pe);
        assertTrue(pg.getId().longValue()>0);
        assertTrue(pe.getId().longValue()>0);

    }

}