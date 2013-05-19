package ru.emdev.ldap.service;

import com.liferay.portal.service.InvokableLocalService;


public class LDAPLocalServiceClp implements LDAPLocalService {
    private InvokableLocalService _invokableLocalService;
    private String _methodName0;
    private String[] _methodParameterTypes0;
    private String _methodName1;
    private String[] _methodParameterTypes1;
    private String _methodName3;
    private String[] _methodParameterTypes3;
    private String _methodName4;
    private String[] _methodParameterTypes4;
    private String _methodName5;
    private String[] _methodParameterTypes5;

    public LDAPLocalServiceClp(InvokableLocalService invokableLocalService) {
        _invokableLocalService = invokableLocalService;

        _methodName0 = "getBeanIdentifier";

        _methodParameterTypes0 = new String[] {  };

        _methodName1 = "setBeanIdentifier";

        _methodParameterTypes1 = new String[] { "java.lang.String" };

        _methodName3 = "startLDAPServer";

        _methodParameterTypes3 = new String[] {  };

        _methodName4 = "stopLDAPServer";

        _methodParameterTypes4 = new String[] {  };

        _methodName5 = "isLDAPServerStarted";

        _methodParameterTypes5 = new String[] {  };
    }

    public java.lang.String getBeanIdentifier() {
        Object returnObj = null;

        try {
            returnObj = _invokableLocalService.invokeMethod(_methodName0,
                    _methodParameterTypes0, new Object[] {  });
        } catch (Throwable t) {
            t = ClpSerializer.translateThrowable(t);

            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() +
                    " is not a valid exception");
            }
        }

        return (java.lang.String) ClpSerializer.translateOutput(returnObj);
    }

    public void setBeanIdentifier(java.lang.String beanIdentifier) {
        try {
            _invokableLocalService.invokeMethod(_methodName1,
                _methodParameterTypes1,
                new Object[] { ClpSerializer.translateInput(beanIdentifier) });
        } catch (Throwable t) {
            t = ClpSerializer.translateThrowable(t);

            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() +
                    " is not a valid exception");
            }
        }
    }

    public java.lang.Object invokeMethod(java.lang.String name,
        java.lang.String[] parameterTypes, java.lang.Object[] arguments)
        throws java.lang.Throwable {
        throw new UnsupportedOperationException();
    }

    public void startLDAPServer()
        throws com.liferay.portal.kernel.exception.SystemException {
        try {
            _invokableLocalService.invokeMethod(_methodName3,
                _methodParameterTypes3, new Object[] {  });
        } catch (Throwable t) {
            t = ClpSerializer.translateThrowable(t);

            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }

            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() +
                    " is not a valid exception");
            }
        }
    }

    public void stopLDAPServer()
        throws com.liferay.portal.kernel.exception.SystemException {
        try {
            _invokableLocalService.invokeMethod(_methodName4,
                _methodParameterTypes4, new Object[] {  });
        } catch (Throwable t) {
            t = ClpSerializer.translateThrowable(t);

            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }

            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() +
                    " is not a valid exception");
            }
        }
    }

    public boolean isLDAPServerStarted() {
        Object returnObj = null;

        try {
            returnObj = _invokableLocalService.invokeMethod(_methodName5,
                    _methodParameterTypes5, new Object[] {  });
        } catch (Throwable t) {
            t = ClpSerializer.translateThrowable(t);

            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() +
                    " is not a valid exception");
            }
        }

        return ((Boolean) returnObj).booleanValue();
    }
}
