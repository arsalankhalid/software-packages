//Arsalan Khalid
//Last Edited: Dec. 1, 2014
//BTP500 - Lew Baxter
//Software Packages - Graph Class

/*
 Graph class holds and manages multiple vertices and edges between them. 
 The dependencies of vertices are directional, multi-directional however is not
 being used in this use case. 
 Please note that all vertices have been broken into seperate sets.
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
	 * edge between them.
	 */
	public void addEdge(String rootVertex, String vertexToDependOn) {
		// check if the root vertex contains an edge
		if (allVertices.containsKey(rootVertex)) {
			// look for an edge between root vertex and the depending vertex
			if (isEdge(rootVertex, vertexToDependOn)) {
				System.out.println("~~~Issue: " + rootVertex + " --> "
						+ vertexToDependOn + ". Edge already exists~~~");
			}
			// if vertex does exists, add the vertexToDependOn and place an edge
			// between the two
			else {
				// get all root vertex's into a set, then add an edge to the
				// vertex
				TreeSet<String> setOfVertices = allVertices.get(rootVertex);

				// place the new vertex to depend on within the set
				setOfVertices.add(vertexToDependOn);

				// assign the rootVertex's key to the set
				allVertices.put(rootVertex, setOfVertices);

				// adds the vertexToDependOn to the allVertices and an edge
				// between the passed in root vertex and vertexToDependOn
				createVertexIfNotExists(vertexToDependOn, null);
				System.out.println(rootVertex + " --> " + vertexToDependOn
						+ ". edge has been added");
			}
		} else {
			// if the root vertex and vertex to depend on don't exist create
			// them and add an edge between them
			createVertexIfNotExists(rootVertex, vertexToDependOn);
			// debug note
			System.out.println(rootVertex + " ===> " + vertexToDependOn
					+ ". This edge has been added");
		}
	}

	/*
	 * Method gets two vertices and adds both of them to allVertices, then adds
	 * an edge between them if the vertexToDependOn argument isn't passed as
	 * null
	 */

	private void createVertexIfNotExists(String newVertex,
			String vertexToDependOn) {
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
				createVertexIfNotExists(vertexToDependOn, null);
			}
			// place edge between the newVertex and the new created Tree Set
			allVertices.put(newVertex, tempTree);
		}
	}

	// Checks for an edge between 2 passed in vertex substrings
	public boolean isEdge(String rootVertex, String vertexToDependOn) {
		boolean result = false; // default set to edges between passed in
								// vertices

		// get all vertices with the root vertex name
		TreeSet<String> verticeSet = allVertices.get(rootVertex);

		// check if the set contains the vertex to depend on as a key, if it
		// does then
		// there's an edge
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
			Iterator mainVerticesIter = verticeSet.iterator();
			// for each key value add 1 to number of Edges
			while (mainVerticesIter.hasNext()) {
				mainVerticesIter.next();
				numOfEdges++;
			}
		}
		return numOfEdges;
	}

	// This method prints the dependency between two vertices if it exists
	// note that only the first edge that is found is printed
	public void printEdges(String vertex1, String vertex2) {
		// create generic collection of vertexes and get their values
		Collection<TreeSet<String>> vertexValues = allVertices.values();
		// create set of distinct keys
		Set<String> keys = allVertices.keySet();
		// iterate through keys
		Iterator<String> allKeys = keys.iterator();
		//
		Iterator<TreeSet<String>> allVertexValues = vertexValues.iterator();

		while (allKeys.hasNext()) {
			// covert keys to string then pass to set
			String keyName = allKeys.next().toString();
			TreeSet<String> mainVertices = (TreeSet<String>) allVertexValues
					.next();
			if (keyName.contains(vertex1)) {
				Iterator<String> mainVerticesIter = mainVertices.iterator();
				while (mainVerticesIter.hasNext()) {
					String vertexToDependOn = mainVerticesIter.next()
							.toString();
					if (vertexToDependOn.contains(vertex2)) {
						System.out.println("Found an edge between: " + keyName
								+ "and" + "--> " + vertexToDependOn);
					}
				}
			}
		}
		System.out.println("The edge between: " + vertex1 + " and " + vertex2
				+ " was not found");
	}

	// metho prints path between two vertex's at a given length
	public void printPath(String vertex1, String vertex2, int pathLength) {
		Iterator allVerticesIter = allVertices.entrySet().iterator();
		ArrayList<String> pastVertices = new ArrayList();
		while (allVerticesIter.hasNext()) {
			Map.Entry pair = (Map.Entry) allVerticesIter.next();
			String vertexName = pair.getKey().toString();
			if (vertexName.contains(vertex1)) {
				int pathCounter = 0;
				String matchedPath = depthFirstPathSearch(vertexName, vertex2,
						pathCounter, pathLength, pastVertices);
				if (matchedPath != "")
					System.out.println(matchedPath);
				else
					System.out.print("The path of length " + pathLength
							+ " between '" + vertex1 + "' and '" + vertex2
							+ "' has not been found\n");
				return;
			}
		}
	}

	private String depthFirstPathSearch(String currentVertex,
			String searchVertex, int pathCounter, int pathLength,
			ArrayList<String> pastVertices) {

		if (pastVertices.contains(currentVertex))
			return "";

		if (pathCounter >= pathLength) {
			if (currentVertex.contains(searchVertex)) {
				return currentVertex;
			} else {
				return "";
			}
		} else {
			pastVertices.add(currentVertex);
			pathCounter++;

			TreeSet<String> mainVerteces = allVertices.get(currentVertex);
			Iterator mainVertecesIter = mainVerteces.iterator();

			while (mainVertecesIter.hasNext()) {
				String currentVertexLeaf = mainVertecesIter.next().toString();
				String matchedPath = depthFirstPathSearch(currentVertexLeaf,
						searchVertex, pathCounter, pathLength, pastVertices);
				if (matchedPath != "") {
					String temp = currentVertex.concat(" ==> ");
					return temp.concat(matchedPath);
				}
			}
			return "";
		}

	}
}