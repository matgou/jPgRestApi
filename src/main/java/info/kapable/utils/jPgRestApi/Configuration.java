package info.kapable.utils.jPgRestApi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
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
	private static Configuration INSTANCE;
	/**
	 * Properties databases
	 */
	private Properties prop = new Properties();	
	
	/**
	 * Constructor
	 */
	private Configuration() {
		LOG.debug("Load configuration ...");
		InputStream configMain = null;
		try {
			configMain = Configuration.class
	                .getClassLoader().getResourceAsStream("config.properties");

			// load a properties file
			this.prop.load(configMain);

		} catch (IOException ex) {
			LOG.error("Error when loading : config.properties", ex);
			Application.exit(255);
		} finally {
			if (configMain != null) {
				try {
					configMain.close();
				} catch (IOException e) {
					LOG.error("Error when loading : config.properties", e);
					Application.exit(255);
				}
			}
		}
	}
	
	/** 
	 * Access to singleton instance of Configuration 
	 * @return
	 */
    public static Configuration getInstance()
    {
    	if(INSTANCE == null) {
    		INSTANCE = new Configuration();
    	}
    	return INSTANCE;
    }

	public String get(String string) {
		return this.prop.getProperty(string);
	}

	public Map<String, String> getAll(String string) {
		Map<String, String> returnMap = new HashMap<String, String>();
		for (Object keyObj : this.prop.keySet()) {
			String key = (String) keyObj;
		    if (!key.startsWith(string))
		        continue;
		    
		    String value = (String) this.prop.get(key);
		    returnMap.put(key, value);
		}
		return returnMap;
	}

	/**
	 * Override properties with propertiesFilePath
	 * 
	 * @param propertiesFilePath
	 */
	public void addConfig(String propertiesFilePath) {
		try {
			Properties customProp = new Properties();
			customProp.load(new FileInputStream(propertiesFilePath));
			// Merge
			Properties merged = new Properties();
			merged.putAll(this.prop);
			merged.putAll(customProp);
			
			this.prop = merged;
		} catch (FileNotFoundException e) {
			LOG.error("Unable to find : " + propertiesFilePath, e);
			Application.exit(255);
		} catch (IOException e) {
			LOG.error("Unable to open : " + propertiesFilePath, e);
			Application.exit(255);
		}

	}
	
}
