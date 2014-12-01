import java.util.Vector;


public class Obama implements Agent {
	private int _turn;
	private double _cost;
	private int _id;
	public Obama(int id) {
		_turn = 0;
		this._id = id;
	}

	@Override
	public Action getAction(Graph g) {
		if(isDropAid()){
			advanceTurn();
			Vertex target = getAidTarget(g);
			return new Action(ActionType.Aid,target);
			}
		else if(isBomb()){
			advanceTurn();
			Vertex target = getBombTarget(g);
			return new Action(ActionType.Bomb,target);
		}
		return new Action(ActionType.NoOp, null);
	}

	private void advanceTurn() {
		_turn++;
		if(_turn == 5)
			_turn = 0;
	}

	private Vertex getAidTarget(Graph g) {
		Vector<Vertex> vertices = g.getVertices();
		if(vertices.isEmpty())
			return null;
		else{
			for(Vertex v:vertices)
				if(v.view_supplies()==0)
					return v;
		}
		return null;
	}

	private Vertex getBombTarget(Graph g) {
		Vector<Vertex> vertices = g.getVertices();
		if(vertices.isEmpty())
			return null;
		else{
			for(Vertex v:vertices)
				if(!v.view_isis().isEmpty()){
//					System.out.println("v:"+ v);
					return v;
					
				}
		}
		return null;
	}

	private boolean isBomb() {
		return _turn == 1;
	}

	private boolean isDropAid() {
		return _turn == 0;
	}

	@Override
	public void addCost(double cost) {
		this._cost+=cost;
		
	}

	@Override
	public double getCost() {
		return this._cost;
	}

	@Override
	public void printAgent() {
		String status =  ((_cost == Double.POSITIVE_INFINITY) ? "dead" : "alive");
		System.out.println("O"+_id+":score("+_cost+")");
		
	}

	@Override
	public void printAgentScore() {
		printAgent();
		
	}

}
