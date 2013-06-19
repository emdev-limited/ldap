package ru.emdev.ldap.event;

import ru.emdev.ldap.service.LDAPLocalServiceUtil;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class LDAPControl {
	private static Log log = LogFactoryUtil.getLog(LDAPShutdownAction.class);
	
	public void cleanUp() {
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
