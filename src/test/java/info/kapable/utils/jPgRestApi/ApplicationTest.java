package info.kapable.utils.jPgRestApi;
import static org.junit.Assert.fail;
import info.kapable.utils.jPgRestApi.Controller.GetTablesController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationTest {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationTest.class);
	
	protected CloseableHttpClient client;
	/**
	 * setUp
	 */
	@Before
	public void setUp() {
		String args[] = null;
	    client = HttpClients.createDefault();
		Application.main(args);
	}

	/**
	 * tearDown
	 */
	@After
	public void tearDown() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Application.getInstance().stop();	
	}
	
	/**
	 * Query database to create table
	 * 
	 * @param tableName
	 */
	protected void CreateTable(String tableName) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:jPgRestApi", "sa", "sa");
			Statement state = conn.createStatement();
			String query = "CREATE TABLE " + tableName + " ( id INTEGER NOT NULL,description VARCHAR(25),submission_date DATE );";
			LOG.info("Create table query : " + query);
			state.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to init table");
		}
	}

	/**
	 * Query database to insert data
	 * 
	 * @param tableName
	 */
	protected void InsertData(String tableName, int id, String description, String date) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:jPgRestApi", "sa", "sa");
			Statement state = conn.createStatement();
			String query = "INSERT INTO " + tableName + " (submission_date, description, id) VALUES ('" + date + "', '" + description + "', " + id +")";
			LOG.info("INSERT query : " + query);
			state.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to init table");
		}
	}
}