
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
	public abstract void printAgent();
	public abstract void printAgentScore();
	

}
