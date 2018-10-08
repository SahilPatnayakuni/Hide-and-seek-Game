package common.messageTypes.communicate;

import java.util.Set;

import common.API.ICommunicate;
import common.messageTypes.ICommMsg;

/**
 * A "well-defined" message type.
 * 
 * Container for a Collection of ICommunicate stubs that should be removed from the
 * receiver's ICommunicate Collection.
 */
public interface IRemoveUsersMsg extends ICommMsg {
	
	/**
	 * Getter for a the set of ICommunicate stubs to remove.
	 * @return List of ICommunicate stubs to add
	 */
	public Set<ICommunicate> getICommunicates();

}
