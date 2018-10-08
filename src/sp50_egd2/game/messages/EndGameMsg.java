package sp50_egd2.game.messages;

import common.messageTypes.ICommMsg;

public class EndGameMsg implements ICommMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9190253461166703850L;
	private Boolean hiderswon;
	
	public EndGameMsg(Boolean hiderswon) {
		this.hiderswon = hiderswon;
	}
	
	public boolean getIfHidersWon() {
		return hiderswon;
	}

}
