package ws;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/wslistener", configurator = Configurator.class)

public class WebEndPoint {
	
	private static String p_msg = "test the endpoint";
	private HttpSession httpSess;
	private ServletConfig Config;
	private ServletContext Context;
	private EndpointConfig EPConfig;
	private static Map<String,String> info;

	@OnOpen
	public void onOpen(Session sesssion, EndpointConfig config){
		EPConfig = config;
		System.out.println("Open Connection ...");
	    
	}
	
	@OnClose
	public void onClose(){
		System.out.println("Close Connection ...");
		
	}
	
	@OnMessage
	public String onMessage(String message){
		httpSess = (HttpSession)EPConfig.getUserProperties().get("httpSession");
		Context = httpSess.getServletContext();
		info = (Map<String,String>)Context.getAttribute("content");
		return info.get("content");
		//System.out.println("Message from the client: " + message);
		//String echoMsg = "Echo from the server :" + message;
		//return p_msg;
	}

	@OnError
	public void onError(Throwable e){
	    e.printStackTrace();	
		
	}
	
	
}
