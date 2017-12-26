package info.kapable.utils.jPgRestApi.Database;

/**
 * This class represent a persistant connection to database
 * @author mgoulin
 *
 */
public class DBConnexion {
	private String jdbcString;
	private String username;
	private String password;
	
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
	
	
}
