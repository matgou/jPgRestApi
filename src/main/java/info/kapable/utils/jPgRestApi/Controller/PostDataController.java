package info.kapable.utils.jPgRestApi.Controller;

import info.kapable.utils.jPgRestApi.Exception.RequestBodyException;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.iki.elonen.NanoHTTPD.Response;

/**
 * This controller return data from database with select query
 * 
 * @author mgoulin
 */
public class PostDataController extends Controller {
	private static final Logger LOG = LoggerFactory.getLogger(PostDataController.class);

	@Override
	public Response process(String uri, Map<String, String> headers, Map<String, String> parms, InputStream data)
			throws RequestBodyException {
		Map<String,Object> dataObject = parseJson(headers, data);
		this.testNotNull(dataObject);
		
		String[] uriParts = uri.split("/");
		if (uriParts.length == 3) {
			return this.insertData(uriParts[2], parms, dataObject);
		}
		throw new RequestBodyException("Invalid uri");
	}

	/**
	 * Insert data in tableName from input
	 * 
	 * @param tableName
	 * @param parms
	 * @param data
	 * @return
	 */
	private Response insertData(String tableName, Map<String, String> parms, Map<String,Object> data) {
		try {
			LOG.debug("insert data");
			List<String> columns = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			
			Iterator<Entry<String, Object>> it = data.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, Object> e = it.next();
				columns.add(e.getKey());
				
				if (e.getValue() instanceof String ){
					values.add("'" + (String) e.getValue() + "'");
				}else if(e.getValue() instanceof Integer){
					values.add(Integer.toString((Integer) e.getValue()));
				}
			}

			this.DB.query("INSERT INTO " + tableName + " (" + String.join(", ", columns) + ") VALUES (" + String.join(", ", values) + ")");
			
			return responseFactory.newSuccessMessage();
		} catch (SQLException e) {
			responseFactory.newSQLError(e);
		}
		return null;
	}

}
