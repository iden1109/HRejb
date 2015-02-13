package com.gseo.zk.util;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.dsc.nana.util.logging.NaNaLog;

/**
 * Handle Database
 * @author Zhengku
 * @since Oct 7, 2014
 */
public class DBUtil {

	private NaNaLog _log;
	
	public DBUtil(NaNaLog log) {
		this._log = log;
	}

	
	/**
	 * Initial a DataSource object for DB operation
	 * @param datasource  ex: java:comp/env/jdbc/DBPool
	 * @return DataSource
	 * @throws ServiceException
	 */
	public DataSource getJndiDataSource(String datasource) throws EJBException {
		if(datasource == null || datasource.equals("")){
			throw new IllegalArgumentException("datasource cannot be empty !");
		}
		DataSource rtnDS = null;
		Context ctx = null;
		try {
			ctx = new InitialContext();
			rtnDS = (DataSource) ctx.lookup("java:/"+datasource);
		} catch (NamingException e) {
			LogUtil.logError(_log, "Get data source  " + datasource + " error.ErrMsg:" + e.getMessage());
			throw new EJBException(e.getMessage());
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				ctx = null;
			}
		}
		
		if (rtnDS == null) {
			throw new EJBException("Can not get DataSource from jndi name " + datasource);
		}
		return rtnDS;
	}
	
	public void releasePrepareStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception ex) {
				LogUtil.logError(_log, "(" + this.hashCode() + ")Close PreparedStatement Fail!! ErrMsg:" + ex.getMessage());
			}
			ps = null;
		}
	}

	public void releaseConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception ex) {
				LogUtil.logError(_log, "(" + this.hashCode() + ") Close Connection Fail!!");
			}
			conn = null;
		}
	}
}
