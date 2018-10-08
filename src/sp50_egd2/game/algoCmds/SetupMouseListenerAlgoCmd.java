package sp50_egd2.game.algoCmds;

import common.API.ICommCmdToModelAdapter;
import common.API.ICommunicate;
import common.messagePacket.CommMsgAlgoCmd;
import common.messagePacket.MessageDataPacket;
import sp50_egd2.game.messages.SetupMouseListenerMsg;

public class SetupMouseListenerAlgoCmd extends CommMsgAlgoCmd<SetupMouseListenerMsg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 748198258668504154L;

	transient ICommCmdToModelAdapter cmdToModelAdpt;
	
	public SetupMouseListenerAlgoCmd(ICommCmdToModelAdapter adpt) {
		this.cmdToModelAdpt = adpt;
	}
	
	@Override
	public Void apply(Class<?> index, MessageDataPacket<SetupMouseListenerMsg, ICommunicate> host, Void... params) {
		//cmdToModelAdpt.
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
		// TODO Auto-generated method stub
		
	}

}
