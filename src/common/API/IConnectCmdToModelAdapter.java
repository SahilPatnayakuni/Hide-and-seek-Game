package common.API;

import java.util.Set;

import common.messageTypes.IConnectMsg;

/**
 * This is the CmdToModelAdapter that bridges the scope between the IConnect commands and the model.
 * @author ZachGramstad
 *
 */
public interface IConnectCmdToModelAdapter extends ICmdToModelAdapter<IConnectMsg, IConnect> {
	
	/**
	 * Return a COPY of the set of all the IConnects that are available. (i.e. your own IConnect and all the ones that 
	 * have connected to you.
	 * @return the set of known IConnects
	 */
	Set<IConnect> getIConnects();
}
