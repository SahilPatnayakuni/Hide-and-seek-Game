package common.messagePacket;

import common.API.ISender;
import common.API.IStatus;

/**
 * Extends the DataPacket class to make typing easier. Used for sending status.
 * @param <T> The type of IStatus to return 
 * @param <S> Sender type (Either IConnect or ICommunicate)
 */
public class StatusDataPacket<T extends IStatus, S extends ISender>  extends MessageDataPacket<T, S> {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID  = 11234234123L;
	
	/**
	 * Calls the DataPacket constructor.
	 * @param c The class of this IStatus
	 * @param data The IStatus to send
	 * @param sender The sender of this IStatus
	 */
	public StatusDataPacket(Class<T> c, T data, S sender) {
		super(c, data, sender);
	}

}
