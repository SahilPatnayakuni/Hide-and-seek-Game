package sp50_egd2.api.messages;

import common.messageTypes.IConnectMsg;
import common.messageTypes.connect.IGetConnectCmdMsg;

public class GetConnectCmdMsg implements IGetConnectCmdMsg {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7188175417243987256L;
	Class<? extends IConnectMsg> cmdClass;
	
	public GetConnectCmdMsg(Class<? extends IConnectMsg> cmdClass) {
		this.cmdClass = cmdClass;
	}

	@Override
	public Class<? extends IConnectMsg> getCmdClass() {
		// TODO Auto-generated method stub
		return cmdClass;
	}

}
