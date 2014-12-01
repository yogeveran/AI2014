import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Dijkstra {
	public static void computePaths(Vertex source,Graph g,boolean ignoreWeight) {
		resetGraph(g);
		source.minDistance = 0;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();

			// Visit each edge exiting u
			for (Edge e : u.view_neighbors()) {
				Vertex v = e.get_target();
				double weight = (ignoreWeight ? 1: e.get_weight());
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);
					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
	}

	private static void resetGraph(Graph g) {
		for(Vertex v: g.getVertices()){
			v.minDistance = Double.POSITIVE_INFINITY;
			v.previous = null;
			}
	}

	public static List<Vertex> getShortestPathTo(Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);
		Collections.reverse(path);
		return path;
	}

	public static void main(String[] args) {
		
		
		Vertex v0 = new Vertex(0,0);
		Vertex v1 = new Vertex(1,0);
		Vertex v2 = new Vertex(2,0);
		Vertex v3 = new Vertex(3,0);
		Vertex v4 = new Vertex(4,0);
		Vertex v5 = new Vertex(5,0);

		v0.addEdge(v1, 5);
		v0.addEdge(v2, 10);
		v0.addEdge(v3, 8);
		
		v1.addEdge(v0, 5);		
		v1.addEdge(v2, 3);
		v1.addEdge(v4, 7);
		
		v2.addEdge(v0, 10);
		v2.addEdge(v1, 3);
		
		v3.addEdge(v0, 8);
		v3.addEdge(v4, 2);
		
		v4.addEdge(v1, 7);
		v4.addEdge(v3, 2);
		
		Vertex[] vertices = { v0, v1, v2, v3, v4, v5 };
		Graph g = new Graph(vertices);
		computePaths(v0,g,true);
		
		for (Vertex v : vertices) {
			System.out.println("Distance to " + v + ": " + v.minDistance);
			List<Vertex> path = getShortestPathTo(v);
			System.out.println("Path: " + path);
		}
	}
}