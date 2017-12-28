package info.kapable.utils.jPgRestApi.Controller;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;
import info.kapable.utils.jPgRestApi.Application;
import info.kapable.utils.jPgRestApi.Configuration;

public class ControllerEngine {

	private static final Logger LOG = LoggerFactory.getLogger(ControllerEngine.class);
	Map<String, Controller> controllers = new HashMap<String, Controller>();
	
	/**
	 * Constructor for ControllerEngine
	 */
	public ControllerEngine() {
		try {
			this.initControllers();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			LOG.error("Error in configuration : ",e);
			Application.exit(255);
		}		
	}
	
	/**
	 * Using config.properties to populate controllersBag
	 *  
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private void initControllers() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Configuration config = Configuration.getInstance();
		Map<String,String> controllersString = config.getAll("query");
		Iterator<Entry<String, String>> it = controllersString.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String className = entry.getValue();
			LOG.debug("loading for key '" + key + "', class : '" + className + "'");
			
			Class<?> clazz = Class.forName(className);
			Constructor<?> ctor = clazz.getConstructor();
			Controller controller = (Controller) ctor.newInstance();
			this.controllers.put(key, controller);
		}
	}
	
	/**
	 * Return a Response after processing a query
	 * @param method
	 * @param uri
	 * @param headers
	 * @param parms
	 * @param data 
	 * @return
	 */
	public Response processQuery(Method method, String uri, Map<String, String> headers,
			Map<String, String> parms, InputStream data) {

		String controllerKey = "query." + method.toString().toLowerCase() + ".root";
		controllerKey = this.findControllerKey(controllerKey, controllerKey, method, uri);
		LOG.debug("Using controller : " + controllerKey);
		
		return this.controllers.get(controllerKey).process(uri, headers, parms, data);
	}

	private String findControllerKey(String currentControllerKey, String controllerKey, Method method, String uri) {
		Configuration config = Configuration.getInstance();
		if(!uri.contentEquals("/") && uri.length() > 1) {
			String[] uriParts = uri.split("/");
			if(config.get(controllerKey + "." + uriParts[1]) != null) {
				currentControllerKey = controllerKey + "." + uriParts[1];
			}
			LOG.debug("test controller : " + controllerKey + "." + uriParts[1]);
			return this.findControllerKey(currentControllerKey, controllerKey + "." + uriParts[1], method, uri.substring(uriParts[1].length() + 1));
		}
		return currentControllerKey;

	}

}
