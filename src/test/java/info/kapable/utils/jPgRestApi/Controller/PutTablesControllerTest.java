package info.kapable.utils.jPgRestApi.Controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.kapable.utils.jPgRestApi.ApplicationTest;

/**
 * Test PUT on /tables URI This controller must add new table on database
 * 
 * @author matgo
 *
 */
public class PutTablesControllerTest extends ApplicationTest {

	String url = "http://localhost:8080/tables";

	@Test
	public void errorInJsonTest() {
		HttpPut putRequest = new HttpPut(url);
		try {
			StringEntity input = new StringEntity("{" 
					+ "\"name\": \"table_test2\", " 
					+ "\"column\": {"
					+ "		\"id\":\"NOT_EXISTING_TYPE NOT NULL\"," 
					+ "		\"description\":\"VARCHAR(25)\"" 
					+ "}" 
					+ "}");

			input.setContentType("application/json");
			putRequest.setEntity(input);
			// Execute the method.
			CloseableHttpResponse response = client.execute(putRequest);
			assertNotEquals(response.getStatusLine().getStatusCode(), 200);
			ObjectMapper mapper = new ObjectMapper();

		    TypeReference<HashMap<String,Object>> typeRef 
		            = new TypeReference<HashMap<String,Object>>() {};
			Map<String, Object> jsonMap = mapper.readValue(response.getEntity().getContent(), typeRef);
			
			assertThat((String) jsonMap.get("ErrorString"), CoreMatchers.containsString("NOT_EXISTING_TYPE"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException during put");
		}
	}

	/**
	 * Basic case
	 */
	@Test
	public void generalRestClientTest() {
		HttpPut putRequest = new HttpPut(url);
		try {
			StringEntity input = new StringEntity("{" 
					+ "\"name\": \"table_test\", " 
					+ "\"column\": {"
					+ "		\"id\":\"INTEGER NOT NULL\"," 
					+ "		\"description\":\"VARCHAR(25)\"" 
					+ "}" 
					+ "}");

			input.setContentType("application/json");
			putRequest.setEntity(input);
			// Execute the method.
			CloseableHttpResponse response = client.execute(putRequest);
			assertEquals(response.getStatusLine().getStatusCode(), 200);

			Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:jPgRestApi", "sa", "sa");
			Statement state = conn.createStatement();
			state.executeQuery("SELECT * FROM table_test");

		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException during put");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("SQL Exception");
		}
	}

	@Test
	public void incompleteRequestTest() {
		HttpPut putRequest = new HttpPut(url);
		try {
			StringEntity input = new StringEntity("{" 
					+ "\"column\": {"
					+ "		\"id\":\"INTEGER NOT NULL\"," 
					+ "		\"description\":\"VARCHAR(25)\"" 
					+ "}" 
					+ "}");

			input.setContentType("application/json");
			putRequest.setEntity(input);
			// Execute the method.
			CloseableHttpResponse response = client.execute(putRequest);
			assertNotEquals(response.getStatusLine().getStatusCode(), 200);
			ObjectMapper mapper = new ObjectMapper();

		    TypeReference<HashMap<String,Object>> typeRef 
		            = new TypeReference<HashMap<String,Object>>() {};
			Map<String, Object> jsonMap = mapper.readValue(response.getEntity().getContent(), typeRef);
			
			assertThat((String) jsonMap.get("ErrorString"), CoreMatchers.containsString("name"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException during put");
		}
	}
}
