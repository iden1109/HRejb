package com.gseo.zk.hr.test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.dsc.nana.util.logging.NaNaLog;
import com.dsc.nana.util.logging.NaNaLogFactory;
import com.gseo.zk.hr.XML;

public class XMLParserTest {

	private static NaNaLog _log;
	
	public XMLParserTest(){
		if(this._log == null){
			this._log = NaNaLogFactory.getLog(XMLParserTest.class);
		}
	}
	
	public static void main(String[] args) {
		testAuditForESS();
	}
	
	private static void testAuditForESS() {
		File f = new File("ejbModule/AuditForESS.xml");
		Document doc = new XML(_log).getXMLDoc(f);

		String serialNum = "", empId = "";
		
		Element root = doc.getRootElement();
		Element parameters = root.element("RequestContent").element("Parameters");
		Iterator it = parameters.elementIterator();
		List listParams = parameters.elements();
		for(int i=0;i<listParams.size();i++){
			Element param = (Element)listParams.get(i);
			if(i == 1)
				serialNum = param.getText();
			if(i == 4)
				empId = param.getText();
			
			System.out.println(param.getText());
		}
		System.out.println("serialNum = "+serialNum);
		System.out.println("empId = "+empId);
	}

	
	private static void testESSF50() {
		File f = new File("ejbModule/ESSF50.xml");
		Document doc = new XML(_log).getXMLDoc(f);

		StringBuilder sb = new StringBuilder();
		Element root = doc.getRootElement();
		Element recds = root.element("d01").element("records");
		Iterator it = recds.elementIterator();
		while (it.hasNext()) {
			Element rec = (Element) it.next();
			Iterator itt = rec.elementIterator();
			while (itt.hasNext()) {
				Element item = (Element) itt.next();
				String id = item.attribute("id").getText();
				String txt = item.getText();
				if(id.equals("ESSBGRY005")){
					sb.append("'"+txt+"'").append(",");
				}
			}
		}
		if(sb.length()>0)
			sb.delete(sb.length()-1, sb.length());
		System.out.println("XML="+sb.toString());
		
		String empId = "";
		if(sb.indexOf(",")>0){
			empId = sb.substring(1, sb.indexOf(",")-1);
		}else{
			empId = sb.substring(1, 6);
		}
		System.out.println("emp id="+empId);
	}

}
