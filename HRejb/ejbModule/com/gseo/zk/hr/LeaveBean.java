package com.gseo.zk.hr;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.dom4j.Document;
import org.dom4j.Element;

import com.dsc.nana.domain.form.FormInstance;
import com.dsc.nana.util.logging.NaNaLog;
import com.dsc.nana.util.logging.NaNaLogFactory;
import com.gseo.zk.hr.model.Assist;
import com.gseo.zk.hr.model.Employee;
import com.gseo.zk.hr.model.Employees;
import com.gseo.zk.hr.model.OverTime;
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
	
	private static final String LEVEL = "99999";  // default 99999 means that there is no data in the Database
	private static final String AAMS_DATASOURCE = "ProdAAMSDS";
	private static final String LEVEL1_LEADER = "level1";
	private static final String LEVEL2_LEADER = "level2";
	private static final String LEVEL_VALUE = "levelvalue";
	private static final String EMP_ID = "employeeid";
	private static final String DIR_INDIR = "DirIndir";
	private static final String DEPT_DESCRIBE = "deptDescribe";
	
	//
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
	 * <DeputyEmployeeId id="DeputyEmployeeId" dataType="java.lang.String">1</DeputyEmployeeId>
	 * }</pre>
	 */
	public FormInstance retrieveLevelName(String pDataSource, FormInstance formInstance) {
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		if(formInstance.fetchFieldValue("ESSQJ008").equals("")){
			throw new IllegalArgumentException("ESSQJ008 cannot be empty !");
		}
		if(formInstance.fetchFieldValue("ESSQJ014").equals("")){
			throw new IllegalArgumentException("ESSQJ014 cannot be empty !");
		}
		if(formInstance.fetchFieldValue("ESSQJ031").equals("")){
			throw new IllegalArgumentException("ESSQJ031 cannot be empty !");
		}
		String eid = formInstance.fetchFieldValue("ESSQJ008");//employee Id
		String oid = formInstance.fetchFieldValue("ESSQJ014");//depetment Id
		String deputyEid = formInstance.fetchFieldValue("ESSQJ031");//deputy employee Id
		_log.debug("pDataSource: " + pDataSource);
		_log.debug("ESSQJ008: " + eid); 
		_log.debug("ESSQJ014: " + oid);
		_log.debug("ESSQJ031: " + deputyEid);
		
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
			
			// Get deputy
			String deputy = getDeputy(conn, deputyEid);
			doc.getRootElement().addElement("DeputyEmployeeId").addAttribute("id", "DeputyEmployeeId").addAttribute("dataType", "java.lang.String").setText(deputy);
			
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
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
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
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
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
	 * 
	 * @param employeeID
	 * @return leaderCode
	 */
	public String retrieveLevel1LeaderCode(String employeeID) {
		if(employeeID.equals("")){
			throw new IllegalArgumentException("employeeID cannot be empty !");
		}
		// Get leader code
		Map leader = getLeaderCodefromAAMS(employeeID);
		return leader.get(LeaveBean.LEVEL1_LEADER) + "<--->"+ leader.get(LeaveBean.LEVEL2_LEADER);
	}
	
	/** 
	 * Retrieve the formInstance object contains some tags like return value
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * 
	 * @param pDataSource  ex:NaNaDS
	 * @param pFormInstance  the DOM object contains ESSJBNXX005 tag
	 * @return FormInstance  this object appends some new tag with 
	 * <pre>{@code
	 * <LevelValue id="LevelValue" dataType="java.lang.String">12</LevelValue>
	 * <Level1LeaderCode id="Level1LeaderCode" dataType="java.lang.String">T0001</Level1LeaderCode>
	 * <Level2LeaderCode id="Level2LeaderCode" dataType="java.lang.String">T0002</Level2LeaderCode>
	 * <DirInDir id="DirInDir" dataType="java.lang.String">Direct</DirInDir>
	 * <DeptDescribe id="DeptDescribe" dataType="java.lang.String">Dept Class</DeptDescribe>
	 * <DirectLeaderCodes id="DirectLeaderCodes" dataType="java.lang.String">T0001;T0002;T0003</DirectLeaderCodes>
	 * <OvertimeName id="OvertimeName" dataType="java.lang.String">Zhengku..</OvertimeName>
	 * <OvertimeHours id="OvertimeHours" dataType="java.lang.String">3..</OvertimeHours>
	 * <OvertimeReason id="OvertimeReason" dataType="java.lang.String">Reason1..</OvertimeReason>
	 * <ESSJBN015 id="ESSJBN015" dataType="java.lang.String">John, 0.5hrs, Reason:reason1</ESSJBN015>
	 * }</pre>
	 */
	public FormInstance retrieveForOvertime(String pDataSource, FormInstance formInstance) {
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		_log.debug("pDataSource: " + pDataSource);

		Document doc = new XML(_log).getXMLDoc(formInstance.getFieldValues());
		//XML xml = new XML(_log);
		//Document doc = xml.getXMLDoc(xml.Big5toUTF8(formInstance.getFieldValues()));
		OverTime ot = retrieveForOvertime(pDataSource, doc.asXML());
		
		// for level value of leaders group
		doc.getRootElement().addElement("LevelValue").addAttribute("id", "LevelValue").addAttribute("dataType", "java.lang.String").setText(ot.getLevelValue());
		
		// get the group of direct leader code
		doc.getRootElement().addElement("DirectLeaderCodes").addAttribute("id", "DirectLeaderCodes").addAttribute("dataType", "java.lang.String").setText(ot.getDirectLeaderCodes());
		
		// Get top leader code, dir/indir, deptDescribe
		doc.getRootElement().addElement("Level1LeaderCode").addAttribute("id", "Level1LeaderCode").addAttribute("dataType", "java.lang.String").setText(ot.getLevel1LeaderCode());
		doc.getRootElement().addElement("Level2LeaderCode").addAttribute("id", "Level2LeaderCode").addAttribute("dataType", "java.lang.String").setText(ot.getLevel2LeaderCode());
		doc.getRootElement().addElement("DirInDir").addAttribute("id", "DirInDir").addAttribute("dataType", "java.lang.String").setText(ot.getDirInDir());
		doc.getRootElement().addElement("DeptDescribe").addAttribute("id", "DeptDescribe").addAttribute("dataType", "java.lang.String").setText(ot.getDeptDescribe());
		
		// Get name, hours and reason for Overtime from XML 
		doc.getRootElement().addElement("OvertimeName").addAttribute("id", "OvertimeName").addAttribute("dataType", "java.lang.String").setText(ot.getOvertimeName());
		doc.getRootElement().addElement("OvertimeHours").addAttribute("id", "OvertimeHours").addAttribute("dataType", "java.lang.String").setText(ot.getOvertimeHours());
		doc.getRootElement().addElement("OvertimeReason").addAttribute("id", "OvertimeReason").addAttribute("dataType", "java.lang.String").setText(ot.getOvertimeReason());
		
		_log.debug("return fieldValues: " + doc.asXML());
		formInstance.setFieldValues(doc.asXML());
		formInstance.configureFieldValue("ESSJBN015", ot.getOvertimeNameHrsReason()); // Put name + hours + reason for Overtime
		return formInstance;
	}
	
	/** 
	 * For TEST response of retrieveForOvertime()
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * 
	 * @ejb.transaction
	 *   type="Required"
	 *   
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 *
	 * @param pDataSource  ex:NaNaDS
	 * @param xml
	 * @return OverTime
	 */
	public OverTime retrieveForOvertime(String pDataSource, String xml) {
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		_log.debug("pDataSource: " + pDataSource);
		if(xml == null || xml.equals("")){
			throw new IllegalArgumentException("xml cannot be empty !");
		}
		
		Document doc = new XML(_log).getXMLDoc(xml);
		StringBuilder sbEmp = getEmpGroup(doc, "ESSJBNXX005");
		_log.debug("ESSJBNXX005: " + sbEmp.toString());
		
		OverTime ot = new OverTime();

		Map m = getLeaderForEmpGroup(pDataSource, sbEmp.toString());
		
		// for level value of leaders group
		String levelvalue = (String) m.get(LeaveBean.LEVEL_VALUE);
		if(levelvalue == null || levelvalue.equalsIgnoreCase("")){
			levelvalue = LeaveBean.LEVEL;
		}
		ot.setLevelValue(levelvalue);
				
		// get the group of direct leader code
		String directLeaders = getDirectLeaderCodesfromAAMS(sbEmp.toString());
		ot.setDirectLeaderCodes(directLeaders);
		
		// Get top leader code, dir/indir, deptDescribe
		String empId = (String) m.get(LeaveBean.EMP_ID);
		if(empId == null || empId.equalsIgnoreCase("")){
			if(sbEmp.indexOf(",")>0){
				empId = sbEmp.substring(1, sbEmp.indexOf(",")-1);
			}else{
				empId = sbEmp.substring(1, 6);
			}
		}
		Map leader = getLeaderCodefromAAMS(empId);
		ot.setLevel1LeaderCode((String)leader.get(LeaveBean.LEVEL1_LEADER));
		ot.setLevel2LeaderCode((String)leader.get(LeaveBean.LEVEL2_LEADER));
		ot.setDirInDir((String)leader.get(LeaveBean.DIR_INDIR));
		ot.setDeptDescribe((String)leader.get(LeaveBean.DEPT_DESCRIBE));
		
		// Get name, hours and reason for Overtime from XML 
		Employee empObj = new Employee();
		empObj.setKey("ESSJBNXX006");
		empObj.setKey("ESSJBNXX019");
		empObj.setKey("ESSJBNXX021");
		Employees empsObj = this.getEmployees(doc, empObj);
		if(empsObj.size()>0){
			String dots = empsObj.size()>1?"..":"";
			ot.setOvertimeName(empsObj.get(0).getValue("ESSJBNXX006") + dots);
			ot.setOvertimeHours(empsObj.get(0).getValue("ESSJBNXX019") + dots);
			ot.setOvertimeReason(empsObj.get(0).getValue("ESSJBNXX021") + dots);
			if(empsObj.size()>1)
				ot.setOvertimeNameHrsReason(ot.getOvertimeName()+ot.MULTI_APPLICANT);
			else 
				ot.setOvertimeNameHrsReason(ot.getOvertimeName() + ", " + ot.getOvertimeHours()+ot.HOURS + ", " + ot.REASON+":"+ot.getOvertimeReason());
		}else{
			ot.setOvertimeName("");
			ot.setOvertimeHours("");
			ot.setOvertimeReason("");
			ot.setOvertimeNameHrsReason("");
		}
		/*
		// Update the process title
		String subject = "";
		if(!ot.getOvertimeNameHrsReason().equals(""))
			subject = ot.OVERTIME_APPLICANT+"("+ot.getOvertimeNameHrsReason()+")";
		updateProcessInstancebySerialNumberfromNANA(pDataSource, subject, processSerialNumber);
		*/
		return ot;
	}
	
	/** 
	 * Retrieve the formInstance object contains some tags like return value
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * @param pDataSource  ex:NaNaDS
	 * @param pFormInstance  the DOM object contains ESSBGRY005 tag
	 * @return FormInstance  this object appends some new tag with 
	 * <pre>{@code
	 * <LevelValue id="LevelValue" dataType="java.lang.String">12</LevelValue>
	 * <Level1LeaderCode id="Level1LeaderCode" dataType="java.lang.String">T0001</Level1LeaderCode>
	 * <Level2LeaderCode id="Level2LeaderCode" dataType="java.lang.String">T0002</Level2LeaderCode>
	 * <DirInDir id="DirInDir" dataType="java.lang.String">Direct</DirInDir>
	 * <DeptDescribe id="DeptDescribe" dataType="java.lang.String">Dept Class</DeptDescribe>
	 * <DirectLeaderCodes id="DirectLeaderCodes" dataType="java.lang.String">T0001;T0002;T0003</DirectLeaderCodes>
	 * }</pre>
	 */
	public FormInstance retrieveForChangeWorkshift(String pDataSource, FormInstance formInstance) {
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		_log.debug("pDataSource: " + pDataSource);

		Document doc = new XML(_log).getXMLDoc(formInstance.getFieldValues());
		OverTime ot = retrieveForChangeWorkshift(pDataSource, doc.asXML());
		
		doc.getRootElement().addElement("LevelValue").addAttribute("id", "LevelValue").addAttribute("dataType", "java.lang.String").setText(ot.getLevelValue());
		
		// get the group of direct leader code
		doc.getRootElement().addElement("DirectLeaderCodes").addAttribute("id", "DirectLeaderCodes").addAttribute("dataType", "java.lang.String").setText(ot.getDirectLeaderCodes());
		
		// Get top leader code, dir/indir, deptDescribe
		doc.getRootElement().addElement("Level1LeaderCode").addAttribute("id", "Level1LeaderCode").addAttribute("dataType", "java.lang.String").setText(ot.getLevel1LeaderCode());
		doc.getRootElement().addElement("Level2LeaderCode").addAttribute("id", "Level2LeaderCode").addAttribute("dataType", "java.lang.String").setText(ot.getLevel2LeaderCode());
		doc.getRootElement().addElement("DirInDir").addAttribute("id", "DirInDir").addAttribute("dataType", "java.lang.String").setText(ot.getDirInDir());
		doc.getRootElement().addElement("DeptDescribe").addAttribute("id", "DeptDescribe").addAttribute("dataType", "java.lang.String").setText(ot.getDeptDescribe());
		
		_log.debug("return fieldValues: " + doc.asXML());
		formInstance.setFieldValues(doc.asXML());
		return formInstance;
	}
	
	/** 
	 * For TEST response of retrieveForChangeWorkshift()
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * @param pDataSource  ex:NaNaDS
	 * @param xml
	 * @return OverTime
	 */
	public OverTime retrieveForChangeWorkshift(String pDataSource, String xml) {
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		_log.debug("pDataSource: " + pDataSource);
		if(xml == null || xml.equals("")){
			throw new IllegalArgumentException("xml cannot be empty !");
		}
		
		Document doc = new XML(_log).getXMLDoc(xml);
		StringBuilder sbEmp = getEmpGroup(doc, "ESSBGRY005");
		_log.debug("ESSBGRY005: " + sbEmp.toString());

		OverTime ot = new OverTime();
		
		Map m = getLeaderForEmpGroup(pDataSource, sbEmp.toString());
		
		// for level value of leaders group
		String levelvalue = (String) m.get(LeaveBean.LEVEL_VALUE);
		if(levelvalue == null || levelvalue.equalsIgnoreCase("")){
			levelvalue = LeaveBean.LEVEL;
		}
		ot.setLevelValue(levelvalue);
		
		// get the group of direct leader code
		String directLeaders = getDirectLeaderCodesfromAAMS(sbEmp.toString());
		ot.setDirectLeaderCodes(directLeaders);
		
		// Get leader code
		String empId = (String) m.get(LeaveBean.EMP_ID);
		if(empId == null || empId.equalsIgnoreCase("")){
			if(sbEmp.indexOf(",")>0){
				empId = sbEmp.substring(1, sbEmp.indexOf(",")-1);
			}else{
				empId = sbEmp.substring(1, 6);
			}
		}
		Map leader = getLeaderCodefromAAMS(empId);
		ot.setLevel1LeaderCode((String)leader.get(LeaveBean.LEVEL1_LEADER));
		ot.setLevel2LeaderCode((String)leader.get(LeaveBean.LEVEL2_LEADER));
		ot.setDirInDir((String)leader.get(LeaveBean.DIR_INDIR));
		ot.setDeptDescribe((String)leader.get(LeaveBean.DEPT_DESCRIBE));
						
		return ot;
	}
	
	/** 
	 * Retrieve the employeeID of Dept. Assistant
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * @param pDataSource  ex:HRMDS
	 * @param pFormInstance  provide the value of "ESSQJ008"
	 * @return FormInstance  this object appends some new tag with 
	 * <pre>{@code
	 * <Assistant id="Assistant" dataType="java.lang.String">T0001;T0002</Assistant>
	 * <Grade id="Grade" dataType="java.lang.String">0</Grade>
	 * }</pre>
	 */
	public FormInstance retrieveAssistant(String pDataSource, FormInstance formInstance) {
		String eid = formInstance.fetchFieldValue("ESSQJ008");//employee Id
		
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		if(eid.equals("")){
			throw new IllegalArgumentException("ESSQJ008 cannot be empty !");
		}
		_log.debug("pDataSource: " + pDataSource);
		_log.debug("ESSQJ008: " + eid); 

		Document doc = new XML(_log).getXMLDoc(formInstance.getFieldValues());
		
		Assist ass = retrieveAssistant(pDataSource, eid);
		doc.getRootElement().addElement("Assistant").addAttribute("id", "Assistant").addAttribute("dataType", "java.lang.String").setText(ass.getAssist());
		doc.getRootElement().addElement("Grade").addAttribute("id", "Grade").addAttribute("dataType", "java.lang.String").setText(ass.getGrade());
		
		_log.debug("return fieldValues: " + doc.asXML());
		formInstance.setFieldValues(doc.asXML());
		return formInstance;
	}
	
	/** 
	 * Retrieve the employeeID of Dept. Assistant
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * 
	 * @param pDataSource  ex:HRMDS
	 * @param empId   Employee ID
	 * @return Assist object
	 */
	public Assist retrieveAssistant(String pDataSource, String empId) {
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		if(empId == null || empId.equals("")){
			throw new IllegalArgumentException("Employee ID cannot be empty !");
		}
		_log.debug("pDataSource: " + pDataSource);
		_log.debug("empId: " + empId); 

		Assist ass = new Assist();
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = _db.getJndiDataSource(pDataSource).getConnection();
			ass.setAssist(this.getAssistantFromHRM(conn, empId));
			ass.setGrade(this.getGradeFromHRM(conn, empId));
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		return ass;
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
		String level = LeaveBean.LEVEL;
		PreparedStatement ps = null;
		try{
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
				level = rs.getString("levelValue") == null || rs.getString("levelValue").trim().equalsIgnoreCase("null")?LeaveBean.LEVEL:rs.getString("levelValue");
				/*if(level.length()<2) //left pad 2 digits with 0
					level = String.format("{0:00}", level);*/
			}
			
			//if level is empty try again
			sb.delete(0, sb.length());
			sb.append("select l.levelValue ");
			sb.append("from employee e, functions f, organizationunit o,functionLevel l ");
			sb.append("where e.useroid=f.occupantoid ");
			sb.append("and f.organizationunitoid=o.oid ");
			sb.append("and f.approvalLevelOID=l.oid ");
			sb.append("and o.organizationoid=e.organizationoid ");
			sb.append("and e.employeeid= '"+eid+"' ");
			ps = conn.prepareStatement(sb.toString());
			rs = ps.executeQuery();
			if (rs.next()) 
				level = rs.getString("levelValue") == null || rs.getString("levelValue").trim().equalsIgnoreCase("null")?LeaveBean.LEVEL:rs.getString("levelValue");
	
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			_db.releasePrepareStatement(ps);
		}
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
		PreparedStatement ps = null;
		try{
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
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			_db.releasePrepareStatement(ps);
		}
		return isManager;
	}
	
	/** 
	 * Get the employee type string from ProdAAMSDS datasource
	 * @param eid  Employee ID
	 * @return EmployeeType
	 */
	private String getEmployeeTypefromAAMS(String eid) {
		if(eid == null || eid.equals("")){
			throw new IllegalArgumentException("Employee ID cannot be empty !");
		}
		_log.debug("Employee ID: " + eid); 

		Connection conn = null;
		PreparedStatement ps = null;
		String rtnEmpType = "";
		try {
			conn = _db.getJndiDataSource(LeaveBean.AAMS_DATASOURCE).getConnection();
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
	 * @return Map  with LeaveBean.LEVEL1_LEADER, LeaveBean.LEVEL2_LEADER, LeaveBean.DIR_INDIR, LeaveBean.DEPT_DESCRIBE
	 */
	private Map getLeaderCodefromAAMS(String eid) {
		if(eid == null || eid.equals("")){
			throw new IllegalArgumentException("Employee ID cannot be empty !");
		}
		_log.debug("Employee ID: " + eid); 

		Connection conn = null;
		PreparedStatement ps = null;
		Map mapRtn = new Hashtable();
		StringBuilder sb = new StringBuilder();
		try {
			conn = _db.getJndiDataSource(LeaveBean.AAMS_DATASOURCE).getConnection();
			sb.append("select direct_leader_code, dept_leader_code, level1_leader_code, level2_leader_code, DirIndir, deptDescribe ");
			sb.append("from V_HRMDB_Employee_Sync ");
			sb.append("where code='"+eid+"' ");
			ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			String direct, dept, level1, level2;
			if (rs.next()) {
				direct = rs.getString("direct_leader_code") != null?rs.getString("direct_leader_code"):"";
				dept = rs.getString("dept_leader_code") != null?rs.getString("dept_leader_code"):"";
				level1 = rs.getString("level1_leader_code") != null?rs.getString("level1_leader_code"):"";
				level2 = rs.getString("level2_leader_code") != null?rs.getString("level2_leader_code"):"";
				mapRtn.put(LeaveBean.LEVEL1_LEADER, direct);
				if(!direct.equalsIgnoreCase(dept)){
					mapRtn.put(LeaveBean.LEVEL2_LEADER, dept);
				}else if(!direct.equalsIgnoreCase(level1)){
					mapRtn.put(LeaveBean.LEVEL2_LEADER, level1);
				}else{
					mapRtn.put(LeaveBean.LEVEL2_LEADER, level2);
				}
				mapRtn.put(LeaveBean.DIR_INDIR, rs.getString("DirIndir") != null?rs.getString("DirIndir"):"");
				mapRtn.put(LeaveBean.DEPT_DESCRIBE, rs.getString("deptDescribe") != null?rs.getString("deptDescribe"):"");
			}else{
				mapRtn.put(LeaveBean.LEVEL1_LEADER, "");
				mapRtn.put(LeaveBean.LEVEL2_LEADER, "");
				mapRtn.put(LeaveBean.DIR_INDIR, "");
				mapRtn.put(LeaveBean.DEPT_DESCRIBE, "");
			}
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + sb.toString() + e.getMessage());
			throw new EJBException(sb.toString() + e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		return mapRtn;
	}
	
	
	/**
	 * Get the leader code from ProdAAMSDS datasource
	 * @param eids  the group of Employee ID just like this 'T0001','T0002','T0003'
	 * @return DirectLeaderCode  ex: T0001;T0002;T0003
	 */
	private String getDirectLeaderCodesfromAAMS(String eids) {
		if(eids == null || eids.equals("")){
			throw new IllegalArgumentException("the group of Employee ID cannot be empty !");
		}
		_log.debug("Employee IDs: " + eids); 

		StringBuilder sbRtn = new StringBuilder();
		Connection conn = null;
		PreparedStatement ps = null;
		StringBuilder sb = new StringBuilder();
		try {
			conn = _db.getJndiDataSource(LeaveBean.AAMS_DATASOURCE).getConnection();
			sb.append("select direct_leader_code ");
			sb.append("from V_HRMDB_Employee_Sync ");
			sb.append("where code in ("+eids+") ");
			sb.append("group by direct_leader_code ");
			ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			String direct;
			while (rs.next()) {
				direct = rs.getString("direct_leader_code") != null?rs.getString("direct_leader_code"):"";
				sbRtn.append(direct+";");
			}
			if(sbRtn.length()>0)
				sbRtn.delete(sbRtn.length()-1, sbRtn.length());
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: "+ sb.toString() + e.getMessage());
			throw new EJBException(sb.toString() + e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		return sbRtn.toString();
	}
	
	/**
	 * Get the employee id and level value for employee group
	 * @param pDataSource  ex:NaNaDS
	 * @param queryEmployeeCondition  just like this 'T0001','T0002','T0003'
	 * @return Map  with LeaveBean.LEVEL_VALUE, LeaveBean.EMP_ID properties
	 */
	private Map getLeaderForEmpGroup(String pDataSource, String queryEmployeeCondition) {
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		if(queryEmployeeCondition == null || queryEmployeeCondition.equals("")){
			throw new IllegalArgumentException("queryEmployeeCondition cannot be empty !");
		}
		Map mapRtn = new Hashtable();
		Connection conn = null;
		PreparedStatement ps = null;
		String empId = "";
		String levelVal = "";
		StringBuilder sb = new StringBuilder();
		try {
			conn = _db.getJndiDataSource(pDataSource).getConnection();
			sb.append("select e.employeeid, l.levelValue ");
			sb.append("from employee e, functions f, organizationunit o,functionLevel l ");
			sb.append("where e.useroid=f.occupantoid ");
			sb.append("and f.organizationunitoid=o.oid ");
			sb.append("and f.approvalLevelOID=l.oid ");
			sb.append("and e.employeeid in ("+ queryEmployeeCondition +") ");
			sb.append("order by levelValue ");
			ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				empId = rs.getString("employeeid") == null?"":rs.getString("employeeid");
				levelVal = rs.getString("levelValue") == null?"":rs.getString("levelValue");
			}
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + sb.toString() + e.getMessage());
			throw new EJBException(sb.toString() + e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releasePrepareStatement(ps);
			_db.releaseConn(conn);
		}
		
		mapRtn.put(LeaveBean.LEVEL_VALUE, levelVal);
		mapRtn.put(LeaveBean.EMP_ID, empId);
		return mapRtn;
	}

	/**
	 * Combine ESSJBNXX005(Employee ID) of d01 tag as a query condition of EmployeeID
	 * @param doc  XML Document object
	 * @return EmployeeIDs  The format likes this 'T0001','T0002','T0003'
	 */
	private StringBuilder getEmpGroup(Document doc, String empKey) {
		if(doc == null){
			throw new IllegalArgumentException("doc cannot be empty !");
		}
		if(empKey == null || empKey.equals("")){
			throw new IllegalArgumentException("empKey cannot be empty !");
		}
		StringBuilder sbEmp = new StringBuilder();
		try{
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
					if(id.equals(empKey)){
						sbEmp.append("'"+txt+"'").append(",");
					}
				}
			}
			if(sbEmp.length()>0)
				sbEmp.delete(sbEmp.length()-1, sbEmp.length());
		}catch(NullPointerException e){
			LogUtil.logError(_log, "Parse "+empKey+" from DOM fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}
		return sbEmp;
	}
	
	private Employees getEmployees(Document doc, Employee emp) {
		if(doc == null){
			throw new IllegalArgumentException("doc cannot be empty !");
		}
		if(emp == null || emp.size() <= 0){
			throw new IllegalArgumentException("Employee object cannot be empty !");
		}
		//StringBuilder sbEmp = new StringBuilder();
		Employees empsRtn = new Employees();
		String[] keys = emp.keys();
		try{
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
					for(int i=0;i<keys.length;i++){
						if(id.equals(keys[i])){
							emp.setValue(keys[i], txt);
						}
					}
				}
				empsRtn.add(emp);
			}
		}catch(NullPointerException e){
			LogUtil.logError(_log, "Parse "+ keys.toString() +" from DOM fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}
		return empsRtn;
	}
	
	/**
	 * Get the employe ID of dept. assistant
	 * @param conn   Connection
	 * @param eid   employee ID
	 * @return EmployeeId  ex:T0001;T0002
	 * @throws SQLException
	 */
	private String getAssistantFromHRM(Connection conn, String eid) throws SQLException {
		String assis1 = "", assis2 = "";
		PreparedStatement ps = null;
		try{
			ResultSet rs;
			ps = conn.prepareStatement("select EyeSight, HealthStatus from employee where Code='"+eid+"' ");
			rs = ps.executeQuery();
			if (rs.next()) {
				assis1 = rs.getString("EyeSight") == null?"":rs.getString("EyeSight");
				assis2 = rs.getString("HealthStatus") == null?"":rs.getString("HealthStatus");
			}
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			_db.releasePrepareStatement(ps);
		}
		if(!assis1.equals("") && !assis2.equals(""))
			return assis1+";"+assis2;
		else if(!assis1.equals(""))
			return assis1;
		else if(!assis2.equals(""))
			return assis2;
		else
			return "0";
	}
	
	/**
	 * Get the Grade of Employee
	 * @param conn   Connection
	 * @param eid   employee ID
	 * @return grade
	 * @throws SQLException
	 */
	private String getGradeFromHRM(Connection conn, String eid) throws SQLException {
		String grade = "";
		PreparedStatement ps = null;
		try{
			ResultSet rs;
			ps = conn.prepareStatement("select Grade from employee where Code='"+eid+"' ");
			rs = ps.executeQuery();
			if (rs.next()) {
				grade = rs.getString("Grade") == null?"":rs.getString("Grade");
			}
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			_db.releasePrepareStatement(ps);
		}
		if(!grade.equals(""))
			return grade;
		
		return "0";
	}
	
	/**
	 * Get the deputy count from Employee ID
	 * @param conn   Connection
	 * @param eid   employee ID
	 * @return count of deputy
	 * @throws SQLException
	 */
	private String getDeputy(Connection conn, String eid) throws SQLException {
		String cnt = "";
		PreparedStatement ps = null;
		try{
			ResultSet rs;
			ps = conn.prepareStatement("select count(*) as cnt from users where id='"+ eid +"' and leaveDate is null ");
			rs = ps.executeQuery();
			if (rs.next()) {
				cnt = rs.getString("cnt") == null?"":rs.getString("cnt");
			}
		}catch (SQLException e){
			LogUtil.logError(_log, "Query DB fail.ErrMsg: " + e.getMessage());
			throw new EJBException(e.getMessage());
		}finally{
			_db.releasePrepareStatement(ps);
		}
		
		return cnt;
	}
	
	/**
	 * Update the title on EasyFlow proecess table
	 * @param pDataSource   ex:NaNaDS
	 * @param subject   the value of ProcessInstance table will be updated
	 * @param serialNumber    the key of ProcessInstance table will be updated
	 * @return
	 */
	private int updateProcessInstancebySerialNumberfromNANA(String pDataSource, String subject, String serialNumber) {
		if(pDataSource == null || pDataSource.equals("")){
			throw new IllegalArgumentException("pDataSource cannot be empty !");
		}
		if(subject == null || subject.equals("")){
			throw new IllegalArgumentException("subject cannot be empty !");
		}
		if(serialNumber == null || serialNumber.equals("")){
			throw new IllegalArgumentException("the serial number cannot be empty !");
		}
		_log.debug("pDataSource: " + pDataSource); 
		_log.debug("subject: " + subject); 
		_log.debug("serialNumber: " + serialNumber); 

		Connection conn = null;
		Statement statement = null;
		StringBuilder sb = new StringBuilder();
		int rs = 0;
		try {
			conn = _db.getJndiDataSource(pDataSource).getConnection();
			sb.append("UPDATE ProcessInstance ");
			sb.append("SET  subject='"+subject+"' ");
			//sb.append("SET  subject='"+subject+"' COLLATE Chinese_Taiwan_Stroke_CI_AS ");
			sb.append("WHERE serialNumber='"+serialNumber+"' ");
			statement = conn.createStatement();
			rs = statement.executeUpdate(sb.toString());
			_log.debug("update " + (rs>0?"success":"fail")+ " : " + sb.toString());
		}catch (SQLException e){
			LogUtil.logError(_log, "Update DB fail.ErrMsg: "+ sb.toString() +" : "+ e.getMessage());
			throw new EJBException(sb.toString() + e.getMessage());
		}finally{
			// Release DataSource to avoid EJB crash
			_db.releaseStatement(statement);
			_db.releaseConn(conn);
		}
		
		return rs;
	}
	
}
