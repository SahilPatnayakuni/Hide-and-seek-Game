package sp50_egd2.game.messages;

import common.messageTypes.ICommMsg;

public class ChatRoomMsg implements ICommMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4865432809315804703L;

	private String text;
	
	public ChatRoomMsg(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
}
