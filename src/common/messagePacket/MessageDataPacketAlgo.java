package common.messagePacket;

import common.API.ICmdToModelAdapter;
import common.API.ISender;
import common.messageTypes.IMessage;
import provided.datapacket.*;

/**
 * Extends the DataPacketAlgo to enforce the typing for the return.
 * @param <S> The ISender type for this visitor. (Should be IConnect or ICommunicate)
 */
public class MessageDataPacketAlgo<S extends ISender> extends DataPacketAlgo<Void, Void> {
	
	/**
	 * Version number for serialization
	 */
	private static final long serialVersionUID = 20459194114770686L;

	/**
	 * Default constructor that takes in the default MessageDataPacketAlgoCmd
	 * @param defaultCmd the default cmd.
	 */
	public MessageDataPacketAlgo(MessageDataPacketAlgoCmd<? extends IMessage, S, ? extends ICmdToModelAdapter<? extends IMessage, S>> defaultCmd) {	
		super(defaultCmd);
	}

}
