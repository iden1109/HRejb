package com.gseo.zk.hr;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import javax.ejb.EJBException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.dsc.nana.util.logging.NaNaLog;
import com.gseo.zk.util.LogUtil;

/**
 * XML DOM object generator
 * @author Zhengku
 * @since Oct 7, 2014
 *
 */
public class XML {

	private NaNaLog _log;
	
	public XML(NaNaLog log) {
		this._log = log;
	}

	
	/**
	 * Generate a XML document object via XML string
	 * @param xml  string of XML structure
	 * @return Document  XML Document object
	 */
	public Document getXMLDoc(String xml){
		SAXReader reader = new SAXReader(); 
		Document doc;
		try {
			doc = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));	
		} catch (DocumentException e1) {
			LogUtil.logError(_log, "XML object operation: " + e1.getMessage());
			throw new EJBException(e1.getMessage());
		} catch (UnsupportedEncodingException e) {
			LogUtil.logError(_log, "XML unsupported encoding: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}
		
		return doc;
	}
}
