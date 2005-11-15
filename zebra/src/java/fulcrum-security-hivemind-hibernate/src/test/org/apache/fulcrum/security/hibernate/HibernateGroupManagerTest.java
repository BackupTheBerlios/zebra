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

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;

import org.apache.fulcrum.security.model.test.AbstractGroupManagerTest;
/**
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: HibernateGroupManagerTest.java,v 1.1 2005/11/15 17:58:11 bgidley Exp $
 */
public class HibernateGroupManagerTest extends AbstractGroupManagerTest
{
//    private SessionFactory sessionFactory;
//    
//    public void setUp() throws Exception{
////      Initialise Database prior to tests
//        try {
//            Configuration conf = new Configuration().configure();
//            new SchemaExport(conf).create(true, true);
//            sessionFactory = conf.buildSessionFactory();
//        } catch (HibernateException e) {
//            throw new RuntimeException(e);
//        }
//        super.setUp();
//    }
}
