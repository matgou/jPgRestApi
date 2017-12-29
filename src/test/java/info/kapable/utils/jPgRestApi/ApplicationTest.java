package info.kapable.utils.jPgRestApi;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;

public class ApplicationTest {
	
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
			state.executeQuery("CREATE TABLE " + tableName + " ( id INTEGER NOT NULL,description VARCHAR(25),submission_date DATE );");
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
			state.executeQuery("INSERT INTO " + tableName + " (submission_date, description, id) VALUES ('" + date + "', '" + description + "', " + id +")");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to init table");
		}
	}
}