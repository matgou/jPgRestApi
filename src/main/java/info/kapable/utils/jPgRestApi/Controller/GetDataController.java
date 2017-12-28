package info.kapable.utils.jPgRestApi.Controller;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import info.kapable.utils.jPgRestApi.Application;
import info.kapable.utils.jPgRestApi.ResponseFactory;
import info.kapable.utils.jPgRestApi.Exception.RequestBodyException;

/**
 * This controller return data from database with select query
 * 
 * @author mgoulin
 */
public class GetDataController extends Controller {
	private static final Logger LOG = LoggerFactory.getLogger(GetDataController.class);

	@Override
	public Response process(String uri, Map<String, String> headers, Map<String, String> parms, InputStream data)
			throws RequestBodyException {
		String[] uriParts = uri.split("/");
		if (uriParts.length == 3) {
			return this.getAllData(uriParts[2], parms);
		}
		
		return ResponseFactory.generateNotImplementedResponse();
	}

	private Response getAllData(String tableName, Map<String, String> parms) {
		try {
			List<Object> allData = new ArrayList<Object>();
			
			ResultSet data = this.DB.query("SELECT * FROM " + tableName);
			ResultSetMetaData metadata = data.getMetaData();
			
			while(data.next()) {
				Map<String, Object> aData = new HashMap<String, Object>();
				for(int i = 1; i <= metadata.getColumnCount(); i++) {
					if (metadata.getColumnType(i) == java.sql.Types.INTEGER) {
						aData.put(metadata.getColumnLabel(i), data.getInt(i));
					} else {
						aData.put(metadata.getColumnLabel(i), data.getString(i));
					}
				}
				allData.add(aData);
			}
			
			return ResponseFactory.generateResponse(Status.OK, allData);
		} catch (SQLException e) {
			ResponseFactory.newSQLError(e);
		}
		return null;
	}

}
