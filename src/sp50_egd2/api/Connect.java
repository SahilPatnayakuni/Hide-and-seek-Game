package sp50_egd2.api;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import common.API.ICommunicate;
import common.API.IConnect;
import common.messagePacket.MessageDataPacket;
import common.messagePacket.MessageDataPacketAlgo;
import common.messageTypes.IConnectMsg;

public class Connect implements IConnect {
	
	MessageDataPacketAlgo<IConnect> visitor;
	
	Set<IConnect> otherConnectStubs = new HashSet<IConnect>();
	
	String name;
	
	public Connect(MessageDataPacketAlgo<IConnect> visitor, String name) {
		this.visitor = visitor;
		this.name = name;
	}

	@Override
	public void processMessage(MessageDataPacket<? extends IConnectMsg, IConnect> message) throws RemoteException {
		message.execute(visitor, (Void[]) null);

	}

	@Override
	public void exchangeIConnect(IConnect connector) throws RemoteException {
		otherConnectStubs.add(connector);
	}

	@Override
	public void removeIConnect(IConnect leaver) throws RemoteException {
		System.out.println("Removing " + leaver.getName() + " in Connect");
		otherConnectStubs.remove(leaver);
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}
	
	public Set<IConnect> getOtherStubs(){
		return otherConnectStubs;
	}

}
