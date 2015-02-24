/*
 * Generated by XDoclet - Do not edit!
 */
package com.gseo.zk.hr;

/**
 * Remote interface for Leave.
 * @since Sep 26, 2014 <!-- end-user-doc --> <!-- begin-xdoclet-definition -->
 * @generated 
 * @wtp generated
 */
public interface Leave
   extends javax.ejb.EJBObject
{
   /**
    * <!-- begin-xdoclet-definition -->
    * @generated     */
   public java.lang.String foo( java.lang.String param )
      throws java.rmi.RemoteException;

   /**
    * Retrieve the formInstance object contains LevelName, isManager and EmployeeType tag <!-- begin-xdoclet-definition -->
    * @generated 
    * @param pDataSource ex:NaNaDS
    * @param pFormInstance provide the value of "ESSQJ008" and "ESSQJ014"
    * @return FormInstance this object appends some new tag with <pre>{@code <LevelName id="LevelName" dataType="java.lang.Integer">12</LevelName> <isManager id="isManager" dataType="java.lang.String">N</isManager> <EmployeeType id="EmployeeType" dataType="java.lang.String">Responsibility</EmployeeType> <Level1LeaderCode id="Level1LeaderCode" dataType="java.lang.String">T0001</Level1LeaderCode> <Level2LeaderCode id="Level2LeaderCode" dataType="java.lang.String">T0002</Level2LeaderCode> <DeputyEmployeeId id="DeputyEmployeeId" dataType="java.lang.String">1</DeputyEmployeeId> }</pre>    */
   public com.dsc.nana.domain.form.FormInstance retrieveLevelName( java.lang.String pDataSource,com.dsc.nana.domain.form.FormInstance formInstance )
      throws java.rmi.RemoteException;

   /**
    * For TEST response of retrieveLevelName(); <!-- begin-xdoclet-definition -->
    * @generated 
    * @param pDataSource ex:NaNaDs
    * @param employeeID
    * @prarm orgID
    * @return string <LevelName><isManager><EmployeeType><Level1LeaderCode><Level2LeaderCode>    */
   public java.lang.String retrieveLevelName( java.lang.String pDataSource,java.lang.String eid,java.lang.String oid )
      throws java.rmi.RemoteException;

   /**
    * Retrieve the formInstance object contains a signed member group of ID and userName <!-- begin-xdoclet-definition -->
    * @generated 
    * @param pDataSource ex:NaNaDS
    * @param pFormInstance must contain the value of "ESSXJ025" and "ESSXJ008"
    * @return FormInstance this object appends some new tag with <pre>{@code <SignedMembers id="SignedMembers" dataType="java.lang.String">T0001_empNameA;T0002_empNameB;T0003_empNameC;</SignedMembers> <Level1LeaderCode id="Level1LeaderCode" dataType="java.lang.String">T0001</Level1LeaderCode> <Level2LeaderCode id="Level2LeaderCode" dataType="java.lang.String">T0002</Level2LeaderCode> }</pre>    */
   public com.dsc.nana.domain.form.FormInstance retrieveSignedMembers( java.lang.String pDataSource,com.dsc.nana.domain.form.FormInstance pFormInstance )
      throws java.rmi.RemoteException;

   /**
    * Retrieve the formInstance object contains Level1LeaderCode tag <!-- begin-xdoclet-definition -->
    * @generated 
    * @param pFormInstance provide the value of "ESSXJ004"
    * @return FormInstance this object appends some new tag with <pre>{@code <Level1LeaderCode id="Level1LeaderCode" dataType="java.lang.String">T0001</Level1LeaderCode> <Level2LeaderCode id="Level2LeaderCode" dataType="java.lang.String">T0002</Level2LeaderCode> }</pre>    */
   public com.dsc.nana.domain.form.FormInstance retrieveLevel1LeaderCode( com.dsc.nana.domain.form.FormInstance formInstance )
      throws java.rmi.RemoteException;

   /**
    * For TEST response of retrieveLevel1LeaderCode() <!-- begin-xdoclet-definition -->
    * @generated 
    * @param employeeID
    * @return leaderCode    */
   public java.lang.String retrieveLevel1LeaderCode( java.lang.String employeeID )
      throws java.rmi.RemoteException;

   /**
    * Retrieve the formInstance object contains some tags like return value <!-- begin-xdoclet-definition -->
    * @generated 
    * @param pDataSource ex:NaNaDS
    * @param pFormInstance the DOM object contains ESSJBNXX005 tag
    * @return FormInstance this object appends some new tag with <pre>{@code <LevelValue id="LevelValue" dataType="java.lang.String">12</LevelValue> <Level1LeaderCode id="Level1LeaderCode" dataType="java.lang.String">T0001</Level1LeaderCode> <Level2LeaderCode id="Level2LeaderCode" dataType="java.lang.String">T0002</Level2LeaderCode> <DirInDir id="DirInDir" dataType="java.lang.String">Direct</DirInDir> <DeptDescribe id="DeptDescribe" dataType="java.lang.String">Dept Class</DeptDescribe> <DirectLeaderCodes id="DirectLeaderCodes" dataType="java.lang.String">T0001;T0002;T0003</DirectLeaderCodes> <OvertimeName id="OvertimeName" dataType="java.lang.String">Zhengku..</OvertimeName> <OvertimeHours id="OvertimeHours" dataType="java.lang.String">3..</OvertimeHours> <OvertimeReason id="OvertimeReason" dataType="java.lang.String">Reason1..</OvertimeReason> <ESSJBN015 id="ESSJBN015" dataType="java.lang.String">John, 0.5hrs, Reason:reason1</ESSJBN015> }</pre>    */
   public com.dsc.nana.domain.form.FormInstance retrieveForOvertime( java.lang.String pDataSource,com.dsc.nana.domain.form.FormInstance formInstance )
      throws java.rmi.RemoteException;

   /**
    * For TEST response of retrieveForOvertime() <!-- begin-xdoclet-definition -->
    * @generated 
    * @param pDataSource ex:NaNaDS
    * @param xml
    * @return OverTime    */
   public com.gseo.zk.hr.model.OverTime retrieveForOvertime( java.lang.String pDataSource,java.lang.String xml )
      throws java.rmi.RemoteException;

   /**
    * Retrieve the formInstance object contains some tags like return value <!-- begin-xdoclet-definition -->
    * @generated 
    * @param pDataSource ex:NaNaDS
    * @param pFormInstance the DOM object contains ESSBGRY005 tag
    * @return FormInstance this object appends some new tag with <pre>{@code <LevelValue id="LevelValue" dataType="java.lang.String">12</LevelValue> <Level1LeaderCode id="Level1LeaderCode" dataType="java.lang.String">T0001</Level1LeaderCode> <Level2LeaderCode id="Level2LeaderCode" dataType="java.lang.String">T0002</Level2LeaderCode> <DirInDir id="DirInDir" dataType="java.lang.String">Direct</DirInDir> <DeptDescribe id="DeptDescribe" dataType="java.lang.String">Dept Class</DeptDescribe> <DirectLeaderCodes id="DirectLeaderCodes" dataType="java.lang.String">T0001;T0002;T0003</DirectLeaderCodes> }</pre>    */
   public com.dsc.nana.domain.form.FormInstance retrieveForChangeWorkshift( java.lang.String pDataSource,com.dsc.nana.domain.form.FormInstance formInstance )
      throws java.rmi.RemoteException;

   /**
    * For TEST response of retrieveForChangeWorkshift() <!-- begin-xdoclet-definition -->
    * @generated 
    * @param pDataSource ex:NaNaDS
    * @param xml
    * @return OverTime    */
   public com.gseo.zk.hr.model.OverTime retrieveForChangeWorkshift( java.lang.String pDataSource,java.lang.String xml )
      throws java.rmi.RemoteException;

   /**
    * Retrieve the employeeID of Dept. Assistant <!-- begin-xdoclet-definition -->
    * @generated 
    * @param pDataSource ex:HRMDS
    * @param pFormInstance provide the value of "ESSQJ008"
    * @return FormInstance this object appends some new tag with <pre>{@code <Assistant id="Assistant" dataType="java.lang.String">T0001;T0002</Assistant> }</pre>    */
   public com.dsc.nana.domain.form.FormInstance retrieveAssistant( java.lang.String pDataSource,com.dsc.nana.domain.form.FormInstance formInstance )
      throws java.rmi.RemoteException;

   /**
    * Retrieve the employeeID of Dept. Assistant <!-- begin-xdoclet-definition -->
    * @generated 
    * @param pDataSource ex:HRMDS
    * @param empId Employee ID
    * @return assistants a string likes T0001;T0002    */
   public java.lang.String retrieveAssistant( java.lang.String pDataSource,java.lang.String empId )
      throws java.rmi.RemoteException;

}
