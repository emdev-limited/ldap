package ru.emdev.ldap.event;

import ru.emdev.ldap.service.LDAPLocalServiceUtil;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class LDAPStartUpAction extends SimpleAction {
	private static Log log = LogFactoryUtil.getLog(LDAPStartUpAction.class);

	@Override
	public void run(String[] ids) throws ActionException {
		try {
			if (!LDAPLocalServiceUtil.isLDAPServerStarted()) {
				log.info("Starting LDAP Server");
				LDAPLocalServiceUtil.startLDAPServer();
			}
		} catch (SystemException ex) {
			log.warn("Cannot start LDAP service on startup: " + ex.getMessage());
		}
		
	}

}
