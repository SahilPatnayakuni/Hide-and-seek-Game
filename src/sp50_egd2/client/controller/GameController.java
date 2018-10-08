package sp50_egd2.client.controller;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Supplier;

import common.API.ICommunicate;
import sp50_egd2.api.GroupWrapper;
import sp50_egd2.api.UserWrapper;

import sp50_egd2.client.model.GameModel;
import sp50_egd2.client.model.IModel2ViewAdapter;
import sp50_egd2.client.view.GameView;
import sp50_egd2.client.view.IView2ModelAdapter;

/**
 * Controller for the Client, connecting the model and the view
 * together by defining the adapters.
 */
public class GameController {

	/**
	 * Variable holding the client model such that it can be operated
	 * upon by the view.
	 */
	private GameModel _chatAppModel;

	/**
	 * Variable holding the client view such that it can be operated upon
	 * by the model.
	 */
	private GameView _chatAppView;

	/**
	 * Constructor for controller, defining the adapters between the views
	 * and the models and instantiating both the view and the model.
	 */
	public GameController() {
		_chatAppView = new GameView(
				new IView2ModelAdapter() {

					@Override
					public void updateUsername(String newUsername) {
						_chatAppModel.setUserName(newUsername);						
					}

					@Override
					public int connect(String ip) {
						return _chatAppModel.connect(ip);
					}

					@Override
					public void closeApp() {
						_chatAppModel.leaveAll();
					}
					
					@Override
					public void nextRound() {
						_chatAppModel.nextRound();
					}

					@Override
					public void endGameHidersWin() {
						_chatAppModel.endGameHidersWin();
						
					}

					@Override
					public void endGameSeekersWin() {
						_chatAppModel.endGameSeekersWin();
						
					}

				});

		_chatAppModel = new GameModel(new IModel2ViewAdapter() {

			@Override
			public void setServerHost(String IP) {
				_chatAppView.setIP(IP);
			}

			@Override
			public void addScrollingComponent(Supplier<Component> compFac) {
				// TODO Auto-generated method stub
				_chatAppView.addScrollingComponent(compFac);
			}

			@Override
			public void addStaticComponent(Supplier<Component> compFac) {
				// TODO Auto-generated method stub
				_chatAppView.addStaticComponent(compFac);
			}
		});
	}

	/**
	 * Initialization method for controller to start the model and view.
	 */
	public void start() {
		_chatAppView.start();
		_chatAppModel.start();
	}

	/**
	 * Launch the application.
	 * @param args input command line arguements
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			/**
			 * Method that runs the controller on an event queue.
			 */
			public void run() {
				try {
					GameController frame = new GameController();
					frame.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}