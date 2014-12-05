import java.util.Vector;


public abstract class Agent {
	public int _id;
	protected Vertex _location;
	public Agent(int id, Vertex location){
		this._id= id;
		this._location = location;
	}
	public abstract Action getAction(Graph g);
	public abstract void addCost(double cost);
	public abstract double getCost();
	public abstract void setCost(double cost);
	public abstract void printAgent();
	public abstract void printAgentScore();
	protected Action Convert(Action chosen, Graph g) {
		 
		Vertex target=null;
		if(chosen.get_type()!=ActionType.NoOp)
			target = g.get_vertices().get((int) (chosen.get_target().get_num()-1));
		return new Action(chosen.get_type(), target);
	}
	public double alphabeta(Graph g, double depth, double alpha,
			double beta, boolean yazidi) {
			  if (cutoffTest(depth,g))
			     return Eval(g);
			  if (yazidi){
			      for( ActionGraph ag: g.getActions(getAgent(_id, g))){//TODO CHECK
			    	  	Graph g2 = ag.getG();
			    	  	Action child = ag.getAct();
			      		double alphabeta = alphabeta(g2.apply(child, getAgent(_id, g2)), depth - 1, alpha, beta, false);
						alpha = Double.max(alpha, alphabeta);
			      		if (beta <= alpha)
			              break;// (* beta cut-off *)
			      	}
			      return alpha;
			  }
			  else{//ISIS
				  for( ActionGraph ag: g.getActions(getAgent(1-_id, g))){//TODO CHECK
					  	Graph g2 = ag.getG();
			    	  	Action child = ag.getAct();
			      	  double alphabeta = alphabeta(g2.apply(child, getAgent(1- _id, g2)), depth - 1, alpha, beta, true);
					beta = Double.min(beta, alphabeta);
			          if (beta <= alpha)
			              break;// (* alpha cut-off *)
			      	}
			      return beta;
			    }
			  }
	protected Agent getAgent(int i, Graph g) {
		Vector<Agent> agents = g.get_agents();
		for(Agent a: agents)
			if(a._id == i)
				return a;
		return null;
	}
	private boolean cutoffTest(double depth, Graph g) {
		return (depth <= 0) || is_a_terminal_node(g);
	}
	private double Eval(Graph g) {
		if(is_a_terminal_node(g)){
			Agent agent = getAgent(0, g);
			if((((Yazidi)agent).get_foodCarried()<0)||sameSpot(g))
				return Double.NEGATIVE_INFINITY;
			return agent.getCost();
			}
		else 
			return h(g);
	}
	private double h(Graph g) {
		Graph g2 = Graph.makePowerGraph(g);
		Dijkstra.computePaths(g2.getAgentLocation(0), g2, false);
		Vertex v = ((Yazidi)g2.get_agents().get(0)).get_goal();
		return getAgent(0,g2).getCost() - v.minDistance;//Add 
	}
	private boolean is_a_terminal_node(Graph g) {
		Yazidi yazidi = (Yazidi)getAgent(0, g);
		if(yazidi._foodCarried<0)
			return true;
		if(yazidi.get_goal().get_num()==yazidi._location.get_num())
			return true;
		if(sameSpot(g))
			return true;
		return false;
		
	}
	private boolean sameSpot(Graph g) {
		return getLoc(this._id,g)==getLoc(1 - this._id,g);
	}
	private Vertex getLoc(int _id, Graph g) {
		Agent a = getAgent(_id, g);
		if(a!=null)
			return a._location;
		return null;
	}
	public abstract void Traverse(Graph graph, Vertex get_target);
	

}
