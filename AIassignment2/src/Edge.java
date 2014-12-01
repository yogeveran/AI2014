
public class Edge {
	public Vertex get_target() {
		return _v;
	}

	public double get_weight() {
		return _weight;
	}

	private Vertex _v;
	private double _weight;
	private boolean _hasBeenPowerd ; 
	
	public Edge(Vertex _v, double _weight) {
		super();
		this._v = _v;
		this._weight = _weight;
		_hasBeenPowerd = false;
	}

	public void powerWeight() {
		this._weight = _weight*_weight;
	}

	public boolean is_hasBeenPowerd() {
		return _hasBeenPowerd;
	}

	public void set_hasBeenPowerd(boolean _hasBeenPowerd) {
		this._hasBeenPowerd = _hasBeenPowerd;
	}

	public String getName() {
		return _v.getName()+"("+_weight+")";
	}

	

}
