package com.gseo.zk.hr.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Scanner;

import javax.rmi.PortableRemoteObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gseo.zk.hr.Leave;
import com.gseo.zk.hr.LeaveHome;
import com.gseo.zk.hr.model.Assist;
import com.gseo.zk.hr.model.OverTime;
import com.gseo.zk.util.EJBFactory;

/**
 * It is a unit test case for Levae EJB
 * @author Zhengku
 * @since Sep 29, 2014
 */
public class LeaveBeanUnitTest {

	protected static Leave bean;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
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
			assertEquals("Test testRetrieveLevelName() success", "<99999><N><外勞><T0629><T0278>", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRetrieveLevel1LeaderCode() {
		try {
			String result = bean.retrieveLevel1LeaderCode("T0289");  //request user:T0289 -> direct:T0359, level1:T1738, level2:T0810
			assertEquals("Test testRetrieveLevel1LeaderCode() success", "T0359<--->T0278", result);
			assertNotEquals("Test testRetrieveLevel1LeaderCode() success", "T1738<--->T0810", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRetrieveForOvertime() {
		try {
			Document doc = getXMLString("ejbModule/ESSF04G_ForOverTime.xml");
			//OverTime ot = bean.retrieveForOvertime("NaNaDS", doc.asXML(), "APPFORMPROCESSPKG_ESSF04G00000064");
			OverTime ot = bean.retrieveForOvertime("NaNaDS", doc.asXML());
			String result = "<"+ot.getLevelValue()+"><"+ot.getLevel1LeaderCode()+"><"+ot.getLevel2LeaderCode()+"><"+"><"+ot.getDirInDir()+"><"+ot.getDeptDescribe()+"><"+ot.getDirectLeaderCodes()+"><"+ot.getOvertimeName()+"><"+ot.getOvertimeHours()+"><"+ot.getOvertimeReason()+">";
			assertEquals("Test testRetrieveForOvertime() success", "<10000><T0557><T0278><><直接><課級部門><T0278;T0359;T0557;T0629><高志濠..><2.0..><8784新模配件整理..>", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	@Test
	public void testRetrieveForOvertime_throws_IllegalArgumentException_for_xml() {
		try {
			thrown.expect(IllegalArgumentException.class);
			thrown.expectMessage("xml cannot be empty !");
			String result = bean.retrieveForOvertime("NaNaDS", "");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	 */
	
	@Test
	public void testRetrieveForChangeWorkshift() {
		try {
			Document doc = getXMLString("ejbModule/ESSF50.xml");
			OverTime ot = bean.retrieveForChangeWorkshift("NaNaDS", doc.asXML());
			String result = "<"+ot.getLevelValue()+"><"+ot.getLevel1LeaderCode()+"><"+ot.getLevel2LeaderCode()+"><"+"><"+ot.getDirInDir()+"><"+ot.getDeptDescribe()+"><"+ot.getDirectLeaderCodes()+">";
			assertEquals("Test testRetrieveForChangeWorkshift() success", "<11000><T0084><T0141><><間接><部級部門><T0084;T1013>", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRetrieveAssistant() {
		try {
			Assist ass = bean.retrieveAssistant("HRMDS", "T0056");
			String result = ass.getAssist();
			assertEquals("Test testRetrieveAssistant() success", "T0128", result);
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
