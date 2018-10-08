package sp50_egd2.game.messages;

import common.messageTypes.ICommMsg;
import gov.nasa.worldwind.geom.Position;

public class PositionMsg implements ICommMsg {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1460799553073681628L;
	Double[] p = new Double[2];
	
	public PositionMsg(Position p) {
		this.p[0] = p.getLatitude().getDegrees();
		this.p[1] = p.getLongitude().getDegrees();
	}
	
	public Double[] getPostion() {
		return p;
	}

}
