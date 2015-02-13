package com.gseo.zk.hr.test;

import static org.junit.Assert.*;

import java.io.File;
import java.rmi.RemoteException;

import javax.rmi.PortableRemoteObject;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gseo.zk.hr.GoBusinessTrip;
import com.gseo.zk.hr.GoBusinessTripHome;
import com.gseo.zk.util.EJBFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class GoBusinessTripBeanUnitTest {

	protected static GoBusinessTrip bean;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Object ref = EJBFactory.getEJB("GoBusinessTrip");		
		GoBusinessTripHome home = (GoBusinessTripHome) PortableRemoteObject.narrow(ref, GoBusinessTripHome.class);
		bean = home.create();
	}

	@Test
	public void testAuditForESS() {
		try {
			//Document doc = getXMLString("ejbModule/AuditForESS.xml");
			String result = bean.auditForESS("http://192.168.100.108/hrws/Integration.asmx", "HRMDS", "201410270012", "T0002");
			assertEquals("Test testAuditForESS() success", "[0301]", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	
	private Document getXMLString(String path) {	
		SAXReader reader = new SAXReader(); 
		Document doc = null;
		try {
			doc = reader.read(new File(path));	
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		return doc;
	}
}
