package com.gseo.zk.ws.test;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import cn.com.dcms.core.generalservice.IntegrationStub;
import cn.com.dcms.core.generalservice.IntegrationStub.IntegrationByXmlWithLanguageEx;
import cn.com.dcms.core.generalservice.IntegrationStub.IntegrationByXmlWithLanguageExResponse;

/**
 * It is a console test. Axis2 ADB solution
 * It will read a xml from ejbModule/WSTest1.xml. To call the web service of http://192.168.100.108/hrws/Integration.asmx. 
 * it will print the xml information of request and response on the console.
 * 
 * @author t1863
 * @since Oct 1, 2014
 *
 */
public class WSClientADBTest {

	public static void main(String[] args) {
		IntegrationStub stub;
		try {
			File f = new File("ejbModule/WSTest3.xml");
			SAXReader reader = new SAXReader(); 
			Document doc = reader.read(f);
			System.out.println("XML: "+doc.asXML());
			
			//Official WebService: 
			//Test WebService: http://192.168.100.108/hrws/Integration.asmx
			stub = new IntegrationStub("http://192.168.100.108/hrws/Integration.asmx");
			stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
			IntegrationByXmlWithLanguageEx req = new IntegrationByXmlWithLanguageEx();

			req.setPXml(doc.asXML());
			req.setLanguage("1");
			String result = stub.integrationByXmlWithLanguageEx(req).getIntegrationByXmlWithLanguageExResult();
			System.out.println("Result: " + result);
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

}
