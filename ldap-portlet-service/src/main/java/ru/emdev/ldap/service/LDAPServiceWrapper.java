package ru.emdev.ldap.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link LDAPService}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       LDAPService
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
    public java.lang.String getBeanIdentifier() {
        return _ldapService.getBeanIdentifier();
    }

    /**
    * Sets the Spring bean ID for this bean.
    *
    * @param beanIdentifier the Spring bean ID for this bean
    */
    public void setBeanIdentifier(java.lang.String beanIdentifier) {
        _ldapService.setBeanIdentifier(beanIdentifier);
    }

    public java.lang.Object invokeMethod(java.lang.String name,
        java.lang.String[] parameterTypes, java.lang.Object[] arguments)
        throws java.lang.Throwable {
        return _ldapService.invokeMethod(name, parameterTypes, arguments);
    }

    /**
     * @deprecated Renamed to {@link #getWrappedService}
     */
    public LDAPService getWrappedLDAPService() {
        return _ldapService;
    }

    /**
     * @deprecated Renamed to {@link #setWrappedService}
     */
    public void setWrappedLDAPService(LDAPService ldapService) {
        _ldapService = ldapService;
    }

    public LDAPService getWrappedService() {
        return _ldapService;
    }

    public void setWrappedService(LDAPService ldapService) {
        _ldapService = ldapService;
    }
}
