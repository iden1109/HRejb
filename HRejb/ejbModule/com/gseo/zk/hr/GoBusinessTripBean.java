/**
 * 
 */
package com.gseo.zk.hr;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.dom4j.Document;
import org.dom4j.Element;

import com.dsc.nana.domain.form.FormInstance;
import com.dsc.nana.util.logging.NaNaLog;
import com.dsc.nana.util.logging.NaNaLogFactory;
import com.gseo.zk.util.DBUtil;
import com.gseo.zk.util.EJBFactory;
import com.gseo.zk.util.LogUtil;
import com.gseo.zk.ws.WebServiceClient;
import com.gseo.zk.ws.WebServiceClientHome;

import javax.rmi.PortableRemoteObject;

/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="GoBusinessTrip"	
 *           description="An EJB named GoBusinessTrip"
 *           display-name="GoBusinessTrip"
 *           jndi-name="GoBusinessTrip"
 *           type="Stateless" 
 *           transaction-type="Container"
 * 
 * <!-- end-xdoclet-definition --> 
 * @generated
 */

public abstract class GoBusinessTripBean implements javax.ejb.SessionBean {

	/** 
	 * <!-- begin-xdoclet-definition --> 
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	private NaNaLog _log;
	private DBUtil _db;
	
	
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
	 * Only for auditForESS that can not provide the serial key to call Web service
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * @param address   ex: "http://192.168.100.108/hrws/Integration.asmx"
	 * @param pDataSource   ex:"HRMDS"
	 * @param pFormInstance   tag of 'txtSerialNumber' and 'txtEmpid'
	 * @return String   0 means OK else the info with error message 
	 */
	public String auditForESS(String address, String pDataSource, FormInstance pFormInstance) {
		if(address == null || address.equals("")){
			throw new IllegalArgumentException("address cannot be empty !");
		}
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		String serialNumber = pFormInstance.fetchFieldValue("txtSerialNumber");
		if(serialNumber.equals("")){
			throw new IllegalArgumentException("txtSerialNumber cannot be empty !");
		}
		String empId = pFormInstance.fetchFieldValue("txtEmpid");
		if(empId.equals("")){
			throw new IllegalArgumentException("txtEmpid cannot be empty !");
		}
		
		return this.auditForESS(address, pDataSource, serialNumber, empId);
	}
	
	/** 
	 * Only for auditForESS that can not provide the serial key to call Web service
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * @param address   ex: "http://192.168.100.108/hrws/Integration.asmx"
	 * @param pDataSource   ex:"HRMDS"
	 * @param serialNumber
	 * @param empId
	 * @return String   0 means OK else the info with error message 
	 */
	public String auditForESS(String address, String pDataSource, String serialNumber, String empId) {
		if(address == null || address.equals("")){
			throw new IllegalArgumentException("address cannot be empty !");
		}
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		if(serialNumber == null || serialNumber.equals("")){
			throw new IllegalArgumentException("serialNumber cannot be empty !");
		}
		if(empId == null || empId.equals("")){
			throw new IllegalArgumentException("empId cannot be empty !");
		}
		_log.debug("address: " + address); 
		_log.debug("pDataSource: " + pDataSource); 
		_log.debug("serialNumber: " + serialNumber); 
		_log.debug("empId: " + empId); 
		
		// 1: Query serial key via serial number
		String serialKey;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = _db.getJndiDataSource(pDataSource).getConnection();
			serialKey = this.getSerialKey(conn, serialNumber);
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		// 2: Prepare the calling xml
		Document newdoc = this.setSerialKeytoXML(serialNumber, serialKey, empId);
		String sNewDoc = newdoc.getRootElement().asXML();
		
		// 3. Invoke WebServiceclientBean
		String result = "";
		_log.debug("Request XML: " + sNewDoc);
		Object ref = EJBFactory.getEJB("WebServiceClient");
		WebServiceClientHome home = (WebServiceClientHome) PortableRemoteObject.narrow(ref, WebServiceClientHome.class);
		WebServiceClient bean;
		try {
			bean = home.create();
			result = bean.IntegrationByXmlWithLanguageEx(address, sNewDoc);
		} catch (RemoteException e) {
			LogUtil.logError(_log, "Invoke WebServiceClientBean fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		} catch (CreateException e) {
			LogUtil.logError(_log, "Create WebServiceClientBean object fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}
		_log.debug("Response XML: " + result);
		
		
		_log.debug("return : " + result);
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
	public GoBusinessTripBean() {
		if(this._log == null){
			this._log = NaNaLogFactory.getLog(LeaveBean.class);
		}
		_db = new DBUtil(_log);
	}

	
	/**
	 * Get the serial key from HRM Database
	 * @param conn   Connection object
	 * @param serialNum   serial number
	 * @return serialKey  a string like this '8608F6B4-2D1C-4D9F-BD34-2A8F52BA6354'
	 * @throws SQLException
	 */
	private String getSerialKey(Connection conn, String serialNum) throws SQLException {
		if(serialNum == null || serialNum.equalsIgnoreCase("")){
			throw new IllegalArgumentException("serialNum cannot be empty !");
		}
		
		String sRtn = "";
		PreparedStatement ps = null;
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("select BusinessRegisterID  ");
			sb.append("from BusinessRegister ");
			sb.append("where Remark  like '%"+serialNum+"%' ");
			ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				sRtn = rs.getString("BusinessRegisterID") != null?rs.getString("BusinessRegisterID"):"";
			}
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			_db.releasePrepareStatement(ps);
		}
		return sRtn;
	}
	
	/**
	 * Set the serialKey into XML document
	 * @param doc
	 * @param serialKey
	 * @return doc
	 */
	private Document setSerialKeytoXML(String serialNum, String serialKey, String empId) {
		if(serialKey == null || serialKey.equalsIgnoreCase("")){
			throw new IllegalArgumentException("serialKey cannot be empty !");
		}
		if(empId == null || empId.equalsIgnoreCase("")){
			throw new IllegalArgumentException("empId cannot be empty !");
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<Request>");
		sb.append("	<Access>");
		sb.append("		<Authentication user=\"gp\" password=\"\"/>");
		sb.append("	</Access>");
		sb.append("	<RequestContent>");
		sb.append("		<ServiceType>Dcms.HR.Services.IBusinessRegisterService,Dcms.HR.Business.Business</ServiceType>");
		sb.append("		<Method>AuditForESS</Method>");
		sb.append("		<Parameters>");
		sb.append("			<Parameter type=\"System.String\"></Parameter>");
		sb.append("			<Parameter type=\"System.String\"></Parameter>");
		sb.append("			<Parameter type=\"System.String\"></Parameter>");
		sb.append("			<Parameter type=\"System.String\"></Parameter>");
		sb.append("			<Parameter type=\"System.String\"></Parameter>");                                                
		sb.append("			<Parameter type=\"System.Int32\"></Parameter>");
		sb.append("		</Parameters>");
		sb.append("	</RequestContent>");
		sb.append("</Request>");
		Document doc = new XML(_log).getXMLDoc(sb.toString());
		
		try{
			Element root = doc.getRootElement();
			Element parameters = root.element("RequestContent").element("Parameters");
			Iterator it = parameters.elementIterator();
			List listParams = parameters.elements();
			for(int i=0;i<listParams.size();i++){
				Element param = (Element)listParams.get(i);
				if(i == 0)
					param.setText("ESSF21");
				if(i == 1)
					param.setText(serialNum);
				if(i == 2)
					param.setText(serialKey);
				if(i == 3)
					param.setText(" ");
				if(i == 4)
					param.setText(empId);
				if(i == 5)
					param.setText("1");
			}
		}catch(NullPointerException e){
			LogUtil.logError(_log, "Parse DOM fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}
		
		return doc;
	}
}
