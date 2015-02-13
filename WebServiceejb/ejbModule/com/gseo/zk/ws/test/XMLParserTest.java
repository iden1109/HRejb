package com.gseo.zk.ws.test;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;

import com.dsc.nana.util.logging.NaNaLog;
import com.dsc.nana.util.logging.NaNaLogFactory;
import com.gseo.zk.ws.XML;

public class XMLParserTest {

	private static NaNaLog _log;
	
	public XMLParserTest(){
		if(this._log == null){
			this._log = NaNaLogFactory.getLog(XMLParserTest.class);
		}
	}
	
	public static void main(String[] args) {
		File f = new File("ejbModule/OutofTime.xml");
		Document doc = new XML(_log).getXMLDoc(f);

		StringBuilder sb = new StringBuilder();
		Element root = doc.getRootElement();
		Element status = root.element("Execution").element("Status");
		if(status.attribute("Code").getText().equalsIgnoreCase("-1")){
			System.out.println(status.attribute("description").getText());
			
			if(status.attribute("description").getText().indexOf("等候操作已逾時") > 0)
				System.out.println("BINGO !!");
		}
		
		/*
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
		*/
	}

}
