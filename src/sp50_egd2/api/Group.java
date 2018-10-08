package sp50_egd2.api;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import common.API.ICommunicate;
import common.API.IGroup;
import common.messagePacket.MessageDataPacket;
import common.messageTypes.ICommMsg;

/**
 * Implementation of IGroup from the API.
 * 
 * @author Edi
 *
 */
public class Group implements IGroup {

	/**
	 * serial version ID
	 */
	private static final long serialVersionUID = 5933248374836288539L;

	/**
	 * this group's name
	 */
	private String name = "NoName Group " + serialVersionUID;
	
	/**
	 * this group's unique id
	 */
	private UUID uuid = UUID.randomUUID();
	
	/**
	 * The group's members
	 */
	private Set<ICommunicate> members = new HashSet<ICommunicate>();
	
	/**
	 * constructor
	 */
	public Group() {
	}
	
	/**
	 * set the group name
	 * @param name group's name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * send a message to all the group's members
	 * @param message the message to be sent
	 */
	@Override
	public void sendMessage(MessageDataPacket<? extends ICommMsg, ICommunicate> message) {
		for(ICommunicate i : members) {
			try {
				i.processMessage(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the group's name
	 * @return the group's name
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	/**
	 * get the group's uuid
	 * @return the group's uuid
	 */
	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return uuid;
	}

	/**
	 * add members to the group
	 * @param newMembers the members to be added
	 */
	public void addMembers(Set<ICommunicate> newMembers) {
		members.addAll(newMembers);
	}
	
	/**
	 * get the group's members
	 * @return the grou's memberss
	 */
	@Override
	public Set<ICommunicate> getMembers() {
		return members;
	}
	
	public void removeMembers(Collection<ICommunicate> oldMembers) {
		for(ICommunicate i : oldMembers) {
			this.members.remove(i);
		}
	}
	
	public int getSize() {
		return members.size();
	}
	
	public void addMember(ICommunicate person2add) {
		members.add(person2add);
	}
	
	public void setUUID(UUID id) {
		this.uuid = id;
	}

}
