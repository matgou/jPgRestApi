package info.kapable.utils.jPgRestApi.Controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.kapable.utils.jPgRestApi.ApplicationTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test PUT on /tables URI This controller must add new table on database
 * 
 * @author matgo
 *
 */
public class QueryDataControllerTest extends ApplicationTest {

	String url = "http://localhost:8080/api/query";

	@Test
	public void queryWithJoinTest() {
		CreateTable("table_join_query1");
		InsertData("table_join_query1", 1, "hello1", "2012-12-12");
		InsertData("table_join_query1", 2, "world1", "2011-12-12");
		
		CreateTable("table_join_query2");
		InsertData("table_join_query2", 1, "hello2", "2012-12-12");
		InsertData("table_join_query2", 2, "world2", "2011-12-12");
		HttpPost postRequest = new HttpPost(url);
		try {
			StringEntity input = new StringEntity("{" 
					+ "\"query\": \"SELECT t1.ID id, t1.DESCRIPTION d1, t2.DESCRIPTION d2 FROM table_join_query1 t1 JOIN table_join_query2 t2 ON (t1.id=t2.id)\""
					+ "}");

			input.setContentType("application/json");
			
			postRequest.setEntity(input);
			// Execute the method.
			CloseableHttpResponse response = client.execute(postRequest);
			assertEquals(response.getStatusLine().getStatusCode(), 200);
			
			ObjectMapper mapper = new ObjectMapper();

			TypeReference<List<Object>> typeRef 
            	= new TypeReference<List<Object>>() {};
			List<Object> jsonList = mapper.readValue(response.getEntity().getContent(), typeRef);
			assertEquals(jsonList.size(), 2);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> object = (Map<String, Object>) jsonList.get(0);
			assertThat((String) object.get("D1"), containsString("hello1"));
			assertThat((String) object.get("D2"), containsString("hello2"));

			@SuppressWarnings("unchecked")
			Map<String, Object> object2 = (Map<String, Object>) jsonList.get(1);
			assertThat((String) object2.get("D1"), containsString("world1"));
			assertThat((String) object2.get("D2"), containsString("world2"));
	
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException during put");
		}
	}
	@Test
	public void errorQueryTest() {
		HttpPost postRequest = new HttpPost(url);
		try {
			StringEntity input = new StringEntity("{" 
					+ "\"query\": \"Siiiii\""
					+ "}");

			input.setContentType("application/json");
			
			postRequest.setEntity(input);
			// Execute the method.
			CloseableHttpResponse response = client.execute(postRequest);
			assertNotEquals(response.getStatusLine().getStatusCode(), 200);
			ObjectMapper mapper = new ObjectMapper();

		    TypeReference<HashMap<String,Object>> typeRef 
		            = new TypeReference<HashMap<String,Object>>() {};
			Map<String, Object> jsonMap = mapper.readValue(response.getEntity().getContent(), typeRef);
			
			assertThat((String) jsonMap.get("ErrorString"), CoreMatchers.containsString("SIIIII"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException during put");
		}
	}
}
