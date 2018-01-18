package info.kapable.utils.jPgRestApi.Controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.kapable.utils.jPgRestApi.ApplicationTest;

public class GetTablesControllerTest extends ApplicationTest {

	String url = "http://localhost:8080/api/tables";
	
	@Test
	public void getColumnOfTableTest() {
		CreateTable("get_column_of_tables");
		try {
			HttpGet getRequest = new HttpGet(url + "/get_column_of_tables");
			HttpResponse response = client.execute(getRequest);
			assertEquals(response.getStatusLine().getStatusCode(), 200);
			ObjectMapper mapper = new ObjectMapper();

		    TypeReference<List<Object>> typeRef 
		            = new TypeReference<List<Object>>() {};
			List<Object> jsonList = mapper.readValue(response.getEntity().getContent(), typeRef);
			assertEquals(jsonList.size(), 3);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> object = (Map<String, Object>) jsonList.get(0);
			assertThat((String) object.get("COLUMN_NAME"), containsString("DESCRIPTION"));
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			fail("ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException");
		}
	}
}
