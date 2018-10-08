package sp50_egd2.game.messages;

import java.util.Collection;
import java.util.Set;

import common.API.ICommunicate;
import common.messageTypes.ICommMsg;
import map.ToggleAnnotation;

public class AddAnnotationsMsg implements ICommMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8004571419960679744L;

	private Collection<ToggleAnnotation> annotations;
	Set<ICommunicate> found;
	
	public AddAnnotationsMsg(Collection<ToggleAnnotation> annotations, Set<ICommunicate> found) {
		this.annotations = annotations;
		this.found = found;
	}
	
	public Collection<ToggleAnnotation> getAnnotations() {
		return annotations;	
	}
	
	public Set<ICommunicate> getFound() {
		return found;
	}
}
