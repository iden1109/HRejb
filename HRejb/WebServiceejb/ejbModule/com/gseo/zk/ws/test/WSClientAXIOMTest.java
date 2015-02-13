package com.gseo.zk.ws.test;

import java.io.File;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * It is a console test. Axis2 AXIOM solution
 * It will read a xml from ejbModule/WSTest1.xml. To call the web service of http://192.168.100.108/hrws/Integration.asmx. 
 * it will print the xml information of request and response on the console.
 * 
 * @author Zhengku
 * @since Oct 2, 2014
 */
public class WSClientAXIOMTest {
	
	public static void main(String[] args) {
		
		File f = new File("ejbModule/WSTest1.xml");
		SAXReader reader = new SAXReader(); 
		Document doc = null;
		try {
			doc = reader.read(f);
			System.out.println("XML: "+doc.asXML());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		ServiceClient client;
		try {
			client = new ServiceClient();
			Options opts = new Options(); 
			opts.setTo(new EndpointReference("http://192.168.100.108/hrws/Integration.asmx")); 
			opts.setAction("http://dcms.com.cn/core/GeneralService/IntegrationByXmlWithLanguageEx");
			client.setOptions(opts); 
			
			OMElement result = client.sendReceive(prepareParameters(doc.asXML(), "1"));
			System.out.println("result text : "+ result.getFirstElement().getText());
		} catch (AxisFault e1) {
			e1.printStackTrace();
		}

	}
	
	public static OMElement prepareParameters(String xml, String language) { 
		OMFactory fac = OMAbstractFactory.getOMFactory(); 
		OMNamespace omNs = fac.createOMNamespace("http://dcms.com.cn/core/GeneralService", "gseo");
		OMElement method = fac.createOMElement("IntegrationByXmlWithLanguageEx", omNs);
		OMElement value;
		
		value = fac.createOMElement("pXml", omNs);
		value.setText(xml);
		method.addChild(value);
		
		value = fac.createOMElement("language", omNs);
		value.setText(language);
		method.addChild(value);
		
		return method;
	}

}
