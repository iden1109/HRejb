package com.gseo.zk.util;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * It is for binding EJB via JNDI
 * @author Zhengku
 * @since Sep 26, 2014
 */
public class EJBFactory {
	
	private static final String _serviceAddr = "127.0.0.1";
	private static final String _port = "1099";
	
	public static Object getEJB(String addr, String port, String jndipath) {
        try {
            Properties props = new Properties();
            props.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
            props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
            props.put("java.naming.provider.url", addr+":"+port);
            
            InitialContext ctx = new InitialContext(props);
            return ctx.lookup(jndipath);
            
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static Object getEJB(String jndipath) {
        return getEJB(_serviceAddr, _port, jndipath);
    }
}
