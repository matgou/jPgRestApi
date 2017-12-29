package info.kapable.utils.jPgRestApi.Controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import info.kapable.utils.jPgRestApi.ApplicationTest;

/**
 * Test PUT on /tables URI This controller must add new table on database
 * 
 * @author matgo
 *
 */
public class GetDataControllerTest extends ApplicationTest {

	String url = "http://localhost:8080/data";

	@Test
	public void getAllDataTest() {
		CreateTable("table_test");
		InsertData("table_test", 1, "hello", "2012-12-12");
		InsertData("table_test", 2, "world", "2011-12-12");
		try {
			HttpGet getRequest = new HttpGet(url + "/table_test");
			HttpResponse response = client.execute(getRequest);
			assertEquals(response.getStatusLine().getStatusCode(), 200);
		
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			fail("ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException");
		}
	}
}
