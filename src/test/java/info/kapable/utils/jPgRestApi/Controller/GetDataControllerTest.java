package info.kapable.utils.jPgRestApi.Controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.either;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.kapable.utils.jPgRestApi.ApplicationTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test PUT on /tables URI This controller must add new table on database
 * 
 * @author mgoulin
 *
 */
public class GetDataControllerTest extends ApplicationTest {

	String url = "http://localhost:8080/api/data";

	@Test
	public void getAllDataTest() {
		CreateTable("table_test_all_data");
		InsertData("table_test_all_data", 1, "hello", "2012-12-12");
		InsertData("table_test_all_data", 2, "world", "2011-12-12");
		try {
			HttpGet getRequest = new HttpGet(url + "/table_test_all_data");
			HttpResponse response = client.execute(getRequest);
			assertEquals(response.getStatusLine().getStatusCode(), 200);
			ObjectMapper mapper = new ObjectMapper();

		    TypeReference<List<Object>> typeRef 
		            = new TypeReference<List<Object>>() {};
			List<Object> jsonList = mapper.readValue(response.getEntity().getContent(), typeRef);
			assertEquals(jsonList.size(), 2);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> object = (Map<String, Object>) jsonList.get(0);
			assertThat((String) object.get("DESCRIPTION"), either(containsString("hello")).or(containsString("world")));
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			fail("ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException");
		}
	}
	
	@Test
	public void getFilterDataTest() {
		CreateTable("table_test_filter_data");
		InsertData("table_test_filter_data", 1, "hello", "2012-12-12");
		InsertData("table_test_filter_data", 2, "world", "2011-12-12");
		try {
			HttpGet getRequest = new HttpGet(url + "/table_test_filter_data?id=1");
			HttpResponse response = client.execute(getRequest);
			assertEquals(response.getStatusLine().getStatusCode(), 200);
			ObjectMapper mapper = new ObjectMapper();

		    TypeReference<List<Object>> typeRef 
		            = new TypeReference<List<Object>>() {};
			List<Object> jsonList = mapper.readValue(response.getEntity().getContent(), typeRef);
			assertEquals(jsonList.size(), 1);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> object = (Map<String, Object>) jsonList.get(0);
			assertThat((String) object.get("DESCRIPTION"), containsString("hello"));
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			fail("ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException");
		}
	}

	@Test
	public void getTextFilterDataTest() {
		CreateTable("table_test_text_filter_data");
		InsertData("table_test_text_filter_data", 1, "hello", "2012-12-12");
		InsertData("table_test_text_filter_data", 2, "world", "2011-12-12");
		try {
			HttpGet getRequest = new HttpGet(url + "/table_test_text_filter_data?DESCRIPTION=%25l%25");
			HttpResponse response = client.execute(getRequest);
			assertEquals(response.getStatusLine().getStatusCode(), 200);
			ObjectMapper mapper = new ObjectMapper();

		    TypeReference<List<Object>> typeRef 
		            = new TypeReference<List<Object>>() {};
			List<Object> jsonList = mapper.readValue(response.getEntity().getContent(), typeRef);
			assertEquals(jsonList.size(), 2);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> object = (Map<String, Object>) jsonList.get(0);
			assertThat((String) object.get("DESCRIPTION"), either(containsString("hello")).or(containsString("world")));
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			fail("ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException");
		}
	}
	
	@Test
	public void getLimitDataTest() {
		CreateTable("table_test_limit_data");
		InsertData("table_test_limit_data", 1, "hello", "2012-12-12");
		InsertData("table_test_limit_data", 2, "world", "2011-12-12");
		InsertData("table_test_limit_data", 3, "world1", "2011-12-12");
		InsertData("table_test_limit_data", 4, "world2", "2011-12-12");
		InsertData("table_test_limit_data", 5, "world3", "2011-12-12");
		InsertData("table_test_limit_data", 6, "world4", "2011-12-12");
		try {
			HttpGet getRequest = new HttpGet(url + "/table_test_limit_data?_limit=3");
			HttpResponse response = client.execute(getRequest);
			assertEquals(response.getStatusLine().getStatusCode(), 200);
			ObjectMapper mapper = new ObjectMapper();

		    TypeReference<List<Object>> typeRef 
		            = new TypeReference<List<Object>>() {};
			List<Object> jsonList = mapper.readValue(response.getEntity().getContent(), typeRef);
			assertEquals(jsonList.size(), 3);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			fail("ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException");
		}
	}

	@Test
	public void getOrderByDataTest() {
		CreateTable("table_order_by_data");
		InsertData("table_order_by_data", 1, "hello", "2012-12-12");
		InsertData("table_order_by_data", 2, "world", "2011-12-12");
		InsertData("table_order_by_data", 3, "world1", "2011-12-12");
		InsertData("table_order_by_data", 4, "world2", "2011-12-12");
		InsertData("table_order_by_data", 5, "world3", "2011-12-12");
		InsertData("table_order_by_data", 6, "world4", "2011-12-12");
		try {
			HttpGet getRequest = new HttpGet(url + "/table_order_by_data?_orderby=id%20desc");
			HttpResponse response = client.execute(getRequest);
			assertEquals(response.getStatusLine().getStatusCode(), 200);
			ObjectMapper mapper = new ObjectMapper();

		    TypeReference<List<Object>> typeRef 
		            = new TypeReference<List<Object>>() {};
			List<Object> jsonList = mapper.readValue(response.getEntity().getContent(), typeRef);

			@SuppressWarnings("unchecked")
			Map<String, Object> object = (Map<String, Object>) jsonList.get(0);
			assertThat((String) object.get("DESCRIPTION"), containsString("world4"));
			
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			fail("ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException");
		}
	}
}
