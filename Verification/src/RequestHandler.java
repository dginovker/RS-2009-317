import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import util.Constants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        if (t.getRequestMethod().equalsIgnoreCase("GET")) {
            try {
                handleRequest(t);
            } catch (IOException e) {
                sendResponse(t, Constants.REQUEST_ERROR);
                e.printStackTrace();
            }
        } else {
            sendResponse(t, Constants.INVALID_REQUEST);
        }
    }

    private void handleRequest(HttpExchange http) throws IOException {
        if (http.getRequestURI().getQuery() == null || http.getRequestURI().getQuery().trim().equalsIgnoreCase("null")) {
            System.out.println("Returned without params");
            sendResponse(http, Constants.INVALID_REQUEST);
            return;
        }

        Map<String, String> params = queryToMap(http.getRequestURI().getQuery());

        boolean verified;
        try {
            verified = Validation.validateRequest(RequestType.forName(params.get("type")), params.get("hash"));
        } catch (NoSuchAlgorithmException e) {
            sendResponse(http, Constants.REQUEST_ERROR);
            e.printStackTrace();
            return;
        } catch (FileNotFoundException e) {
            sendResponse(http, Constants.NO_FILE_ERROR);
            System.err.println("File '" + RequestType.forName(params.get("type")).getFilePath() + "' not found!");
            return;
        }

        String response = verified ? Constants.VALIDATED_REQUEST : Constants.REQUEST_DENIED;
        sendResponse(http, response);
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }

}
