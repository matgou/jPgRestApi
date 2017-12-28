package info.kapable.utils.jPgRestApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

import static org.junit.Assert.*;

import java.io.IOException;

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
}