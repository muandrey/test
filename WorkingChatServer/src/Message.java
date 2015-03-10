import java.io.Serializable;
import java.util.Date;

import org.json.simple.JSONObject;


public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private static int sid=0;
	int messageID;
	String NickName;
	String Text;
	String addedDate;
	Message(JSONObject obj)
	{
		messageID=++sid;
		NickName=(String) obj.get("NickName");
		Text=(String) obj.get("Text");
		addedDate=(String) obj.get("addedDate");
		System.out.println("got message: "+Text);
	}
	public String toString()
	{
		/*return "{\"messageID\": "+messageID+
				",\"NickName\": "+NickName+
				",\"Text\": "+Text+
				",\"addedDate\": "+addedDate+"}";*/
		JSONObject js=new JSONObject();
		js.put("messageID", messageID);
		js.put("NickName", NickName);
		js.put("Text",Text);
		js.put("addedDate",addedDate);
		return js.toJSONString();
	}
}
