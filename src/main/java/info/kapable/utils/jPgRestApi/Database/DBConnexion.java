package info.kapable.utils.jPgRestApi.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import info.kapable.utils.jPgRestApi.Configuration;

/**
 * This class represent a persistant connection to database
 * @author mgoulin
 *
 */
public class DBConnexion {
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
	    Statement state = conn.createStatement();
	    return state.executeQuery(sql);
	}
	
	
}
