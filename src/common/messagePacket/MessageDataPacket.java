package common.messagePacket;

import provided.datapacket.DataPacket;
import common.API.ISender;
import common.messageTypes.IMessage;


/**
 * Extends the DataPacket class to make typing easier.
 * @param <T> The class inside the data packet
 * @param <S> The type of the sender of the data packet
 */
public class MessageDataPacket<T extends IMessage, S extends ISender> extends DataPacket<T, S> {
	
	/**
	 * This IS serializable!
	 */
	private static final long serialVersionUID = -4384652266644231822L;


	/**
	 * Calls the DataPacket constructor.
	 * @param c  The class of the data packet
	 * @param data The actual data
	 * @param sender The sender
	 */
	public MessageDataPacket(Class<T> c, T data, S sender) {
		super(c, data, sender);
	}

}
