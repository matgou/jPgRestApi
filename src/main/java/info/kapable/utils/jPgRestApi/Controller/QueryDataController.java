package info.kapable.utils.jPgRestApi.Controller;

import info.kapable.utils.jPgRestApi.Exception.RequestBodyException;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class QueryDataController extends Controller {
	private static final Logger LOG = LoggerFactory.getLogger(QueryDataController.class);

	@Override
	public Response process(String uri, Map<String, String> headers, Map<String, String> parms, InputStream data) throws RequestBodyException {
		Map<String,Object> dataObject = parseJson(headers, data);
		this.testNotNull(dataObject);
		this.testContains("query", dataObject);
		String sql = (String) dataObject.get("query");
		
		LOG.info("query : " + sql );
		
		// Commit query
		try {
			ResultSet resultSet = this.DB.query(sql);

			// convert resultSet to List object
			List<Object> allData = this.DB.resultSetToList(resultSet);
			return responseFactory.generateResponse(Status.OK, allData);
		} catch(SQLException e) {
			LOG.error("Error in sql processing", e);
			return responseFactory.newSQLError(e);
		}
		
	}
}
