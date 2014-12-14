import java.util.Vector;


public abstract class Agent {
	public int _id;
	protected Vertex _location;
	protected int _foodCarried;
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
	public Tuple<Double, Double> alphabeta(Graph g, double depth, Tuple<Double, Double> alpha,
			Tuple<Double, Double> beta, boolean yazidi) {
			  if (cutoffTest(depth,g))
			     return Eval(g);
			  if (yazidi){
			      for( ActionGraph ag: g.getActions(getAgent(0, g))){//TODO CHECK
			    	  	Graph g2 = ag.getG();
			    	  	Action child = ag.getAct();
			    	  	Tuple<Double, Double> alphabeta = alphabeta(g2.apply(child, getAgent(0, g2)), depth - 1, alpha, beta, false);
						alpha = Tuple.maxLeft(alpha, alphabeta);
			      		if (beta.val1 <= alpha.val1)
			              break;// (* beta cut-off *)
			      	}
			      return alpha;
			  }
			  else{//ISIS
				  for( ActionGraph ag: g.getActions(getAgent(1, g))){
					  	Graph g2 = ag.getG();
			    	  	Action child = ag.getAct();
			    	  	Tuple<Double, Double> alphabeta = alphabeta(g2.apply(child, getAgent(1, g2)), depth - 1, alpha, beta, true);
			    	  	beta = Tuple.minLeft(beta, alphabeta);
			          if (beta.val1 <= alpha.val1)
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
	protected boolean cutoffTest(double depth, Graph g) {
		return (depth <= 0) || is_a_terminal_node(g);
	}
	protected Tuple<Double,Double> Eval(Graph g) {
		switch(g.gt){
		case ZeroSum:
			if(is_a_terminal_node(g)){
				Agent agent = getAgent(0, g);
				if(isDead(g, agent))
					return new Tuple<Double, Double>(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);//TODO FIX
				return new Tuple<Double, Double>(agent.getCost(),-agent.getCost());
				}
			else 
				return h(g);
		default:	
				return h(g);//TODO
		}
	}
	protected boolean isDead(Graph g, Agent agent) {
		return (((Yazidi)agent).get_foodCarried()<0)||sameSpot(g);
	}
	
	protected Tuple<Double,Double> h(Graph g) {
		switch(g.gt){
		case ZeroSum:
			Graph g2 = Graph.makePowerGraph(g);
			Dijkstra.computePaths(g2.getAgentLocation(0), g2, false);
			Vertex v = ((Yazidi)g2.get_agents().get(0)).get_goal();
			Double score = getAgent(0,g2).getCost() - v.minDistance;
			return new Tuple<Double, Double>(score,-score); 
		default:
			Graph g21 = Graph.makePowerGraph(g);
			Dijkstra.computePaths(g21.getAgentLocation(_id), g21, false);
			Vertex v1 = ((Yazidi)g21.get_agents().get(_id)).get_goal();
			Double score1 = getAgent(_id,g21).getCost() - v1.minDistance;//Add
			
			Graph g211 = Graph.makePowerGraph(g);
			Dijkstra.computePaths(g211.getAgentLocation(1-_id), g211, false);
			Vertex v11 = ((Yazidi)g211.get_agents().get(1-_id)).get_goal();
			Double score11 = getAgent(1-_id,g211).getCost() - v11.minDistance;//Add
			return new Tuple<Double, Double>(score1,score11); 
		}
		

	}
	protected boolean is_a_terminal_node(Graph g) {
		return g.shouldStop(g);
		
	}
	protected boolean sameSpot(Graph g) {
		return getLoc(this._id,g)==getLoc(1 - this._id,g);
	}
	protected Vertex getLoc(int _id, Graph g) {
		Agent a = getAgent(_id, g);
		if(a!=null)
			return a._location;
		return null;
	}
	public abstract void Traverse(Graph graph, Vertex get_target);
	

}
