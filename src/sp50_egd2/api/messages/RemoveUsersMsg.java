package sp50_egd2.api.messages;

import java.util.HashSet;
import java.util.Set;

import common.API.ICommunicate;
import common.messageTypes.communicate.IRemoveUsersMsg;

public class RemoveUsersMsg implements IRemoveUsersMsg {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8755264414343711581L;
	Set<ICommunicate> data;
	
	public RemoveUsersMsg(Set<ICommunicate> dataSet) {
		this.data = dataSet;
	}

	@Override
	public Set<ICommunicate> getICommunicates() {
		// TODO Auto-generated method stub
		return data;
	}

}
