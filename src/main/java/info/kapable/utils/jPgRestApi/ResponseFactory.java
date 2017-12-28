package info.kapable.utils.jPgRestApi;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import info.kapable.utils.jPgRestApi.Exception.RequestBodyException;

/**
 * Static method to convert object to Response
 * @author mgoulin
 */
public class ResponseFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ResponseFactory.class);
	
	// ObjectMapper
    private static ObjectMapper objectMapper = new ObjectMapper();
    
	/**
	 * From Exception return FixedLenghtResponse
	 * 
	 * @param e
	 * @return
	 */
	public static Response newSQLError(SQLException e) {
	    Map<String, Object> errorObject = new HashMap<String,Object>();
	    errorObject.put("ErrorString", e.getMessage());

		return generateResponse(Status.BAD_REQUEST, errorObject);
	}

	/**
	 * Return a generic message to confirm success of operation
	 * 
	 * @return
	 */
	public static Response newSuccessMessage() {
	    Map<String, Object> confirmObject = new HashMap<String,Object>();
	    confirmObject.put("Message", "Operation performed");
		return generateResponse(Status.OK, confirmObject);
	}

	/**
	 * Return a error message in case of badRequest
	 * 
	 * @param e
	 * @return
	 */
	public static Response newBadRequestException(RequestBodyException e) {
	    Map<String, Object> errorObject = new HashMap<String,Object>();
	    errorObject.put("ErrorString", e.getMessage());

		return generateResponse(Status.BAD_REQUEST, errorObject);
	}

	/**
	 * Generate a response from a java Map
	 * 
	 * @param status
	 * @param object
	 * @return
	 */
	public static Response generateResponse(IStatus status, Map<String, Object> object) {
		try {
			String jsonString = objectMapper.writeValueAsString(object);
			LOG.debug("Response (" + status.toString() + ") => " + jsonString);
			return NanoHTTPD.newFixedLengthResponse(status, "application/json", jsonString);
		} catch (JsonProcessingException e) {
			LOG.error("Error in JsonProcessing", e);
		}
		return null;
	}
}
