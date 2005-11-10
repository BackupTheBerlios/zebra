package com.anite.borris.services.security.manager.api;

import com.anite.borris.services.security.entity.api.SecurityEntity;
import com.anite.borris.services.security.entity.utils.DataBackendException;

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
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ben@gidley.co.uk">Ben Gidley</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: EntityManager.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */

public interface EntityManager {
    /**
     * Removes the security entity from the database
     * @param securityEntity
     * @throws DataBackendException
     */
    void removeEntity(SecurityEntity securityEntity) throws DataBackendException;

    /**
     * Updates the given security object
     * @param securityEntity
     * @throws DataBackendException
     */
    void updateEntity(SecurityEntity securityEntity) throws DataBackendException;

    /**
     * Adds new security object to the database
     * @param securityEntity
     * @throws DataBackendException
     */
    void addEntity(SecurityEntity securityEntity) throws DataBackendException;
}
