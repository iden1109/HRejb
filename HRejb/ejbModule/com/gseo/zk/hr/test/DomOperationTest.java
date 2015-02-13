package com.gseo.zk.hr.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * It is a console test. 
 * It will read a file from ejbModule/fieldValues.xml and append "LevelName" and "isManager" tag on it, finally it will print them all on the console.
 * @author t1863
 * @since Sep 30, 2014
 */
public class DomOperationTest {

	public static void main(String[] args) {
		SAXReader reader = new SAXReader(); 
		Document doc = null;
		Element root = null;
		File f = new File("ejbModule/fieldValues.xml");

		StringBuilder sb = new StringBuilder();
		sb.append("<ESSF17>");
		sb.append("<ESSXJ001 id='ESSXJ001' dataType='java.lang.String'>ESSF17</ESSXJ001>");
		sb.append("<ESSXJ002 id='ESSXJ002' dataType='java.lang.String'>201409190002</ESSXJ002>");
		sb.append("</ESSF17>");
		
		try {
			doc = reader.read(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
			root = doc.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		root.addElement("LevelName").addAttribute("id", "LevelName").addAttribute("dataType", "java.lang.Integer").setText("12");
		root.addElement("isManager").addAttribute("id", "isManager").addAttribute("dataType", "java.lang.String").setText("Y");
		System.out.println(doc.asXML());
	}

}
