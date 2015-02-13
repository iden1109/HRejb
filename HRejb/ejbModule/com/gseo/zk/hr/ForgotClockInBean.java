/**
 * 
 */
package com.gseo.zk.hr;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.dom4j.Document;
import org.dom4j.Element;

import com.dsc.nana.domain.form.FormInstance;
import com.dsc.nana.util.logging.NaNaLog;
import com.dsc.nana.util.logging.NaNaLogFactory;
import com.gseo.zk.util.DBUtil;
import com.gseo.zk.util.LogUtil;
import com.gseo.zk.util.EJBFactory;
import com.gseo.zk.ws.WebServiceClient;
import com.gseo.zk.ws.WebServiceClientHome;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.rmi.PortableRemoteObject;


/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="ForgotClockIn"	
 *           description="An EJB named ForgotClockIn"
 *           display-name="ForgotClockIn"
 *           jndi-name="ForgotClockIn"
 *           type="Stateless" 
 *           transaction-type="Container"
 * 
 * <!-- end-xdoclet-definition --> 
 * @generated
 */

public abstract class ForgotClockInBean implements javax.ejb.SessionBean {

	/** 
	 * <!-- begin-xdoclet-definition --> 
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	private SessionContext _sessionCtx;
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
	 * Check and Remove from AttendanceCollect Table via AttendanceCollectId
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * @param pDataSource   ex:"HRMDS"
	 * @param collectId   collect id
	 * @return String   0 means OK else the info with error message 
	 */
	public String checktoRemoveAttendanceCollectData(String pDataSource, String collectId) {
		if(collectId == null || collectId.equals("")){
			throw new IllegalArgumentException("collectId cannot be empty !");
		}
		_log.debug("collectId: " + collectId); 

		String sRtn = "";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = _db.getJndiDataSource(pDataSource).getConnection();
			if(this.hasAttendanceCollectData(conn, collectId)){
				if (this.deleteFromAttendanceCollectData(conn, collectId) > 0)
					sRtn = "0";
				else
					sRtn = "Delete the collect id '"+collectId+"' fail, please check the permission!";
			}else
				sRtn = "0";
			
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		_log.debug("return : " + sRtn);
		return sRtn;
	}
	
	/** 
	 * Only for saveForESS that can not provide the serial key to call Web service
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * @param address   ex: "http://192.168.100.108/hrws/Integration.asmx"
	 * @param pDataSource   ex:"NaNaDS"
	 * @param serialNumber
	 * @return String   0 means OK else the info with error message 
	 */
	public String saveForESS(String address, String pDataSource, String serialNumber) {
		if(address == null || address.equals("")){
			throw new IllegalArgumentException("address cannot be empty !");
		}
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		if(serialNumber == null || serialNumber.equals("")){
			throw new IllegalArgumentException("serialNumber cannot be empty !");
		}
		_log.debug("address: " + address); 
		_log.debug("pDataSource: " + pDataSource); 
		_log.debug("serialNumber: " + serialNumber); 
		
		// 1: Query serial key via serial number
		String xml;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = _db.getJndiDataSource(pDataSource).getConnection();
			xml = this.getXML(conn, serialNumber);
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		// 2: Prepare the calling xml
		Document newdoc = this.prepareXML(xml, serialNumber);
		String newXML = newdoc.getRootElement().asXML();
		
		// 3. Invoke WebServiceclientBean
		String result = "";
		_log.debug("Request XML: " + newXML);
		Object ref = EJBFactory.getEJB("WebServiceClient");
		WebServiceClientHome home = (WebServiceClientHome) PortableRemoteObject.narrow(ref, WebServiceClientHome.class);
		WebServiceClient bean;
		try {
			bean = home.create();
			result = bean.IntegrationByXmlWithLanguageEx(address, newXML);
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
	public ForgotClockInBean() {
		if(this._log == null){
			this._log = NaNaLogFactory.getLog(LeaveBean.class);
		}
		_db = new DBUtil(_log);
	}
	
	/**
	 * check the AttendanceCollect data exists
	 * @param conn   Connection
	 * @param collectId   collect id
	 * @return   true / false
	 * @throws SQLException
	 */
	private boolean hasAttendanceCollectData(Connection conn, String collectId) throws SQLException {
		PreparedStatement ps = null;
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("select * ");
			sb.append("from AttendanceCollect ");
			sb.append("where AttendanceCollectId='"+collectId+"' ");
			ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			_db.releasePrepareStatement(ps);
		}
		return false;
	}
	
	/**
	 * Remove the AttendanceCollect data via collect id
	 * @param conn   Connection
	 * @param collectId   collect id
	 * @return  count  that has been deleted
	 * @throws SQLException
	 */
	private int deleteFromAttendanceCollectData(Connection conn, String collectId) throws SQLException {
		int cnt = 0;
		PreparedStatement ps = null;
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("delete from AttendanceCollect ");
			sb.append("where AttendanceCollectId='"+collectId+"' ");
			ps = conn.prepareStatement(sb.toString());
			cnt = ps.executeUpdate();
		}catch (SQLException e){
			LogUtil.logError(_log, "Delete DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			_db.releasePrepareStatement(ps);
		}
		return cnt;
	}
	
	/**
	 * Get XML from Database
	 * @param conn   Connection object
	 * @param serialNum   serial number
	 * @return serialKey  a string like this '8608F6B4-2D1C-4D9F-BD34-2A8F52BA6354'
	 * @throws SQLException
	 */
	private String getXML(Connection conn, String serialNum) throws SQLException {
		if(serialNum == null || serialNum.equalsIgnoreCase("")){
			throw new IllegalArgumentException("serialNum cannot be empty !");
		}
		
		String sRtn = "";
		PreparedStatement ps = null;
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("select fieldValues from FormInstance ");
			sb.append("where OID in (select valueOID from LocalRelevantData ");
			sb.append("              where containerOID in (select contextOID from ProcessInstance ");
			sb.append("                                     where serialNumber='"+ serialNum +"')) ");
			ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				sRtn = rs.getString("fieldValues") != null?rs.getString("fieldValues"):"";
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
	 * Prepare the XML document for Web Service Calling
	 * @param origXML
	 * @return doc
	 */
	private Document prepareXML(String origXML, String serialNumber) {
		if(origXML == null || origXML.equalsIgnoreCase("")){
			throw new IllegalArgumentException("origXML cannot be empty !");
		}
		if(serialNumber == null || serialNumber.equalsIgnoreCase("")){
			throw new IllegalArgumentException("serialNumber cannot be empty !");
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("<Request>");
		sb.append("	<Access>");
		sb.append("		<Authentication user=\"gp\" password=\"\"/>");
		sb.append("	</Access>");
		sb.append("	<RequestContent>");
		sb.append("		<ServiceType>Dcms.HR.Services.IAttendanceCollectService,Dcms.HR.Business.AttendanceCollect</ServiceType>");
		sb.append("		<Method>SaveForESS</Method>");
		sb.append("		<Parameters>");
		sb.append("			<Parameter type=\"System.String\"></Parameter>");
		sb.append("			<Parameter type=\"System.DateTime\"></Parameter>");
		sb.append("			<Parameter type=\"System.DateTime\"></Parameter>");
		sb.append("			<Parameter type=\"System.String\"></Parameter>");
		sb.append("			<Parameter type=\"System.String\"></Parameter>");
		sb.append("			<Parameter type=\"System.String\"></Parameter>");
		sb.append("			<Parameter type=\"System.String\"></Parameter>");
		sb.append("			<Parameter type=\"System.String\">00:00</Parameter>");                                                
		sb.append("			<Parameter type=\"System.String\">23:59</Parameter>");
		sb.append("		</Parameters>");
		sb.append("	</RequestContent>");
		sb.append("</Request>");
		Document targetDoc = new XML(_log).getXMLDoc(sb.toString());
		try{
			Document srcDoc = new XML(_log).getXMLDoc(origXML);
			Element srcRoot = srcDoc.getRootElement();
			
			Element root = targetDoc.getRootElement();
			Element parameters = root.element("RequestContent").element("Parameters");
			List listParams = parameters.elements();
			for(int i=0;i<listParams.size();i++){
				Element param = (Element)listParams.get(i);
				if(i == 0)
					param.setText(srcRoot.element("employeeId_shenQingRen").getText());
				if(i == 1)
					param.setText(srcRoot.element("StartDate").getText());
				if(i == 2)
					param.setText(srcRoot.element("EndDate").getText());
				if(i == 3)
					param.setText(srcRoot.element("shangBan_time").getText()+","+srcRoot.element("xiaBan_time").getText()+",,,,");
				if(i == 4)
					param.setText(srcRoot.element("buShaKaYuanYin_txt").getText());
				if(i == 5)
					param.addCDATA(serialNumber);
				if(i == 6)
					param.setText(srcRoot.element("employeeId_tianXieRen").getText());
			}
		}catch(NullPointerException e){
			LogUtil.logError(_log, "Parse DOM fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}
		
		return targetDoc;
	}
	
}
