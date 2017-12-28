package info.kapable.utils.jPgRestApi.Controller;

import java.io.InputStream;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

public class RootController extends Controller {
	@Override
	public Response process(String uri, Map<String, String> headers, Map<String, String> parms, InputStream data) {
		return NanoHTTPD.newFixedLengthResponse("<html><body>jPgRestApi</body></html>\n");
	}
}
