package common.messageTypes.connect;

import common.messageTypes.IConnectMsg;

/**
 * One of four "well-defined" message types.
 * 
 * This message is sent to a receiver (Person B) when the sender (Person A) wants to have its command. 
 * This is mainly a wrapper for command class the requested command.
 */
public interface IGetConnectCmdMsg extends IConnectMsg {
	/**
	 * The class of the requested command should be included in this message.
	 * @return The class of the requested command.
	 */
	public Class<? extends IConnectMsg>  getCmdClass();
}