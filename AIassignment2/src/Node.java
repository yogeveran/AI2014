import java.util.Collection;
import java.util.Vector;


public class Node {
	private Node _parent;
	private Vertex _state;
	private double _debth;
	private Vector<Node> _children;
	private double _currentSupplies;
	
	public Node(Node _parent, Vertex _state,double supplies, double _debth) {
		super();
		this._parent = _parent;
		this._state = _state;
		this._debth = _debth;
		this._children = new Vector<Node>();
		this._currentSupplies = supplies;
	}
	

	public double get_currentSupplies() {
		return _currentSupplies;
	}


	public Node get_parent() {
		return _parent;
	}

	public Vertex get_state() {
		return _state;
	}

	public double get_debth() {
		return _debth;
	}

	public Collection<? extends Node> expand() {
		Vector<Node> nodes = new Vector<Node>();
		//System.out.println("Expanding Node:"+ this +"- v"+_state.get_num()+" supplies:"+_currentSupplies+" Depth:"+_debth);
		for(Edge e: _state.view_neighbors()){
			double newDebth =  ((Math.sqrt(e.get_weight()) > _currentSupplies) ? Double.POSITIVE_INFINITY : (_debth+_currentSupplies*Math.sqrt(e.get_weight())));
			double nextSupplies = _currentSupplies-Math.sqrt(e.get_weight()) + e.get_target().view_supplies();
			Node n = new Node(this, e.get_target(), nextSupplies, newDebth);
			//System.out.println("Create Node:"+ n +"- vertex-"+e.get_target().get_num()+" supplies:"+nextSupplies+" newDepth:"+newDebth);
				nodes.add(n);
				_children.add(n);
		}
		//Add noOp Node

		Node n1 = new Node(this, _state, _currentSupplies-1, (( _currentSupplies>0) ? (_debth+1) : Double.POSITIVE_INFINITY));
		//System.out.println("Create NoOp Node:"+ n1 +"- vertex-"+_state.get_num()+" supplies:"+(_currentSupplies-1)+" newDepth:"+(( _currentSupplies>0) ? (_debth+1) : Double.POSITIVE_INFINITY));
		//System.out.println();
		nodes.add(n1);
		_children.add(n1);

		
		return nodes;
	}


	public void removeChild(Node child) {
		_children.remove(child);
		
	}


	public void addChild(Node child) {
		_children.add(child);
		
	}


	public void set_parent(Node node) {
		this._parent = node;
	}


	public Vector<Node> get_children() {
		// TODO Auto-generated method stub
		return _children;
	}


	public void add_depth(double d) {
		_debth+=d;
		
	}


	

}
