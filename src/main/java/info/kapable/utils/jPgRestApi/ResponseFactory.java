package info.kapable.utils.jPgRestApi;

import info.kapable.utils.jPgRestApi.Exception.RequestBodyException;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;

/**
 * Static method to convert object to Response
 * @author mgoulin
 */
public class ResponseFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ResponseFactory.class);

	// Singleton
	private static ResponseFactory INSTANCE = new ResponseFactory();

	// ObjectMapper
    private ObjectMapper objectMapper;
    
    /**
     * Constructor to init objectMapper
     */
    private ResponseFactory() {
    	objectMapper = new ObjectMapper();
    	objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    }
    
    public static ResponseFactory getInstance() {
    	return INSTANCE;
    }
    
	/**
	 * From Exception return FixedLenghtResponse
	 * 
	 * @param e
	 * @return
	 */
	public Response newSQLError(SQLException e) {
	    Map<String, Object> errorObject = new HashMap<String,Object>();
	    errorObject.put("ErrorString", e.getMessage());

		return generateResponse(Status.BAD_REQUEST, errorObject);
	}

	/**
	 * Return a generic message to confirm success of operation
	 * 
	 * @return
	 */
	public Response newSuccessMessage() {
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
	public Response newBadRequestException(RequestBodyException e) {
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
	public Response generateResponse(IStatus status, Map<String, Object> object) {
		try {
			String jsonString = objectMapper.writeValueAsString(object);
			LOG.debug("Response (" + status.toString() + ") => " + jsonString);
			return NanoHTTPD.newFixedLengthResponse(status, "application/json", jsonString);
		} catch (JsonProcessingException e) {
			LOG.error("Error in JsonProcessing", e);
		}
		return null;
	}

	public Response generateResponse(IStatus status, List<Object> object) {
		try {
			String jsonString = objectMapper.writeValueAsString(object);
			LOG.debug("Response (" + status.toString() + ") => " + jsonString);
			return NanoHTTPD.newFixedLengthResponse(status, "application/json", jsonString);
		} catch (JsonProcessingException e) {
			LOG.error("Error in JsonProcessing", e);
		}
		return null;
	}

	public Response generateNotImplementedResponse() {
	    Map<String, Object> errorObject = new HashMap<String,Object>();
	    errorObject.put("ErrorString", "Not Implemented");

		String jsonString;
		try {
			jsonString = objectMapper.writeValueAsString(errorObject);
			return NanoHTTPD.newFixedLengthResponse(Status.NOT_IMPLEMENTED, "application/json", jsonString);
		} catch (JsonProcessingException e) {
			LOG.error("Error in JsonProcessing", e);
		}
		return null;
	}
}
