package info.kapable.utils.jPgRestApi.Controller;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import info.kapable.utils.jPgRestApi.Configuration;
import info.kapable.utils.jPgRestApi.Exception.RequestBodyException;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetTablesController extends Controller {

	private static final Logger LOG = LoggerFactory.getLogger(GetTablesController.class);
	
	@Override
	public Response process(String uri, Map<String, String> headers,
			Map<String, String> parms, InputStream data)
			throws RequestBodyException {
		String[] uriParts = uri.split("/");
		if (uriParts.length == 3) {
			return this.getHeadersOfTable(uriParts[2], null, parms);
		} else if (uriParts.length == 4) {
			return this.getHeadersOfTable(uriParts[2], uriParts[3], parms);
		}
		
		return responseFactory.generateNotImplementedResponse();
	}

	private Response getHeadersOfTable(String table_name, String schema_name, Map<String, String> parms) {
		String sql = "";
		String jdbcString = Configuration.getInstance().get("jdbcString");
		LOG.info(jdbcString);
		sql = this.getQueryForPostgres(table_name, schema_name);
		
		// perform query
		try {
			ResultSet data = this.DB.query(sql);
			
			// convert resultSet to List object
			List<Object> allHeaders = this.DB.resultSetToList(data);
			
			return responseFactory.generateResponse(Status.OK, allHeaders);
		} catch (SQLException e) {
			return responseFactory.newSQLError(e);
		}
	}
	
	private String getQueryForPostgres(String table_name, String schema_name) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COLUMN_NAME, DTD_IDENTIFIER FROM INFORMATION_SCHEMA.COLUMNS ");
		query.append("WHERE upper(TABLE_NAME) LIKE '");
		query.append(table_name.toUpperCase());
		if(schema_name != null) {
			query.append(" AND upper(schema_name) = '");
			query.append(schema_name.toUpperCase());
		}
		query.append("' ORDER BY COLUMN_NAME ASC;");
		
		return query.toString();
	}

}
