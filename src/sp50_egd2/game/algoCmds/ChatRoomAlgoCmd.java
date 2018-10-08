package sp50_egd2.game.algoCmds;

import java.rmi.RemoteException;
import java.util.UUID;

import javax.swing.JLabel;
import javax.swing.JPanel;

import common.API.ICommCmdToModelAdapter;
import common.API.ICommunicate;
import common.messagePacket.CommMsgAlgoCmd;
import common.messagePacket.MessageDataPacket;
import provided.mixedData.MixedDataKey;
import sp50_egd2.game.messages.ChatRoomMsg;

public class ChatRoomAlgoCmd extends CommMsgAlgoCmd<ChatRoomMsg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6256759145946991600L;

	transient private ICommCmdToModelAdapter cmd2ModelAdpt;
	
	public ChatRoomAlgoCmd(ICommCmdToModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
	
	@Override
	public Void apply(Class<?> index, MessageDataPacket<ChatRoomMsg, ICommunicate> host, Void... params) {
		// TODO Auto-generated method stub
		if (!cmd2ModelAdpt.dictionaryContainsKey(new MixedDataKey<JPanel>(UUID.fromString("ee175b5c-62b4-4214-a4b1-396f37a619d8"), "centerPanel", JPanel.class))) {
			return null;
		}
		JPanel centerPanel = cmd2ModelAdpt.getFromDictionary(
				new MixedDataKey<JPanel>(UUID.fromString("ee175b5c-62b4-4214-a4b1-396f37a619d8"), "centerPanel", JPanel.class));
		try {
			centerPanel.add(new JLabel("<" + host.getSender().getGroupName() + "> " + host.getSender().getName() + ": " + host.getData().getText()));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		centerPanel.revalidate();
		centerPanel.repaint();
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
		// TODO Auto-generated method stub
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}
