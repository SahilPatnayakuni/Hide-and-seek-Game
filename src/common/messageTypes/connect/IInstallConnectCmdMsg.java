package common.messageTypes.connect;

import common.messagePacket.ConnectMsgAlgoCmd;
import common.messageTypes.IConnectMsg;

/**
 * One of four "well-defined" message types.
 * 
 * This message is sent to a receiver (Person B) when the sender (Person A) wants to install a set of commands remotely. 
 * This is mainly a wrapper for Person A's collection of commands, which is necessary to trigger their addition to 
 * the application.
 *
 */
public interface IInstallConnectCmdMsg extends IConnectMsg {
	
	/**
	 * This message should include the command to be installed.
	 * @return A command.
	 */
	public ConnectMsgAlgoCmd<?> getCmd();
	
	/**
	 * Get the message class that this command process.
	 * @return The message class 
	 */
	public Class<? extends IConnectMsg> getMsgClass();
}
