/**
 * IntegrationCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package cn.com.dcms.core.generalservice;

/**
 * IntegrationCallbackHandler Callback class, Users can extend this class and
 * implement their own receiveResult and receiveError methods.
 */
public abstract class IntegrationCallbackHandler {

	protected Object clientData;

	/**
	 * User can pass in any object that needs to be accessed once the
	 * NonBlocking Web service call is finished and appropriate method of this
	 * CallBack is called.
	 * 
	 * @param clientData
	 *            Object mechanism by which the user can pass in user data that
	 *            will be avilable at the time this callback is called.
	 */
	public IntegrationCallbackHandler(Object clientData) {
		this.clientData = clientData;
	}

	/**
	 * Please use this constructor if you don't want to set any clientData
	 */
	public IntegrationCallbackHandler() {
		this.clientData = null;
	}

	/**
	 * Get the client data
	 */

	public Object getClientData() {
		return clientData;
	}

	/**
	 * auto generated Axis2 call back method for callbackSrv method override
	 * this method for handling normal response from callbackSrv operation
	 */
	public void receiveResultcallbackSrv(
			cn.com.dcms.core.generalservice.IntegrationStub.CallbackSrvResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from callbackSrv operation
	 */
	public void receiveErrorcallbackSrv(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for integrationByXmlWithLanguage
	 * method override this method for handling normal response from
	 * integrationByXmlWithLanguage operation
	 */
	public void receiveResultintegrationByXmlWithLanguage(
			cn.com.dcms.core.generalservice.IntegrationStub.IntegrationByXmlWithLanguageResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from integrationByXmlWithLanguage operation
	 */
	public void receiveErrorintegrationByXmlWithLanguage(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for dbConnectionString method
	 * override this method for handling normal response from dbConnectionString
	 * operation
	 */
	public void receiveResultdbConnectionString(
			cn.com.dcms.core.generalservice.IntegrationStub.DbConnectionStringResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from dbConnectionString operation
	 */
	public void receiveErrordbConnectionString(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for integrationGP method override
	 * this method for handling normal response from integrationGP operation
	 */
	public void receiveResultintegrationGP(
			cn.com.dcms.core.generalservice.IntegrationStub.IntegrationGPResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from integrationGP operation
	 */
	public void receiveErrorintegrationGP(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for checkPortalUserPassword method
	 * override this method for handling normal response from
	 * checkPortalUserPassword operation
	 */
	public void receiveResultcheckPortalUserPassword(
			cn.com.dcms.core.generalservice.IntegrationStub.CheckPortalUserPasswordResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from checkPortalUserPassword operation
	 */
	public void receiveErrorcheckPortalUserPassword(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for autoTriggerForHuman method
	 * override this method for handling normal response from
	 * autoTriggerForHuman operation
	 */
	public void receiveResultautoTriggerForHuman(
			cn.com.dcms.core.generalservice.IntegrationStub.AutoTriggerForHumanResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from autoTriggerForHuman operation
	 */
	public void receiveErrorautoTriggerForHuman(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for syncProd method override this
	 * method for handling normal response from syncProd operation
	 */
	public void receiveResultsyncProd(
			cn.com.dcms.core.generalservice.IntegrationStub.SyncProdResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from syncProd operation
	 */
	public void receiveErrorsyncProd(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for hRFinacialGateWay method
	 * override this method for handling normal response from hRFinacialGateWay
	 * operation
	 */
	public void receiveResulthRFinacialGateWay(
			cn.com.dcms.core.generalservice.IntegrationStub.HRFinacialGateWayResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from hRFinacialGateWay operation
	 */
	public void receiveErrorhRFinacialGateWay(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for invokeSrv method override this
	 * method for handling normal response from invokeSrv operation
	 */
	public void receiveResultinvokeSrv(
			cn.com.dcms.core.generalservice.IntegrationStub.InvokeSrvResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from invokeSrv operation
	 */
	public void receiveErrorinvokeSrv(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for integrationByXml method
	 * override this method for handling normal response from integrationByXml
	 * operation
	 */
	public void receiveResultintegrationByXml(
			cn.com.dcms.core.generalservice.IntegrationStub.IntegrationByXmlResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from integrationByXml operation
	 */
	public void receiveErrorintegrationByXml(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for mCloudGateWay method override
	 * this method for handling normal response from mCloudGateWay operation
	 */
	public void receiveResultmCloudGateWay(
			cn.com.dcms.core.generalservice.IntegrationStub.MCloudGateWayResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from mCloudGateWay operation
	 */
	public void receiveErrormCloudGateWay(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for hRGateWay method override this
	 * method for handling normal response from hRGateWay operation
	 */
	public void receiveResulthRGateWay(
			cn.com.dcms.core.generalservice.IntegrationStub.HRGateWayResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from hRGateWay operation
	 */
	public void receiveErrorhRGateWay(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for getHRPowerWhereSql method
	 * override this method for handling normal response from getHRPowerWhereSql
	 * operation
	 */
	public void receiveResultgetHRPowerWhereSql(
			cn.com.dcms.core.generalservice.IntegrationStub.GetHRPowerWhereSqlResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from getHRPowerWhereSql operation
	 */
	public void receiveErrorgetHRPowerWhereSql(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for integrationByXmlWithLanguageEx
	 * method override this method for handling normal response from
	 * integrationByXmlWithLanguageEx operation
	 */
	public void receiveResultintegrationByXmlWithLanguageEx(
			cn.com.dcms.core.generalservice.IntegrationStub.IntegrationByXmlWithLanguageExResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from integrationByXmlWithLanguageEx operation
	 */
	public void receiveErrorintegrationByXmlWithLanguageEx(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for syncAccountData method override
	 * this method for handling normal response from syncAccountData operation
	 */
	public void receiveResultsyncAccountData(
			cn.com.dcms.core.generalservice.IntegrationStub.SyncAccountDataResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from syncAccountData operation
	 */
	public void receiveErrorsyncAccountData(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for invokeMdm method override this
	 * method for handling normal response from invokeMdm operation
	 */
	public void receiveResultinvokeMdm(
			cn.com.dcms.core.generalservice.IntegrationStub.InvokeMdmResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from invokeMdm operation
	 */
	public void receiveErrorinvokeMdm(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for hRReportGateWay method override
	 * this method for handling normal response from hRReportGateWay operation
	 */
	public void receiveResulthRReportGateWay(
			cn.com.dcms.core.generalservice.IntegrationStub.HRReportGateWayResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from hRReportGateWay operation
	 */
	public void receiveErrorhRReportGateWay(java.lang.Exception e) {
	}

}
