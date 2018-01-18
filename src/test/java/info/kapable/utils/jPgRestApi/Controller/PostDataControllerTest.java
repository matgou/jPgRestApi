package info.kapable.utils.jPgRestApi.Controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import info.kapable.utils.jPgRestApi.ApplicationTest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

/**
 * Test PUT on /tables URI This controller must add new table on database
 * 
 * @author mgoulin
 *
 */
public class PostDataControllerTest extends ApplicationTest {

	String url = "http://localhost:8080/api/data";

	@Test
	public void getAllDataTest() {
		CreateTable("table_post_data");
		InsertData("table_post_data", 1, "hello", "2012-12-12");
		InsertData("table_post_data", 2, "world", "2011-12-12");
		HttpPost postRequest = new HttpPost(url + "/table_post_data");
		String description = "hello world";
		String date = "2012-12-12";
		int id = 5;
		
		try {
			StringEntity input = new StringEntity("{" 
				+ "\"description\": \"" + description + "\", "
				+ "\"id\": " + id + ","
				+ "\"submission_date\": \"" + date + "\" "
				+ "}");

				input.setContentType("application/json");
				postRequest.setEntity(input);
				HttpResponse response = client.execute(postRequest);
				
				assertEquals(response.getStatusLine().getStatusCode(), 200);
				
				Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:jPgRestApi", "sa", "sa");
				Statement state = conn.createStatement();
				ResultSet result = state.executeQuery("SELECT description FROM table_post_data WHERE id = 5");
				result.next();
				assertEquals(result.getString(1), description);

			} catch (IOException e) {
				e.printStackTrace();
				fail("IOException during put");
			} catch (SQLException e) {
				e.printStackTrace();
				fail("SQLException during put");
			}
	}
}
