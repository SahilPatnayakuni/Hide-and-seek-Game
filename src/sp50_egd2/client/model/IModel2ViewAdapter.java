package sp50_egd2.client.model;

import java.awt.Component;
import java.util.function.Supplier;

/**
 * the model to view adapter.
 * @param <TChatAppWrapper> - the generic that's supposed to represent the chat app wrapper. 
 */
public interface IModel2ViewAdapter {

	/**
	 * sets the server host IP text box.
	 * @param IP - the IP of our system
	 */
	public void setServerHost(String IP);

	public void addScrollingComponent(Supplier<Component> compFac);	
	
	public void addStaticComponent(Supplier<Component> compFac);	
}
