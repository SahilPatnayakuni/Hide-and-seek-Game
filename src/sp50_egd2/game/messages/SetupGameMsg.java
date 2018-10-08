package sp50_egd2.game.messages;

import java.awt.Component;
import java.util.function.Supplier;

import common.messageTypes.ICommMsg;
import map.MapPanel;
import provided.mixedData.MixedDataKey;

public class SetupGameMsg implements ICommMsg {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1658363619722256467L;
	Supplier<Component> data;
	MixedDataKey<MapPanel> key;
	
	public SetupGameMsg() {
		
	}
	/*
	public SetupGameMsg(Supplier<Component> T, MixedDataKey<MapPanel> key) {
		this.data = T;
		this.key = key;
	}
	
	public Supplier<Component> getSupplier(){
		return data;
	}
	
	public MixedDataKey<MapPanel> getKey() {
		return key;
	}
*/
}
