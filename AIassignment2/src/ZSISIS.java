import java.util.Vector;


public class ZSISIS extends ISIS {

	public ZSISIS(Vertex _location, int _foodCarried, int id) {
		super(_location, _foodCarried, id);
		
	}

	public ZSISIS(ZSISIS a, Vector<Vertex> _vertices) {
		super(_vertices.get((int) (a._location.get_num()-1)),a.get_foodCarried(), a._id);
		_vertices.get((int) (a._location.get_num()-1)).addIsis(this);
		super.setCost(a.getCost());
	}

	@Override
	public Action getAction(Graph g) {
		int depth = g._horizon;
		Tuple<Double, Double> alpha = new Tuple<Double, Double>(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		Tuple<Double, Double> beta = new Tuple<Double, Double>(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
		Action chosen = new Action(ActionType.NoOp, null);
		
		for(ActionGraph ag: g.getActions(getAgent(_id, g))){//TODO CHECK
    	  	Graph g2 = ag.getG();
    	  	Action child = ag.getAct();
    	  	Tuple<Double, Double> newBeta = alphabeta(g2.apply(child,getAgent(_id, g2)), depth - 1, alpha, beta, true);
    	  	if(newBeta.val1<beta.val1){
    	  		chosen=child;
    	  		beta = newBeta;
    	  		}
      		
      		if (beta.val1 <= alpha.val1)
              break;// (* beta cut-off *)
      	}
		
		return Convert(chosen,g);
	}

}
