import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Query {
	  public static void main(String[] args) throws IOException{
		Graph graph = new Graph();
		
		String goalsCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_goal.csv",
				depCapCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_cap.csv",
				capBinCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_rpm.csv",
				binDepCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_srpm.csv"
				;
		
		TreeSet<String> goals = new TreeSet<String>();
		TreeMap<String,String> capabilityBinaries = new TreeMap<String,String>();
		ArrayList<String> sources = new ArrayList<String>();
		ArrayList<String> capabilities = new ArrayList<String>();
		
		ArrayList<String> binary = new ArrayList<String>();
		ArrayList<String> binSources = new ArrayList<String>();
		
		TreeMap<String, TreeSet<String>> sourceBinaries = new TreeMap<String, TreeSet<String>>();
		
		CSVReader reader = new CSVReader();
		
		goals = reader.getTreeSet(goalsCSV, 0);
		sources = reader.getArrayList(depCapCSV, 0);
		capabilities = reader.getArrayList(depCapCSV, 1);

		binary = reader.getArrayList(binDepCSV, 0);
		binSources = reader.getArrayList(binDepCSV, 1);
		
		// extract all binaries that are true for a source and put them in a map of TreeSet objects
		for(int i=0; i<binSources.size(); i++){
			TreeSet<String> binarySet = new TreeSet<String>();
			
			for(int j=i; j<binSources.size(); j++){
				if (binSources.get(i).equals(binSources.get(j))){
					binarySet.add(binary.get(j));
				}
			}
			
			sourceBinaries.put(binSources.get(i), binarySet);
		}
		
		System.out.println("Size of the Map with TreeSets: " + sourceBinaries.size());
		
		Set<String> keys = sourceBinaries.keySet();
		
		java.util.Iterator<String> iterator = keys.iterator();
		
		capabilityBinaries = reader.getMap(capBinCSV);

		/*
		 * AddEdges
		 */
		int counter=0;
		iterator = goals.iterator();
		
		while(iterator.hasNext()){
			String goal = iterator.next();
			//System.out.println("Checking for goal" + goal);
			for(int i=0; i<sources.size(); i++){
				//TreeSet<String> capabs = sources.get(i);
				if (sources.get(i).contains(goal)){
					//System.out.println("Found capabilities for goal: " + capabilities.get(i));
					String capab = capabilities.get(i);
					String bin = capabilityBinaries.get(capab);
					
					for (Map.Entry<String,TreeSet<String>> entry : sourceBinaries.entrySet()) {
						TreeSet<String> bins = entry.getValue();
						if (bins.contains(bin) ){//&& goals.contains(entry.getKey()) ){
						   graph.addEdge(goal, entry.getKey());
						   counter++;
						}
					}
					
				}
			}
		}
		System.out.println("Total Edges added: " + counter);
		
		int edges, verts;
		verts = graph.getNumberOfVertices();
		edges = graph.getNumberOfEdges();
		System.out.println("Number of Vertices: " + verts + "\nNumber of Edges: " + edges);
		
		graph.printEdges("dbus", "dbus");
		
		graph.printPath("devel", "devel", 4);
		
		System.out.println("Main Finished");
		
	  }
}
