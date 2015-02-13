package com.gseo.zk.hr.test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.rmi.PortableRemoteObject;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gseo.zk.hr.Leave;
import com.gseo.zk.hr.LeaveHome;
import com.gseo.zk.util.EJBFactory;

/**
 * It is a unit test case for Levae EJB
 * @author Zhengku
 * @since Sep 29, 2014
 */
public class LeaveBeanUnitTest {

	protected static Leave bean;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Object ref = EJBFactory.getEJB("Leave");
		//ref = EJBFactory.getEJB("192.168.100.105", "1099", "Leave");
		
		LeaveHome home = (LeaveHome) PortableRemoteObject.narrow(ref, LeaveHome.class);
		bean = home.create();
	}

	@Test
	public void testFoo() {
		try {
			String result = bean.foo("123");
			assertEquals("Test foo() success", "123", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRetrieveLevelName() {
		try {
			String result = bean.retrieveLevelName("NaNaDS", "T1596", "TB110");
			assertEquals("Test testRetrieveLevelName() success", "<99><N><外勞><T0629><T1738>", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRetrieveLevel1LeaderCode() {
		try {
			String result = bean.retrieveLevel1LeaderCode("T0289");  //request user:T0289 -> direct:T0359, level1:T1738, level2:T0810
			assertEquals("Test testRetrieveLevel1LeaderCode() success", "T0359<--->T1738", result);
			assertNotEquals("Test testRetrieveLevel1LeaderCode() success", "T1738<--->T0810", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

}
