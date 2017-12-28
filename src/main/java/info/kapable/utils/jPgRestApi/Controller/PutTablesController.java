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

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

public class PutTablesController extends Controller {
	private static final Logger LOG = LoggerFactory.getLogger(PutTablesController.class);

	@Override
	public Response process(String uri, Map<String, String> headers, Map<String, String> parms, InputStream data) {
		Map<String,Object> dataObject = parseJson(headers, data);
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
			return null;
		}
		return NanoHTTPD.newFixedLengthResponse("<html><body>"+dataObject+"</body></html>\n");
	}
}