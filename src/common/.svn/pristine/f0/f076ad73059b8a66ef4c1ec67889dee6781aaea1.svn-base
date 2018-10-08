package common.API;

import common.messageTypes.ICommMsg;
import common.messageTypes.IConnectMsg;
import common.messageTypes.IMessage;

/**
 * Return status types for IUser.processMessage(). When you call another user's processMessage() method, they 
 * determine whether the call failed, or was refused and return an object of corresponding type.
 * The returned object should have a descriptive message.
 *
 */
public interface IStatus extends IConnectMsg, ICommMsg {
	/**
	 * If desired, the status's sender can give a description of the status.
	 * @return -information about the status
	 */
	public String getStatus();
	
	/**
	 * Gets the class of the message that the status is being returned for.
	 * @return the class of the message that was processed.
	 */
	public Class<? extends IMessage> getMessageClass();
	
	
	/**
	 * Represents that there was a failure in receiving the message. Information about the
	 * failure can be accessed via getStatus().
	 *
	 */
	public interface IFailure extends IStatus {
		
	}
	
	/**
	 * Represents that the message was processed successfully, but the sender's request was refused.
	 * This case is distinct from both success and failure.
	 *
	 */
	public interface IRefused extends IStatus {
		
	}
	
	/**
	 * Represents that there was a unknown cmd in the receiving message. 
	 */
	public interface IUnknown extends IStatus {
		
	}

}
