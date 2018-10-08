package sp50_egd2.api.messages;

import java.util.Set;

import common.API.ICommunicate;
import common.messageTypes.communicate.IAddUsersMsg;

public class AddUsersMsg implements IAddUsersMsg {
	
	Set<ICommunicate> icommunicates;
	
	public AddUsersMsg(Set<ICommunicate> icommunicates) {
		this.icommunicates = icommunicates;
	}
	
	@Override
	public Set<ICommunicate> getICommunicates() {
		return icommunicates;
	}

}
