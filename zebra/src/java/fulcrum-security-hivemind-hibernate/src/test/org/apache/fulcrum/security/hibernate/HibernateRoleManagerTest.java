package org.apache.fulcrum.security.hibernate;
/*
 *  Copyright 2001-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.apache.fulcrum.hivemind.RegistryManager;
import org.apache.fulcrum.security.model.test.AbstractRoleManagerTest;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
/**
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: HibernateRoleManagerTest.java,v 1.2 2005/11/21 13:31:48 bgidley Exp $
 */
public class HibernateRoleManagerTest extends AbstractRoleManagerTest
{
    public void setUp() throws Exception {
        // Force Registry to have test configuration
        Resource resource = new ClasspathResource(new DefaultClassResolver(), "META-INF/hivemodule_test.xml");
        RegistryManager.getInstance().getResources().add(resource);
        super.setUp();
    }
}
