package sp50_egd2.client.view;

import java.util.Collection;
import java.util.UUID;

/**
 * adapter from main view to main model
 * @author sahilpatnayakuni, irisgau
 *
 * @param <TDL1> the other users
 * @param <TDL2> the chatrooms
 */
public interface IView2ModelAdapter {
	
	/**
	 * updates the username
	 * @param newUsername username to update to
	 */
	public void updateUsername(String newUsername);
	
	/**
	 * tells model to connect to a user
	 * @param userID user to connect to
	 * @return return code
	 */
	public int connect(String ip);
	
	/**
	 * tells the model the app is closing
	 */
	public void closeApp();
	
	public void nextRound();
	
	public void endGameHidersWin();
	
	public void endGameSeekersWin();
	
	
}
