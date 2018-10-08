package common.messageTypes.connect;

import common.API.IConnect;
import common.messageTypes.IConnectMsg;

/**
 * One of four "well-defined" message types.
 * 
 * This message is sent to a receiver (Person B) when the sender (Person A) wants to join their application. 
 * This is mainly a wrapper for Person A's stub, which is necessary to trigger their addition to 
 * the application (IConnect level) or Group (ICommunicate level).
 *
 */
public interface IJoinMsg extends IConnectMsg {
	
	/**
	 * Getter for the sender's stub.
	 * @return - the sender's stub
	 */
	public IConnect getUserStub();

}
