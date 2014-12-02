import java.util.Vector;


public class ZSYazidi extends Yazidi {

	public ZSYazidi(Vertex _location, Vertex _goal, int _foodCarried, int id) {
		super(_location, _goal, _foodCarried, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action getAction(Graph g) {
		int depth = 10;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		Action chosen = null;
		alphabeta(g, depth, alpha, beta, true);
		for(Action child: g.getActions(1)){//TODO CHECK
    	  	Graph g2 = new Graph(g);
    	  	double newAlpha = alphabeta(g2.apply(child), depth - 1, alpha, beta, false);
    	  	if(newAlpha>alpha)
    	  		chosen=child;
      		alpha = Double.max(alpha, newAlpha);
      		if (beta <= alpha)
              break;// (* beta cut-off *)
      	}
		
		return chosen;
	}

	 public double alphabeta(Graph g, double depth, double alpha, double beta, boolean yazidi){
	      if (cutoffTest(depth,g))
	         return Eval(g);
	      if (yazidi){
	          for( Action child: g.getActions(1)){//TODO CHECK
	        	  	Graph g2 = new Graph(g);
	          		alpha = Double.max(alpha, alphabeta(g2.apply(child), depth - 1, alpha, beta, false));
	          		if (beta <= alpha)
	                  break;// (* beta cut-off *)
	          	}
	          return alpha;
	      }
	      else{//ISIS
	    	  for( Action child: g.getActions(2)){//TODO CHECK
	    		  Graph g2 = new Graph(g);
	          	  beta = Double.min(beta, alphabeta(g2.apply(child), depth - 1, alpha, beta, true));
	              if (beta <= alpha)
	                  break;// (* alpha cut-off *)
	          	}
	          return beta;
	        }
	      }

	private boolean cutoffTest(double depth, Graph g) {
		return (depth == 0) || is_a_terminal_node(g);
	}

	private double Eval(Graph g) {
		// TODO Auto-generated method stub
		return 0;
	}

	private boolean is_a_terminal_node(Graph g) {
		Vertex goal = g.get_vertices().get((int) this._goal.get_num());
		if((!goal.view_yazidi().isEmpty())
				for()
	}
	
}
