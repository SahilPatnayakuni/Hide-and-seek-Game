package common.API;

import java.rmi.Remote;
import java.rmi.RemoteException;

import common.messagePacket.MessageDataPacket;
import common.messageTypes.IConnectMsg;

/**
 * This is the connection interface. 
 * This should hold a collection of ICommunicate objects, one for each application you are in.
 * Should be exported and  binded in registry as "IConnect".
 */
public interface IConnect extends Remote, ISender {
	/**
	 * This is the name every IConnect stub should be bound to the RMI Registry with. 
	 * Because only one IConnect can be bound per IP at a given time, we can mandate that a common
	 * String is used. This should be the name you use to look up someone else's stub.
	 */
	public String BOUND_NAME = "IConnect";
	
	
	/**
	 * Processes the message sent to the user from the sender and displays it locally according to the corresponding algorithms. 
	 * sender is the IConnect stub of the IConnect that send the message.
	 * Processing is handled using the extended visitor design pattern, 
	 * where the DataPacket is the host and the defined algorithms are the visitors. 
	 * @param message - the message
	 * @throws RemoteException exception
	 */
	public void processMessage(MessageDataPacket<? extends IConnectMsg, IConnect> message) throws RemoteException;
	
	/**
	 * This is provided for auto-"connect back." When connecting to a game server (or other player),
	 * it puts the two people at the same level. 
	 * @param connector - connector's IConnect
	 * @throws RemoteException When connecting to the remote object
	 */
	public void exchangeIConnect(IConnect connector) throws RemoteException;

	/**
	 * Removes the IConnect from the list of IConnects. Called by user who passes in their own IConnect.
	 * @param leaver - leaver's IConnect
	 * @throws RemoteException When connecting to the remote object
	 */
	public void removeIConnect(IConnect leaver) throws RemoteException;
	
	/**
	 * Gets your global name. You should set this when you create your IConnect. This is here
	 * so you know whose IConnect stub you have. This is different from the notion of username that a
	 * ICommunicate has.
	 * @return your global username.
	 * @throws RemoteException exception
	 */
	public String getName() throws RemoteException;
		
}
