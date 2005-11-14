package org.apache.fulcrum.security.util;

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

/**
 * Thrown upon an attempt to create an User,Role,Group or Permission that
 * already exists.
 *
 * @author <a href="mailto:krzewski@e-point.pl">Rafal Krzewski</a>
 * @version $Id: EntityExistsException.java,v 1.1 2005/11/14 18:20:45 bgidley Exp $
 */
public class EntityExistsException
    extends TurbineSecurityException
{
    /**
     * Construct an EntityExistsException with specified detail message.
     *
     * @param msg The detail message.
     */
    public EntityExistsException(String msg)
    {
        super(msg);
    }
};
