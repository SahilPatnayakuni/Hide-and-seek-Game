package sp50_egd2.game.algoCmds;

import java.awt.Component;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import javax.swing.JLabel;

import common.API.ICommCmdToModelAdapter;
import common.API.ICommunicate;
import common.messagePacket.CommMsgAlgoCmd;
import common.messagePacket.MessageDataPacket;
import map.MapLayer;
import map.ToggleAnnotation;
import provided.mixedData.MixedDataKey;
import sp50_egd2.game.messages.AddAnnotationsMsg;

public class AddAnnotationsAlgoCmd extends CommMsgAlgoCmd<AddAnnotationsMsg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 229174639391112862L;
	
	transient private ICommCmdToModelAdapter cmd2ModelAdpt;

	public AddAnnotationsAlgoCmd(ICommCmdToModelAdapter adpt) {
		this.cmd2ModelAdpt = adpt;
	}
	
	@Override
	public Void apply(Class<?> index, MessageDataPacket<AddAnnotationsMsg, ICommunicate> host, Void... params) {
		// TODO Auto-generated method stub
		
//		if (!cmd2ModelAdpt.dictionaryContainsKey(new MixedDataKey<MapLayer>(UUID.fromString("6d230595-a9c0-47c5-abae-4f93108668d4"), "layer", MapLayer.class))) {
//			return null;
//		}
		MapLayer myLayer = cmd2ModelAdpt.getFromDictionary(new MixedDataKey<MapLayer>(UUID.fromString("6d230595-a9c0-47c5-abae-4f93108668d4"), "layer", MapLayer.class));
		myLayer.removeAllAnnotations();
		for (ToggleAnnotation t : host.getData().getAnnotations()) {
			myLayer.addAnnotation(t);
			cmd2ModelAdpt.putInDictionary(new MixedDataKey<MapLayer>(UUID.fromString("6d230595-a9c0-47c5-abae-4f93108668d4"), "layer", MapLayer.class), myLayer);
		}
		cmd2ModelAdpt.putInDictionary(new MixedDataKey<Boolean>(UUID.fromString("0980d767-f3ce-4e04-94bb-0f472ef9418e"), "clicked", Boolean.class), new Boolean(false));
		
		Set<ICommunicate> found = host.getData().getFound();
		
		String foundHiders = "Look who has been found! Bad hiders include: \n";
		
		for (ICommunicate i : found) {
			try {
				foundHiders = foundHiders + i.getName().toString() + "\n";
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		final String fFound = foundHiders;
		
		if (found.isEmpty()) {
			return null;
		}
		
		cmd2ModelAdpt.handleScrollingComponentFactory(new Supplier<Component>() {

			@Override
			public Component get() {
				// TODO Auto-generated method stub
				JLabel label = new JLabel();
				label.setText(fFound);
				
				return label;
			}
			
		});
		
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
		// TODO Auto-generated method stub
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}
