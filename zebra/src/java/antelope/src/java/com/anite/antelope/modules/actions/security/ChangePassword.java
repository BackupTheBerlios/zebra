/*
 * Copyright 2004 Anite - Central Government Division
 *    http://www.anite.com/publicsector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anite.antelope.modules.actions.security;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.entity.User;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.anite.antelope.modules.actions.SecureAction;
import com.anite.antelope.utils.AvalonServiceHelper;
/**
 * @author Michael.Jones
 */
public class ChangePassword extends SecureAction {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.turbine.modules.actions.VelocitySecureAction#doPerform(org.apache.turbine.util.RunData,
	 *      org.apache.velocity.context.Context)
	 */
	public void doPerform(RunData data, Context context) throws Exception {
		String passwordOld = data.getParameters().get("passwordOld");
		String passwordNew = data.getParameters().get("passwordNew");
		String passwordConf = data.getParameters().get("passwordConf");
		// They could have javascript turned off and cant type
		if (!passwordNew.equals(passwordConf)) {
			data.setMessage("New password fields dont match!");
			return;
		}
		UserManager usermanager;
		User user;
		usermanager = AvalonServiceHelper.instance().getSecurityService()
				.getUserManager();
		user = usermanager.getUser(data.getUser().getName());
		usermanager.changePassword(user, passwordOld, passwordNew);
		data.setMessage("Your password has been changed");
	}
}