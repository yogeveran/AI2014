import java.util.Vector;


public class FCYazidi extends Yazidi {

	public FCYazidi(Vertex _location, Vertex _goal, int _foodCarried, int id) {
		super(_location, _goal, _foodCarried, id);
		// TODO Auto-generated constructor stub
	}

	public FCYazidi(FCYazidi a, Vector<Vertex> _vertices) {
		super(_vertices.get((int) (a._location.get_num()-1)), _vertices.get((int) (a._goal.get_num()-1)), a._foodCarried, a._id);
		_vertices.get((int) (a._location.get_num()-1)).addYazidi(this);
		super._cost=a._cost;
	}

	@Override
	public Action getAction(Graph g) {
		int depth = g._horizon;
		Tuple<Double,Double> best = new Tuple<Double,Double>(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
		Action chosen = new Action(ActionType.NoOp, null);
		
		for(ActionGraph ag: g.getActions(getAgent(_id, g))){//TODO CHECK
			Graph g2 = ag.getG();
    	  	Action child = ag.getAct();
    	  	Tuple<Double,Double> score  = maximax(g2.apply(child,getAgent(_id, g2)), depth - 1,1-_id);
    	  	if(worseThan(best, score,_id,g)){
    	  		chosen=child;
    	  		best = score;
    	  	} 
    	  	
      	}
		
		return Convert(chosen,g);
	}

}
