package com.gseo.zk.util;

import com.dsc.nana.util.logging.NaNaLog;

/**
 * Log Utility
 * @author Zhengku
 * @since Sep 26, 2014
 */
public class LogUtil {

	
	/**
	 * Log out the error message
	 * @param log  NaNaLog
	 * @param msg  a message string
	 */
	public static void logError(NaNaLog log, String msg) {
		if(log.isErrorEnabled()){
			log.error(msg);
		}
	}
	
	/**
	 * Log out the warning message
	 * @param log  NaNaLog
	 * @param msg  a message string
	 */
	public static void logWarning(NaNaLog log, String msg) {
		if(log.isWarnEnabled()){
			log.warn(msg);
		}
	}
	
	/**
	 * Log out the message for debug
	 * @param log  NaNaLog
	 * @param msg  a message string
	 */
	public static void logDebug(NaNaLog log, String msg) {
		log.debug(msg);
	}
	
}
