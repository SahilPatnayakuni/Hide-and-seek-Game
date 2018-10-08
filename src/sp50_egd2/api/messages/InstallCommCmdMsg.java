package sp50_egd2.api.messages;

import common.messagePacket.CommMsgAlgoCmd;
import common.messageTypes.ICommMsg;
import common.messageTypes.communicate.IInstallCommCmdMsg;

public class InstallCommCmdMsg implements IInstallCommCmdMsg {
	
	Class<? extends ICommMsg> msgClass;
	
	CommMsgAlgoCmd<?> cmd;
	
	public InstallCommCmdMsg(Class<? extends ICommMsg> msgClass, CommMsgAlgoCmd<?> cmd) {
		this.msgClass = msgClass;
		this.cmd = cmd;
	}

	@Override
	public CommMsgAlgoCmd<?> getCmd() {
		// TODO Auto-generated method stub
		return cmd;
	}

	@Override
	public Class<? extends ICommMsg> getMsgClass() {
		// TODO Auto-generated method stub
		return msgClass;
	}

}
