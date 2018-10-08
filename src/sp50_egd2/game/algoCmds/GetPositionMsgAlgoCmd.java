package sp50_egd2.game.algoCmds;

import java.util.Random;
import java.util.UUID;

import common.API.ICommCmdToModelAdapter;
import common.API.ICommunicate;
import common.messagePacket.CommMsgAlgoCmd;
import common.messagePacket.MessageDataPacket;
import gov.nasa.worldwind.geom.Position;
import provided.mixedData.MixedDataKey;
import sp50_egd2.game.messages.GetPositionMsg;
import sp50_egd2.game.messages.PositionMsg;

public class GetPositionMsgAlgoCmd extends CommMsgAlgoCmd<GetPositionMsg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3316796152759295156L;
	
	transient ICommCmdToModelAdapter cmdToModelAdpt;
	
	public GetPositionMsgAlgoCmd(ICommCmdToModelAdapter adpt) {
		this.cmdToModelAdpt = adpt;
	}

	@Override
	public Void apply(Class<?> index, MessageDataPacket<GetPositionMsg, ICommunicate> host, Void... params) {
		MixedDataKey<Position> positionkey = new MixedDataKey<Position>(UUID.fromString("b35b9f7c-3b91-4703-9c2f-ba2cccb1244d"), "position", Position.class);
		Position myPosition;
		if(cmdToModelAdpt.dictionaryContainsKey(positionkey)) {
			myPosition = cmdToModelAdpt.getFromDictionary(positionkey);
		} else {
			Random r = new Random();
			myPosition = Position.fromDegrees((r.nextDouble()-0.5) * 180.0, (r.nextDouble()-0.5)*180.0);
			cmdToModelAdpt.putInDictionary(positionkey, myPosition);
		}
		if(myPosition == null) {
			Random r = new Random();
			myPosition = Position.fromDegrees((r.nextDouble()-0.5) * 180.0, (r.nextDouble()-0.5)*180.0);
			cmdToModelAdpt.putInDictionary(positionkey, myPosition);
		} else if(myPosition.getLatitude().getDegrees() > 90) {
			double longitude = myPosition.getLongitude().getDegrees();
			myPosition = Position.fromDegrees(90, longitude);
			cmdToModelAdpt.putInDictionary(positionkey, myPosition);
		} else if(myPosition.getLatitude().getDegrees() < -90) {
			double longitude = myPosition.getLongitude().getDegrees();
			myPosition = Position.fromDegrees(-90, longitude);
			cmdToModelAdpt.putInDictionary(positionkey, myPosition);
		}
		cmdToModelAdpt.sendMessageTo(host.getSender(), PositionMsg.class, new PositionMsg(myPosition));
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
		cmdToModelAdpt = cmd2ModelAdpt; 
		
	}

}
