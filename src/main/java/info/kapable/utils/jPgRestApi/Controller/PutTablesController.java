package info.kapable.utils.jPgRestApi.Controller;

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
import info.kapable.utils.jPgRestApi.ResponseFactory;
import info.kapable.utils.jPgRestApi.Exception.RequestBodyException;

public class PutTablesController extends Controller {
	private static final Logger LOG = LoggerFactory.getLogger(PutTablesController.class);

	@Override
	public Response process(String uri, Map<String, String> headers, Map<String, String> parms, InputStream data) throws RequestBodyException {
		Map<String,Object> dataObject = parseJson(headers, data);
		this.testNotNull(dataObject);
		this.testContains("name", dataObject);
		this.testContains("column", dataObject);
		
		LOG.info("data : " + dataObject);
		StringBuilder queryString = new StringBuilder();
		queryString.append("CREATE TABLE ");
		queryString.append(dataObject.get("name") + " ( ");
		@SuppressWarnings("unchecked")
		Map<String,Object> columns = (Map<String,Object>) dataObject.get("column");
		Iterator<Entry<String, Object>> it = columns.entrySet().iterator();
		List<String> columnsQuery = new ArrayList<String>();
		while(it.hasNext()) {
			Entry<String, Object> e = it.next();
			columnsQuery.add(e.getKey() + " " + e.getValue());
		}
		queryString.append(String.join(",", columnsQuery));
		queryString.append(" );");
		
		// Commit query
		try {
			this.DB.query(queryString.toString());
			
		} catch(SQLException e) {
			LOG.error("Error in sql processing", e);
			return RESPONSE_FACTORY.newSQLError(e);
		}
		return RESPONSE_FACTORY.newSuccessMessage();
	}
}
