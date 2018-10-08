package common.messageTypes.communicate;

import common.messageTypes.ICommMsg;

/**
 * A "well-defined" message type.
 * 
 * This message is sent to a receiver (Person B) when the sender (Person A) wants to have its command. 
 * This is mainly a wrapper for command class the requested command.
 */
public interface IGetCommCmdMsg extends ICommMsg {
	/**
	 * The class of the requested command should be included in this message.
	 * @return The class of the requested command.
	 */
	public Class<? extends ICommMsg>  getCmdClass();
	
}
