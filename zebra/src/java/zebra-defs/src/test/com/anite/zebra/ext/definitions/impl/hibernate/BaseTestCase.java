/*
 * Copyright 2004 Anite - Central Government Division
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

import junit.framework.TestCase;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;

public class BaseTestCase extends TestCase {

    private SessionFactory sessionFactory;



    public SessionFactory getSessionFactory() throws HibernateException {

        if (sessionFactory == null) {
            Configuration configuration = new Configuration();

            sessionFactory = configuration.configure().buildSessionFactory();

            new SchemaExport(configuration).create(true, true);

        }
        return sessionFactory;

    }


}