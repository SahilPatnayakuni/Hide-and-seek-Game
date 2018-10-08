package sp50_egd2.api.messages;

import common.API.IConnect;
import common.messageTypes.connect.IJoinMsg;

public class JoinMsg implements IJoinMsg {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8744551082728415622L;
	IConnect connectStub;
	
	public JoinMsg(IConnect stub) {
		connectStub = stub;
	}

	@Override
	public IConnect getUserStub() {
		return connectStub;
	}

}
