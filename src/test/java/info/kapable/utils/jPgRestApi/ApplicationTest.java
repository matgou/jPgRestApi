package info.kapable.utils.jPgRestApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

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
	 * Send request to create table
	 * 
	 * @param tableName
	 */
	protected void CreateTable(String tableName) {
	    client = HttpClients.createDefault();

		HttpPut putRequest = new HttpPut("http://localhost:8080/tables");
		try {
			StringEntity input = new StringEntity("{" 
					+ "\"name\": \"" + tableName + "\", " 
					+ "\"column\": {"
					+ "		\"id\":\"INTEGER NOT NULL\"," 
					+ "		\"description\":\"VARCHAR(25)\"" 
					+ "}" 
					+ "}");

			input.setContentType("application/json");
			putRequest.setEntity(input);
			client.execute(putRequest);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException during put");
		}
	}

	protected void InsertData(String tableName, int i, String string) {
	    client = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost("http://localhost:8080/data/" + tableName);
		try {
			StringEntity input = new StringEntity("{" 
					+ "\"description\": \"" + string + "\", "
					+ "\"id\": " + i 
					+ "}");

			input.setContentType("application/json");
			postRequest.setEntity(input);
			client.execute(postRequest);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException during put");
		}
	}
}