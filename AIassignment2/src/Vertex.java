import java.util.Vector;


public class Vertex implements Comparable<Vertex> {
	private double _num;
	public double get_num() {
		return _num;
	}

	private Vector<ISIS> _isis;
	private Vector<Yazidi> _yazidi;
	private Vector<Human> _human;
	private Integer _supplies;
	private Vector<Edge> _neighbors;
	
	public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    
    public String toString() { return getName(); }
    
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
	
	public double view_num() {
		return _num;
	}

	public Vector<ISIS> view_isis() {
		return _isis;
	}

	public Vector<Yazidi> view_yazidi() {
		return _yazidi;
	}

	public Integer view_supplies() {
		return _supplies;
	}

	public Vector<Edge> view_neighbors() {
		return _neighbors;
	}

	public Vector<Human> view_human() {
		return _human;
	}

	
	public Vertex(double num, Integer supplies){
		_num = num;
		_neighbors = new Vector<Edge>();
		_supplies = supplies;
		_isis = new Vector<ISIS>();
		_yazidi = new Vector<Yazidi>();
		_human = new Vector<Human>();
	}
	
	public void addEdge(Vertex v,double weight){
		_neighbors.add(new Edge(v,weight));
	}
	public void printNeighbors() {
		System.out.print(getName()+"("+_supplies+")"+":");
		for(Edge v:_neighbors)
			System.out.print(v.getName()+",");
		System.out.println();
		
	}

	public String getName() {
		return "V"+_num;
	}

	public boolean addIsis(ISIS toAdd){return _isis.add(toAdd);}
	public boolean addYazidi(Yazidi toAdd){return _yazidi.add(toAdd);}
	public boolean addHuman(Human toAdd){return _human.add(toAdd);}
	public void addSupplies(Integer toAdd){_supplies+=toAdd;}
	
	public boolean removeYazidi(Yazidi y){
		return _yazidi.remove(y);
	}
	public boolean removeHuman(Human y){
		return _human.remove(y);
	}
	public boolean removeIsis(ISIS i){
		return _isis.remove(i);
	}
	public Integer takeSupplies(){
		Integer toTake = _supplies;
		_supplies = new Integer(0);
		return toTake;
	}

	public void RemoveIsis() {
		this._isis = new Vector<ISIS>();
		
	}

	public void removeYazidi() {
		this._yazidi = new Vector<Yazidi>();
		
	}

	public void printVertex() {
		System.out.print(getName() +":Sup("+_supplies+"):Y("+_yazidi.size()+"), ");
	}

	public void removeHuman() {
		this._human = new Vector<Human>();
		
	}

	public void addEdge(Edge newE) {
		_neighbors.add(newE);
		
	}

	public Edge findEdge(Vertex target) {
		for(Edge e: this.view_neighbors()){
			if(target == e.get_target()){
				return e;
			}		}
		return null;
	}

}
