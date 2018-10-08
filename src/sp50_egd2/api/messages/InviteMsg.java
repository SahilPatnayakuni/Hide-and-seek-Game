package sp50_egd2.api.messages;

import common.API.ICommunicate;
import common.messageTypes.connect.IInviteMsg;

public class InviteMsg implements IInviteMsg {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6471208407385470491L;
	ICommunicate communicateStub;
	
	public InviteMsg(ICommunicate stub) {
		communicateStub = stub;
	}

	@Override
	public ICommunicate getStub() {
		return communicateStub;
	}

}
