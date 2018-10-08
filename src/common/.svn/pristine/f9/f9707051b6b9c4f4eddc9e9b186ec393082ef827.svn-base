package common.API;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import common.messagePacket.MessageDataPacket;
import common.messageTypes.ICommMsg;

/**
 * Abstraction of a group in an application. A given group has a set of ICommunicate stubs for each member within.
 * A group has the function of sending message to its members. It has a unique UUID.
 */
public interface IGroup extends Serializable {
	
	/**
	 * This method sends messages to all its members
	 * @param message The message to be sent.
	 */
	public void sendMessage(MessageDataPacket<? extends ICommMsg, ICommunicate> message);
	
	/**
	 * This should be the name of the group.
	 * @return The name of the group.
	 */
	public String getName();
	
	/**
	 * Each group is uniquely identified by a UUID.
	 * @return The unique UUID of the group.
	 */
	public UUID getUUID();
	
	/**
	 * The group has all the ICommunicate stubs from its members.
	 * @return A set of ICommunicate stubs from its members.
	 */
	public Set<ICommunicate> getMembers();
}
