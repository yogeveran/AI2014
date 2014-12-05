
import java.util.Scanner;

import javax.tools.JavaFileManager.Location;


public class Human extends Agent {

	private Vertex _goal;
	private int _foodCarried;
	private double _cost;
	
	public Human(Vertex _location, Vertex _goal, int _foodCarried, int id) {
		super(id,_location);
		this._goal = _goal;
		this._foodCarried = _foodCarried;

	}

	@Override
	public Action getAction(Graph g) {
		Scanner s = new Scanner(System.in);
		//Get User Vertex to goto or NoOp
		System.out.println();
		for(int i=0;i<_location.view_neighbors().size();i++){
			System.out.println("Current neighbors : " + _location.view_neighbors().get(i).getName());
		}
		while(true){
			System.out.println("Please enter destination or 0 to stay : ");
			String line = getLine(s);
			try{
				int neighborNum = Integer.decode(line);
				if(neighborNum==0)
					return new Action(ActionType.NoOp, null);
				
				if(0<neighborNum && neighborNum<=_location.view_neighbors().size()){
					System.out.println();
					System.out.println(_location.view_neighbors().get(neighborNum-1).getName());
					return new Action(ActionType.Traverse, _location.view_neighbors().get(neighborNum-1).get_target());
				}else
					throw new Exception("Out Of Bounds");
			}catch (Exception e){}
		}
	}

	public static String getLine(Scanner s) {

		
		String line = s.nextLine(); 
		return line;
	}
	
	@Override
	public void addCost(double cost) {
		this._cost+=cost;
		
	}

	@Override
	public double getCost() {
		return this._cost;
	}
	
	public void setCost(double cost) {
		_cost = cost;
		
	}

	@Override
	public void printAgent() {
		String status = (_location==_goal) ? "win" : ((_cost == Double.POSITIVE_INFINITY) ? "dead" : "alive");
		System.out.print("Y"+_id+":score("+_cost+"):food("+_foodCarried+"):loc("+_location+"):status(" +status+ ")" );
		
	}
	
	public void addFood(int i) {
		_foodCarried+=i;
	}
	
	public Vertex get_location() {
		return _location;
	}

	public void set_location(Vertex _location) {
		this._location = _location;
	}

	public int get_foodCarried() {
		return _foodCarried;
	}

	public void set_foodCarried(int _foodCarried) {
		this._foodCarried = _foodCarried;
	}

	public Vertex get_goal() {
		return _goal;
	}

	public Edge findEdge(Vertex target) {
		for(Edge e: this._location.view_neighbors()){
			if(target == e.get_target()){
				return e;
			}		}
		return null;
	}

	@Override
	public void printAgentScore() {
		printAgent();
		
	}

	@Override
	public void Traverse(Graph graph, Vertex get_target){
		graph.Traverse(this, get_target);
	}
	
}
