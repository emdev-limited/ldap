package ru.emdev.ldap.event;

import ru.emdev.ldap.service.LDAPLocalServiceUtil;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 *
 * TODO - for now shutdown event is not redefined in hook. So, server is not stopped on portlet undeploy
 * @author akakunin
 *
 */
public class LDAPShutdownAction extends SimpleAction {
	private static Log log = LogFactoryUtil.getLog(LDAPShutdownAction.class);

	@Override
	public void run(String[] ids) throws ActionException {
		try {
			if (LDAPLocalServiceUtil.isLDAPServerStarted()) {
				log.info("Stopping LDAP Server");
				LDAPLocalServiceUtil.stopLDAPServer();
			}
		} catch (SystemException ex) {
			log.warn("Cannot stop LDAP service on shutdown: " + ex.getMessage());
		}
		
	}

}
