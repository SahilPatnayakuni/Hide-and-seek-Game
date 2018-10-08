package sp50_egd2.game.algoCmds;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.rmi.RemoteException;
import java.util.UUID;
import java.util.function.Supplier;

import javax.swing.JLabel;
import javax.swing.JPanel;

import common.API.ICommCmdToModelAdapter;
import common.API.ICommunicate;
import common.messagePacket.CommMsgAlgoCmd;
import common.messagePacket.MessageDataPacket;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.EarthFlat;
import map.IRightClickAction;
import map.MapLayer;
import map.MapPanel;
import map.ToggleAnnotation;
import provided.mixedData.MixedDataKey;
import sp50_egd2.game.messages.SetupGameMsg;

public class SetupGameAlgoCmd extends CommMsgAlgoCmd<SetupGameMsg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5949907855186277451L;

	transient ICommCmdToModelAdapter cmdToModelAdpt;
	
	public SetupGameAlgoCmd(ICommCmdToModelAdapter adpt) {
		this.cmdToModelAdpt = adpt;
	}
	
	@Override
	public Void apply(Class<?> index, MessageDataPacket<SetupGameMsg, ICommunicate> host, Void... params) {
		System.out.println("making supplier....");
		Supplier<Component> mapPanelSupplier = new Supplier<Component>() {

			@Override
			public Component get() {
				MapPanel mapPanel = new MapPanel(EarthFlat.class);
				
				MapLayer myLayer = new MapLayer();
				
				
				mapPanel.setPreferredSize(new Dimension(750, 500));
				try {
					mapPanel.addRightClickAction(new IRightClickAction() {
	
						@Override
						public void apply(Position p) {
							// TODO Auto-generated method stub
							
							if (cmdToModelAdpt.getFromDictionary(new MixedDataKey<Boolean>(UUID.fromString("0980d767-f3ce-4e04-94bb-0f472ef9418e"), "clicked", Boolean.class))) {
								return;
							}
							
							Position newPos = null;
							cmdToModelAdpt.putInDictionary(new MixedDataKey<Boolean>(UUID.fromString("0980d767-f3ce-4e04-94bb-0f472ef9418e"), "clicked", Boolean.class), true);
							
							if (cmdToModelAdpt.dictionaryContainsKey(new MixedDataKey<Position>(UUID.fromString("b35b9f7c-3b91-4703-9c2f-ba2cccb1244d"), "position", Position.class))) {
								Position oldPos = cmdToModelAdpt.getFromDictionary(new MixedDataKey<Position>(UUID.fromString("b35b9f7c-3b91-4703-9c2f-ba2cccb1244d"), "position", Position.class));
								if (Position.linearDistance(oldPos, p).getDegrees() > 45) {
									Angle linAzi = Position.linearAzimuth(oldPos, p);
									newPos = Position.fromDegrees(oldPos.getLatitude().getDegrees() + (-1. * 45. * linAzi.cos()),
											oldPos.getLongitude().getDegrees() + (45. * linAzi.sin()));
								} else {
									newPos = p;
								}
							} else {
								newPos = p;
							}
							
							cmdToModelAdpt.putInDictionary(new MixedDataKey<Position>(UUID.fromString("b35b9f7c-3b91-4703-9c2f-ba2cccb1244d"), "position", Position.class), newPos);
							
							//myLayer.addAnnotation(new ToggleAnnotation(cmdToModelAdpt.getName(), cmdToModelAdpt.getName(), p));
						}
					});
				} catch(Exception e) {
					System.out.println("Invalid new position.");
				}
				mapPanel.start();
				mapPanel.addLayer(myLayer);
				cmdToModelAdpt.putInDictionary(new MixedDataKey<MapLayer>(UUID.fromString("6d230595-a9c0-47c5-abae-4f93108668d4"), "layer", MapLayer.class), myLayer);
				return mapPanel;
			}
			
		};
		
		Supplier<Component> teamNameSupplier = new Supplier<Component>() {

			@Override
			public Component get() {
				// TODO Auto-generated method stub
				JLabel teamname = new JLabel();
				teamname.setText("Your team is " + cmdToModelAdpt.getGroup().getName());
				return teamname;
			}
			
		};
		
		/*
		SetupGameMsg msg = host.getData();
		Supplier<Component> mapPanelSupplier = host.getData().getSupplier();
		MapPanel mapPanel = (MapPanel) mapPanelSupplier.get();
		*/
		//cmdToModelAdpt.putInDictionary(msg.getKey(), mapPanel);
		//MapLayer layer = cmdToModelAdpt.getFromDictionary(new MixedDataKey<MapLayer>(UUID.fromString("6d230595-a9c0-47c5-abae-4f93108668d4"), "layer", MapLayer.class));
		cmdToModelAdpt.putInDictionary(new MixedDataKey<Boolean>(UUID.fromString("0980d767-f3ce-4e04-94bb-0f472ef9418e"), "clicked", Boolean.class), new Boolean(false));
		System.out.println("displaying map...");
		cmdToModelAdpt.handleStaticComponentFactory(mapPanelSupplier);
		System.out.println("displaying team");
		cmdToModelAdpt.handleScrollingComponentFactory(teamNameSupplier);
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
		cmdToModelAdpt = cmd2ModelAdpt;
		
	}
}
