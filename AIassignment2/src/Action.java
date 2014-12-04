import java.lang.annotation.Target;



public class Action {
	private ActionType _type;
	private Vertex _target;
	
	public Action(ActionType type, Vertex target){
		_type = type;
		_target = target;
	}

	public ActionType get_type() {
		return _type;
	}

	public Vertex get_target() {
		return _target;
	}

	@Override
	public String toString() {
		return "Action [_type=" + _type + ", _target=" + _target + "]";
	}
	
	

}
