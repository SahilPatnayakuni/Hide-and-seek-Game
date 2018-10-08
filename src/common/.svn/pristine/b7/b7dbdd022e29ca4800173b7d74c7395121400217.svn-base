package common.API;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

import common.messagePacket.MessageDataPacket;
import common.messageTypes.ICommMsg;

/**
 * Abstraction of a user in an application. A given user has a unique ICommunicate stub for each application they are in.
 * This object gets stubbed and serves as the communication portal.
 */
public interface ICommunicate extends Remote, ISender {
	
	/**
	 * Processes the message sent to the user from the sender and displays it locally according to the corresponding algorithms.
	 * 
	 * @param message Message to process. Must be an ICommMessage.
	 * @throws RemoteException Exception thrown if there is a network error.
	 */
	public void processMessage(MessageDataPacket<? extends ICommMsg, ICommunicate> message) throws RemoteException;
	
	/**
	 * Each ICommunicate should have a username so other users can know who sent a message.
	 * Does not need to be unique; that's the purpose of the UUID.
	 * 
	 * @return This IUser's name
	 * @throws RemoteException Exception thrown if there is a network error.
	 */
	public String getName() throws RemoteException;
	
	
	/**
	 * Each ICommunicate is uniquely identified by a UUID. This is the
	 * getter method for that UUID.
	 * @return The unique UUID of the ICommunicate.
	 * @throws RemoteException Exception thrown if there is a network error.
	 */
	public UUID getUUID() throws RemoteException;
	
	
	/**
	 * Each ICommunicate represents a presence in a group. This gets the group name of that group.
	 * @return the group name
	 * @throws RemoteException Exception thrown if there is a network error.
	 */
	public String getGroupName() throws RemoteException;
	
	
	/**
	 * Each ICommunicate represents a presence in a group. This gets the group UUID of that group.
	 * @return the group UUID
	 * @throws RemoteException Exception thrown if there is a network error.
	 */
	public UUID getGroupUUID() throws RemoteException;
	
	
	/**
	 * This is to allow other ICommunicates a way to access the connect level. 
	 * This also supports the auto connect back feature.
	 * @return The IConnect STUB for this user.
	 * @throws RemoteException Exception thrown if there is a network error.
	 */
	public IConnect getConnect() throws RemoteException;
}
