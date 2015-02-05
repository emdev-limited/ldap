package ru.emdev.ldap.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link LDAPLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see LDAPLocalService
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
    @Override
    public java.lang.String getBeanIdentifier() {
        return _ldapLocalService.getBeanIdentifier();
    }

    /**
    * Sets the Spring bean ID for this bean.
    *
    * @param beanIdentifier the Spring bean ID for this bean
    */
    @Override
    public void setBeanIdentifier(java.lang.String beanIdentifier) {
        _ldapLocalService.setBeanIdentifier(beanIdentifier);
    }

    @Override
    public java.lang.Object invokeMethod(java.lang.String name,
        java.lang.String[] parameterTypes, java.lang.Object[] arguments)
        throws java.lang.Throwable {
        return _ldapLocalService.invokeMethod(name, parameterTypes, arguments);
    }

    @Override
    public void startLDAPServer()
        throws com.liferay.portal.kernel.exception.SystemException {
        _ldapLocalService.startLDAPServer();
    }

    @Override
    public void stopLDAPServer()
        throws com.liferay.portal.kernel.exception.SystemException {
        _ldapLocalService.stopLDAPServer();
    }

    @Override
    public boolean isLDAPServerStarted() {
        return _ldapLocalService.isLDAPServerStarted();
    }

    /**
     * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
     */
    public LDAPLocalService getWrappedLDAPLocalService() {
        return _ldapLocalService;
    }

    /**
     * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
     */
    public void setWrappedLDAPLocalService(LDAPLocalService ldapLocalService) {
        _ldapLocalService = ldapLocalService;
    }

    @Override
    public LDAPLocalService getWrappedService() {
        return _ldapLocalService;
    }

    @Override
    public void setWrappedService(LDAPLocalService ldapLocalService) {
        _ldapLocalService = ldapLocalService;
    }
}
