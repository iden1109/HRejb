/**
 * 
 */
package com.gseo.zk.ws;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.dom4j.Document;

import com.dsc.nana.util.logging.NaNaLog;
import com.dsc.nana.util.logging.NaNaLogFactory;
import com.gseo.zk.util.LogUtil;

import cn.com.dcms.core.generalservice.IntegrationStub;
import cn.com.dcms.core.generalservice.IntegrationStub.IntegrationByXmlWithLanguageEx;


/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean.
 *  It is a WebService client to call web service in the outside.
 *  @author Zhengku
 *  @since Oct 1, 2014
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="WebServiceClient"	
 *           description="An EJB named WebServiceClient"
 *           display-name="WebServiceClient"
 *           jndi-name="WebServiceClient"
 *           type="Stateless" 
 *           transaction-type="Container"
 * 
 * <!-- end-xdoclet-definition --> 
 * @generated
 */

public abstract class WebServiceClientBean implements javax.ejb.SessionBean {

	/** 
	 * <!-- begin-xdoclet-definition --> 
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	
	private SessionContext _sessionCtx;
	private NaNaLog _log;

	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.create-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 */
	public void ejbCreate() {
	}

	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 */
	public String foo(String param) {
		return null;
	}
	
	/** 
	 * Call Web service on outside server
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * @param address  ex: "http://192.168.100.108/hrws/Integration.asmx"
	 * @param soapAction  ex: "http://dcms.com.cn/core/GeneralService/IntegrationByXmlWithLanguageEx"
	 * @param method  ex: "IntegrationByXmlWithLanguageEx"
	 * @param params  the pair data of key and value to send (becomes the content of SOAP body)
	 * <pre>{@code
	 * ex:
	 * Map params = new HashMap();
	 * params.put("pXml", "<Request><Access></Access><RequestContent></RequestContent></Request>");
	 * params.put("language", "1");
	 * }</pre>
	 * @return String  Web service response XML information
	 */
	public String callWebService(String address, String soapAction, String method, Map params) {
		if(address == null || address.equals("")){
			LogUtil.logWarning(_log, "Parameter: address cannot be empty !");
			return responseMessage("Parameter: address cannot be empty !");
		}
		if(soapAction == null || soapAction.equals("")){
			LogUtil.logWarning(_log, "Parameter: soapAction cannot be empty !");
			return responseMessage("Parameter: soapAction cannot be empty !");
		}
		if(method == null || method.equals("")){
			LogUtil.logWarning(_log, "Parameter: method cannot be empty !");
			return responseMessage("Parameter: method cannot be empty !");
		}
		if(params == null || params.isEmpty()){
			LogUtil.logWarning(_log, "Parameter: params cannot be empty !");
			return responseMessage("Parameter: params cannot be empty !");
		}
		
		ServiceClient client;
		try {
			client = new ServiceClient();
			Options opts = new Options();
			opts.setTo(new EndpointReference(address));
			opts.setAction(soapAction);
			client.setOptions(opts);
			OMElement result = client.sendReceive(fillParameters(soapAction, method, params));
			
			_log.debug("result : "+ result.getFirstElement().getText());
			return result.getFirstElement().getText();
		} catch (AxisFault e) {
			LogUtil.logError(_log, "Axis2 fail.ErrMsg: " + e.getMessage());
			//throw new EJBException(e.getMessage());
			return responseMessage("Axis2 fail.ErrMsg: " + e.getMessage());
		}
		
	}
	
	/** 
	 * Call method "IntegrationByXmlWithLanguageEx" of "http://192.168.100.108/hrws/Integration.asmx" web service
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * @param xml  the string of XML structure
	 * @return String  Web service response XML information
	 */
	public String IntegrationByXmlWithLanguageEx(String xml) {
		return IntegrationByXmlWithLanguageEx("http://192.168.100.108/hrws/Integration.asmx", xml, "1");// default 1 is Chinese Tradition
	}
	
	/** 
	 * Call method "IntegrationByXmlWithLanguageEx" on specifying the address of the service
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * @param address  ex: "http://192.168.100.108/hrws/Integration.asmx"
	 * @param xml  the string of XML structure
	 * @return String  Web service response XML information
	 */
	public String IntegrationByXmlWithLanguageEx(String address, String xml) {
		return IntegrationByXmlWithLanguageEx(address, xml, "1");// default 1 is Chinese Tradition
	}
	
	/** 
	 * Call method "IntegrationByXmlWithLanguageEx" on specifying the address of the service
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * @param address  ex: "http://192.168.100.108/hrws/Integration.asmx"
	 * @param xml  the string of XML structure
	 * @param language  0=Chinese Simplify; 1=Chinese Tradition; 2=English (always pass 1 if you have no idea)
	 * @return String  Web service response XML information
	 */
	public String IntegrationByXmlWithLanguageEx(String address, String xml, String language){
		if(address == null || address.equals("")){
			//throw new IllegalArgumentException("Parameter: address cannot be empty !");
			LogUtil.logWarning(_log, "Parameter: address cannot be empty !");
			return responseMessage("Parameter: address cannot be empty !");
		}
		if(xml == null || xml.equals("")){
			//throw new IllegalArgumentException("Parameter: XML cannot be empty !");
			LogUtil.logWarning(_log, "Parameter: xml cannot be empty !");
			return responseMessage("Parameter: xml cannot be empty !");
		}
		if(language == null || language.equals("")){
			//throw new IllegalArgumentException("Parameter: language cannot be empty !");
			LogUtil.logWarning(_log, "Parameter: language cannot be empty !");
			return responseMessage("Parameter: language cannot be empty !");
		}
		
		_log.debug("Request XML: " + xml);
		Document doc = new XML(_log).getXMLDoc(xml);
		
		String result = "";
		try {
			IntegrationStub stub = new IntegrationStub(address);
			stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);			
			IntegrationByXmlWithLanguageEx req = new IntegrationByXmlWithLanguageEx();
			req.setPXml(doc.asXML());
			req.setLanguage(language); // 0=Chinese Simplify;1=Chinese Tradition;2=English [always pass 1]
			result = stub.integrationByXmlWithLanguageEx(req).getIntegrationByXmlWithLanguageExResult();
		} catch (AxisFault e) {
			LogUtil.logError(_log, "Axis call fail.ErrMsg: " + e.getMessage());
			//throw new EJBException(e.getMessage());
			return responseMessage("Axis call fail.ErrMsg: " + e.getMessage());
		} catch (RemoteException e) {
			LogUtil.logError(_log, "Remote fail.ErrMsg: " + e.getMessage());
			//throw new EJBException(e.getMessage());
			return responseMessage("Remote fail.ErrMsg: " + e.getMessage());
		}
		
		_log.debug("Response XML: " + result);
		return result;
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
	}

	/**
	 * 
	 */
	public WebServiceClientBean() {
		if(this._log == null){
			this._log = NaNaLogFactory.getLog(WebServiceClientBean.class);
		}
	}
	
	/**
	 * Get ready for parameters of web service call
	 * @param soapAction  the soap action value of WSDL ex: "http://dcms.com.cn/core/GeneralService/IntegrationByXmlWithLanguageEx"
	 * @param method  ex: "IntegrationByXmlWithLanguageEx"
	 * @param params  the pair of key and value
	 * @return OMElement
	 */
	private OMElement fillParameters(String soapAction, String method, Map params) {
		OMFactory omf = OMAbstractFactory.getOMFactory();
		OMNamespace omns = omf.createOMNamespace(soapAction.substring(0, soapAction.indexOf(method)-1), "gseo");
		OMElement omMethod = omf.createOMElement(method, omns);
		
		Iterator it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry en = (Entry) it.next();
			OMElement value = omf.createOMElement((String)en.getKey(), omns);
			value.setText((String)en.getValue());
			omMethod.addChild(value);
		}
		return omMethod;
	}
	
	private String responseMessage(String info){
		return "<?xml version=\"1.0\" encoding=\"utf-16\"?><Response><Execution><Status Code=\"-9999\" description=\"\" /></Execution><ResponseContent><Data>"+info+"</Data></ResponseContent></Response>";
	}
}
