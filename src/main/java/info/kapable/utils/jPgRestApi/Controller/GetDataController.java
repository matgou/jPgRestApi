package info.kapable.utils.jPgRestApi.Controller;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		
		return responseFactory.generateNotImplementedResponse();
	}

	/**
	 * Return all data from table
	 * 
	 * @param tableName the tablename to query
	 * @param parms query parameters
	 * @return
	 */
	private Response getAllData(String tableName, Map<String, String> parms) {
		try {
			// Extract from parms condition as SQL 
			List<String> conditionsSQL = new ArrayList<String>();
			String orderBySQL = "";
			Iterator<Entry<String, String>> it = parms.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				if(!e.getKey().startsWith("_")) {
					if(e.getValue().contains("%")) {
						conditionsSQL.add(e.getKey() + " LIKE '" + e.getValue() + "'");
					} else {
						conditionsSQL.add(e.getKey() + " = '" + e.getValue() + "'");
					}
				} else if(!e.getKey().contentEquals("_orderby")) {
					orderBySQL = " ORDER BY " + e.getValue();
				}
			}

			String sql = "SELECT * FROM " + tableName;
			
			// Add condition if present
			if(conditionsSQL.size() > 0) {
				sql = sql + " WHERE " + String.join(" AND ", conditionsSQL);
			}
			
			// Add order by
			sql = sql + orderBySQL;
			
			// perform query
			ResultSet data = this.DB.query(sql);
			
			// convert resultSet to List object
			List<Object> allData = this.DB.resultSetToList(data);
			
			return responseFactory.generateResponse(Status.OK, allData);
		} catch (SQLException e) {
			return responseFactory.newSQLError(e);
		}
	}

}
