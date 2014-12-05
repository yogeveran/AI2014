import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Stream;


public class Graph {
	private Vector<Vertex> _vertices;
	private Vector<Agent> _agents;
	int _horizon = Integer.MAX_VALUE;
	
	GameType gt;
	private Graph(){
		_vertices = new Vector<Vertex>();
		_agents = new Vector<Agent>();
		_horizon = Integer.MAX_VALUE;
		gt = GameType.ZeroSum;
	}
	public Graph(Vertex[] vertices){
		_vertices = new Vector<Vertex>(Arrays.asList(vertices));
		_agents = new Vector<Agent>();
	}
	Graph(Graph g){
		_vertices = new Vector<Vertex>(); 
		_agents = new Vector<Agent>();
		_horizon = g._horizon;
		gt=g.gt;
		//Copy Vertices
		for(Vertex v: g.get_vertices()){
			Vertex newV = new Vertex(v.get_num(),v.view_supplies());
			_vertices.add(newV);
		}
		//Copy agents
		for(Agent a: g._agents){
			if(a instanceof ZSYazidi){
				_agents.add(new ZSYazidi((ZSYazidi)a,_vertices));
			}
			if(a instanceof ZSISIS){
				_agents.add(new ZSISIS((ZSISIS)a,_vertices));
			}
		}
		//Copy Edges
		for(Vertex v: g.get_vertices()){
			for(Edge e: v.view_neighbors()){
				Edge newE = new Edge(_vertices.get((int) (e.get_target().get_num()-1)), e.get_weight());
				_vertices.get((int) (v.get_num()-1)).addEdge(newE);

			}
		}


	}
	public static  Graph makePowerGraph(Graph g){
		Graph powerGraph =  new Graph(g);
		for(Vertex a: powerGraph.get_vertices()){
			for (Edge e : a.view_neighbors()){
				if(!e.is_hasBeenPowerd()){
					e.set_hasBeenPowerd(true);
					e.powerWeight();
				}
			}
		}
		return powerGraph;
	}
	public static void main(String[] args) {
		Graph g = initGraph();

		//addAgents(g);

		g.runSimulation(g);


	}
	
	private static Graph initGraph() {
		Graph g = new Graph();
		g.buildGraph("graphs/graph.txt");
		g.printGraph();
		return g;
	}

	private  void runSimulation(Graph g) {
		System.out.println("Running Simulation:");
		while(!shouldStop(g)){
			System.out.println("-------------------------------------------------");
			for(Agent a: g.get_agents()){
				System.out.println("Agent is working now: " + a + " At Loc:"+a._location.get_num());
				if(a.getCost()!=Double.POSITIVE_INFINITY){
					//g.printWorld();
					Action act = a.getAction(g);
					switch(act.get_type()){
					case NoOp:
						System.out.println("Chose NoOp");
						caseNoOp(a);
						break;
					case Traverse:
						System.out.println("Chose To Traverse to: v"+act.get_target().get_num());
						caseTraverse(a, act);
						break;
					}
				}
				if(GameType.ZeroSum==this.gt)
					_agents.get(1).setCost(_agents.get(0).getCost());
			}
			//g.printWorld();
			//g.printScores();
		}

	}
	private void printScores() {
		for(Agent a: _agents)
			a.printAgentScore();
		
	}
	private void printWorld() {
		System.out.println("Vertices:");
		for(Vertex v: _vertices)
			v.printVertex();
		System.out.println();
		System.out.println("Agents:");
		for(Agent a: _agents)
			a.printAgent();

	}
	private static boolean shouldStop(Graph g) {
/*		int yazidi = 0;
		int deadOrGoal = 0;
		for(Agent a: g.get_agents())
			if(a instanceof Yazidi ){
				yazidi++;
				if((((Yazidi) a).get_location() == ((Yazidi) a).get_goal())||a.getCost() == Double.POSITIVE_INFINITY)
					deadOrGoal++;
			}
			else if (a instanceof Human){
				yazidi++;
				if((((Human) a).get_location() == ((Human) a).get_goal())||a.getCost() == Double.POSITIVE_INFINITY)
					deadOrGoal++;
			}
		return (yazidi == deadOrGoal);*/
		Yazidi yazidi = (Yazidi) g.get_agents().get(0);
		boolean sameLoc = (yazidi._location==g.get_agents().get(1)._location);
		boolean noFood = ((Yazidi)yazidi)._foodCarried<0;
		boolean atGoal = yazidi._location==yazidi.get_goal();

		return sameLoc||noFood||atGoal;
	}
	private static void caseNoOp(Agent a) {
		if(a instanceof Yazidi) {
			Yazidi yaz = (Yazidi)a;
			removeFood(yaz);
		}
		if(a instanceof Human) {
			Human hum = (Human)a;
			removeFood(hum);
		}
		a.addCost(-1);
	}
	private static void caseTraverse(Agent a, Action act) {
		if (a instanceof Yazidi) {
			Yazidi new_a = (Yazidi) a;
			Traverse(new_a,act.get_target());
		}else if (a instanceof ISIS) {
			ISIS new_a = (ISIS) a;
			Traverse(new_a,act.get_target());
		}
		else if(a instanceof Human){
			Human new_a = (Human) a;
			Traverse(new_a,act.get_target());
		}
	}


	private static void Traverse(Human a, Vertex vertex) {
		if(!vertex.view_isis().isEmpty()||(a.findEdge(vertex).get_weight()>a.get_foodCarried())){
			removeFood(a);
			a.addCost(-1);
		}else{
			double cost = a.findEdge(vertex).get_weight()*a.get_foodCarried();
			int edgeWeight = (int) a.findEdge(vertex).get_weight();
			a.get_location().view_human().remove(a);
			a.set_location(vertex);
			vertex.view_human().add(a);
			a.addCost(-cost);
			a.set_foodCarried(a.get_foodCarried()+vertex.takeSupplies()-edgeWeight);
		}

	}
	private static void removeFood(Human a) {
		a.set_foodCarried(a.get_foodCarried()-1);
		if(a.get_foodCarried()<0){
			a.setCost(Double.NEGATIVE_INFINITY);
			a.get_location().removeHuman(a);
		}
	}
	private static void Traverse(Yazidi a, Vertex vertex) {
		int edgeWeight = (int) a.findEdge(vertex).get_weight();
		int foodCarried = a.get_foodCarried();
		double cost = edgeWeight*foodCarried;
		if(!vertex.view_isis().isEmpty()||(edgeWeight>foodCarried)){
			removeFood(a);
			a.addCost(-1);
		}else{
			a.get_location().view_yazidi().remove(a);
			a.set_location(vertex);
			vertex.view_yazidi().add(a);
			a.addCost(-cost);
			a.set_foodCarried(foodCarried+vertex.takeSupplies()-edgeWeight);
		}

	}
	private static void Traverse(ISIS a, Vertex vertex) {
		a.get_location().view_isis().remove(a);
		a.set_location(vertex);
		vertex.view_isis().add(a);
		for(Yazidi y: vertex.view_yazidi())
			y.setCost(Double.POSITIVE_INFINITY);
		for(Human y: vertex.view_human())
			y.setCost(Double.POSITIVE_INFINITY);
		vertex.removeYazidi();
		vertex.removeHuman();
		a.set_foodCarried(a.get_foodCarried()+vertex.takeSupplies());
	}

	private static void removeFood(Yazidi a) {
		a.set_foodCarried(a.get_foodCarried()-1);
		if(a.get_foodCarried()<0){
			a.setCost(Double.NEGATIVE_INFINITY);
			a.get_location().removeYazidi(a);
		}
	}


	private void addAgent(String[] s) {
		int agentStartVertex = 0;
		int agentGoalVertex=0;

		int agentType = getAgentType(s);
		switch(agentType){
		case 1://Human
			agentStartVertex = getAgentStartVertex(this,s);
			agentGoalVertex = getAgentGoalVertex(this,s);
			this.addHuman(agentStartVertex,agentGoalVertex,_agents.size());
			break;
		case 2://Yazidi
				agentStartVertex = getAgentStartVertex(this,s);
				agentGoalVertex = getAgentGoalVertex(this,s);
				this.addYazidi(agentStartVertex,agentGoalVertex,_agents.size());
				break;
		case 3: //Isis
			if(gt != GameType.ZeroSum){
				System.out.println("Wrong type of game!");
				System.exit(-1);			}
				
			agentStartVertex = getAgentStartVertex(this,s);
			this.addIsis(agentStartVertex,_agents.size());
			break;
		default:
			System.exit(-1);
			break;
		}
	}


	public Vector<Vertex> get_vertices() {
		return _vertices;
	}
	public Vector<Agent> get_agents() {
		return _agents;
	}
	public static String getLine(Scanner s) {
		String line = s.nextLine(); 
		return line;
	}

	private static int getAgentGoalVertex(Graph g,String[] s) {
				if(g.getVertices().size()==0){
					System.err.println("Why did you not add vertices?");
					System.exit(-1);
					}
				return Integer.decode(s[3]);
}

	private static int getAgentStartVertex(Graph g,String[] s) {
		if(g.getVertices().size()==0){
			System.err.println("Why did you not add vertices?");
			System.exit(-1);
			}
		return Integer.decode(s[2]);
	}

	private static int getAgentType(String[] s) {
				int type = Integer.decode(s[1]);
				if(0<type && type<4)
					return type;
				return -1;
	}

	private void buildGraph(String inputFile){
		try (Stream<String> stream = Files.lines(Paths.get(inputFile),Charset.defaultCharset())) {
			stream.forEach(this::parseln);
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}

	public Vector<Vertex> getVertices(){
		return _vertices;
	}

	public void printGraph(){
		for(Vertex v:_vertices)
			v.printNeighbors();
	}

	private void addEdge(int firstVertex, int secondVertex,int weight) {
		getVertex(firstVertex).addEdge(getVertex(secondVertex),weight);
		getVertex(secondVertex).addEdge(getVertex(firstVertex),weight);	
	}

	private void addHuman(int agentStartVertex, int agentGoalVertex, int id) {

		Human toAdd = new Human(_vertices.get(agentStartVertex-1),_vertices.get(agentGoalVertex-1),_vertices.get(agentStartVertex-1).takeSupplies(),id);
		this._agents.add(toAdd);
		_vertices.get(agentStartVertex-1).addHuman(toAdd);

	}

	private void addIsis(int agentStartVertex, int agentsAmount) {
		ISIS toAdd = new ZSISIS(_vertices.get(agentStartVertex-1),_vertices.get(agentStartVertex-1).takeSupplies(),agentsAmount);
		this._agents.add(toAdd);
		_vertices.get(agentStartVertex-1).addIsis(toAdd);

	}


	private void addYazidi(int agentStartVertex, int agentGoalVertex,int id) {
		Yazidi yazidi = null;
		switch(gt){
		case ZeroSum:
			yazidi = new ZSYazidi(_vertices.get(agentStartVertex-1),_vertices.get(agentGoalVertex-1),_vertices.get(agentStartVertex-1).takeSupplies(), id);
			break;
		case nonZeroSum:
			yazidi = new NZSYazidi(_vertices.get(agentStartVertex-1),_vertices.get(agentGoalVertex-1),_vertices.get(agentStartVertex-1).takeSupplies(), id);
			break;
		case FullyCooperative:
			yazidi = new FCYazidi(_vertices.get(agentStartVertex-1),_vertices.get(agentGoalVertex-1),_vertices.get(agentStartVertex-1).takeSupplies(), id);
			break;
		}
		this._agents.add(yazidi);
		_vertices.get(agentStartVertex-1).addYazidi(yazidi);
	}

	private String getEdgeWeight(String[] edge) {
		return edge[3];
	}

	private int getFirstVertex(String[] edge) {
		return Integer.decode(edge[1])-1;
	}

	private int getSecondVertex(String[] edge) {
		return Integer.decode(edge[2])-1;
	}

	private Vertex getVertex(int v) {
		return _vertices.get(v);
	}

	private int getVertex(String line[]) {

		return Integer.decode(String.valueOf(line[1]));
	}

	private boolean isEdge(String line) {
		return line.contains("#E");
	}

	private boolean isMissingVertex(String[] edge) {
		return getFirstVertex(edge)>_vertices.size() || getSecondVertex(edge)>_vertices.size();
	}

	private boolean isNegativeWeight(String[] edge) {
		return Integer.decode(getEdgeWeight(edge))<0;
	}

	private boolean isVertex(String line) {
		return line.contains("#V");
	}

	private void parseln(String line){
		if(isGameType(line)){
			String gtype[] = line.split(" ");
			gt = GameType.valueOf(gtype[1]);
		}else if(isHorizon(line)){
			String hor[] = line.split(" ");
			_horizon = Integer.decode(hor[1]);
		}else if(isVertex(line)){
			String vert[] = line.split(" ");
			_vertices =  new Vector<Vertex>(getVertex(vert));
			for(int i=1;i<=getVertex(vert);++i)
				_vertices.add(new Vertex(i,Integer.decode(vert[i+1])));
		}
		else if(isEdge(line)){
			String edge[] = line.split(" ");
			if(isMissingVertex(edge)){
				System.out.println("Missing vertex");
				return;

			}
			if(isNegativeWeight(edge)){
				System.out.println("Weight must be non negative");
				return;
			}
			//Edge is fine
			addEdge(getFirstVertex(edge),getSecondVertex(edge),Integer.decode(getEdgeWeight(edge)));
		}else if(isAgent(line)){
			addAgent(line.split(" "));
		}
	}



	private boolean isGameType(String line) {
		return line.contains("#G");
	}
	private boolean isHorizon(String line) {
		return line.contains("#H");
	}
	private boolean isAgent(String line) {
		return line.contains("#A");
	}
	public Vertex getAgentLocation(int i) {
		for(Vertex v: _vertices)
			if(!v.view_yazidi().isEmpty())
				for(Agent y: v.view_yazidi())
					if(y._id==i)
						return v;
		return null;
	}
	public Vector<ActionGraph> getActions(Agent agent) {
		Vector<ActionGraph> res = new Vector<ActionGraph>();
		
		for(Edge e: agent._location.view_neighbors()){
			Graph g2 = new Graph(this);
			Vertex dest = g2._vertices.get((int) (e.get_target().get_num())-1);
			
			Action act = new Action(ActionType.Traverse, dest);
			res.add(new ActionGraph(g2, act));
		}
		res.add(new ActionGraph(new Graph(this), new Action(ActionType.NoOp, null)));
//		for(ActionGraph ag: res)
//			System.out.print(ag.getAct()+",  ");
//		System.out.println();
		return res;
	}
	public Graph apply(Action act,Agent a) {
		switch(act.get_type()){
		case NoOp:
			caseNoOp(a);
			break;
		case Traverse:
			caseTraverse(a, act);
			break;
		}
		if(GameType.ZeroSum==this.gt)
			_agents.get(1).setCost(-_agents.get(0).getCost());
		return this;
	}



}
