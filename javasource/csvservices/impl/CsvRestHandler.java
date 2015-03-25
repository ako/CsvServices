package csvservices.impl;

import com.mendix.core.Core;
import com.mendix.core.CoreRuntimeException;
import com.mendix.externalinterface.connector.RequestHandler;
import com.mendix.logging.ILogNode;
import com.mendix.m2ee.api.IMxRuntimeRequest;
import com.mendix.m2ee.api.IMxRuntimeResponse;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.ISession;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by ako on 1/24/2015.
 */
public class CsvRestHandler extends RequestHandler {
    public static String serviceRoot = "csv/";
    private static ILogNode logger = Core.getLogger(CsvRestHandler.class.getName());
    private final IContext context;

    public CsvRestHandler(IContext context) {
        logger.info("CsvRestHandler");
        this.context = context;
    }

    @Override
    protected void processRequest(IMxRuntimeRequest iMxRuntimeRequest, IMxRuntimeResponse iMxRuntimeResponse, String s) throws Exception {
        /*
         * Validate credentials
         */
        IContext context = validateCredentials(iMxRuntimeRequest.getHttpServletRequest());
        if (context == null) {
            iMxRuntimeResponse.setStatus(401);
            return;
        }
        /*
         * get path
         */
        String pathInfo = iMxRuntimeRequest.getHttpServletRequest().getPathInfo().replace("/" + CsvRestHandler.serviceRoot, "");
        logger.info("pathInfo: " + pathInfo);
        String[] path = pathInfo.split("[\\/\\?\\#]");
        for (int i = 0; i < path.length; i++) {
            logger.info("path, " + i + " " + path[i]);
        }
        Writer writer = iMxRuntimeResponse.getWriter();
        /*
         * save all objects
         */
        logger.info("method: " + iMxRuntimeRequest.getHttpServletRequest().getMethod());
        if (iMxRuntimeRequest.getHttpServletRequest().getMethod().equals("POST")) {
            // inserting new objects
            CsvImporter importer = new CsvImporter();
            importer.csvToEntities(context, writer, path[0], path[1], iMxRuntimeRequest.getInputStream());
        } else if (iMxRuntimeRequest.getHttpServletRequest().getMethod().equals("GET")) {
            /*
             * retrieve all objects from entities, return as csv (comma separated values)
             */
            try {
                iMxRuntimeResponse.setContentType("text/csv");
                CsvExporter exporter = new CsvExporter();
                exporter.entityToCsv(context, writer, path[0], path[1]);
            } catch (CoreRuntimeException e) {
                logger.error(stacktraceToString(e));
                logger.error("Error while retrieving specified objects: " + e.getMessage());
                iMxRuntimeResponse.setStatus(500);
                writer.write(e.getMessage());
            } catch (Exception e) {
                logger.error(stacktraceToString(e));
                logger.error("Error while retrieving specified objects: " + e.getMessage());
                iMxRuntimeResponse.setStatus(500);
                writer.write(e.getMessage());
            }
        }

        /*
         * Finish session
         */
        Core.logout(context.getSession());
    }


    /*
     * Validate mendix username password using basic authentication
     */
    private IContext validateCredentials(HttpServletRequest httpServletRequest) {
        boolean hasCredentials = false;
        String authHeader = httpServletRequest.getHeader("Authorization");
        String username = null;
        String password = null;
        ISession session = null;
        IContext context = null;
        logger.info("authorization: " + authHeader);
        if (authHeader != null && authHeader.trim().startsWith("Basic")) {
            String base64Credentials = authHeader.trim().substring("Basic".length()).trim();
            String base64 = new String(javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Credentials));
            logger.info("credentials: " + base64);
            String[] parts = base64.split(":");
            username = parts[0];
            password = parts[1];
        }
        logger.info("username: " + username + "," + password);
        if (username != null) {
            try {
                session = Core.login(username, password);
                context = session.createContext();
            } catch (Exception e) {
            }
        }
        return context;
    }

    private String stacktraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
