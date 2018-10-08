package sp50_egd2.api.messages;

import common.messageTypes.ICommMsg;
import common.messageTypes.communicate.IGetCommCmdMsg;

public class GetCommCmdMsg implements IGetCommCmdMsg {
	
	Class<? extends ICommMsg> thisClass;
	
	public GetCommCmdMsg(Class<? extends ICommMsg> thisClass) {
		this.thisClass = thisClass;
	}

	@Override
	public Class<? extends ICommMsg> getCmdClass() {
		// TODO Auto-generated method stub
		return thisClass;
	}

}
