package ws;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.PrintWriter;
import java.util.List;
import java.io.*;
import javax.json.*;
import javax.json.JsonValue.ValueType;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.*;
import java.text.*;
import org.apache.catalina.connector.Response;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import credential.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import beans.MessAttri;
/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = -2831353241859419298L;
    private static final Logger LOGGER = Logger.getLogger(TestServlet.class.getName());
	private String MqttContent; 
	private String MqttBroker;
	private ArrayList<String> MqttTopic;
    private String ClientId;
	private MemoryPersistence persistence;
    private MqttClient MainClient;
	private MqttConnectOptions connOpts;

	private ServletConfig Config;
	private ServletContext Context;
	private String debug_info = null;
	//those two lists must be synchronized , any desynchronizations will cause unexpected behavior  
	private ArrayList<JsonObject>  JsonList = null;                    //save the mqtt json packet
	private ArrayList<MessAttri>   InfoList =  null;                         //save the mqttmessage a
	@Override
    public void init() throws ServletException {
		super.init();
		String topics = GetInitParam("topic","test");		
		MqttTopic = new ArrayList<String>(Arrays.asList(topics.split(";")));
	    MqttBroker = "tcp://" + GetInitParam("host","localhost") + ":1883";
	    System.out.println(MqttBroker);
	    ClientId = "WebEndClient";
	    persistence = new MemoryPersistence();
	    try {
		    MainClient = new MqttClient(MqttBroker, ClientId, persistence); 
		    connOpts = new MqttConnectOptions();
		    connOpts.setUserName(Info.Get_USERNAME());
		    connOpts.setPassword(Info.Get_PASSWORD());
		    connOpts.setCleanSession(false);
		    connOpts.setConnectionTimeout(0);
            MainClient.connect(connOpts);	
            MainClient.setCallback(new CusCb());
            MainClient.subscribe("test");
	    }
	    catch(MqttException me)
	    {

	    	me.printStackTrace();
	    }
	}
    	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	class CusCb implements MqttCallback
    {
		@Override 
		public void messageArrived(String topic, MqttMessage message) throws Exception
		{
			if(getServletContext().getAttribute("JsonList") != null)
			{
				JsonList = (ArrayList<JsonObject>)getServletContext().getAttribute("JsonList");
				InfoList = (ArrayList<MessAttri>)getServletContext().getAttribute("InfoList");
				
			}
			else
			{
				JsonList = new ArrayList<JsonObject>();
				InfoList = new ArrayList<MessAttri>();
				
			}
			if(ValidJson(message))
			{
			    JsonReader jsonReader = Json.createReader(new ByteArrayInputStream(message.getPayload()));
				JsonObject json_message = jsonReader.readObject();
                JsonList.add(0,json_message);
				InfoList.add(0,new MessAttri(message.getId(),topic,message.isDuplicate(),message.isRetained(),GetTimeStamp()));
			}
		    Config = getServletConfig();
		    Context = Config.getServletContext();
		    Context.setAttribute("JsonList", JsonList);
		    Context.setAttribute("InfoList", InfoList);
            JsonList = null;
		    InfoList = null;
		}
		
		@Override
		public void deliveryComplete(IMqttDeliveryToken token)
		{
			
		
		}
		
		
		@Override
		public void connectionLost(Throwable cause)
		{
		   if(!MqttTopic.isEmpty())
		   {
			  MqttTopic.clear();
		   }
		   cause.printStackTrace(System.out);
		   System.out.println("The client has lose its connection to broker, the cause is: ");
           System.out.println(cause.toString());	
           System.out.println("end of exception");
		}	
	}
	
	
	private String GetTimeStamp()
	{
		ZoneId zoneId = ZoneId.of("America/Los_Angeles");
		ZonedDateTime timestamp = ZonedDateTime.now(zoneId);
		return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH'h'mm'm'ss's'SSS'ms'"));		
	}
	
	
	private String GetInitParam(String name,String DefVal)
	{
		String result = getInitParameter(name);
		if(result == null)
		{
			return DefVal;
		}
		return result;
	}
	
	//connect to t
	private boolean Connect_Broker(String url, String Username, String password, String SetCleanSession)
	{
		if(url == null)
		{
			debug_info = "empty url, please re-enter";
			return false;
		}
		
		MqttBroker = url;
	    connOpts = new MqttConnectOptions();
        System.out.println(connOpts.getUserName());
		if(MainClient == null)
		{
			try
			{
		      MainClient = new MqttClient(MqttBroker, ClientId, persistence); 
			}
			catch(MqttException e)
			{
				debug_info = e.toString();
				return false;
			}
		}
		else
		{
			if(MainClient.isConnected())
			{
				try
				{
				   MainClient.disconnectForcibly();
				}
				catch(MqttException e)
				{
				   try
				   {
					   MainClient.close();
					   
				   }
				   catch(MqttException ex)
				   {
					   MainClient = null;
				   }
					
				}
			}
			else
			{
				  try
				  {
					  MainClient.close();
					   
				  }
				  catch(MqttException ex)
				  {
					  MainClient = null;
				  }		
			}
			try
			{
		      MainClient = new MqttClient(MqttBroker, ClientId, persistence); 
			}
			catch(MqttException e)
			{
				debug_info = e.toString();
				return false;
			}
			catch(Exception e)
			{
				debug_info = e.toString();
				return false;
			}
		}
		try
        {
        	if(Username != null && Username != "")
        	{ 
        		connOpts.setUserName(Username);
			
        	}
        	if(password != null && password != "")
        	{
        		connOpts.setPassword(password.toCharArray());
        	}
        }
        catch(Exception e)
        {
        	debug_info = e.toString();
        	return false;
        }
		if(SetCleanSession != null)
		{
			if(SetCleanSession.equals("yes")) 		connOpts.setCleanSession(true);
			else connOpts.setCleanSession(false);
		}
		try
		{
			MainClient.connect(connOpts);
            MainClient.setCallback(new CusCb());
		}
		catch(MqttException e)
		{
			debug_info = e.toString();
			return false;
		}
		return true;
	}
	
	private boolean Subscribe_topic(String topic, boolean choice)
	{
		if(topic == null || topic == "")
		{
			debug_info = "empty topic input";
			return false;
		}
		if(MainClient == null)
		{
			debug_info = "No Mqtt Broker Connected";
			return false;
		}
		if(!MainClient.isConnected())
		{
			debug_info = "broker connection timed out";
			return false;
		}
		try
		{
			String[] data = topic.split(";");

			if(choice)
			{
				
				
				
				MainClient.subscribe(data);
						
			}
			else
			{
				MainClient.unsubscribe(data);
			}

		}
		catch(MqttException e)
		{
			debug_info = e.toString();
			return false;
		}
		String[] data = topic.split(";");
		if(choice)
		{
			for(int i = 0; i < data.length; i++)
			{
				if(!MqttTopic.contains(data[i]))
				{
					MqttTopic.add(data[i]);
				}
			}
		}
		else
		{
			if(MqttTopic.isEmpty())
			{
				debug_info = "no topic subscribed!";
				return false;
			}
			System.out.println("check0");
			for(int i = 0; i < data.length; i++)
			{
				if(MqttTopic.contains(data[i]))
				{
					MqttTopic.remove(data[i]);
				}
			}
		}
		return true;
	}
	
	private String ClientStatus()
	{
		String temp = "Currently Connected to broker at: <br>";
		if(MainClient != null)
		{
			if(MainClient.isConnected())
			{
			   return temp + MainClient.getCurrentServerURI();
				
			}
			else
			{
				try{
					MainClient.close();
					
				}
				catch(MqttException e)
				{
					MainClient = null;
					connOpts = null;
				}
				return "No Mqtt Broker Connected";
			}
		}
		else
		{
			return "No Mqtt Broker Connected";		
		}
	}
	
	private String[] get_topics()
	{
		if(MainClient == null)
		{
			MqttTopic.clear();
			return new String[]{"Currently No broker connected"};
			
		}
		else
		{
			if(MainClient.isConnected())
			{
				if(MqttTopic != null)
				{
					String[] temp = new String[MqttTopic.size()];
					for(int i = 0; i < MqttTopic.size();i++)
					{
						temp[i] = MqttTopic.get(i);
					}
					return temp;
				}
				else
				{
					return null;
				}
			}
			else{
				MqttTopic.clear();
				return new String[]{"broker connection timed out"};
			}
			
		}
		
		
	}
	
	
	private Boolean Publish_message(String topics, String Message, String QoS, String retained){
		if(topics == null || topics == "")
		{
			debug_info = "empty topic input";
			return false;
		}
		if(MainClient == null)
		{
			debug_info = "No Mqtt Broker Connected";
			return false;
		}
		if(!MainClient.isConnected())
		{
			debug_info = "broker connection timed out";
			return false;
		}
		try
		{
			String data[] = topics.split(";");
			int oslvl = Integer.parseInt(QoS);
			boolean retain = true;
			JsonObject temp_object = Json.createObjectBuilder().add("content", Message).build();
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Json.createWriter(stream).write(temp_object);
			byte[] msg_sent = stream.toByteArray();


			if(retained.equals("no"))
			{
				retain = false;
				
			}
			for(int i = 0; i < data.length; i++)
			{
				MainClient.publish(data[i], msg_sent ,oslvl,retain);
			}
		}
		catch(MqttException e)
		{
			debug_info = e.toString();
			return false;
		}
		catch(Exception e)
		{
			debug_info = e.toString();
			return false;
		}
		return true;
	}
	
	
	
	
	private Boolean ValidJson(MqttMessage message){
		try{
			JsonReader jsonReader = Json.createReader(new ByteArrayInputStream(message.getPayload()));
			JsonObject light_bulb = jsonReader.readObject();
		}
		catch(Exception ex)
		{		
			return false;
		}
		return true;
	}
	
	private Boolean ValidIndex(int size, String input)
	{
		if(input == null)
		{
			return false;
		}
		int temp = 0;
		try
		{
			temp = Integer.parseInt(input);
			
		}
		catch(Exception e)
		{
			return false;
		}
		if (temp > size-1 || temp < 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
		out.println("<html>\n<head>\n<title>\n MQTT Message Testing \n</title>\n</head>\n<body>\n");
		out.println("\n<p>" + tempmessage + "\n</p>\n");
		out.println("\n</body>" +  "\n</html>\n");
		out.flush();
		out.close();
		*/
		// traverse the json message
		// read parameter from the request
		debug_info = null;
	    try    	
	    {
	    	InfoList = (ArrayList<MessAttri>)getServletContext().getAttribute("InfoList");
            JsonList = (ArrayList<JsonObject>)getServletContext().getAttribute("JsonList");
	    	if(request.getParameter("request").equals("ViewDetail") && ValidIndex(InfoList.size(),request.getParameter("Index")))
	    	{
	    		JsonObject json_message = JsonList.get(Integer.parseInt(request.getParameter("Index")));
	    		JsonObjectBuilder column1_builder = Json.createObjectBuilder();
	    		JsonObjectBuilder column2_builder = Json.createObjectBuilder();
	    		Iterator<Entry<String,JsonValue>> cursor = json_message.entrySet().iterator();
	    		Entry<String, JsonValue> temp_entry = null;  //use to save the temporary entry
	    		boolean flag = true;   //determine which column to add to 
	    		while(cursor.hasNext())
	    		{
	    			temp_entry = cursor.next();
	    			if(flag) 	
	    			{
	    				column1_builder.add(temp_entry.getKey(), temp_entry.getValue());
	    			}
	    			else        
	    			{
	    				column2_builder.add(temp_entry.getKey(), temp_entry.getValue());
	    			}
	    			flag = !flag;
	    		}
	    		JsonObject column1 = column1_builder.build();
	    		JsonObject column2 = column2_builder.build();
            //create content for column 1
	    		Stack<Iterator<String>> keystack = new Stack<Iterator<String>>();
	    		Stack<ListIterator<JsonValue>> arraystack = new Stack<ListIterator<JsonValue>>();
	    		Stack<JsonValue> objstack = new Stack<JsonValue>();
	    		objstack.push(column1);
	    		keystack.push(column1.keySet().iterator());
	    		Iterator<String> cursor_key = null; 
	    		ListIterator<JsonValue> cursor_array = null;
	    		JsonObject curr_object = null;
	    		JsonValue last_value = null;
	    		JsonArray temp_array = null;
	    		JsonValue temp_value = null;
	    		String temp = null;
	    		boolean previous_arr_flag = false;
	    		String display_col1 = ""; //the display of the first column of the table
	    		String display_col2 = ""; //the display of the second column of the table
	    		while(!objstack.empty())
	    		{
	    			if(ValueType.OBJECT ==  objstack.peek().getValueType())
	    			{
	    				cursor_key = keystack.pop();
	    			}
	    			while(cursor_key.hasNext() || (objstack.peek().getValueType() == ValueType.ARRAY))
	    			{
	    				if(objstack.peek().getValueType() == ValueType.ARRAY)
	    				{
	    					cursor_array = arraystack.pop();
	    					if(cursor_array.hasNext())
	    					{
	    						if(cursor_array.nextIndex() > 0)
	    						{
	    							display_col1 += "</td></tr>";
	    						}
	    						temp_value = cursor_array.next();
	    						if(!previous_arr_flag)	 display_col1 += "<tr><td class = \"T_Cell\">";
	    						else  display_col1 += "<td class = \"T_Cell\">";
	    						previous_arr_flag = false;
	    						if(temp_value.getValueType() == ValueType.ARRAY || temp_value.getValueType() == ValueType.OBJECT)
	    						{
	    							objstack.push(temp_value);
	    							arraystack.push(cursor_array);
	    							display_col1 += "\n<table class = \"Table_JSON\">";
	    							if(temp_value.getValueType() != ValueType.ARRAY)
	    							{
	    								cursor_key = temp_value.asJsonObject().keySet().iterator();
	    							}
	    							else
	    							{
	    								cursor_array = temp_value.asJsonArray().listIterator();
	    								arraystack.push(cursor_array);
	    							}
	    						}
	    						else
	    						{
	    							display_col1 += temp_value.toString() + "</td>" + "</tr>";
	    						}
	    					} 
	    					else
	    					{
	    						break;
	    					}
	    				}
	    				else if(objstack.peek().getValueType() == ValueType.OBJECT)
	    				{
	    					temp = cursor_key.next();
	    					curr_object = (JsonObject)objstack.peek();	 
	    					temp_value = curr_object.getValue("/"+temp);  
	    					if(temp_value.getValueType() == ValueType.ARRAY)
	    					{
	    						keystack.push(cursor_key);
	    						objstack.push(temp_value);
	    						temp_array = temp_value.asJsonArray();
	    						cursor_array = temp_array.listIterator();
	    						arraystack.push(cursor_array);
	    						display_col1 += "\n<tr><td class = \"T_Cell\" rowspan =\"" + temp_array.size() +"\">" + temp + "</td>\n";
	    						previous_arr_flag = true;
	    					}
	    					else if(temp_value.getValueType() == ValueType.OBJECT)
	    					{
	    						keystack.push(cursor_key);
	    						objstack.push(temp_value);
	    						curr_object =(JsonObject)temp_value;
	    						cursor_key =  curr_object.keySet().iterator();
	    						display_col1 += "\n<tr><td class = \"T_Cell\"> " + temp + "</td>" + "<td class = \"T_Cell\">\n<table class = \"Table_JSON\">";
	    					}
	    					//top of objstack is array
	    					else
	    					{
	    						display_col1 += "\n<tr><td class = \"T_Cell\"> " + temp + "</td>" + "<td class = \"T_Cell\">" + temp_value + "</td></tr>";
	    					}
	    				}
	    			}
	    			last_value = objstack.pop();
	    			if(!objstack.empty())
	    			{
	    				if((last_value.getValueType() == ValueType.ARRAY) && (objstack.peek().getValueType() == ValueType.ARRAY))
	    				{
	    					display_col1 += "</td></tr></table>\n";
	    				}
	    				else if((last_value.getValueType() == ValueType.ARRAY) && (objstack.peek().getValueType() == ValueType.OBJECT))
	    				{
	    					display_col1 += "</td></tr>\n";
	    				}
	    				else if(last_value.getValueType() == ValueType.OBJECT)
	    				{
	    					display_col1 += "</table>\n";
	    				}
	    			}
	    		}
				objstack.push(column2);
				keystack.push(column2.keySet().iterator());	
				while(!objstack.empty())
				{
					if(ValueType.OBJECT ==  objstack.peek().getValueType())
					{
						cursor_key = keystack.pop();
					}
					while(cursor_key.hasNext() || (objstack.peek().getValueType() == ValueType.ARRAY))
					{
						if(objstack.peek().getValueType() == ValueType.ARRAY)
						{
							cursor_array = arraystack.pop();
							if(cursor_array.hasNext())
							{
								if(cursor_array.nextIndex() > 0)
								{
									display_col2 += "</td></tr>";
								}
								temp_value = cursor_array.next();
								if(!previous_arr_flag)	 display_col2 += "<tr><td class = \"T_Cell\">";
								else  display_col2 += "<td class = \"T_Cell\">";
								previous_arr_flag = false;
								if(temp_value.getValueType() == ValueType.ARRAY || temp_value.getValueType() == ValueType.OBJECT)
								{
									objstack.push(temp_value);
									arraystack.push(cursor_array);
									display_col2 += "\n<table class = \"Table_JSON\">";
									if(temp_value.getValueType() != ValueType.ARRAY)
									{
										cursor_key = temp_value.asJsonObject().keySet().iterator();
									}
									else
									{
										cursor_array = temp_value.asJsonArray().listIterator();
										arraystack.push(cursor_array);
									}
								}
								else
								{
									display_col2 += temp_value.toString() + "</td>" + "</tr>";
								}
							} 
							else
							{
								break;
							}
						}
						else if(objstack.peek().getValueType() == ValueType.OBJECT)
						{
							temp = cursor_key.next();
							curr_object = (JsonObject)objstack.peek();	 
							temp_value = curr_object.getValue("/"+temp);  
							if(temp_value.getValueType() == ValueType.ARRAY)
							{
								keystack.push(cursor_key);
								objstack.push(temp_value);
								temp_array = temp_value.asJsonArray();
								cursor_array = temp_array.listIterator();
								arraystack.push(cursor_array);
								display_col2 += "\n<tr><td class = \"T_Cell\" rowspan =\"" + temp_array.size() +"\">" + temp + "</td>\n";
								previous_arr_flag = true;
							}
							else if(temp_value.getValueType() == ValueType.OBJECT)
							{
								keystack.push(cursor_key);
								objstack.push(temp_value);
								curr_object =(JsonObject)temp_value;
								cursor_key =  curr_object.keySet().iterator();
								display_col2 += "\n<tr><td class = \"T_Cell\"> " + temp + "</td>" + "<td class = \"T_Cell\">\n<table class = \"Table_JSON\">";
							}
							//top of objstack is array
							else
							{
								display_col2 += "\n<tr><td class = \"T_Cell\"> " + temp + "</td>" + "<td class = \"T_Cell\">" + temp_value + "</td></tr>";
							}
						}
					}
					last_value = objstack.pop();
					if(!objstack.empty())
					{
						if((last_value.getValueType() == ValueType.ARRAY) && (objstack.peek().getValueType() == ValueType.ARRAY))
						{
							display_col2 += "</td></tr></table>\n";
						}
						else if((last_value.getValueType() == ValueType.ARRAY) && (objstack.peek().getValueType() == ValueType.OBJECT))
						{
							display_col2 += "</td></tr>\n";
						}
						else if(last_value.getValueType() == ValueType.OBJECT)
						{
							display_col2 += "</table>\n";
						}
					}
				}
				request.setAttribute("table_col_1",display_col1);
				request.setAttribute("table_col_2",display_col2);

				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/DetailInfo.jsp");
				dispatcher.forward(request, response);
	    	}
	    	else if(request.getParameter("request").equals("SetConnection"))
	    	{
	    		if(Connect_Broker(request.getParameter("url"),request.getParameter("username"),request.getParameter("password"),request.getParameter("cleansession")))
	    		{	          
	    			request.getSession().setAttribute("result", "Connection Succeeded");
	    		}
	    		else
	    		{
	    			request.getSession().setAttribute("result", "Connection Failed");
                    System.out.println("warning set");
	    			request.getSession().setAttribute("warning", debug_info);
	    			debug_info = null;
	    		}
		        request.getSession().setAttribute("Status", ClientStatus());
		        request.getSession().setAttribute("topics", get_topics());
	    		request.getSession().setAttribute("processed", true);
	    		request.getSession().setAttribute("MessList",  getServletContext().getAttribute("InfoList"));
	    		response.sendRedirect("WbTest.jsp");
	    	}
	    	else if(request.getParameter("request").equals("sub"))
	    	{
	    		boolean choice = true;
	    	    if(request.getParameter("sub").equals("Subscribe"))
	    	    {
	    	    	choice = true;
	    	    }
	    	    else
	    	    {
	    	    	choice = false;
	    	    }
		        request.getSession().setAttribute("Status", ClientStatus());
		        if(request.getParameter("topics") != "")
                { 
	    		   if(Subscribe_topic(request.getParameter("topics"),choice))
	    		   {
		    			request.getSession().setAttribute("result", "Operation Succeeded");
	    		   }
	    		   else
	    		   {
		    			request.getSession().setAttribute("result", "Operation Failed");
		    			request.getSession().setAttribute("warning", debug_info);

	    		   }
	    			   
                }
		        else
		        {
	    			request.getSession().setAttribute("result", "Operation Failed");
	    			request.getSession().setAttribute("warning", "empty Topic input");

		        }
		        request.getSession().setAttribute("topics", get_topics());
	    		request.getSession().setAttribute("processed", true);
	    		request.getSession().setAttribute("MessList",  getServletContext().getAttribute("InfoList"));
	    		response.sendRedirect("WbTest.jsp");
	    	}
	    	else if(request.getParameter("request").equals("pub"))
	    	{
		       request.getSession().setAttribute("Status", ClientStatus());

		       if(Publish_message(request.getParameter("topics"),request.getParameter("message"),request.getParameter("QoS"),request.getParameter("retained")))		  
    		   {
	    			request.getSession().setAttribute("result", "Operation Succeeded");
    		   }
    		   else
    		   {
	    			request.getSession().setAttribute("result", "Operation Failed");
	    			request.getSession().setAttribute("warning", debug_info);

    		   }
		       request.getSession().setAttribute("topics", get_topics());
	    	   request.getSession().setAttribute("processed", true);
	    	   request.getSession().setAttribute("MessList",  getServletContext().getAttribute("InfoList"));
	    	   response.sendRedirect("WbTest.jsp");
	    	}
	    }
	    catch(Exception e)
	    {	    	
	    	System.out.println(e.getClass().getName()+": global catch");	
	        request.getSession().setAttribute("Status", ClientStatus());
	        request.getSession().setAttribute("topics", get_topics());
    		request.getSession().setAttribute("processed", true);
    		request.getSession().setAttribute("MessList", getServletContext().getAttribute("InfoList"));

    		response.sendRedirect("WbTest.jsp");
	    	
	    }
	   /* out.println("</BODY></HTML>");*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
