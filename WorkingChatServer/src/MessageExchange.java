import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MessageExchange {

    private JSONParser jsonParser = new JSONParser();

    public String getToken(int index) {
        Integer number = index * 8 + 11;
        return "TN" + number + "EN";
    }

    public int getIndex(String token) {
        return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
    }

    public String getServerResponse(List<Message> messages) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messages", messages);
        //jsonObject.put("token", getToken(messages.size()));
        jsonObject.put("token", messages.size());
        return jsonObject.toJSONString();
    }

    public String getClientSendMessageRequest(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        return jsonObject.toJSONString();
    }

    public Message getClientMessage(InputStream inputStream) throws ParseException {
        return new Message(getJSONObject(inputStreamToString(inputStream)));
    }

    public JSONObject getJSONObject(String json) throws ParseException {
        return (JSONObject) jsonParser.parse(json);
    }

    public String inputStreamToString(InputStream in) {
    	StringBuilder sb = new StringBuilder();
    	try
    	{
	        InputStreamReader is = new InputStreamReader(in,"UTF-8");
	        BufferedReader br = new BufferedReader(is);
	        String read = null;
	        read = br.readLine();	
	        while (read != null) {
	            sb.append(read);
	            try {
	                read = br.readLine();
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	
	        }
    	}catch(IOException e){e.printStackTrace();}
        return sb.toString();
    }
}
