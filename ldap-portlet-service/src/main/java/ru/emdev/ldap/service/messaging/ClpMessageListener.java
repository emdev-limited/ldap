package ru.emdev.ldap.service.messaging;

import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;

import ru.emdev.ldap.service.ClpSerializer;
import ru.emdev.ldap.service.LDAPLocalServiceUtil;
import ru.emdev.ldap.service.LDAPServiceUtil;


public class ClpMessageListener extends BaseMessageListener {
    public static String getServletContextName() {
        return ClpSerializer.getServletContextName();
    }

    @Override
    protected void doReceive(Message message) throws Exception {
        String command = message.getString("command");
        String servletContextName = message.getString("servletContextName");

        if (command.equals("undeploy") &&
                servletContextName.equals(getServletContextName())) {
            LDAPLocalServiceUtil.clearService();

            LDAPServiceUtil.clearService();
        }
    }
}
