package org.apache.fulcrum.security.authenticator;
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

import junit.framework.TestCase;

import org.apache.fulcrum.hivemind.RegistryManager;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;

/**
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: CryptoAuthenticatorTest.java,v 1.2 2005/11/15 09:30:28 bgidley Exp $
 */
public class CryptoAuthenticatorTest extends TestCase
{
    
    private static final String preDefinedInput = "Oeltanks";
    private static final String preDefinedResult = "uVDiJHaavRYX8oWt5ctkaa7j1cw=";
    /**
    	* Constructor for CryptoAuthenticatorTest.
    	* @param arg0
    	*/
    public CryptoAuthenticatorTest(String arg0)
    {
        super(arg0);
    }
    public void setUp()
    {
        
    }
    public void testAuthenticate() throws Exception
    {
        User user = new DynamicUser();
        user.setName("Bob");
        user.setPassword(preDefinedResult);
        Authenticator authenticator = (Authenticator)RegistryManager.getInstance().getRegistry().getService("fulcrum.security.authenticatorCrypto", Authenticator.class);
        assertTrue(authenticator.authenticate(user, preDefinedInput));
        assertFalse(authenticator.authenticate(user, "mypassword"));
    }
}
