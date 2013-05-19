package ru.emdev.ldap.service.base;

import ru.emdev.ldap.service.LDAPLocalServiceUtil;

import java.util.Arrays;


public class LDAPLocalServiceClpInvoker {
    private String _methodName20;
    private String[] _methodParameterTypes20;
    private String _methodName21;
    private String[] _methodParameterTypes21;
    private String _methodName24;
    private String[] _methodParameterTypes24;
    private String _methodName25;
    private String[] _methodParameterTypes25;
    private String _methodName26;
    private String[] _methodParameterTypes26;

    public LDAPLocalServiceClpInvoker() {
        _methodName20 = "getBeanIdentifier";

        _methodParameterTypes20 = new String[] {  };

        _methodName21 = "setBeanIdentifier";

        _methodParameterTypes21 = new String[] { "java.lang.String" };

        _methodName24 = "startLDAPServer";

        _methodParameterTypes24 = new String[] {  };

        _methodName25 = "stopLDAPServer";

        _methodParameterTypes25 = new String[] {  };

        _methodName26 = "isLDAPServerStarted";

        _methodParameterTypes26 = new String[] {  };
    }

    public Object invokeMethod(String name, String[] parameterTypes,
        Object[] arguments) throws Throwable {
        if (_methodName20.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes20, parameterTypes)) {
            return LDAPLocalServiceUtil.getBeanIdentifier();
        }

        if (_methodName21.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes21, parameterTypes)) {
            LDAPLocalServiceUtil.setBeanIdentifier((java.lang.String) arguments[0]);
        }

        if (_methodName24.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes24, parameterTypes)) {
            LDAPLocalServiceUtil.startLDAPServer();
        }

        if (_methodName25.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes25, parameterTypes)) {
            LDAPLocalServiceUtil.stopLDAPServer();
        }

        if (_methodName26.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes26, parameterTypes)) {
            return LDAPLocalServiceUtil.isLDAPServerStarted();
        }

        throw new UnsupportedOperationException();
    }
}
