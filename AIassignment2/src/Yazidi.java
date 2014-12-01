import java.util.List;


public class Yazidi implements Agent {

	private int _id;

	public Yazidi(Vertex _location, Vertex _goal, int _foodCarried,int id) {
		super();
		this._location = _location;
		this._goal = _goal;
		this._foodCarried = _foodCarried;
		this._path = null;
		this._id = id;
	}
	protected Vertex _location;
	protected Vertex _goal;
	protected int _foodCarried;
	protected List<Vertex> _path;
	protected double _cost;
	
	@Override
	public Action getAction(Graph g) {
		if(this._path == null){
			Dijkstra.computePaths(this._location,g,false);
			this._path = Dijkstra.getShortestPathTo(_goal);
			_path.remove(0);
			
			if(_path!=null){
				System.out.print("Path of "+this+":");
				for(Vertex v: _path)
					System.out.print(v.getName()+", ");
				System.out.println();
			}else{
				System.out.println(this + " found no solution.");
			}
			}
		if(noRouteOrAtGoal()||notEnoughFood())
			return new Action(ActionType.NoOp, null);
		return new Action(ActionType.Traverse, _path.remove(0));
	}

	public Vertex get_location() {
		return _location;
	}

	public void set_location(Vertex _location) {
		this._location = _location;
	}

	public int get_foodCarried() {
		return _foodCarried;
	}

	public void set_foodCarried(int _foodCarried) {
		this._foodCarried = _foodCarried;
	}

	public Vertex get_goal() {
		return _goal;
	}

	protected boolean notEnoughFood() {
		if(findEdge(_path.get(0))== null)
			return _foodCarried>0 ? false : true;
		System.out.println(findEdge(_path.get(0)).get_weight());
		System.out.println(this._foodCarried);
		return findEdge(_path.get(0)).get_weight()>this._foodCarried;
	}

	public Edge findEdge(Vertex target) {
		for(Edge e: this._location.view_neighbors()){
			if(target == e.get_target()){
				return e;
			}		}
		return null;
	}

	protected boolean noRouteOrAtGoal() {
		
		return _path.size()==0;
	}

	@Override
	public void addCost(double cost) {
		this._cost+=cost;
		
	}

	@Override
	public double getCost() {
		return this._cost;
	}

	public void setCost(double cost) {
		_cost = cost;
		
	}

	@Override
	public void printAgent() {
		String status = (_location==_goal) ? "win" : ((_cost == Double.POSITIVE_INFINITY) ? "dead" : "alive");
		System.out.println("Y"+_id+":score("+_cost+"):food("+_foodCarried+"):loc("+_location+"):status(" +status+ ")" );
	}

	public void addFood(int i) {
		_foodCarried+=i;
	}

	@Override
	public void printAgentScore() {
		printAgent();
		
	}

}