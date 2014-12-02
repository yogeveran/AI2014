
public class ActionGraph {
	private Graph g;
	private Action act;
	public Action getAct() {
		return act;
	}
	public void setAct(Action act) {
		this.act = act;
	}
	public Graph getG() {
		return g;
	}
	public void setG(Graph g) {
		this.g = g;
	}
	public ActionGraph(Graph g, Action act) {
		super();
		this.g = g;
		this.act = act;
	}

}
