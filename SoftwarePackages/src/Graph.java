//Arsalan Khalid
//Last Edited: Dec. 1, 2014
//BTP500 - Lew Baxter
//Software Packages - Graph Class

/*
 Graph class holds and manages multiple vertices and edges 
 The dependencies of vertices are directional, multi-directional however is not
 being used in this use case. 
 Please note that all vertices have been broken into seperate sets.
 */

//Data-Strucutres USED:
/********************
 **	ArrayList
 **	TreeMap
 **	TreeSet
 **	Map + Entry Map
 **	Collections + Iterators
 **  Sets 
 */

import java.util.*;

public class Graph {
	/*
	 * The TreeMap 'key' property is a string which holds the name of a vertex.
	 * The value property represents the set of string which designate upon
	 * which vertex the vertex or key it depends on! Therefore: Value=set of
	 * Vertex strings, Key=Dependency
	 */
	private TreeMap<String, TreeSet<String>> allVertices;

	// default constructor creates default data-structure for all vertices
	public Graph() {
		allVertices = new TreeMap<String, TreeSet<String>>();
	}

	/*
	 * addEdge() method gets the names of the desired vertex and the vertex to
	 * link to through the parameters, then adds both of those vertices to the
	 * list of known vertices (if they haven't been added before), and adds the
	 * edge from the rootVertex to vertexToDependOn
	 */

	public void addEdge(String rootVertex, String vertexToDependOn) {
		// check if the root vertex contains an edge
		if (allVertices.containsKey(rootVertex)) {
			// look for an edge with root vertex and the depending vertex
			if (isEdge(rootVertex, vertexToDependOn)) {
				// for debuging
				// System.out.println("~~~Issue: " + rootVertex + " --> "
				// + vertexToDependOn + ". Edge already exists~~~");
			}
			// if vertex does exists, add the vertexToDependOn and place an edge
			// from rootVertex to vertexToDependOn
			else {
				// get all root vertex's into a set, then add an edge to the
				// vertex
				TreeSet<String> setOfVertices = allVertices.get(rootVertex);

				// place the new vertex to depend on within the set
				setOfVertices.add(vertexToDependOn);

				// assign the rootVertex's key to the set
				allVertices.put(rootVertex, setOfVertices);

				// adds the vertexToDependOn to the allVertices and an edge
				// from rootVertex to the verTextoDependOn
				createVertexAndEdges(vertexToDependOn, null);
				// for debugging
				/*
				 * System.out.println(rootVertex + " --> " + vertexToDependOn +
				 * ". edge has been added");
				 */
			}
		} else {
			// if the root vertex and vertex to depend on don't exist create
			// them and add an edge from the rootVertex to the vertexToDependOn
			createVertexAndEdges(rootVertex, vertexToDependOn);
			// debug note
			/*
			 * System.out.println(rootVertex + " ===> " + vertexToDependOn +
			 * ". This edge has been added");
			 */
		}
	}

	/*
	 * Method gets two vertices and adds both of them to allVertices, then adds
	 * an edge from the newVertex to the vertexToDependOn them if the
	 * vertexToDependOn argument isn't passed as null
	 */

	private void createVertexAndEdges(String newVertex, String vertexToDependOn) {
		// check if the newVertex exists
		if (!allVertices.containsKey(newVertex)) {
			// put in a newVertex key with an empty TreeSet obj
			allVertices.put(newVertex, new TreeSet<String>());
			// now create new TreeSet obj and create value
			TreeSet<String> tempTree = new TreeSet<String>();
			// If the second argument (vertexToDependOn isn't null, then add to
			// the tree, and
			// add an edge with the newVertex as the key
			if (vertexToDependOn != null) {
				tempTree.add(vertexToDependOn);
				// recursive call, adds vertexToDependOn to main list
				createVertexAndEdges(vertexToDependOn, null);
			}
			// place edge to the newVertex and the new created Tree Set
			// (rootVertex)
			allVertices.put(newVertex, tempTree);
		}
	}

	// Checks for an edge from rootVertex to vertexToDependOn
	private boolean isEdge(String rootVertex, String vertexToDependOn) {
		boolean result = false;

		// get all vertices with the root vertex name
		TreeSet<String> verticeSet = allVertices.get(rootVertex);

		// check if the set contains the vertex to depend on as a key, if it
		// does then there's an edge

		if (verticeSet.contains(vertexToDependOn))
			result = true;

		return result;
	}

	// get number of vertices from main map
	public int getNumberOfVertices() {
		return allVertices.size();
	}

	// get number of edges by checking each vertex for a key that matches
	// another vertex using an iterator
	public int getNumberOfEdges() {
		int numOfEdges = 0;
		// create a generic collection holding all the vertex's (all the
		// vertexValues)
		Collection<TreeSet<String>> vertexValues = allVertices.values();
		// Set iterator go through each vertex key
		Iterator vertexValuesIter = vertexValues.iterator();
		// hasNext starts at -1 then goes to each vertex through each iteration
		while (vertexValuesIter.hasNext()) {

			// create a TreeSet for each vertex
			TreeSet<String> verticeSet = (TreeSet<String>) vertexValuesIter
					.next();
			Iterator vertexesIter = verticeSet.iterator();
			// for each key value add 1 to number of Edges
			while (vertexesIter.hasNext()) {
				vertexesIter.next();
				numOfEdges++;
			}
		}
		return numOfEdges;
	}

	// This method prints the edges from vertex1 to vertex2
	// it must first check if the vertex's exist and if they contain edges
	// note that only the first edge that is found is printed

	public void printEdges(String vertex1, String vertex2) {
		// create generic collection of vertexes and get their values
		Collection<TreeSet<String>> vertexValues = allVertices.values();
		// create set of distinct keys
		Set<String> keys = allVertices.keySet();
		// iterate through keys
		Iterator<String> allKeys = keys.iterator();
		// retrives all vertex's from the iterator
		Iterator<TreeSet<String>> allVertexValues = vertexValues.iterator();
		// loop through all keys and print every edge found
		while (allKeys.hasNext()) {
			// convert keys to string then pass to set
			String keyName = allKeys.next().toString();
			// create set of vertexes for each iteration containing vertex
			// names/values
			TreeSet<String> vertexes = (TreeSet<String>) allVertexValues.next();
			// checks if there's an edge to vertex1 (performs check to do print)
			if (keyName.contains(vertex1)) {
				Iterator<String> vertexesIter = vertexes.iterator();
				while (vertexesIter.hasNext()) {
					String vertexToDependOn = vertexesIter.next().toString();
					if (vertexToDependOn.contains(vertex2)) {
						System.out.println(keyName
								+ "to" + "--> " + vertexToDependOn);
						System.out.println("1");
					}
					// if the edge from vertex1 to vertex2 depends on it'self?
					if (vertexToDependOn.contains(vertex1)) {
						// System.out
						//		.println("The edge from vertex1 to vertex2 depend on each other");
					}
				}
			}
		}
		// worst case - no edge found to print
		System.out.println("The edge from " + vertex1 + " to " + vertex2
				+ " was not found");
	}

	// method prints a from vertex1 to vertex2 at a given length
	public void printMatchingPath(String vertex1, String vertex2, int pathLength) {
		// retrieves all vertices to iterate through
		Iterator allVerticesIter = allVertices.entrySet().iterator();
		// utilized an ArrayList for dynamic efficiency
		ArrayList<String> previousVertexes = new ArrayList();
		// loop through allVerticies to find matching paths
		while (allVerticesIter.hasNext()) {
			// create a collection view of map holding references of each vertex
			Map.Entry pair = (Map.Entry) allVerticesIter.next();
			String vertexName = pair.getKey().toString();
			// check if each collection has edge from vertex1 to vertex
			if (vertexName.contains(vertex1)) {
				// get the length of the path
				int pathCounter = 0;
				// perform full depth search to get the path length after
				// matching
				String matchedPath = depthFirstPathSearch(vertexName, vertex2,
						pathCounter, pathLength, previousVertexes);
				if (matchedPath != "")
					System.out.println(matchedPath);
				else
					// print lenth and matching path from vertex 1 to vertex 2
					// note that matching path is first matching path, not full
					// circles
					System.out.print("The path of length " + pathLength
							+ " from '" + vertex1 + "' and '" + vertex2
							+ "' has not been found\n");
			}
		}
	}

	// this private method recursively determines (divides into sub-problems)
	// the depth from the current vertex
	// to the desired or desiredVertex, utilizes paramter of length to determine
	// path length

	private String depthFirstPathSearch(String currentVertex,
			String desiredVertex, int pathCounter, int pathLength,
			ArrayList<String> previousVertexes) {

		// if previousVertex contains the current vertex then end
		if (previousVertexes.contains(currentVertex))
			return "";

		// check to see if path contains desired Vertex
		if (pathCounter >= pathLength) {
			if (currentVertex.contains(desiredVertex)) {
				return currentVertex;
			} else {
				return "";
			}
		} else {
			// add current vertexes to previous path and move forward
			previousVertexes.add(currentVertex);
			pathCounter++;

			// create set of current verticies holding current vertexes for
			// current path
			TreeSet<String> currentVertices = allVertices.get(currentVertex);
			Iterator currentVerticesIter = currentVertices.iterator();

			// iterate through current path
			while (currentVerticesIter.hasNext()) {
				String currentVertexLeaf = currentVerticesIter.next()
						.toString();
				// recursively find desired path (divides into sub-problems/sub
				// map)
				String matchedPath = depthFirstPathSearch(currentVertexLeaf,
						desiredVertex, pathCounter, pathLength,
						previousVertexes);
				if (matchedPath != "") {
					String temp = currentVertex.concat(" ==> ");
					return temp.concat(matchedPath);
				}
			}
			return "";
		}

	}
}