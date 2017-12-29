package info.kapable.utils.jPgRestApi;

import info.kapable.utils.jPgRestApi.Controller.ControllerEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.iki.elonen.NanoHTTPD;

/**
 * This Application is a gateway
 * to translate rest query to database query
 * @author mgoulin
 *
 */
public class Application extends NanoHTTPD {
	
	private static Application application;
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	private ControllerEngine controllerEngine;
	 
	/**
	 * Entry point to start application
	 */
	public static void main(String[] args) {
		Application.application = new Application(Integer.parseInt(Configuration.getInstance().get("port")));
		application.Run(args);
	}

	/**
	 * Constructor
	 * @param port
	 */
	public Application(int port) {
		super(port);
		LOG.info("jPgRestApi starting on port : "+port);
	}
	
	/**
	 * Run application
	 */
	public void Run(String[] args) {
	    CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		Option config = Option.builder("c")
			    .longOpt( "config" )
			    .desc( "use custom properties file" )
			    .hasArg()
			    .argName( "CONFIG" )
			    .build();
		options.addOption(config);
		
        try {
			CommandLine line = parser.parse( options, args );
			if( line.hasOption( "config" ) ) {
				// use specific properties
				Configuration.getInstance().addConfig(line.getOptionValue( "config" ));
			}
	        try {
	        	this.controllerEngine = new ControllerEngine();
				start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
				
				LOG.info("Running! Point your browsers to http://localhost:8080/ ");
			} catch (IOException e) {
				LOG.error("IOException => ", e);
				Application.exit(0);
			}
		} catch (ParseException e1) {
			LOG.error("ParseException => ", e1);
			Application.exit(255);
		}
	}


    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> parms = session.getParms();
        Method method = session.getMethod();
        Map<String, String> headers = session.getHeaders();
        InputStream data = session.getInputStream();
        String uri = session.getUri();
        LOG.info(method + " " + uri + "?" + session.getQueryParameterString());
        return this.controllerEngine.processQuery(method, uri, headers, parms, data);
    }
	
	/**
	 * Exit
	 * @param i
	 */
	public static void exit(int i) {
		LOG.info("jPgRestApi exit RC=" + i);
		System.exit(i);
	}

	/**
	 * Return singleton of application
	 * @return
	 */
	public static Application getInstance() {
		if(Application.application == null) {
			Application.application = new Application(Integer.parseInt(Configuration.getInstance().get("port")));
		}
		return Application.application;
	}
}