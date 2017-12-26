package info.kapable.utils.jPgRestApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class store configuration
 * Config is load from properties file
 * @author mgoulin
 */
public class Configuration {
	 private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
	 
	/**
	 * Singleton
	 */
	private static Configuration INSTANCE = new Configuration();
	/**
	 * Properties databases
	 */
	private Properties prop = new Properties();	
	
	/**
	 * Constructor
	 */
	private Configuration() {
		LOG.debug("Load configuration ...");
		InputStream input = null;
		URL configtUrl = Configuration.class
                .getClassLoader().getResource("config.properties");
		try {
	        File configFile = new File(configtUrl.toURI());
			input = new FileInputStream(configFile);

			// load a properties file
			this.prop.load(input);

		} catch (IOException | URISyntaxException ex) {
			LOG.error("Error when loading : config.properties", ex);
			Application.exit(255);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/** 
	 * Access to singleton instance of Configuration 
	 * @return
	 */
    public static Configuration getInstance()
    {   return INSTANCE;
    }
	
}
