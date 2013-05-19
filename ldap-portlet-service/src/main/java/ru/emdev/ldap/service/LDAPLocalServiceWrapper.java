package ru.emdev.ldap.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link LDAPLocalService}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       LDAPLocalService
 * @generated
 */
public class LDAPLocalServiceWrapper implements LDAPLocalService,
    ServiceWrapper<LDAPLocalService> {
    private LDAPLocalService _ldapLocalService;

    public LDAPLocalServiceWrapper(LDAPLocalService ldapLocalService) {
        _ldapLocalService = ldapLocalService;
    }

    /**
    * Returns the Spring bean ID for this bean.
    *
    * @return the Spring bean ID for this bean
    */
    public java.lang.String getBeanIdentifier() {
        return _ldapLocalService.getBeanIdentifier();
    }

    /**
    * Sets the Spring bean ID for this bean.
    *
    * @param beanIdentifier the Spring bean ID for this bean
    */
    public void setBeanIdentifier(java.lang.String beanIdentifier) {
        _ldapLocalService.setBeanIdentifier(beanIdentifier);
    }

    public java.lang.Object invokeMethod(java.lang.String name,
        java.lang.String[] parameterTypes, java.lang.Object[] arguments)
        throws java.lang.Throwable {
        return _ldapLocalService.invokeMethod(name, parameterTypes, arguments);
    }

    public void startLDAPServer()
        throws com.liferay.portal.kernel.exception.SystemException {
        _ldapLocalService.startLDAPServer();
    }

    public void stopLDAPServer()
        throws com.liferay.portal.kernel.exception.SystemException {
        _ldapLocalService.stopLDAPServer();
    }

    public boolean isLDAPServerStarted() {
        return _ldapLocalService.isLDAPServerStarted();
    }

    /**
     * @deprecated Renamed to {@link #getWrappedService}
     */
    public LDAPLocalService getWrappedLDAPLocalService() {
        return _ldapLocalService;
    }

    /**
     * @deprecated Renamed to {@link #setWrappedService}
     */
    public void setWrappedLDAPLocalService(LDAPLocalService ldapLocalService) {
        _ldapLocalService = ldapLocalService;
    }

    public LDAPLocalService getWrappedService() {
        return _ldapLocalService;
    }

    public void setWrappedService(LDAPLocalService ldapLocalService) {
        _ldapLocalService = ldapLocalService;
    }
}
