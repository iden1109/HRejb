package com.gseo.zk.hr;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.dom4j.Document;

import com.dsc.nana.domain.form.FormInstance;
import com.dsc.nana.util.logging.NaNaLog;
import com.dsc.nana.util.logging.NaNaLogFactory;
import com.gseo.zk.util.DBUtil;
import com.gseo.zk.util.LogUtil;

/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean.
 * It is for EFGP retrieving some information.
 * @author Zhengku
 * @since Sep 26, 2014
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="Leave"	
 *           description="An EJB named Leave"
 *           display-name="Leave"
 *           jndi-name="Leave"
 *           type="Stateless" 
 *           transaction-type="Container"
 * 
 * <!-- end-xdoclet-definition --> 
 * @generated
 */

public abstract class LeaveBean implements javax.ejb.SessionBean {

	/** 
	 * <!-- begin-xdoclet-definition --> 
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	private SessionContext _sessionCtx;
	private NaNaLog _log;
	private DBUtil _db;
	private static final String LEVEL1_LEADER = "level1";
	private static final String LEVEL2_LEADER = "level2";
	
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
		_log.debug("== foo("+param+") ==");
		return param;
	}
	
	/** 
	 * Retrieve the formInstance object contains LevelName, isManager and EmployeeType tag
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * @param pDataSource  ex:NaNaDS
	 * @param pFormInstance  provide the value of "ESSQJ008" and "ESSQJ014"
	 * @return FormInstance  this object appends some new tag with 
	 * <pre>{@code
	 * <LevelName id="LevelName" dataType="java.lang.Integer">12</LevelName>
	 * <isManager id="isManager" dataType="java.lang.String">N</isManager>
	 * <EmployeeType id="EmployeeType" dataType="java.lang.String">Responsibility</EmployeeType>
	 * <Level1LeaderCode id="Level1LeaderCode" dataType="java.lang.String">T0001</Level1LeaderCode>
	 * <Level2LeaderCode id="Level2LeaderCode" dataType="java.lang.String">T0002</Level2LeaderCode>
	 * }</pre>
	 */
	public FormInstance retrieveLevelName(String pDataSource, FormInstance formInstance) {
		
		if(formInstance.fetchFieldValue("ESSQJ008").equals("")){
			throw new IllegalArgumentException("ESSQJ008 cannot be empty !");
		}
		if(formInstance.fetchFieldValue("ESSQJ014").equals("")){
			throw new IllegalArgumentException("ESSQJ014 cannot be empty !");
		}
		String eid = formInstance.fetchFieldValue("ESSQJ008");//employee Id
		String oid = formInstance.fetchFieldValue("ESSQJ014");//depetment Id
		_log.debug("pDataSource: " + pDataSource);
		_log.debug("ESSQJ008: " + eid); 
		_log.debug("ESSQJ014: " + oid);
		
		Document doc = new XML(_log).getXMLDoc(formInstance.getFieldValues());
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = _db.getJndiDataSource(pDataSource).getConnection();
			
			// Generate LevelName element
			String level = getLevelValue(conn, eid, oid);
			doc.getRootElement().addElement("LevelName").addAttribute("id", "LevelName").addAttribute("dataType", "java.lang.Integer").setText(level);
			
			// Generate isManager element of Y/N value
			String isManager = isManager(conn, eid, oid);
			doc.getRootElement().addElement("isManager").addAttribute("id", "isManager").addAttribute("dataType", "java.lang.String").setText(isManager);
			
			// Get the employee type
			String empType = getEmployeeTypefromAAMS(eid);
			doc.getRootElement().addElement("EmployeeType").addAttribute("id", "EmployeeType").addAttribute("dataType", "java.lang.String").setText(empType);
			
			// Get leader code
			Map leader = getLeaderCodefromAAMS(eid);
			doc.getRootElement().addElement("Level1LeaderCode").addAttribute("id", "Level1LeaderCode").addAttribute("dataType", "java.lang.String").setText((String)leader.get(LeaveBean.LEVEL1_LEADER));
			doc.getRootElement().addElement("Level2LeaderCode").addAttribute("id", "Level2LeaderCode").addAttribute("dataType", "java.lang.String").setText((String)leader.get(LeaveBean.LEVEL2_LEADER));
			
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		_log.debug("return fieldValues: " + doc.asXML());
		//formInstance.configureFieldValue("existed field", "new information");
		formInstance.setFieldValues(doc.asXML());
		return formInstance;
	}
	
	/** 
	 * For TEST response of retrieveLevelName();
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * @param pDataSource  ex:NaNaDs
	 * @param employeeID
	 * @prarm orgID
	 * @return string  <LevelName><isManager><EmployeeType><Level1LeaderCode><Level2LeaderCode>
	 */
	public String retrieveLevelName(String pDataSource, String eid, String oid) {
		
		if(eid.equals("")){
			throw new IllegalArgumentException("eid cannot be empty !");
		}
		if(oid.equals("")){
			throw new IllegalArgumentException("oid cannot be empty !");
		}
		
		String level = "";
		String isManager = "";
		String empType = "";
		Map leader;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = _db.getJndiDataSource(pDataSource).getConnection();
			
			// Generate LevelName element
			level = getLevelValue(conn, eid, oid);
			// Generate isManager element of Y/N value
			isManager = isManager(conn, eid, oid);
			// Get the employee type
			empType = getEmployeeTypefromAAMS(eid);
			// Get leader code
			leader = getLeaderCodefromAAMS(eid);
			
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		return "<"+ level + "><"+ isManager + "><"+ empType + "><"+ leader.get(LeaveBean.LEVEL1_LEADER) + "><"+ leader.get(LeaveBean.LEVEL2_LEADER) + ">";
	}
	
	/** 
	 * Retrieve the formInstance object contains a signed member group of ID and userName
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * @param pDataSource  ex:NaNaDS
	 * @param pFormInstance  must contain the value of "ESSXJ025" and "ESSXJ008"
	 * @return FormInstance  this object appends some new tag with 
	 * <pre>{@code
	 * <SignedMembers id="SignedMembers" dataType="java.lang.String">T0001_empNameA;T0002_empNameB;T0003_empNameC;</SignedMembers>
	 * <Level1LeaderCode id="Level1LeaderCode" dataType="java.lang.String">T0001</Level1LeaderCode>
	 * <Level2LeaderCode id="Level2LeaderCode" dataType="java.lang.String">T0002</Level2LeaderCode>
	 * }</pre>
	 */
	public FormInstance retrieveSignedMembers(String pDataSource, FormInstance pFormInstance) {
		
		if(pFormInstance.fetchFieldValue("ESSXJ025").equals("")){
			throw new IllegalArgumentException("ESSXJ025 cannot be empty !");
		}
		if(pFormInstance.fetchFieldValue("ESSXJ008").equals("")){
			throw new IllegalArgumentException("ESSXJ008 cannot be empty !");
		}
		String j025 = pFormInstance.fetchFieldValue("ESSXJ025");
		String eid = pFormInstance.fetchFieldValue("ESSXJ008");
		_log.debug("pDataSource: " + pDataSource);
		_log.debug("ESSXJ025: " + j025);
		_log.debug("ESSXJ008: " + eid);
			
		Document doc = new XML(_log).getXMLDoc(pFormInstance.getFieldValues());
		
		DataSource ds = null;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			ds = _db.getJndiDataSource(pDataSource);
			conn = ds.getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("select w.workItemName, w.performerOID,u.OID, id, userName  ");
			sb.append("from WorkItem w, users u, ProcessInstance p ");
			sb.append("where w.contextOID=p.contextOID ");
			sb.append("and u.OID=w.performerOID ");
			sb.append("and p.serialNumber=(select a.processSerialNumber from essf07g a, essf17 b ");
			
			String formNo = "ESSF07G";
			if(j025.indexOf("-") > 0)
				formNo = j025.split("-")[0];
			if(formNo.length() < 7){ // cancel leaves condition: "ESSF07" case
				sb.append("where a.ESSQJ001=substring(b.ESSXJ025,1,6) ");
				sb.append("AND a.ESSQJ002=substring(b.ESSXJ025,8,12) ");
				sb.append("and substring(b.ESSXJ025,1,19)='"+ j025 +"') ");
			}else{ // "ESSF07G" case
				sb.append("where a.ESSQJ001=substring(b.ESSXJ025,1,7) ");
				sb.append("AND a.ESSQJ002=substring(b.ESSXJ025,9,12) ");
				sb.append("and substring(b.ESSXJ025,1,20)='"+ j025 +"') ");
			}
			ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			
			String id, name;
			StringBuilder rtnSb = new StringBuilder();
			while (rs.next()) {
				id = rs.getString("id")==null?"":rs.getString("id");
				name = rs.getString("userName")==null?"":rs.getString("userName");
				rtnSb.append(id).append("_").append(name).append(";");// organize data
			}
			doc.getRootElement().addElement("SignedMembers").addAttribute("id", "SignedMembers").addAttribute("dataType", "java.lang.String").setText(rtnSb.toString());
			
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			//Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		// Get leader code
		Map leader = getLeaderCodefromAAMS(eid);
		doc.getRootElement().addElement("Level1LeaderCode").addAttribute("id", "Level1LeaderCode").addAttribute("dataType", "java.lang.String").setText((String)leader.get(LeaveBean.LEVEL1_LEADER));
		doc.getRootElement().addElement("Level2LeaderCode").addAttribute("id", "Level2LeaderCode").addAttribute("dataType", "java.lang.String").setText((String)leader.get(LeaveBean.LEVEL2_LEADER));
		
		_log.debug("return fieldValues: " + doc.asXML());
		pFormInstance.setFieldValues(doc.asXML());
		return pFormInstance;
	}
	
	/** 
	 * Retrieve the formInstance object contains Level1LeaderCode tag
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * @param pFormInstance  provide the value of "ESSXJ004"
	 * @return FormInstance  this object appends some new tag with 
	 * <pre>{@code
	 * <Level1LeaderCode id="Level1LeaderCode" dataType="java.lang.String">T0001</Level1LeaderCode>
	 * <Level2LeaderCode id="Level2LeaderCode" dataType="java.lang.String">T0002</Level2LeaderCode>
	 * }</pre>
	 */
	public FormInstance retrieveLevel1LeaderCode(FormInstance formInstance) {
		
		if(formInstance.fetchFieldValue("ESSBCBG004").equals("")){
			throw new IllegalArgumentException("ESSBCBG004 cannot be empty !");
		}
		String eid = formInstance.fetchFieldValue("ESSBCBG004");//employee Id
		_log.debug("ESSBCBG004: " + eid); 

		Document doc = new XML(_log).getXMLDoc(formInstance.getFieldValues());
		
		// Get leader code
		Map leader = getLeaderCodefromAAMS(eid);
		doc.getRootElement().addElement("Level1LeaderCode").addAttribute("id", "Level1LeaderCode").addAttribute("dataType", "java.lang.String").setText((String)leader.get(LeaveBean.LEVEL1_LEADER));
		doc.getRootElement().addElement("Level2LeaderCode").addAttribute("id", "Level2LeaderCode").addAttribute("dataType", "java.lang.String").setText((String)leader.get(LeaveBean.LEVEL2_LEADER));
		
		_log.debug("return fieldValues: " + doc.asXML());
		formInstance.setFieldValues(doc.asXML());
		return formInstance;
	}
	
	/** 
	 * For TEST response of retrieveLevel1LeaderCode()
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * @param employeeID
	 * @return leaderCode
	 */
	public String retrieveLevel1LeaderCode(String employeeID) {
		
		if(employeeID.equals("")){
			throw new IllegalArgumentException("employeeID cannot be empty !");
		}
		String eid = employeeID;

		// Get leader code
		Map leader = getLeaderCodefromAAMS(eid);
		return leader.get(LeaveBean.LEVEL1_LEADER) + "<--->"+ leader.get(LeaveBean.LEVEL2_LEADER);
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
	public void setSessionContext(SessionContext ctx) throws EJBException,
			RemoteException {
		if(ctx != null)
			_sessionCtx = ctx;
	}

	/**
	 * 
	 */
	public LeaveBean() {
		if(this._log == null){
			this._log = NaNaLogFactory.getLog(LeaveBean.class);
		}
		_db = new DBUtil(_log);
	}
	
	
	/**
	 * Get the level value
	 * @param conn  Connection
	 * @param eid  Employee ID
	 * @param oid  Organization ID
	 * @return String  Level value
	 * @throws SQLException
	 */
	private String getLevelValue(Connection conn, String eid, String oid) throws SQLException {
		String level = "99"; // default 99 means that there is no data in the Database
		PreparedStatement ps;
		StringBuilder sb = new StringBuilder();
		sb.append("select l.levelValue ");
		sb.append("from employee e, functions f, organizationunit o,functionLevel l ");
		sb.append("where e.useroid=f.occupantoid ");
		sb.append("and f.organizationunitoid=o.oid ");
		sb.append("and f.approvalLevelOID=l.oid ");
		sb.append("and o.organizationoid=e.organizationoid ");
		sb.append("and e.employeeid= '"+eid+"' ");
		sb.append("and o.id= '"+oid+"' ");
		ps = conn.prepareStatement(sb.toString());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			level = rs.getString("levelValue") == null || rs.getString("levelValue").trim().equalsIgnoreCase("null")?"99":rs.getString("levelValue");
			/*if(level.length()<2) //left pad 2 digits with 0
				level = String.format("{0:00}", level);*/
		}
		_db.releasePrepareStatement(ps);	
		return level;
	}

	/**
	 * Check and return manager or not
	 * @param conn  Connection
	 * @param eid  Employee ID
	 * @param oid  Organization ID
	 * @return String Y/N
	 * @throws SQLException
	 */
	private String isManager(Connection conn, String eid, String oid) throws SQLException {
		String isManager = "N";
		PreparedStatement ps;
		ResultSet rs;
		StringBuilder sb = new StringBuilder();
		sb.append("select count(e.employeeid) cnt ");
		sb.append("from employee e, OrganizationUnit o, OrganizationUnitLevel l ");
		sb.append("where e.organizationoid=o.organizationoid ");
		sb.append("and o.manageroid=e.useroid ");
		sb.append("and o.levelOID=l.oid ");
		sb.append("and levelvalue=6000 ");
		sb.append("and e.employeeid='"+eid+"' ");
		sb.append("and o.id='"+oid+"' ");
		ps = conn.prepareStatement(sb.toString());
		rs = ps.executeQuery();
		if (rs.next()) {
			// if this employee is a manager
			if(rs.getInt("cnt") > 0)
				isManager = "Y";
		}
		_db.releasePrepareStatement(ps);
		return isManager;
	}
	
	/** 
	 * Get the employee type string from ProdAAMSDS datasource
	 * @param eid  Employee ID
	 * @return EmployeeType
	 */
	private String getEmployeeTypefromAAMS(String eid) {
		String dataSource = "ProdAAMSDS";
		if(eid == null || eid.equals("")){
			throw new IllegalArgumentException("Employee ID cannot be empty !");
		}
		_log.debug("Employee ID: " + eid); 

		Connection conn = null;
		PreparedStatement ps = null;
		String rtnEmpType = "";
		try {
			conn = _db.getJndiDataSource(dataSource).getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("select EmployeeType from dbo.V_HRMDB_Employee_Sync ");
			sb.append("where code='"+eid+"' ");
			ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				rtnEmpType = rs.getString("EmployeeType") != null?rs.getString("EmployeeType"):"";
			}
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		return rtnEmpType;
	}
	
	/**
	 * Get the leader code from ProdAAMSDS datasource
	 * @param eid  Employee ID
	 * @return LeaderCode  list of leader code
	 */
	private Map getLeaderCodefromAAMS(String eid) {
		String dataSource = "ProdAAMSDS";
		if(eid == null || eid.equals("")){
			throw new IllegalArgumentException("Employee ID cannot be empty !");
		}
		_log.debug("Employee ID: " + eid); 

		Connection conn = null;
		PreparedStatement ps = null;
		Map rtnMap = new Hashtable();
		try {
			conn = _db.getJndiDataSource(dataSource).getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("select direct_leader_code, level1_leader_code, level2_leader_code from V_HRMDB_Employee_Sync ");
			sb.append("where code='"+eid+"' ");
			ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String direct = rs.getString("direct_leader_code") != null?rs.getString("direct_leader_code"):"";
				String level1 = rs.getString("level1_leader_code") != null?rs.getString("level1_leader_code"):"";
				String level2 = rs.getString("level2_leader_code") != null?rs.getString("level2_leader_code"):"";
				if(!direct.equalsIgnoreCase(level1)){
					rtnMap.put(LeaveBean.LEVEL1_LEADER, direct);
					rtnMap.put(LeaveBean.LEVEL2_LEADER, level1);
				}else{
					rtnMap.put(LeaveBean.LEVEL1_LEADER, level1);
					rtnMap.put(LeaveBean.LEVEL2_LEADER, level2);
				}
			}else{
				rtnMap.put(LeaveBean.LEVEL1_LEADER, "");
				rtnMap.put(LeaveBean.LEVEL2_LEADER, "");
			}
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		return rtnMap;
	}
	
}
