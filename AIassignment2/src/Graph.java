import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
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
	}
	public Graph(Vertex[] vertices){
		_vertices = new Vector<Vertex>(Arrays.asList(vertices));
		_agents = new Vector<Agent>();
	}
	private Graph(Graph g){
		_vertices = new Vector<Vertex>(); 
		_agents = new Vector<Agent>();
		for(Vertex v: g.get_vertices()){
			Vertex newV = new Vertex(v.get_num(),v.view_supplies());
			_vertices.add(newV);
		}

		for(Vertex v: g.get_vertices()){
			for(Edge e: v.view_neighbors()){
				Edge newE = new Edge(_vertices.get((int) (e.get_target().get_num()-1)), e.get_weight());
				_vertices.get((int) (v.get_num()-1)).addEdge(newE);

			}
		}


	}

	public static void main(String[] args) {
		Graph g = initGraph();

		//addAgents(g);

		runSimulation(g);


	}
	
	private static Graph initGraph() {
		Graph g = new Graph();
		g.buildGraph("graphass3.txt");
		g.printGraph();
		return g;
	}

	private static void runSimulation(Graph g) {
		System.out.println("Running Simulation:");
		while(!shouldStop(g)){
			System.out.println("-------------------------------------------------");
			for(Agent a: g.get_agents()){
				System.out.println("Which agent is working now::::::::::::::::::::::::: " + a);
				if(a.getCost()!=Double.POSITIVE_INFINITY){
					g.printWorld();
					Action act = a.getAction(g);
					switch(act.get_type()){
					case NoOp:
						caseNoOp(a);
						break;
					case Traverse:
						caseTraverse(a, act);
						break;
					case Aid:
						if(act.get_target().view_isis().isEmpty()&&act.get_target().view_yazidi().isEmpty()&&act.get_target().view_human().isEmpty()){
							System.out.println("Add food to vertex "+act.get_target().getName());	
							act.get_target().addSupplies(10);
						}
						else if(act.get_target().view_isis().isEmpty()){
							if(act.get_target().view_human().isEmpty()){	
								act.get_target().view_yazidi().get(0).addFood(10);
								System.out.println("Add food to yazidi");
							}else{
								act.get_target().view_human().get(0).addFood(10);
								System.out.println("Add food to human");
							}
						}
						a.addCost(10);
					case Bomb:
						for(ISIS i: act.get_target().view_isis())
							i.setCost(Double.POSITIVE_INFINITY);
						act.get_target().RemoveIsis();
						break;
					}
				}
			}
			g.printWorld();
			g.printScores();
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
		int yazidi = 0;
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
		return (yazidi == deadOrGoal);
	}
	private static void caseNoOp(Agent a) {
		if(a instanceof Yazidi)
			removeFood((Yazidi)a);
		if(a instanceof Human)
			removeFood((Human)a);
		a.addCost(1);
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
			a.addCost(1);
		}else{
			double cost = a.findEdge(vertex).get_weight()*a.get_foodCarried();
			int edgeWeight = (int) a.findEdge(vertex).get_weight();
			a.get_location().view_human().remove(a);
			a.set_location(vertex);
			vertex.view_human().add(a);
			a.addCost(cost);
			a.set_foodCarried(a.get_foodCarried()+vertex.takeSupplies()-edgeWeight);
		}

	}
	private static void removeFood(Human a) {
		a.set_foodCarried(a.get_foodCarried()-1);
		if(a.get_foodCarried()<0){
			a.setCost(Double.POSITIVE_INFINITY);
			a.get_location().removeHuman(a);
		}
	}
	private static void Traverse(Yazidi a, Vertex vertex) {
		if(!vertex.view_isis().isEmpty()||(a.findEdge(vertex).get_weight()>a.get_foodCarried())){
			removeFood(a);
			a.addCost(1);
		}else{
			double cost = a.findEdge(vertex).get_weight()*a.get_foodCarried();
			int edgeWeight = (int) a.findEdge(vertex).get_weight();
			a.get_location().view_yazidi().remove(a);
			a.set_location(vertex);
			vertex.view_yazidi().add(a);
			a.addCost(cost);
			a.set_foodCarried(a.get_foodCarried()+vertex.takeSupplies()-edgeWeight);
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
			a.setCost(Double.POSITIVE_INFINITY);
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
			switch(gt){
			case ZeroSum:
				agentStartVertex = getAgentStartVertex(this,s);
				agentGoalVertex = getAgentGoalVertex(this,s);
				this.addYazidi(agentStartVertex,agentGoalVertex,_agents.size());
				break;
			case nonZeroSum:
				agentStartVertex = getAgentStartVertex(this,s);
				agentGoalVertex = getAgentGoalVertex(this,s);
				this.addYazidi(agentStartVertex,agentGoalVertex,_agents.size());
				break;
			case FullyCooperative:
				agentStartVertex = getAgentStartVertex(this,s);
				agentGoalVertex = getAgentGoalVertex(this,s);
				this.addYazidi(agentStartVertex,agentGoalVertex,_agents.size());
				break;
			}
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

	private int getAgentMaxExpansions(Graph g, String[] s) {
		if(g.getVertices().size()==0){
			System.err.println("Why did you not add vertices?");
			System.exit(-1);
			}
		return Integer.decode(s[4]);
	}
	private void addAStarYazidi(int agentStartVertex, int agentGoalVertex, int id, Graph g) {
		Yazidi yazidi = new AStarYazidi(_vertices.get(agentStartVertex-1), _vertices.get(agentGoalVertex-1), _vertices.get(agentStartVertex-1).takeSupplies(), id, g);
		this._agents.add(yazidi);
		_vertices.get(agentStartVertex-1).addYazidi(yazidi);
	}
	private void addRTAStarYazidi(int agentStartVertex, int agentGoalVertex, int id, Graph g,int maxExpansions) {
		Yazidi yazidi = new RTAStarYazidi(_vertices.get(agentStartVertex-1), _vertices.get(agentGoalVertex-1), _vertices.get(agentStartVertex-1).takeSupplies(), id, g, maxExpansions);
		this._agents.add(yazidi);
		_vertices.get(agentStartVertex-1).addYazidi(yazidi);
	}
	private void addSearchYazidi(int agentStartVertex, int agentGoalVertex, int id, Graph g) {
		Yazidi yazidi = new GreedySearchYazidi(_vertices.get(agentStartVertex-1), _vertices.get(agentGoalVertex-1), _vertices.get(agentStartVertex-1).takeSupplies(), id, g);
		this._agents.add(yazidi);
		_vertices.get(agentStartVertex-1).addYazidi(yazidi);

	}
	@SuppressWarnings("unused")
	private static int getAgentCount(Scanner s) {
		while(true){
			try{
				System.out.println("Please enter amount of agents wanted:");
				String line = getLine(s);
				int count = Integer.decode(line);
				if(count>0)
					return count;
			}catch (NumberFormatException e){}
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
				if(0<type && type<8)
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
		this._agents.add(toAdd);//TODO CHANGE FROM 10
		_vertices.get(agentStartVertex-1).addHuman(toAdd);

	}

	private void addIsis(int agentStartVertex, int agentsAmount) {
		ISIS toAdd = new ISIS(_vertices.get(agentStartVertex-1),_vertices.get(agentStartVertex-1).takeSupplies(),agentsAmount);
		this._agents.add(toAdd);//TODO CHANGE FROM 10
		_vertices.get(agentStartVertex-1).addIsis(toAdd);

	}

	private void addObama(int id) {
		this._agents.add(new Obama(id));//TODO CHANGE FROM 10

	}

	private void addYazidi(int agentStartVertex, int agentGoalVertex,int id) {
		Yazidi yazidi = new Yazidi(_vertices.get(agentStartVertex-1),_vertices.get(agentGoalVertex-1),_vertices.get(agentStartVertex-1).takeSupplies(), id);
		this._agents.add(yazidi);//TODO CHANGE FROM 10
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


}
