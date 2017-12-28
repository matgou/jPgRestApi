package info.kapable.utils.jPgRestApi.Controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import info.kapable.utils.jPgRestApi.ApplicationTest;

/**
 * Test PUT on /tables URI This controller must add new table on database
 * 
 * @author matgo
 *
 */
public class PutTablesControllerTest extends ApplicationTest {
	@Test
	public void generalRestClientTest() {
		String url = "http://localhost:8080/tables";
		HttpPut putRequest = new HttpPut(url);

		StringEntity input;
		try {
			input = new StringEntity("{\"name\": \"table_test\", \"column\": { \"id\":\"INTEGER NOT NULL\",\"description\":\"VARCHAR(25)\"}}");

			input.setContentType("application/json");
			putRequest.setEntity(input);
			// Execute the method.
			try {
				CloseableHttpResponse response = client.execute(putRequest);
				assertEquals(response.getStatusLine().getStatusCode(), 200);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("IOException during put");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			fail("Error in test definition");
		}
	}

}
