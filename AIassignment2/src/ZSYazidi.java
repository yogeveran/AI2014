import java.util.Vector;


public class ZSYazidi extends Yazidi {

	public ZSYazidi(Vertex _location, Vertex _goal, int _foodCarried, int id) {
		super(_location, _goal, _foodCarried, id);
	}

	public ZSYazidi(ZSYazidi a, Vector<Vertex> _vertices) {
		super(_vertices.get((int) (a._location.get_num()-1)), _vertices.get((int) (a._goal.get_num()-1)), a._foodCarried, a._id);
		_vertices.get((int) (a._location.get_num()-1)).addYazidi(this);
	}

	@Override
	public Action getAction(Graph g) {
		int depth = 10;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		Action chosen = new Action(ActionType.NoOp, null);
		
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
	
}
