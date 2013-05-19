package ru.emdev.ldap.service.base;

import ru.emdev.ldap.service.LDAPServiceUtil;

import java.util.Arrays;


public class LDAPServiceClpInvoker {
    private String _methodName20;
    private String[] _methodParameterTypes20;
    private String _methodName21;
    private String[] _methodParameterTypes21;

    public LDAPServiceClpInvoker() {
        _methodName20 = "getBeanIdentifier";

        _methodParameterTypes20 = new String[] {  };

        _methodName21 = "setBeanIdentifier";

        _methodParameterTypes21 = new String[] { "java.lang.String" };
    }

    public Object invokeMethod(String name, String[] parameterTypes,
        Object[] arguments) throws Throwable {
        if (_methodName20.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes20, parameterTypes)) {
            return LDAPServiceUtil.getBeanIdentifier();
        }

        if (_methodName21.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes21, parameterTypes)) {
            LDAPServiceUtil.setBeanIdentifier((java.lang.String) arguments[0]);
        }

        throw new UnsupportedOperationException();
    }
}
