package common.API;

import common.messageTypes.ICommMsg;

/**
 * This is the CmdToModelAdapter that bridges the scope between the ICommunicate commands and the model.
 * @author ZachGramstad
 *
 */
public interface ICommCmdToModelAdapter extends ICmdToModelAdapter<ICommMsg, ICommunicate> {

	/**
	 * Get a COPY of the local group that is coupled with this adapter.
	 * @return the IGroup that is coupled with the adapter
	 */
	IGroup getGroup();
	
	/**
	 * Allows the commands to send messages to a relevant group of users.
	 * 
	 * @param id The class of the message
	 * @param message The message
	 * @param <T> The type of message
	 */  
	public <T extends ICommMsg> void sendMessageToLocalSet(Class<T> id, T message);
	
}
