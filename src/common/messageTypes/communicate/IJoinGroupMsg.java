package common.messageTypes.communicate;

import common.API.ICommunicate;
import common.messageTypes.ICommMsg;

/**
 * A "well-defined" message type.
 * 
 * This message is sent to a receiver (Person B) when the sender (Person A) wants to join their application. 
 * This is mainly a wrapper for Person A's stub, which is necessary to trigger their addition to 
 * the application (IConnect level) or Group (ICommunicate level).
 *
 * Note that you should only send STUBS here, not entire objects!
 */
public interface IJoinGroupMsg extends ICommMsg {
	
	/**
	 * Getter for the sender's stub.
	 * @return - the sender's stub
	 */
	public ICommunicate getUserStub();

}
