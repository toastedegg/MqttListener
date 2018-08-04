package listeners;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Application Lifecycle Listener implementation class SRlistenser
 *
 */
@WebListener
public class SRlistener implements ServletRequestListener {

    /**
     * Default constructor. 
     */

    public void requestDestroyed(ServletRequestEvent sec)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
     */
    
    //create http section
    @Override
    public void requestInitialized(ServletRequestEvent sec)  { 
    	//((HttpServletRequest)sec.getServletRequest()).getSession();
    }
	
}
