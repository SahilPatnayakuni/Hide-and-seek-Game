package sp50_egd2.api.messages;

import common.API.ICommunicate;
import common.messageTypes.communicate.IJoinGroupMsg;

public class JoinGroupMsg implements IJoinGroupMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2573859193836996566L;

	private ICommunicate userStub;
	
	public JoinGroupMsg(ICommunicate userStub) {
		this.userStub = userStub;
	}
	
	@Override
	public ICommunicate getUserStub() {
		// TODO Auto-generated method stub
		return userStub;
	}

}
