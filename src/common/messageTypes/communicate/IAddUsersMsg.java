package common.messageTypes.communicate;

import java.util.Set;

import common.API.ICommunicate;
import common.messageTypes.ICommMsg;


/**
 * A "well-defined" message type.
 * 
 * Container for a collection of ICommunicate stubs that should be added to the
 * receiver's ICommunicate collection.
 */
public interface IAddUsersMsg extends ICommMsg {
	/**
	 * Getter for a field that contains the collection of ICommunicate stubs to send.
	 * @return - collection of ICommunicate stubs to add
	 */
	public Set<ICommunicate> getICommunicates();
}
