package sp50_egd2.api;

import java.rmi.RemoteException;
import java.util.UUID;

import common.API.ICommunicate;
import common.API.IConnect;
import common.API.IGroup;
import common.messagePacket.MessageDataPacket;
import common.messagePacket.MessageDataPacketAlgo;
import common.messageTypes.ICommMsg;

public class Communicate implements ICommunicate {
	
	IConnect myIConnectStub;
	
	MessageDataPacketAlgo<ICommunicate> visitor;
	
	String name;
	
	UUID uid;
	
	String groupName;
	
	UUID groupUID;
	
	public Communicate(MessageDataPacketAlgo<ICommunicate> visitor, String name, String groupname, UUID groupUid, IConnect connectStub) {
		this(visitor, name, groupname, groupUid, connectStub, UUID.randomUUID());
	}
	
	public Communicate(MessageDataPacketAlgo<ICommunicate> visitor, String name, String groupname, UUID groupUid, IConnect connectStub, UUID uid) {
		this.visitor = visitor;
		this.name = name;
		this.uid = uid;
		this.groupName = groupname;
		this.groupUID = groupUid;
		this.myIConnectStub = connectStub;
	}

	@Override
	public void processMessage(MessageDataPacket<? extends ICommMsg, ICommunicate> message) throws RemoteException {
		message.execute(visitor, (Void[]) null);

	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public UUID getUUID() throws RemoteException {
		return uid;
	}

	@Override
	public String getGroupName() throws RemoteException {
		return groupName;
	}

	@Override
	public UUID getGroupUUID() throws RemoteException {
		return groupUID;
	}

	@Override
	public IConnect getConnect() throws RemoteException {
		return myIConnectStub;
	}

	public void setVisitor(MessageDataPacketAlgo<ICommunicate> visitor) {
		this.visitor = visitor;
	}
	
	public MessageDataPacketAlgo<ICommunicate> getVisitor() {
		return visitor;
	}
}
