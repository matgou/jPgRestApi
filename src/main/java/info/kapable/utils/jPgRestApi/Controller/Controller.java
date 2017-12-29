package info.kapable.utils.jPgRestApi.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.iki.elonen.NanoHTTPD.Response;
import info.kapable.utils.jPgRestApi.ResponseFactory;
import info.kapable.utils.jPgRestApi.Database.DBConnexion;
import info.kapable.utils.jPgRestApi.Exception.RequestBodyException;

public abstract class Controller {
	private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

	protected DBConnexion DB;

	protected ResponseFactory RESPONSE_FACTORY;
	
	public abstract Response process(String uri, Map<String, String> headers,
			Map<String, String> parms, InputStream data) throws RequestBodyException;
	
	public Controller() {
		try {
			this.DB = DBConnexion.getInstance();
		} catch (SQLException e){
			LOG.error("SQLException in connexion to database : ", e);
		}
		
		this.RESPONSE_FACTORY = ResponseFactory.getInstance();
	}
	/**
	 * Return string from data
	 * @param data
	 * @return 
	 */
	protected String parseData(Map<String, String> headers, InputStream data) {
		Integer contentLength = Integer.parseInt(headers.get("content-length"));
		byte[] buffer = new byte[contentLength];
		try {
			data.read(buffer, 0, contentLength);
			return new String(buffer);
		} catch (IOException e) {
			LOG.error("Error when reading data : ", e);
		}
		return null;
	}
	
	/**
	 * Return Map from JSON Request body
	 * 
	 * @param headers
	 * @param data
	 * @return
	 */
	protected Map<String, Object> parseJson(Map<String, String> headers, InputStream data) {
		String dataString = this.parseData(headers, data);
		LOG.debug("Data : " + dataString);
	
	    TypeReference<HashMap<String,Object>> typeRef 
	            = new TypeReference<HashMap<String,Object>>() {};
	
	    ObjectMapper objectMapper = new ObjectMapper();
		try {
			HashMap<String,Object> dataObject = objectMapper.readValue(dataString, typeRef);
			return dataObject;
		} catch (IOException e) {
			LOG.error("Error when reading json data : ", e);
		}
		return null;
	}
	
	/**
	 * Test if object is not null
	 * @param dataObject
	 */
	protected void testNotNull(Map<String, Object> dataObject) throws RequestBodyException {
		if(dataObject == null) {
			throw new RequestBodyException("object is null");
		}
	}
	
	/**
	 * Test if object contains specified key
	 * @param dataObject
	 */
	protected void testContains(String text, Map<String, Object> dataObject) throws RequestBodyException {
		if(!dataObject.containsKey(text)) {
			throw new RequestBodyException(text + " is missing");
		}
	}
	
	
}
