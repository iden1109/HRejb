package com.gseo.zk.ws.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.ejb.CreateException;
import javax.rmi.PortableRemoteObject;

import junit.framework.Assert;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gseo.zk.ws.WebServiceClient;
import com.gseo.zk.ws.WebServiceClientHome;
import com.gseo.zk.util.EJBFactory;

/**
 * It is a unit test case for WebServiceClient EJB
 * @author Zhengku
 * @since Sep 30, 2014
 *
 */
public class WSClientBeanUnitTest {

	private static Object ref;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ref = EJBFactory.getEJB("WebServiceClient");

	}
	
	@Test
	public void testCallWebService(){

		String xml = getXMLString();
		
		WebServiceClientHome home = (WebServiceClientHome) PortableRemoteObject.narrow(ref, WebServiceClientHome.class);
		WebServiceClient bean;
		try {
			bean = home.create();
			Map params = new HashMap();
			//params.put("pXml", xml);
			//params.put("language", "1");
			String result = bean.callWebService("http://192.168.100.108/hrws/Integration.asmx", "http://dcms.com.cn/core/GeneralService/IntegrationByXmlWithLanguageEx", "IntegrationByXmlWithLanguageEx", params);
			System.out.println("Result : "+result);
			String code = result.substring(result.indexOf("Code=")+6, result.indexOf("description=")).replaceAll("\"", "").trim();
			System.out.println("code: " + code);
			assertEquals("IntegrationByXmlWithLanguageEx service calls successfully", "-1", code);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testIntegrationByXmlWithLanguageEx() {
		
		String xml = getXMLString();
		
		WebServiceClientHome home = (WebServiceClientHome) PortableRemoteObject.narrow(ref, WebServiceClientHome.class);
		WebServiceClient bean;
		try {
			bean = home.create();
			String result = bean.IntegrationByXmlWithLanguageEx(xml);
			System.out.println("Result : "+result);
			//String code = result.substring(result.indexOf("Code=")+6, result.indexOf("description=")).replaceAll("\"", "").trim();
			//System.out.println("code: " + code);
			assertEquals("IntegrationByXmlWithLanguageEx service calls successfully", "-1", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
		
	}

	
	private String getXMLString() {
		String xml = "";
		Scanner scanner;
		try {
			scanner = new Scanner(new File("ejbModule/WSTest1.xml")).useDelimiter("\\Z");
			xml = scanner.next();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return xml;
	}

}
