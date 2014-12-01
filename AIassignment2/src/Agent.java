
public interface Agent {
	public Action getAction(Graph g);
	public void addCost(double cost);
	public double getCost();
	public void printAgent();
	public void printAgentScore();

}
