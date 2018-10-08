package sp50_egd2.api.messages;

import common.messagePacket.ConnectMsgAlgoCmd;
import common.messageTypes.IConnectMsg;
import common.messageTypes.connect.IInstallConnectCmdMsg;

public class InstallConnectCmdMsg implements IInstallConnectCmdMsg {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3468516627045178520L;

	ConnectMsgAlgoCmd<?> cmd;
	
	Class<? extends IConnectMsg> msgClass;
	
	public InstallConnectCmdMsg(ConnectMsgAlgoCmd<?> cmd, Class<? extends IConnectMsg> msgClass) {
		this.cmd = cmd;
		this.msgClass = msgClass;
	}

	@Override
	public ConnectMsgAlgoCmd<?> getCmd() {
		return cmd;
	}

	@Override
	public Class<? extends IConnectMsg> getMsgClass() {
		return msgClass;
	}

}
