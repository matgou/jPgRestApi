package info.kapable.utils.jPgRestApi.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.kapable.utils.jPgRestApi.Application;
import info.kapable.utils.jPgRestApi.Configuration;

/**
 * This class represent a persistant connection to database
 * @author mgoulin
 *
 */
public class DBConnexion {
	private static final Logger LOG = LoggerFactory.getLogger(DBConnexion.class);

	private static DBConnexion INSTANCE;
	
	private String jdbcString;
	private String username;
	private String password;

	private Connection conn;
	
	private DBConnexion(String jdbcString, String jdbcUser, String jdbcPassword) throws SQLException {
		this.jdbcString = jdbcString;
		this.username = jdbcUser;
		this.password = jdbcPassword;
		
	    conn = DriverManager.getConnection(this.jdbcString, this.username, this.password);
	}

	public static DBConnexion getInstance() throws SQLException {
		if(INSTANCE == null) {
			Configuration config = Configuration.getInstance();
			INSTANCE = new DBConnexion(config.get("jdbcString"), config.get("jdbcUser"), config.get("jdbcPassword"));
		}
		return INSTANCE;
	}
	
	/**
	 * @return the jdbcString
	 */
	public String getJdbcString() {
		return jdbcString;
	}
	/**
	 * @param jdbcString the jdbcString to set
	 */
	public void setJdbcString(String jdbcString) {
		this.jdbcString = jdbcString;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public ResultSet query(String sql) throws SQLException {
		LOG.debug("Executing query : " + sql);
	    Statement state = conn.createStatement();
	    return state.executeQuery(sql);
	}

	/**
	 * Convert a SQL ResultSet to a List
	 * 
	 * @param data
	 * @return
	 * @throws SQLException
	 */
	public List<Object> resultSetToList(ResultSet data) throws SQLException {
		ResultSetMetaData metadata = data.getMetaData();		
		List<Object> allData = new ArrayList<Object>();
		while(data.next()) {
			Map<String, Object> aData = new HashMap<String, Object>();
			for(int i = 1; i <= metadata.getColumnCount(); i++) {
				if (metadata.getColumnType(i) == java.sql.Types.INTEGER) {
					aData.put(metadata.getColumnLabel(i), data.getInt(i));
				} else if (metadata.getColumnType(i) == java.sql.Types.DATE) {
					aData.put(metadata.getColumnLabel(i), data.getDate(i));
				} else {
					aData.put(metadata.getColumnLabel(i), data.getString(i));
				}
			}
			allData.add(aData);
		}
		
		return allData;
	}
	
	
}
