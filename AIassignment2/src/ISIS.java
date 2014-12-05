import java.util.List;


public abstract class ISIS extends Agent {
	
	private int _foodCarried;
	private List<Vertex> _path;
	private Double _cost = 0.0;
	
	public ISIS(Vertex _location, int _foodCarried, int id) {
		super(id,_location);
		this._foodCarried = _foodCarried;
	}
	
	@Override
	public abstract Action getAction(Graph g);

	protected Vertex findGoal(Graph g) {
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

	

	protected Edge findEdge(Vertex target) {
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
		_cost+=cost;
		
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
