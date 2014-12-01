import java.util.List;


public class ISIS implements Agent {
	private Vertex _location;
	private int _foodCarried;
	private List<Vertex> _path;
	private Double _cost = 0.0;
	private int _id;
	
	public ISIS(Vertex _location, int _foodCarried, int id) {
		super();
		this._location = _location;
		this._foodCarried = _foodCarried;
		this._id = id;
	}
	
	@Override
	public Action getAction(Graph g) {
		Dijkstra.computePaths(this._location,g,true);
		this._path = Dijkstra.getShortestPathTo(findGoal(g));
		if(!_path.isEmpty())
			_path.remove(0);
		if(noRouteOrAtGoal()){
			System.out.println(this + "chose noOP");
			return new Action(ActionType.NoOp, null);
		}
		System.out.println(this + " chose Move to "+_path.get(0));
		return new Action(ActionType.Traverse, _path.remove(0));
	}

	private Vertex findGoal(Graph g) {
		Vertex minDistanceV = null;
		for (Vertex v: g.getVertices())
			if((!v.view_yazidi().isEmpty())||(!v.view_human().isEmpty())){
				if(minDistanceV == null)
					minDistanceV = v;
				else
					if(v.minDistance<minDistanceV.minDistance)
						minDistanceV = v;
			}
		return minDistanceV;
				
	}

	

	private Edge findEdge(Vertex target) {
		for(Edge e: this._location.view_neighbors()){
			if(target == e.get_target()){
				return e;
			}		}
		return null;
	}
	private boolean noRouteOrAtGoal() {
		return _path.size()==0;
	}

	@Override
	public void addCost(double cost) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getCost() {
		// TODO Auto-generated method stub
		return _cost;
	}

	public Vertex get_location() {
		return _location;
	}
	public void set_location(Vertex v) {
		this._location = v;
	}	
	
	public void setCost(double cost) {
		_cost = cost;
		
	}

	public Integer get_foodCarried() {
		// TODO Auto-generated method stub
		return _foodCarried;
	}

	public void set_foodCarried(int i) {
		this._foodCarried = i;
		
	}

	@Override
	public void printAgent() {
		String status =  ((_cost == Double.POSITIVE_INFINITY) ? "dead" : "alive");
		System.out.println();
		System.out.println("I"+_id+":score("+_cost+"):loc("+_location+"):status(" +status+ ")" );
		
	}

	@Override
	public void printAgentScore() {
		// TODO Auto-generated method stub
		printAgent();
	}
	}
