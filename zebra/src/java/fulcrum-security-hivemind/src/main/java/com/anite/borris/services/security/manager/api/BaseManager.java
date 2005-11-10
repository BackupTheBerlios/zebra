package com.anite.borris.services.security.manager.api;

import com.anite.borris.services.hibernate.api.ISessionManager;

/**
 * Hivemind session manager for security model
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: BaseManager.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */
public interface BaseManager {

    public ISessionManager getiSessionManager();
    public void setiSessionManager(ISessionManager iSessionManager);
}
