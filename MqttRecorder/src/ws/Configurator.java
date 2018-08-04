/**
 * 
 */
package ws;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;;
/**
 * @author toastedegg
 *
 *
 */
public class Configurator extends ServerEndpointConfig.Configurator {

	@Override
	public void modifyHandshake(ServerEndpointConfig config,HandshakeRequest request, HandshakeResponse response)
	{
    	System.out.println("Configurator hit");
		HttpSession httpSess = (HttpSession)request.getHttpSession();
		config.getUserProperties().put("httpSession", httpSess);
		
		
		
	}
}
