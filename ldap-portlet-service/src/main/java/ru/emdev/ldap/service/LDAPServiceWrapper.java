package ru.emdev.ldap.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link LDAPService}.
 *
 * @author Brian Wing Shun Chan
 * @see LDAPService
 * @generated
 */
public class LDAPServiceWrapper implements LDAPService,
    ServiceWrapper<LDAPService> {
    private LDAPService _ldapService;

    public LDAPServiceWrapper(LDAPService ldapService) {
        _ldapService = ldapService;
    }

    /**
    * Returns the Spring bean ID for this bean.
    *
    * @return the Spring bean ID for this bean
    */
    @Override
    public java.lang.String getBeanIdentifier() {
        return _ldapService.getBeanIdentifier();
    }

    /**
    * Sets the Spring bean ID for this bean.
    *
    * @param beanIdentifier the Spring bean ID for this bean
    */
    @Override
    public void setBeanIdentifier(java.lang.String beanIdentifier) {
        _ldapService.setBeanIdentifier(beanIdentifier);
    }

    @Override
    public java.lang.Object invokeMethod(java.lang.String name,
        java.lang.String[] parameterTypes, java.lang.Object[] arguments)
        throws java.lang.Throwable {
        return _ldapService.invokeMethod(name, parameterTypes, arguments);
    }

    /**
     * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
     */
    public LDAPService getWrappedLDAPService() {
        return _ldapService;
    }

    /**
     * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
     */
    public void setWrappedLDAPService(LDAPService ldapService) {
        _ldapService = ldapService;
    }

    @Override
    public LDAPService getWrappedService() {
        return _ldapService;
    }

    @Override
    public void setWrappedService(LDAPService ldapService) {
        _ldapService = ldapService;
    }
}
