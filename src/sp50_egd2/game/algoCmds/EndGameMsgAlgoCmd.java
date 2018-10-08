package sp50_egd2.game.algoCmds;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.Supplier;

import javax.swing.JLabel;
import javax.swing.JPanel;

import common.API.ICommCmdToModelAdapter;
import common.API.ICommunicate;
import common.messagePacket.CommMsgAlgoCmd;
import common.messagePacket.MessageDataPacket;
import sp50_egd2.game.messages.EndGameMsg;

public class EndGameMsgAlgoCmd extends CommMsgAlgoCmd<EndGameMsg> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1620609541458743432L;
	private transient ICommCmdToModelAdapter cmdToModelAdpt;
	
	public EndGameMsgAlgoCmd(ICommCmdToModelAdapter adpt) {
		this.cmdToModelAdpt = adpt;
	}

	@Override
	public Void apply(Class<?> index, MessageDataPacket<EndGameMsg, ICommunicate> host, Void... params) {
		boolean hidersWon = host.getData().getIfHidersWon();
		String theString;
		if(hidersWon) {
			theString = "The Hiding Team Won";
		} else {
			theString = "The Seeking Team Won";
		}
		final String theFinalString = theString;
		cmdToModelAdpt.handleStaticComponentFactory(new Supplier<Component>() {

			@Override
			public Component get() {
				JLabel label = new JLabel();
				label.setText(theFinalString);
				label.setFont (label.getFont ().deriveFont (128.0f));
				return label;
			}
			
		});
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
		cmdToModelAdpt = cmd2ModelAdpt;
	}

}
