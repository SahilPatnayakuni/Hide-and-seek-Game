package sp50_egd2.client.model;

import java.awt.Component;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import common.API.ICommCmdToModelAdapter;
import common.API.ICommunicate;
import common.API.IConnect;
import common.API.IConnectCmdToModelAdapter;
import common.API.IGroup;
import common.messagePacket.CommMsgAlgoCmd;
import common.messagePacket.ConnectMsgAlgoCmd;
import common.messagePacket.MessageDataPacket;
import common.messagePacket.MessageDataPacketAlgo;
import common.messageTypes.ICommMsg;
import common.messageTypes.IConnectMsg;
import common.messageTypes.communicate.IJoinGroupMsg;
import common.messageTypes.communicate.IRemoveUsersMsg;
import common.messageTypes.communicate.IAddUsersMsg;
import common.messageTypes.communicate.IGetCommCmdMsg;
import common.messageTypes.communicate.IInstallCommCmdMsg;
import common.messageTypes.connect.IGetConnectCmdMsg;
import common.messageTypes.connect.IInstallConnectCmdMsg;
import common.messageTypes.connect.IInviteMsg;
import common.messageTypes.connect.IJoinMsg;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Earth;
import map.MapPanel;
import map.ToggleAnnotation;
import provided.datapacket.ADataPacket;
import provided.extvisitor.IExtVisitorCmd;
import provided.extvisitor.IExtVisitorHost;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.rmiUtils.*;
import sp50_egd2.api.Group;
import sp50_egd2.api.GroupWrapper;
import sp50_egd2.api.Communicate;
import sp50_egd2.api.Connect;
import sp50_egd2.api.UserWrapper;
import sp50_egd2.api.messages.AddUsersMsg;
import sp50_egd2.api.messages.GetCommCmdMsg;
import sp50_egd2.api.messages.GetConnectCmdMsg;
import sp50_egd2.api.messages.InstallCommCmdMsg;
import sp50_egd2.api.messages.InstallConnectCmdMsg;
import sp50_egd2.api.messages.InviteMsg;
import sp50_egd2.api.messages.JoinGroupMsg;
import sp50_egd2.api.messages.JoinMsg;
import sp50_egd2.api.messages.RemoveUsersMsg;
import sp50_egd2.game.algoCmds.AddAnnotationsAlgoCmd;
import sp50_egd2.game.algoCmds.AddChatRoomAlgoCmd;
import sp50_egd2.game.algoCmds.ChatRoomAlgoCmd;
import sp50_egd2.game.algoCmds.EndGameMsgAlgoCmd;
import sp50_egd2.game.algoCmds.GetPositionMsgAlgoCmd;
import sp50_egd2.game.algoCmds.SetupGameAlgoCmd;
import sp50_egd2.game.messages.AddAnnotationsMsg;
import sp50_egd2.game.messages.AddChatRoomMsg;
import sp50_egd2.game.messages.ChatRoomMsg;
import sp50_egd2.game.messages.EndGameMsg;
import sp50_egd2.game.messages.GetPositionMsg;
import sp50_egd2.game.messages.PositionMsg;
import sp50_egd2.game.messages.SetupGameMsg;

/**
 * the chat app model.
 */
public class GameModel {
	/**
	 * the adapter from model to view.
	 */
	private IModel2ViewAdapter _model2View;
	/**
	 * our registry variable.
	 */
	private Registry registry;
	
	private Connect myIConnect;
	
	IConnect myIConnectStub;
	
	Group hideGroup;
	
	Group seekGroup;
	
	Group clientGroup;
	
	private Communicate hideCommunicate;
	
	private Communicate seekCommunicate;
	
	private Communicate clientComm;
	
	private ICommunicate clientCommStub;
	
	private transient ICommCmdToModelAdapter clientAdapter;
	
	private transient ICommCmdToModelAdapter hideAdapter;
	
	private transient ICommCmdToModelAdapter seekAdapter;
	
	private transient IConnectCmdToModelAdapter connectAdapter;
	
	ICommunicate hideCommunicateStub;
	
	ICommunicate seekCommunicateStub;
	
	String myName;
	
	private MixedDataDictionary mdd;
	
	private boolean isClient = false;
	
	MessageDataPacketAlgo<ICommunicate> hideVisitor;
	
	MessageDataPacketAlgo<ICommunicate> seekVisitor;
	
	Map<Class<? extends ICommMsg>, CommMsgAlgoCmd<?>> gameCmds = new HashMap<Class<? extends ICommMsg>, CommMsgAlgoCmd<?>>();
	
	MixedDataKey<MapPanel> mapPanelKey = new MixedDataKey<MapPanel>(UUID.randomUUID(), "MapPanel", MapPanel.class); 
	
	Map<Class<? extends ICommMsg>, ArrayList<MessageDataPacket<ICommMsg, ICommunicate>>> commMessageCacheMap = new HashMap<Class<? extends ICommMsg>, ArrayList<MessageDataPacket<ICommMsg, ICommunicate>>>();
	
	Map<Class<? extends IConnectMsg>, ArrayList<MessageDataPacket<IConnectMsg, IConnect>>> connectMessageCacheMap = new HashMap<Class<? extends IConnectMsg>, ArrayList<MessageDataPacket<IConnectMsg, IConnect>>>();
	
	Map<ICommunicate, Position> hiderPositions = new HashMap<ICommunicate, Position>();
	
	Map<ICommunicate, Position> seekerPositions = new HashMap<ICommunicate, Position>();
	
	Set<ICommunicate> globalFound = new HashSet<ICommunicate>();

	/**
	 * Consumer output string to define how RMI users can add to the view of the GUI.
	 */
	private Consumer<String> output = new Consumer<String>() {
		@Override
		public void accept(String t) {
		}
	};

	/**
	 * Creating a RMI utility to use provided utilities.
	 */
	private IRMIUtils util = new RMIUtils(output);

	/**
	 * the constructor for the chat app model.
	 * @param adapter - the adapter for this model to the view
	 */
	public GameModel(IModel2ViewAdapter adapter) {
		this._model2View = adapter;
	}

	/**
	 * the start method for the model, which will bind our chat app to the registry.
	 */
	public void start() {
		myName = "edi_sahil";
		try {
			_model2View.setServerHost(util.getLocalAddress());
		} catch (Exception e) {
			System.err.println("ERROR: Failed to identify local client address.");
		}

		try {
			util.startRMI(IRMI_Defs.CLASS_SERVER_PORT_CLIENT);
			
			mdd = new MixedDataDictionary();
			
			clientAdapter = new ICommCmdToModelAdapter() {

				@Override
				public <T extends ICommMsg> void sendMessageTo(ICommunicate recipient, Class<T> id, T message) {
					// TODO Auto-generated method stub
					try {
						recipient.processMessage(new MessageDataPacket<ICommMsg, ICommunicate>((Class<ICommMsg>) id, message, clientCommStub));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void handleScrollingComponentFactory(Supplier<Component> compFac) {
					// TODO Auto-generated method stub
					_model2View.addScrollingComponent(compFac);
				}

				@Override
				public void handleStaticComponentFactory(Supplier<Component> compFac) {
					// TODO Auto-generated method stub
					_model2View.addStaticComponent(compFac);
				}

				@Override
				public <T> T getFromDictionary(MixedDataKey<T> key) {
					// TODO Auto-generated method stub
					return mdd.get(key);
				}

				@Override
				public <T> void putInDictionary(MixedDataKey<T> key, T obj) {
					// TODO Auto-generated method stub
					 mdd.put(key, obj);
				}

				@Override
				public boolean dictionaryContainsKey(MixedDataKey<?> key) {
					// TODO Auto-generated method stub
					return mdd.containsKey(key);
				}

				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return myName;
				}

				@Override
				public IGroup getGroup() {
					// TODO Auto-generated method stub
					return clientGroup;
				}

				@SuppressWarnings("unchecked")
				@Override
				public <T extends ICommMsg> void sendMessageToLocalSet(Class<T> id, T message) {
					// TODO Auto-generated method stub
					clientGroup.sendMessage(new MessageDataPacket<ICommMsg, ICommunicate>((Class<ICommMsg>) id, message, clientCommStub));
				}
			};
			
			hideAdapter = new ICommCmdToModelAdapter() {

				@Override
				public <T extends ICommMsg> void sendMessageTo(ICommunicate recipient, Class<T> id, T message) {
					// TODO Auto-generated method stub
					try {
						recipient.processMessage(new MessageDataPacket<ICommMsg, ICommunicate>((Class<ICommMsg>) id, message, hideCommunicateStub));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void handleScrollingComponentFactory(Supplier<Component> compFac) {
				}

				@Override
				public void handleStaticComponentFactory(Supplier<Component> compFac) {
				}

				@Override
				public <T> T getFromDictionary(MixedDataKey<T> key) {
					// TODO Auto-generated method stub
					return mdd.get(key);
				}

				@Override
				public <T> void putInDictionary(MixedDataKey<T> key, T obj) {
					// TODO Auto-generated method stub
					 mdd.put(key, obj);
				}

				@Override
				public boolean dictionaryContainsKey(MixedDataKey<?> key) {
					// TODO Auto-generated method stub
					return mdd.containsKey(key);
				}

				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return myName;
				}

				@Override
				public IGroup getGroup() {
					// TODO Auto-generated method stub
					return hideGroup;
				}

				@SuppressWarnings("unchecked")
				@Override
				public <T extends ICommMsg> void sendMessageToLocalSet(Class<T> id, T message) {
					// TODO Auto-generated method stub
					hideGroup.sendMessage(new MessageDataPacket<ICommMsg, ICommunicate>((Class<ICommMsg>) id, message, hideCommunicateStub));
				}
			};
			
			seekAdapter = new ICommCmdToModelAdapter() {

				@Override
				public <T extends ICommMsg> void sendMessageTo(ICommunicate recipient, Class<T> id, T message) {
					// TODO Auto-generated method stub
					try {
						recipient.processMessage(new MessageDataPacket<ICommMsg, ICommunicate>((Class<ICommMsg>) id, message, seekCommunicateStub));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void handleScrollingComponentFactory(Supplier<Component> compFac) {
				}

				@Override
				public void handleStaticComponentFactory(Supplier<Component> compFac) {
				}

				@Override
				public <T> T getFromDictionary(MixedDataKey<T> key) {
					// TODO Auto-generated method stub
					return mdd.get(key);
				}

				@Override
				public <T> void putInDictionary(MixedDataKey<T> key, T obj) {
					// TODO Auto-generated method stub
					 mdd.put(key, obj);
				}

				@Override
				public boolean dictionaryContainsKey(MixedDataKey<?> key) {
					// TODO Auto-generated method stub
					return mdd.containsKey(key);
				}

				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return myName;
				}

				@Override
				public IGroup getGroup() {
					// TODO Auto-generated method stub
					return seekGroup;
				}

				@SuppressWarnings("unchecked")
				@Override
				public <T extends ICommMsg> void sendMessageToLocalSet(Class<T> id, T message) {
					// TODO Auto-generated method stub
					seekGroup.sendMessage(new MessageDataPacket<ICommMsg, ICommunicate>((Class<ICommMsg>) id, message, seekCommunicateStub));
				}
			};
			
			hideGroup = new Group();
			
			seekGroup = new Group();
			
			clientGroup = new Group();
			
			hideGroup.setName("Hiders");
			
			seekGroup.setName("Seekers");
			
			hideCommunicate = new Communicate(null, myName, hideGroup.getName(), hideGroup.getUUID(), myIConnectStub);

			seekCommunicate = new Communicate(null, myName, seekGroup.getName(), seekGroup.getUUID(), myIConnectStub);
			
			hideCommunicateStub = (ICommunicate) UnicastRemoteObject.exportObject(hideCommunicate, IRMI_Defs.CLASS_SERVER_PORT_SERVER);
			
			hideCommunicate.setVisitor(makeCommunicateVisitor(hideAdapter, hideCommunicate, hideCommunicateStub));
			
			seekCommunicateStub = (ICommunicate) UnicastRemoteObject.exportObject(seekCommunicate, IRMI_Defs.CLASS_SERVER_PORT_SERVER);
			
			seekCommunicate.setVisitor(makeCommunicateVisitor(seekAdapter, seekCommunicate, seekCommunicateStub));
				
			hideGroup.addMember(hideCommunicateStub);
			
			seekGroup.addMember(seekCommunicateStub);
			
			addGameCmds();
			
			connectAdapter = new IConnectCmdToModelAdapter() {

				@Override
				public <T extends IConnectMsg> void sendMessageTo(IConnect recipient, Class<T> id, T message) {
					try {
						recipient.processMessage(new MessageDataPacket<IConnectMsg, IConnect>((Class<IConnectMsg>) id, message, myIConnectStub));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void handleScrollingComponentFactory(Supplier<Component> compFac) {
					_model2View.addScrollingComponent(compFac);
					
				}

				@Override
				public void handleStaticComponentFactory(Supplier<Component> compFac) {
					_model2View.addStaticComponent(compFac);
					
				}

				@Override
				public <T> T getFromDictionary(MixedDataKey<T> key) {
					// TODO Auto-generated method stub
					return mdd.get(key);
				}

				@Override
				public <T> void putInDictionary(MixedDataKey<T> key, T obj) {
					mdd.put(key, obj);
					
				}

				@Override
				public boolean dictionaryContainsKey(MixedDataKey<?> key) {
					// TODO Auto-generated method stub
					return mdd.containsKey(key);
				}

				@Override
				public String getName() {
					// TODO Auto-generated method stub
					try {
						return myIConnect.getName();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				}

				@Override
				public Set<IConnect> getIConnects() {
					// TODO Auto-generated method stub
					return myIConnect.getOtherStubs();
				}
				
			};
			
			MessageDataPacketAlgo<IConnect> connectVisitor = new MessageDataPacketAlgo<IConnect>(new ConnectMsgAlgoCmd<IConnectMsg>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 2132328832965541251L;
				transient IConnectCmdToModelAdapter cmdToModelAdpt = connectAdapter;
				
				@Override
				public Void apply(Class<?> index, MessageDataPacket<IConnectMsg, IConnect> host, Void... params) {
					try {
						if(connectMessageCacheMap.containsKey(index)) {
							System.out.println("encountered repeat unknown connect msg " + index.toString());
							ArrayList<MessageDataPacket<IConnectMsg, IConnect>> messagelist = connectMessageCacheMap.get(index);
							messagelist.add(host);
							connectMessageCacheMap.replace((Class<? extends IConnectMsg>) index, messagelist);
						} else {
							System.out.println("encountered new unknown connect msg " + index.toString());
							ArrayList<MessageDataPacket<IConnectMsg, IConnect>> messagelist = new ArrayList<MessageDataPacket<IConnectMsg, IConnect>>();
							messagelist.add(host);
							connectMessageCacheMap.put((Class<? extends IConnectMsg>) index, messagelist);
						}
						IGetConnectCmdMsg msg = new GetConnectCmdMsg((Class<? extends IConnectMsg>) index);
						host.getSender().processMessage(new MessageDataPacket<IGetConnectCmdMsg, IConnect>(IGetConnectCmdMsg.class, msg, myIConnectStub)); // send getConnectCmdMsg
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//
					return null;
				}

				@Override
				public void setCmd2ModelAdpt(IConnectCmdToModelAdapter cmd2ModelAdpt) {
					cmdToModelAdpt = cmd2ModelAdpt;
				}
				
			});
			
			ConnectMsgAlgoCmd<IGetConnectCmdMsg> getConnectCmdAlgo = new ConnectMsgAlgoCmd<IGetConnectCmdMsg>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = -7752884389684904552L;
				transient IConnectCmdToModelAdapter cmdToModelAdpt = connectAdapter;

				@Override
				public void setCmd2ModelAdpt(IConnectCmdToModelAdapter cmd2ModelAdpt) {
					cmdToModelAdpt = cmd2ModelAdpt;
					
				}

				@Override
				public Void apply(Class<?> index, MessageDataPacket<IGetConnectCmdMsg, IConnect> host, Void... params) {
					Class<? extends IConnectMsg> cmdClass = host.getData().getCmdClass();
					IInstallConnectCmdMsg msg = new InstallConnectCmdMsg((ConnectMsgAlgoCmd<?>) connectVisitor.getCmd(cmdClass), cmdClass);
					try {
						host.getSender().processMessage(new MessageDataPacket<IInstallConnectCmdMsg, IConnect>(IInstallConnectCmdMsg.class, msg, myIConnectStub));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					return null;
				}
				
			};
			
			
			connectVisitor.setCmd(IGetConnectCmdMsg.class, getConnectCmdAlgo);
			
			ConnectMsgAlgoCmd<IInstallConnectCmdMsg> installConnectCmdAlgo = new ConnectMsgAlgoCmd<IInstallConnectCmdMsg>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 576368245363932236L;
				transient IConnectCmdToModelAdapter cmdToModelAdpt = connectAdapter;
				
				@Override
				public Void apply(Class<?> index, MessageDataPacket<IInstallConnectCmdMsg, IConnect> host,
						Void... params) {
					IInstallConnectCmdMsg msg = host.getData();
					
					System.out.println("INSTALLING COMMAND " + msg.getMsgClass().toString());
					
					
					Class<? extends IConnectMsg> cmdClass = msg.getMsgClass();
					ConnectMsgAlgoCmd<?> cmd = msg.getCmd();
					cmd.setCmd2ModelAdpt(cmdToModelAdpt);
					connectVisitor.setCmd(cmdClass, cmd);
					if(connectMessageCacheMap.containsKey(cmdClass)) {
						ArrayList<MessageDataPacket<IConnectMsg, IConnect>> messagelist = connectMessageCacheMap.get(cmdClass);
						for(MessageDataPacket<IConnectMsg, IConnect> message : messagelist) {
							message.execute(connectVisitor, (Void[]) null);
						}
					}
					return null;
				}

				@Override
				public void setCmd2ModelAdpt(IConnectCmdToModelAdapter cmd2ModelAdpt) {
					cmdToModelAdpt = cmd2ModelAdpt;
				}
				
			};
			
			connectVisitor.setCmd(IInstallConnectCmdMsg.class, installConnectCmdAlgo);

			ConnectMsgAlgoCmd<IInviteMsg> inviteMsgAlgoCmd = new ConnectMsgAlgoCmd<IInviteMsg>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1406296472416019068L;
				transient IConnectCmdToModelAdapter cmdToModelAdpt = connectAdapter;

				@Override
				public Void apply(Class<?> index, MessageDataPacket<IInviteMsg, IConnect> host, Void... params){
					try {
						ICommunicate communicateStub = host.getData().getStub();
						System.out.println("CREATING CLIENT ICOMMUNICATE");
						clientComm = new Communicate(null, myName, communicateStub.getGroupName(), communicateStub.getGroupUUID(), myIConnectStub);
						clientGroup.setName(communicateStub.getGroupName());
						clientGroup.setUUID(communicateStub.getGroupUUID());
						clientCommStub = (ICommunicate) UnicastRemoteObject.exportObject(clientComm, IRMI_Defs.CLASS_SERVER_PORT_SERVER);
						clientComm.setVisitor(makeCommunicateVisitor(clientAdapter, clientComm, clientCommStub));
						communicateStub.processMessage(new MessageDataPacket<IJoinGroupMsg, ICommunicate>(IJoinGroupMsg.class, new JoinGroupMsg(clientCommStub), clientCommStub));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}

				@Override
				public void setCmd2ModelAdpt(IConnectCmdToModelAdapter cmd2ModelAdpt) {
					cmdToModelAdpt  = cmd2ModelAdpt;					
				}
				
			};
			
			connectVisitor.setCmd(IInviteMsg.class, inviteMsgAlgoCmd);
			
			ConnectMsgAlgoCmd<IJoinMsg> joinMsgAlgoCmd = new ConnectMsgAlgoCmd<IJoinMsg>() {
				/**
				 * serial version ID
				 */
				private static final long serialVersionUID = -3056370086927392106L;
				transient IConnectCmdToModelAdapter cmdToModelAdpt = connectAdapter;
				
				@Override
				public Void apply(Class<?> index, MessageDataPacket<IJoinMsg, IConnect> host, Void... params) {
					if (isClient) {
						return null;
					}
					
					try {
						System.out.println(host.getSender().getName() + " joined.");
						IConnect sender = host.getData().getUserStub();
						InviteMsg msg;
						if(hideGroup.getSize() < seekGroup.getSize() * 2) {
							msg = new InviteMsg(hideCommunicate);
						} else {
							msg = new InviteMsg(seekCommunicate); 
						}
						sender.processMessage(new MessageDataPacket<IInviteMsg, IConnect>(IInviteMsg.class, msg, myIConnectStub));;
					} catch(RemoteException e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				public void setCmd2ModelAdpt(IConnectCmdToModelAdapter cmd2ModelAdpt) {
					cmdToModelAdpt = cmd2ModelAdpt;
				}
				
			};
			
			connectVisitor.setCmd(IJoinMsg.class, joinMsgAlgoCmd);
			
			myIConnect = new Connect(connectVisitor, myName);

			registry = util.getLocalRegistry();

			myIConnectStub = (IConnect) UnicastRemoteObject.exportObject(myIConnect, IRMI_Defs.CLASS_SERVER_PORT_SERVER);
			
			registry.rebind(IConnect.BOUND_NAME, myIConnectStub);
			
			globalFound.add(hideCommunicateStub);

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}


	/**
	 * sets the user name for our chat app model.
	 * @param username - the username we want to set it to
	 */
	public void setUserName(String username) {
		myName = username;
	}
	
	public int connect(String ip) {
		try {

			Registry temp_registry = util.getRemoteRegistry(ip);

			IConnect remoteConnect = (IConnect) temp_registry.lookup(IConnect.BOUND_NAME);  //gets compute object from registry
			
			remoteConnect.exchangeIConnect(myIConnectStub);
			myIConnect.exchangeIConnect(remoteConnect);
			
			MessageDataPacket<IJoinMsg, IConnect> tempMessage = 
					new MessageDataPacket<IJoinMsg, IConnect>(IJoinMsg.class, new JoinMsg(myIConnectStub), myIConnectStub);
			
			remoteConnect.processMessage(tempMessage);
			
			isClient = true;
			
			return 1;

		} catch (Exception e) {
			System.out.println("ERROR: No server connection possible.");
			e.printStackTrace();
			return -1;
		}
	}
	
	public void addGameCmds() {
		
		
		CommMsgAlgoCmd<SetupGameMsg> hideSetupGameAlgoCmd = new SetupGameAlgoCmd(hideAdapter);
		CommMsgAlgoCmd<SetupGameMsg> seekSetupGameAlgoCmd = new SetupGameAlgoCmd(seekAdapter);
		CommMsgAlgoCmd<GetPositionMsg> hideGetPositionMsgAlgoCmd = new GetPositionMsgAlgoCmd(hideAdapter);
		CommMsgAlgoCmd<GetPositionMsg> seekGetPositionMsgAlgoCmd = new GetPositionMsgAlgoCmd(seekAdapter);
		CommMsgAlgoCmd<PositionMsg> hidePositionMsgAlgoCmd = new CommMsgAlgoCmd<PositionMsg>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4791344161629742856L;
			transient ICommCmdToModelAdapter cmdToModelAdpt = hideAdapter;
	
			@Override
			public Void apply(Class<?> index, MessageDataPacket<PositionMsg, ICommunicate> host, Void... params) {
				Double[] posArr = host.getData().getPostion();
				Position pos = Position.fromDegrees(posArr[0], posArr[1]);
				hiderPositions.put(host.getSender(), pos);
				return null;
			}
	
			@Override
			public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
				cmdToModelAdpt = cmd2ModelAdpt;
			}
		};
		CommMsgAlgoCmd<PositionMsg> seekPositionMsgAlgoCmd = new CommMsgAlgoCmd<PositionMsg>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4791344161629742856L;
			transient ICommCmdToModelAdapter cmdToModelAdpt = seekAdapter;
	
			@Override
			public Void apply(Class<?> index, MessageDataPacket<PositionMsg, ICommunicate> host, Void... params) {
				Double[] posArr = host.getData().getPostion();
				Position pos = Position.fromDegrees(posArr[0], posArr[1]);
				seekerPositions.put(host.getSender(), pos);
				return null;
			}
	
			@Override
			public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
				cmdToModelAdpt = cmd2ModelAdpt;
			}
		};
		gameCmds.put(SetupGameMsg.class, hideSetupGameAlgoCmd);
		gameCmds.put(GetPositionMsg.class, hideGetPositionMsgAlgoCmd);
		
		CommMsgAlgoCmd<AddChatRoomMsg> hideAddChatRoomAlgoCmd = new AddChatRoomAlgoCmd(hideAdapter);
		CommMsgAlgoCmd<AddChatRoomMsg> seekAddChatRoomAlgoCmd = new AddChatRoomAlgoCmd(seekAdapter);
		gameCmds.put(AddChatRoomMsg.class, hideAddChatRoomAlgoCmd);
		
		CommMsgAlgoCmd<ChatRoomMsg> hideChatRoomAlgoCmd = new ChatRoomAlgoCmd(hideAdapter);
		CommMsgAlgoCmd<ChatRoomMsg> seekChatRoomAlgoCmd = new ChatRoomAlgoCmd(seekAdapter);
		gameCmds.put(ChatRoomMsg.class, hideChatRoomAlgoCmd);
		
		CommMsgAlgoCmd<AddAnnotationsMsg> hideAnnotationAlgoCmd = new AddAnnotationsAlgoCmd(hideAdapter);
		CommMsgAlgoCmd<AddAnnotationsMsg> seekAnnotationAlgoCmd = new AddAnnotationsAlgoCmd(seekAdapter);
		gameCmds.put(AddAnnotationsMsg.class, hideAnnotationAlgoCmd);
		
		CommMsgAlgoCmd<EndGameMsg> hideEndGameAlgoCmd = new EndGameMsgAlgoCmd(hideAdapter);
		CommMsgAlgoCmd<EndGameMsg> seekEndGameAlgoCmd = new EndGameMsgAlgoCmd(seekAdapter);
		gameCmds.put(EndGameMsg.class, hideEndGameAlgoCmd);
		
		
		
		hideVisitor = hideCommunicate.getVisitor();
		seekVisitor = seekCommunicate.getVisitor();
		
		hideVisitor.setCmd(SetupGameMsg.class, hideSetupGameAlgoCmd);
		seekVisitor.setCmd(SetupGameMsg.class, seekSetupGameAlgoCmd);
		
		hideVisitor.setCmd(PositionMsg.class, hidePositionMsgAlgoCmd);
		seekVisitor.setCmd(PositionMsg.class, seekPositionMsgAlgoCmd);
		
		hideVisitor.setCmd(GetPositionMsg.class, hideGetPositionMsgAlgoCmd);
		seekVisitor.setCmd(GetPositionMsg.class, seekGetPositionMsgAlgoCmd);
		
		hideVisitor.setCmd(AddChatRoomMsg.class, hideAddChatRoomAlgoCmd);
		seekVisitor.setCmd(AddChatRoomMsg.class, seekAddChatRoomAlgoCmd);
		
		hideVisitor.setCmd(ChatRoomMsg.class, hideChatRoomAlgoCmd);
		seekVisitor.setCmd(ChatRoomMsg.class, seekChatRoomAlgoCmd);
		
		hideVisitor.setCmd(AddAnnotationsMsg.class, hideAnnotationAlgoCmd);
		seekVisitor.setCmd(AddAnnotationsMsg.class, seekAnnotationAlgoCmd);
		
		hideVisitor.setCmd(EndGameMsg.class, hideEndGameAlgoCmd);
		seekVisitor.setCmd(EndGameMsg.class, seekEndGameAlgoCmd);
		
		hideCommunicate.setVisitor(hideVisitor);
		seekCommunicate.setVisitor(seekVisitor);
	}
	
	public void initGame(ICommunicate senderStub, ICommunicate player) {
		//player.processMessage(new MessageDataPacket(SetupMapMessage.class, new SetupMapMessage(), team));
		//player.processMessage(new MessageDataPacket(SetupClickListenerMessage.class, new SetupClickListenerMessage(), team));
		try {
			for (Entry<Class<? extends ICommMsg>, CommMsgAlgoCmd<?>> entry : gameCmds.entrySet()) {
			    Class<? extends ICommMsg> key = entry.getKey();
			     CommMsgAlgoCmd<?> value = entry.getValue();
			     player.processMessage(new MessageDataPacket<IInstallCommCmdMsg, ICommunicate>(IInstallCommCmdMsg.class, 
							new InstallCommCmdMsg(key, value), senderStub));
			}
			SetupGameMsg setupGameMsg = new SetupGameMsg();
			player.processMessage(new MessageDataPacket<SetupGameMsg, ICommunicate>(SetupGameMsg.class, 
					setupGameMsg, senderStub));
			AddChatRoomMsg addChatRoomMsg = new AddChatRoomMsg();
			player.processMessage(new MessageDataPacket<AddChatRoomMsg, ICommunicate>(AddChatRoomMsg.class, 
					addChatRoomMsg, senderStub));
			
//			try {
//				TimeUnit.SECONDS.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void endGameHidersWin() {
		endGame(true);
	}
	
	public void endGameSeekersWin() {
		endGame(false);
	}
	
	public void endGame(Boolean hiderswon) {
		EndGameMsg msg = new EndGameMsg(hiderswon);
		hideGroup.sendMessage(new MessageDataPacket<EndGameMsg, ICommunicate>(EndGameMsg.class, msg, hideCommunicateStub));
		seekGroup.sendMessage(new MessageDataPacket<EndGameMsg, ICommunicate>(EndGameMsg.class, msg, seekCommunicateStub));
	}
	
	public void nextRound() {
		try {
			Set<ICommunicate> oldHideMembers = new HashSet<ICommunicate>(hideGroup.getMembers());
			oldHideMembers.remove(hideCommunicateStub);
			
			System.out.println("people in oldHiders at the start of nextround");
			for (ICommunicate p : oldHideMembers) {
				System.out.println(p.getName());
			}
			
			Set<ICommunicate> hideMembers = new HashSet<ICommunicate>(hideGroup.getMembers());
			Set<ICommunicate> found = checkPosition();
			
			for (ICommunicate c : found) {
				hiderPositions.remove(c);
			}
			
			hideMembers.removeAll(globalFound);
			
			Set<ICommunicate> seekMembers = seekGroup.getMembers();
			seekMembers.remove(seekCommunicateStub);
			GetPositionMsg msg = new GetPositionMsg();
			
			for(ICommunicate hider: hideMembers) {
				hider.processMessage(new MessageDataPacket<GetPositionMsg, ICommunicate>(GetPositionMsg.class, msg, hideCommunicateStub));
			}
			
			for(ICommunicate seeker: seekMembers) {
				seeker.processMessage(new MessageDataPacket<GetPositionMsg, ICommunicate>(GetPositionMsg.class, msg, seekCommunicateStub));
			}
			
			if(hideMembers.isEmpty()) {
				endGame(false);
			}
			
			Set<ToggleAnnotation> annotationSet = new HashSet<ToggleAnnotation>();
			
			for(ICommunicate hider : hiderPositions.keySet()) {
				System.out.println("hider " + hider.getName() +": "+ hiderPositions.get(hider).toString());
				String hiderString = "Hider: " + hider.getName();
				annotationSet.add(new ToggleAnnotation(hiderString, hiderString, hiderPositions.get(hider)));
			}
			
			for(ICommunicate seeker : seekerPositions.keySet()) {
				System.out.println("seeker " + seeker.getName() +": "+  seekerPositions.get(seeker).toString());
				String seekerString = "Seeker: " + seeker.getName();
				annotationSet.add(new ToggleAnnotation(seekerString, seekerString, seekerPositions.get(seeker)));
			}
			
			AddAnnotationsMsg addAnnotationsMsg = new AddAnnotationsMsg(annotationSet, found);
			
			System.out.println();
			
			System.out.println("people in oldHiders at the end of nextround");
			for (ICommunicate p : oldHideMembers) {
				System.out.println(p.getName());
			}
			
			for (ICommunicate player: oldHideMembers) {
				player.processMessage(new MessageDataPacket<AddAnnotationsMsg, ICommunicate>(AddAnnotationsMsg.class,
						addAnnotationsMsg, hideCommunicateStub));
			}
			
			for (ICommunicate player: seekMembers) {
				player.processMessage(new MessageDataPacket<AddAnnotationsMsg, ICommunicate>(AddAnnotationsMsg.class,
						addAnnotationsMsg, seekCommunicateStub));
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public Set<ICommunicate> checkPosition() {
		Set<ICommunicate> foundHiders = new HashSet<ICommunicate>();
		
		for (ICommunicate pKey : seekerPositions.keySet()) {
			Position p = seekerPositions.get(pKey);
			for (ICommunicate oKey : hiderPositions.keySet()) {
				Position o = hiderPositions.get(oKey);
				if (Position.linearDistance(p, o).getRadians() < (Angle.fromDegrees(45)).getRadians()) {
					try {
						System.out.println("Found " + oKey.getName());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					foundHiders.add(oKey);
				}
			}
		}
		
		globalFound.addAll(foundHiders);
		
		return foundHiders;
	}
	
	private MessageDataPacketAlgo<ICommunicate> makeCommunicateVisitor(ICommCmdToModelAdapter adpt, ICommunicate thisComm, ICommunicate thisCommStub) {
		MessageDataPacketAlgo<ICommunicate> commVisitor = new MessageDataPacketAlgo<ICommunicate>(null);
				
		CommMsgAlgoCmd<ICommMsg> defaultCmd = new CommMsgAlgoCmd<ICommMsg>() {
			/**
			 * seerial version id
			 */
			private static final long serialVersionUID = -428551036350540355L;
			transient ICommCmdToModelAdapter cmd2ModelAdpt;
			
			@SuppressWarnings("unchecked")
			@Override
			public Void apply(Class<?> index, MessageDataPacket<ICommMsg, ICommunicate> host, Void... params) {
				try {
					if(commMessageCacheMap.containsKey(index)) {
						System.out.println("repeat unknown Comm message encountered " + index.toString());
						ArrayList<MessageDataPacket<ICommMsg, ICommunicate>> messagelist = commMessageCacheMap.get(index);
						messagelist.add(host);
						commMessageCacheMap.replace((Class<? extends ICommMsg>) index, messagelist);
					} else {
						System.out.println("new unknown Comm message encountered " + index.toString());
						ArrayList<MessageDataPacket<ICommMsg, ICommunicate>> messagelist = new ArrayList<MessageDataPacket<ICommMsg, ICommunicate>>();
						messagelist.add(host);
						commMessageCacheMap.put((Class<? extends ICommMsg>) index, messagelist);
					}
					host.getSender().processMessage((MessageDataPacket<? extends ICommMsg, ICommunicate>)
							new MessageDataPacket<IGetCommCmdMsg, ICommunicate>(IGetCommCmdMsg.class, new GetCommCmdMsg((Class<? extends ICommMsg>) index) , thisCommStub));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
				this.cmd2ModelAdpt = cmd2ModelAdpt;
			}
		};
		defaultCmd.setCmd2ModelAdpt(adpt);
		commVisitor.setDefaultCmd(defaultCmd);
		
		CommMsgAlgoCmd<IInstallCommCmdMsg> installCmd = new CommMsgAlgoCmd<IInstallCommCmdMsg>() {

			/**
			 * serial version ID
			 */
			private static final long serialVersionUID = -4364989839633466984L;
			
			transient ICommCmdToModelAdapter cmd2ModelAdpt; 

			@Override
			public Void apply(Class<?> index, MessageDataPacket<IInstallCommCmdMsg, ICommunicate> host, Void... params) {
				IInstallCommCmdMsg msg = host.getData();
				
				try {
					System.out.println(host.getSender().getName());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("INSTALLING COMMAND " + msg.getMsgClass().toString());
				
				CommMsgAlgoCmd<?> cmd = msg.getCmd();
				
				cmd.setCmd2ModelAdpt(cmd2ModelAdpt);
				commVisitor.setCmd(msg.getMsgClass(), cmd);
				Class<? extends ICommMsg> msgClass = msg.getMsgClass();
				if(commMessageCacheMap.containsKey(msgClass)) {
					
					ArrayList<MessageDataPacket<ICommMsg, ICommunicate>> messagelist = commMessageCacheMap.get(msgClass);
					for(MessageDataPacket<ICommMsg, ICommunicate> message : messagelist) {
						System.out.println("executing commands");
						MessageDataPacketAlgo<ICommunicate> v = ((Communicate) thisComm).getVisitor();
						message.execute(((Communicate) thisComm).getVisitor(), (Void[]) null);
					}
					commMessageCacheMap.remove(msgClass);
				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				this.cmd2ModelAdpt = cmd2ModelAdpt;
			}
			
		};
		installCmd.setCmd2ModelAdpt(adpt);
		commVisitor.setCmd(IInstallCommCmdMsg.class, installCmd);
		
		CommMsgAlgoCmd<IAddUsersMsg> addUsersCmd = new CommMsgAlgoCmd<IAddUsersMsg>() {

			/**
			 * serial version ID
			 */
			private static final long serialVersionUID = -4364989839633466984L;
			
			transient ICommCmdToModelAdapter cmd2ModelAdpt; 

			@Override
			public Void apply(Class<?> index, MessageDataPacket<IAddUsersMsg, ICommunicate> host, Void... params) {
				IAddUsersMsg msg = host.getData();
				
				Group group = (Group)cmd2ModelAdpt.getGroup();
				int size = group.getSize();
				group.addMembers(msg.getICommunicates());
				size = size - group.getSize();
				
				if (size < 0) {
					group.sendMessage(host);
				}
				
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				this.cmd2ModelAdpt = cmd2ModelAdpt;
			}
			
		};
		addUsersCmd.setCmd2ModelAdpt(adpt);
		commVisitor.setCmd(IAddUsersMsg.class, addUsersCmd);
		
		CommMsgAlgoCmd<IJoinGroupMsg> joinGroupCmd = new CommMsgAlgoCmd<IJoinGroupMsg>() {

			/**
			 * serial version ID
			 */
			private static final long serialVersionUID = -4364989839633466984L;
			
			transient ICommCmdToModelAdapter cmd2ModelAdpt; 

			@Override
			public Void apply(Class<?> index, MessageDataPacket<IJoinGroupMsg, ICommunicate> host, Void... params) {
				IJoinGroupMsg msg = host.getData();
				Group group = (Group) cmd2ModelAdpt.getGroup();
				group.addMember(msg.getUserStub());
				HashSet<ICommunicate> everyone = new HashSet<ICommunicate>(group.getMembers());
				AddUsersMsg addMsg = new AddUsersMsg(everyone);
				group.sendMessage(new MessageDataPacket<IAddUsersMsg, ICommunicate>(IAddUsersMsg.class, addMsg, thisCommStub));
				if(!isClient) {
					initGame(seekCommunicateStub, msg.getUserStub());
				}
				
				
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				this.cmd2ModelAdpt = cmd2ModelAdpt;
			}
			
		};
		joinGroupCmd.setCmd2ModelAdpt(adpt);
		commVisitor.setCmd(IJoinGroupMsg.class, joinGroupCmd);
		
		CommMsgAlgoCmd<IRemoveUsersMsg> removeUsersCmd = new CommMsgAlgoCmd<IRemoveUsersMsg>() {

			/**
			 * serial version ID
			 */
			private static final long serialVersionUID = -4364989839633466984L;
			
			transient ICommCmdToModelAdapter cmd2ModelAdpt; 

			@Override
			public Void apply(Class<?> index, MessageDataPacket<IRemoveUsersMsg, ICommunicate> host, Void... params) {
				IRemoveUsersMsg msg = host.getData();
				
				Set<ICommunicate> set = msg.getICommunicates();
				
				System.out.println("Removing users now\n");
				
				Group group = (Group)cmd2ModelAdpt.getGroup();
				int size = group.getSize();
				group.removeMembers(set);	
				size = size - group.getSize();
				
				if (size > 0) {
					group.sendMessage(host);
				}
				
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				this.cmd2ModelAdpt = cmd2ModelAdpt;
			}
			
		};
		removeUsersCmd.setCmd2ModelAdpt(adpt);
		commVisitor.setCmd(IRemoveUsersMsg.class, removeUsersCmd);
		
		CommMsgAlgoCmd<IGetCommCmdMsg> getCommCmdMsg = new CommMsgAlgoCmd<IGetCommCmdMsg>() {

			/**
			 * serial version ID
			 */
			private static final long serialVersionUID = -4364989839633466984L;
			
			transient ICommCmdToModelAdapter cmd2ModelAdpt; 

			@Override
			public Void apply(Class<?> index, MessageDataPacket<IGetCommCmdMsg, ICommunicate> host, Void... params) {
				IGetCommCmdMsg msg = host.getData();
				
				Class<? extends ICommMsg> cmdClass = msg.getCmdClass();
				
				System.out.println("someone requested " + cmdClass.toString());
				
				try {
					
//					host.getSender().processMessage(
//							new MessageDataPacket<IInstallCommCmdMsg, ICommunicate>(IInstallCommCmdMsg.class, new IInstallCommCmdMsg() {
//
//								/**
//								 * 
//								 */
//								private static final long serialVersionUID = -9124966782249845694L;
//
//								@Override
//								public CommMsgAlgoCmd<?> getCmd() {
//									return (CommMsgAlgoCmd<?>) ((Communicate)thisComm).getVisitor().getCmd(cmdClass);
//								}
//
//								@Override
//								public Class<? extends ICommMsg> getMsgClass() {
//									// TODO Auto-generated method stub
//									return cmdClass;
//								}
//								
//							}, thisComm));
					MessageDataPacketAlgo<ICommunicate> v = ((Communicate) thisComm).getVisitor();
					CommMsgAlgoCmd<?> c = (CommMsgAlgoCmd<?>)((Communicate)thisComm).getVisitor().getCmd(cmdClass);
					
					host.getSender().processMessage(new MessageDataPacket<IInstallCommCmdMsg, ICommunicate>(IInstallCommCmdMsg.class, 
							new InstallCommCmdMsg(cmdClass, (CommMsgAlgoCmd<?>)((Communicate)thisComm).getVisitor().getCmd(cmdClass)), thisCommStub));
						
						
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				this.cmd2ModelAdpt = cmd2ModelAdpt;
			}
			
		};
		getCommCmdMsg.setCmd2ModelAdpt(adpt);
		commVisitor.setCmd(IGetCommCmdMsg.class, getCommCmdMsg);
		
		return commVisitor;
	}
	
	public void leaveAll() {
//		System.out.println("got here");
		Set<ICommunicate> setOfMe = new HashSet<ICommunicate>();
		setOfMe.add(clientCommStub);
		clientGroup.removeMembers(setOfMe);
		RemoveUsersMsg msg = new RemoveUsersMsg(setOfMe);
		clientGroup.sendMessage(new MessageDataPacket<IRemoveUsersMsg, ICommunicate>(IRemoveUsersMsg.class, msg, clientCommStub));
		for (IConnect other : myIConnect.getOtherStubs()) {
			try {
				other.removeIConnect(myIConnectStub);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}
