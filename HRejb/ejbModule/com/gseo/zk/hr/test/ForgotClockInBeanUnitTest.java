package com.gseo.zk.hr.test;

import static org.junit.Assert.*;

import java.io.File;
import java.rmi.RemoteException;

import javax.rmi.PortableRemoteObject;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gseo.zk.hr.ForgotClockIn;
import com.gseo.zk.hr.ForgotClockInHome;
import com.gseo.zk.util.EJBFactory;


public class ForgotClockInBeanUnitTest {

	protected static ForgotClockIn bean;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Object ref = EJBFactory.getEJB("ForgotClockIn");		
		ForgotClockInHome home = (ForgotClockInHome) PortableRemoteObject.narrow(ref, ForgotClockInHome.class);
		bean = home.create();
	}

	@Test
	public void testSaveForESS() {
		try {
			//Document doc = getXMLString("ejbModule/AuditForESS.xml");
			String result = bean.saveForESS("http://192.168.100.108/hrws/Integration.asmx", "NaNaDS", "TWHR_FC01_clone00000197");
			assertEquals("Test testSaveForESS() success", "[0]", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
