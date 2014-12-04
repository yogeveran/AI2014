import java.util.Vector;


public class ZSYazidi extends Yazidi {

	public ZSYazidi(Vertex _location, Vertex _goal, int _foodCarried, int id) {
		super(_location, _goal, _foodCarried, id);
	}

	public ZSYazidi(ZSYazidi a, Vector<Vertex> _vertices) {
		super(_vertices.get((int) (a._location.get_num()-1)), _vertices.get((int) (a._goal.get_num()-1)), a._foodCarried, a._id);
	}

	@Override
	public Action getAction(Graph g) {
		int depth = 10;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		Action chosen = null;
		
		for(ActionGraph ag: g.getActions(getAgent(_id, g))){//TODO CHECK
			Graph g2 = ag.getG();
    	  	Action child = ag.getAct();
    	  	double newAlpha = alphabeta(g2.apply(child,getAgent(_id, g2)), depth - 1, alpha, beta, false);
    	  	if(newAlpha>alpha)
    	  		chosen=child;
      		alpha = Double.max(alpha, newAlpha);
      		if (beta <= alpha)
              break;// (* beta cut-off *)
      	}
		
		return Convert(chosen,g);
	}

	 private Action Convert(Action chosen, Graph g) {
		 
		Vertex target=null;
		if(chosen.get_type()!=ActionType.NoOp)
			target = g.get_vertices().get((int) (chosen.get_target().get_num()-1));
		return new Action(chosen.get_type(), target);
	}

	public double alphabeta(Graph g, double depth, double alpha, double beta, boolean yazidi){
	      if (cutoffTest(depth,g))
	         return Eval(g);
	      if (yazidi){
	          for( ActionGraph ag: g.getActions(getAgent(_id, g))){//TODO CHECK
	        	  	Graph g2 = ag.getG();
		    	  	Action child = ag.getAct();
	          		alpha = Double.max(alpha, alphabeta(g2.apply(child, getAgent(_id, g2)), depth - 1, alpha, beta, false));
	          		if (beta <= alpha)
	                  break;// (* beta cut-off *)
	          	}
	          return alpha;
	      }
	      else{//ISIS
	    	  for( ActionGraph ag: g.getActions(getAgent(1-_id, g))){//TODO CHECK
	    		  	Graph g2 = ag.getG();
		    	  	Action child = ag.getAct();
	          	  beta = Double.min(beta, alphabeta(g2.apply(child, getAgent(1- _id, g2)), depth - 1, alpha, beta, true));
	              if (beta <= alpha)
	                  break;// (* alpha cut-off *)
	          	}
	          return beta;
	        }
	      }

	private Agent getAgent(int i,Graph g) {
		Vector<Agent> agents = g.get_agents();
		for(Agent a: agents)
			if(a._id == _id)
				return a;
		return null;
	}

	private boolean cutoffTest(double depth, Graph g) {
		return (depth == 0) || is_a_terminal_node(g);
	}

	private double Eval(Graph g) {
		if(is_a_terminal_node(g))
			return getAgent(_id, g).getCost();
		else 
			return h(g);
	}

	private double h(Graph g) {
		Graph g2 = Graph.makePowerGraph(g);
		Dijkstra.computePaths(g.getAgentLocation(_id), g2, false);
		Vertex v = ((Yazidi)g2.get_agents().get(_id)).get_goal();
		return getAgent(_id,g2).getCost() - v.minDistance;//Add 
	}

	private boolean is_a_terminal_node(Graph g) {
		Vertex goal = g.get_vertices().get((int) this._goal.get_num());//TODO CHECK if index is good
		if((!goal.view_yazidi().isEmpty()))
				for(Agent y: goal.view_yazidi())
					if(y._id == this._id)
						return true;
		if(getLoc(this._id,g)==getLoc(1 - this._id,g))
			return true;
		return false;
		
	}

	private Vertex getLoc(int _id, Graph g) {
		Agent a = getAgent(_id, g);
		if(a!=null)
			return a._location;
		return null;
	}
	
}
