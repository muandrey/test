import java.io.InputStream;
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
	String mod="false";
	private Message()
	{
	}
	Message(JSONObject obj)
	{
		messageID=++sid;
		NickName=(String) obj.get("NickName");
		Text=(String) obj.get("Text");
		addedDate=(String) obj.get("addedDate");
	}
	Message(int id)
	{
		messageID=id;
		Text="";
		NickName="Deleter";
		addedDate="now";
	}
	public String toString()
	{
		JSONObject js=new JSONObject();
		js.put("messageID", messageID);
		js.put("NickName", NickName);
		js.put("Text",Text);
		js.put("addedDate",addedDate);
		js.put("mod", mod);
		return js.toJSONString();
	}
	public static Message getEditedMessage(JSONObject obj)
	{
		Message res=new Message();
		res.messageID=Integer.parseInt((String)obj.get("messageID"));
		res.NickName=(String) obj.get("NickName");
		res.Text=(String) obj.get("Text");
		res.addedDate=(String) obj.get("addedDate");
		return res;
	}
}
