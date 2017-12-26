package info.kapable.utils.jPgRestApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Application {
	 private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	 
	/**
	 * Entry point to start application
	 */
	public static void main(String[] args) {
		Application application = new Application();
		application.Run(args);
	}


	/**
	 * Run application
	 */
	private void Run(String[] args) {
		LOG.info("jPgRestApi starting ...");
		// TODO Auto-generated method stub
		Application.exit(0);
	}


	public static void exit(int i) {
		LOG.info("jPgRestApi exit RC=" + i);
		System.exit(i);
	}
}