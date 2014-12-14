import java.util.List;


public abstract class Yazidi extends Agent {

	

	public Yazidi(Vertex _location, Vertex _goal, int _foodCarried,int id) {
		super(id,_location);
		this._goal = _goal;
		this._foodCarried = _foodCarried;
		this._path = null;
	}
	protected Vertex _goal;
	protected double _cost;
	protected List<Vertex> _path;
	
	
	@Override
	public abstract Action getAction(Graph g);

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
	
	@Override
	public void Traverse(Graph graph, Vertex get_target){
		graph.Traverse(this, get_target);
	}
	
	protected Tuple<Double, Double> maximax(Graph g, int depth,int nextId) {
		Tuple<Double,Double> best = new Tuple<Double,Double>(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
		if (cutoffTest(depth,g))
			return Eval(g);
		Yazidi a = (Yazidi) getAgent(nextId, g);
		if((a._foodCarried<0)||(a.get_goal()==a._location))
			return maximax(g, depth-1, 1 - nextId);
		for(ActionGraph ag: g.getActions(getAgent(nextId, g))){//TODO CHECK
			Graph g2 = ag.getG();
			Action child = ag.getAct();
			Tuple<Double,Double> score  = maximax(g2.apply(child,getAgent(nextId, g2)), depth - 1, 1 - nextId);
			if(worseThan(best, score,nextId,g)){
				best = score;
			}
		}
		return best;
	}


	protected boolean worseThan(Tuple<Double, Double> best,Tuple<Double, Double> score,int currId,Graph g) {
		switch(g.gt){
		case FullyCooperative:
			if((best.val1+best.val2)<(score.val1+score.val2))
				return true;
			if((best.val1+best.val2)>(score.val1+score.val2))
				return false;
			if((!best.val1.equals(Double.NEGATIVE_INFINITY))&&(!best.val2.equals(Double.NEGATIVE_INFINITY))&&(!score.val1.equals(Double.NEGATIVE_INFINITY))&&(!score.val2.equals(Double.NEGATIVE_INFINITY)))
					return false;
			if(best.val1.equals(Double.NEGATIVE_INFINITY))
				if(score.val1.equals(Double.NEGATIVE_INFINITY))
					return best.val2<score.val2;
				else
					return best.val2<score.val1;
			else if(score.val1.equals(Double.NEGATIVE_INFINITY))
						return best.val1<score.val2;
					else
						return best.val1<score.val1;
		case ZeroSum://Dont care
			if(currId==0){
				return (score.val1>best.val1)||((score.val1.equals(best.val1))&&(score.val2>best.val2));
				}
			return (score.val2>best.val2)||((score.val2.equals(best.val2))&&(score.val1>best.val1));
		case nonZeroSum:
			if(currId==0){
				return (score.val1>best.val1)||((score.val1.equals(best.val1))&&(score.val2>best.val2));
				}
			return (score.val2>best.val2)||((score.val2.equals(best.val2))&&(score.val1>best.val1));
		default:
			System.err.println("CWAP");
			return true;

		}
	}

}
