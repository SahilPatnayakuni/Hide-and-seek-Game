package common.messageTypes.communicate;

import common.messagePacket.CommMsgAlgoCmd;
import common.messageTypes.ICommMsg;

/**
 * A "well-defined" message type.
 * 
 * This message is sent to a receiver (Person B) when the sender (Person A) wants to install a command remotely. 
 * This is mainly a wrapper for Person A's command, which is necessary to trigger their addition to 
 * the application.
 */
public interface IInstallCommCmdMsg extends ICommMsg {
	
	/**
	 * This message should include the command to be installed.
	 * @return A command.
	 */
	public CommMsgAlgoCmd<?> getCmd();

	/**
	 * Get the message class that this command processes.
	 * @return The message class 
	 */
	public Class<? extends ICommMsg> getMsgClass();
}
