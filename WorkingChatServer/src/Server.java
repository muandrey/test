import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
//import org.json.simple.parser.ParseException;







import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

public class Server implements HttpHandler {
    private List<Message> history = new ArrayList<Message>();
    private MessageExchange messageExchange = new MessageExchange();

    public static void main(String[] args) {
        Integer port;
        if (args.length != 1)
            port=80;
        else {
        	port = Integer.parseInt(args[0]);
        }
            try {
                System.out.println("Server is starting...");
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                System.out.println("Server started.");
                String serverHost = InetAddress.getLocalHost().getHostAddress();
                System.out.println("Get list of messages: GET http://" + serverHost + ":" + port + "?token={token}");
                System.out.println("Send message: POST http://" + serverHost + ":" + port + " provide body json in format {\"message\" : \"{message}\"} ");

                server.createContext("/", new Server());
                server.setExecutor(null);
                server.start();
            } catch (IOException e) {
                System.out.println("Error creating http server: " + e);
            }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";

        if ("GET".equals(httpExchange.getRequestMethod())) {
            response = doGet(httpExchange);
        } else if ("POST".equals(httpExchange.getRequestMethod())) {
            doPost(httpExchange);
        } else if ("DELETE".equals(httpExchange.getRequestMethod())) {
            doDelete(httpExchange);
        } else if ("PUT".equals(httpExchange.getRequestMethod())) {
            doPut(httpExchange);
        } else {
            response = "Unsupported http method: " + httpExchange.getRequestMethod();
        }

        sendResponse(httpExchange, response);
    }
    private String typeOf(File file)
    {
    	String str=file.toString();
    	if(str.indexOf(".css")>=0)  return "text/css";
    	if(str.indexOf(".js")>=0)  return "text/javascript";
    	return "text/plain";
    }
    private List<Message> getRightSLafterID(int index)
    {
    	if(index==0)
    		return history;
    	int i=0;
    	for(;i<history.size() && history.get(i).messageID!=index;i++)
    	{}
    	if(i<history.size() && history.get(i).Text=="")
    		return history.subList(i, history.size());
    	return history.subList(i+1, history.size());
    }
    
    
    private String doGet(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            Map<String, String> map = queryToMap(query);
            String token = map.get("token");
            System.out.println("Get request:token "+token+" recieved");
            if (token != null && !"".equals(token)) {
                int index = messageExchange.getIndex(token);
                return messageExchange.getServerResponse(getRightSLafterID(index));
            } else {
                return "Token query parameter is absent in url: " + query;
            }
        }
        StringBuilder resp= new StringBuilder(65536);
        String path=httpExchange.getRequestURI().getPath();
        if(path!=null && !path.equals("/"))
        {
        	System.out.println("Get request:path "+path+" recieved");
        	try {
        		byte arr[]= new byte[64*1024];
        		path="./chat"+path;
        		File f=new File(path);
        		String a= typeOf(f);
        		List<String> list=new ArrayList<String>();
        		list.add(a);
        		httpExchange.getResponseHeaders().put("Content-Type",list );
    			FileInputStream fi=new FileInputStream(f);
    			while(true)
    			{
	    			int r=fi.read(arr);
	    			if(r<1) break;
	    			resp.append(new String(arr,0,r));
    			}
    			fi.close();
    		} catch (Throwable e) {
    			return "could open file at this path";
    		}
            return  resp.toString();
        }
    	try {
    		System.out.println("Get request recieved");
    		byte arr[]= new byte[64*1024];
			FileInputStream fi=new FileInputStream("chat/homepage.html");
			while(true)
			{
    			int r=fi.read(arr);
    			if(r<1) break;
    			resp.append(new String(arr,0,r));
			}
			fi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return  resp.toString();
    }
    private void doPut(HttpExchange httpExchange)
    {
    	try {
            Message message = messageExchange.getEditedMessage(httpExchange.getRequestBody());
            System.out.println("Put Message from User "+message.NickName+" : " + message.Text);
            history.add(message);
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }
    private void doPost(HttpExchange httpExchange) {
        try {
            Message message = messageExchange.getClientMessage(httpExchange.getRequestBody());
            System.out.println("Post Message from User "+message.NickName+" : " + message.Text);
            history.add(message);
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }
    private void doDelete(HttpExchange httpExchange)
    {
    	String query= httpExchange.getRequestURI().getQuery();
    	if (query != null) {
            Map<String, String> map = queryToMap(query);
            String id = map.get("id");
            System.out.println("DELETE request:id= "+id+" recieved");
            if (id != null && !"".equals(id)) {
                int index =Integer.parseInt(id);
                delFromHist(index);
            }
        }
    }
    private void delFromHist(int id)
    {
    	int i=0;
    	for(;history.get(i).messageID!=id;i++)
    	{}
    	if(i<history.size())
    	{
    		history.remove(i);
    		history.add(new Message(id));
    	}
    }
    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes("UTF-8"));
        os.flush();
        os.close();
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
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
