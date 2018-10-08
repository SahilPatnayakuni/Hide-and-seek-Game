package common.messagePacket;

import common.API.ICmdToModelAdapter;
import common.API.ISender;
import common.messageTypes.IMessage;
import provided.datapacket.ADataPacketAlgoCmd;

/**
 * Class that extends ADataPacketAlgoCmd to enforce the typing.  
 *
 * @param <M> Message type that this command processes
 * @param <S> Sender type that sends messages this command processes & is using this command
 * @param <A> Adapter type that this command corresponds to
 */

public abstract class MessageDataPacketAlgoCmd<M extends IMessage, S extends ISender, A extends ICmdToModelAdapter<? extends IMessage, S>>  extends ADataPacketAlgoCmd<Void, M, Void, A, MessageDataPacket<M, S>>{
	
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -474598072072827707L;

}
