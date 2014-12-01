
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;



public class GreedySearchYazidi extends Yazidi {
	private Graph _powerGraph;
	private long _expand = 0;
	public GreedySearchYazidi(Vertex _location, Vertex _goal, int _foodCarried, int id,Graph g) {
		super(_location, _goal, _foodCarried, id);
		_powerGraph = Graph.makePowerGraph(g);

		for(Vertex v: _powerGraph.get_vertices())
			if(v.get_num()==_goal.get_num()){
				Dijkstra.computePaths(v,_powerGraph,false);
				break;
			}
		_path = ConvertToReal(g,GeneralSearch());
		if(_path!=null){
			System.out.print("Path of "+this+":");
			for(Vertex v: _path)
				System.out.print(v.getName()+", ");
			System.out.println();
		}else{
			System.out.println(this + " found no solution.");
		}
	}
	
	private List<Vertex> ConvertToReal(Graph g, List<Vertex> list) {
		if(list==null)
			return null;
		Vector<Vertex> realList = new Vector<Vertex>();
		for(Vertex v: list)
			for(Vertex realV: g.get_vertices())
				if(v.get_num()==realV.get_num()){
					realList.add(realV);
					break;
				}
		return realList;
	}

	@Override
	public Action getAction(Graph g) {

		if((_path==null)||noRouteOrAtGoal()||notEnoughFood()){
				System.out.println(this +" Chose NoOp");
				return new Action(ActionType.NoOp, null);
			}
		System.out.println(this +" Chose to move to "+_path.get(0).getName());
		return new Action(ActionType.Traverse, _path.remove(0));
	}
	
	public double heuristic(Node v){
		//System.out.println("H("+v.get_state().getName()+")="+v.get_state().minDistance);
		return v.get_state().minDistance;
	}

	public List<Vertex> GeneralSearch() {
		PriorityQueue<Node> nodes = makeEmptyQueue();
		for(Vertex v: _powerGraph.get_vertices())
			if(v.get_num()==_location.get_num()){
				nodes.add(new Node(null, v,_foodCarried, 0));
				break;
			}
		
		Node node = null;
		while(true){
			if(nodes.isEmpty()){
				System.out.println("No Solution Found.");
				return null;//Failure
				}
			node = nodes.remove();
			System.out.println();
			System.out.println("Pulled Node:"+node+"- vertex-"+node.get_state().get_num()+" supplies:"+node.get_currentSupplies()+" newDepth:"+node.get_debth());
			
			if(goalTest(node)){
				System.out.println("Found solution Node: vertex-"+node.get_state().get_num()+" supplies:"+node.get_currentSupplies()+" newDepth:"+node.get_debth());
				return buildRoute(node);
				}
			
			if(node.get_debth()!=Double.POSITIVE_INFINITY){
				_expand++;
				nodes.addAll(node.expand());
				}
		}
	}
	
	private List<Vertex> buildRoute(Node node) {
		Vector<Vertex> route = new Vector<Vertex>();
		while(node != null){
			route.add(node.get_state());
			node = node.get_parent();
		}
		Collections.reverse(route);
		if(!route.isEmpty())
			route.remove(0);
		return route;
	}
	
	

	private boolean goalTest(Node node) {
		return heuristic(node)==0;
	}

	private PriorityQueue<Node> makeEmptyQueue() {
		PriorityQueue<Node> nodes = new PriorityQueue<Node>(new Comparator<Node>() {
			
			@Override
			public int compare(Node arg0, Node arg1) {
				int res =  (int) (heuristic(arg0) - heuristic(arg1));
				if(res==0)
					if(arg0.get_state().get_num() < arg1.get_state().get_num())
						res = 1; 
					else
						res = -1;
				return res;
			}
	});
		return nodes;
	}
	
	@Override
	public void printAgentScore() {
		double f1 = _cost+_expand;
		double f2 = _cost*100+_expand;
		double f3 = _cost*10000+_expand;
		System.out.println(this+": f=1: score("+f1+") f=100: score("+f2+") f=10000: score("+f3+")");
	}
}
