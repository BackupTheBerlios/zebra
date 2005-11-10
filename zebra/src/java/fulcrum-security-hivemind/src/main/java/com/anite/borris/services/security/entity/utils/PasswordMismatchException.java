package com.anite.borris.services.security.entity.utils;

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
 * Thrown to indicate that the password supplied by user was incorrect.
 *
 * @author <a href="mailto:krzewski@e-point.pl">Rafal Krzewski</a>
 * @version $Id: PasswordMismatchException.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */
public class PasswordMismatchException
    extends TurbineSecurityException
{
    /**
     * Construct an PasswordMismatchException with specified detail message.
     *
     * @param msg The detail message.
     */
    public PasswordMismatchException(String msg)
    {
        super(msg);
    }
}