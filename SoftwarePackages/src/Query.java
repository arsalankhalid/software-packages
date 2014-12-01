/*
 * Query class deals with Linux packages and its dependencies
 * 
 * Written By: Pavlo Kuzhel
 * Date: Nov 30, 2014
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Query {
	
	public static void main(String[] args){
		
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

		System.out.println("--- Software packages and their dependencies ---");
		System.out.println("Initializing... \nPlease wait a few moments as the required files are loaded...");

		try {
			goals = reader.getTreeSet(goalsCSV, 0);
			sources = reader.getArrayList(depCapCSV, 0);
			capabilities = reader.getArrayList(depCapCSV, 1);
	
			binary = reader.getArrayList(binDepCSV, 0);
			binSources = reader.getArrayList(binDepCSV, 1);
	
			capabilityBinaries = reader.getMap(capBinCSV, 0, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Seems like you are not connected to the internet.");
			System.out.println("Please connect to the internet. Try again? (Y or N)");
			//e.printStackTrace();
		}

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

		Set<String> keys = sourceBinaries.keySet();

		java.util.Iterator<String> iterator = keys.iterator();

		/*
		 * Adding direct dependencies
		 */
		iterator = goals.iterator();

		System.out.print("Adding package dependencies.");
		int counter = 0;
		while(iterator.hasNext()){
			if(counter%1000 == 0){
				System.out.print(".");
			}
			String goal = iterator.next();
			for(int i=0; i<sources.size(); i++){
				if (sources.get(i).contains(goal)){
					String capab = capabilities.get(i);
					String bin = capabilityBinaries.get(capab);
					for (Map.Entry<String,TreeSet<String>> entry : sourceBinaries.entrySet()) {
						TreeSet<String> bins = entry.getValue();
						String key = entry.getKey();
						if (bins.contains(bin) && goals.contains(key) ){
							graph.addEdge(goal, key);
						}
					}
				}
			}
			counter++;
		}
		
		System.out.println("\nThank you for your patience. Please select an option from the menu below: ");

		int edges, verts;
		verts = graph.getNumberOfVertices();
		edges = graph.getNumberOfEdges();
		System.out.println("Number of Vertices: " + verts + "\nNumber of Edges: " + edges);

		graph.printEdges("dbus", "dbus");

		// change to printMatchingPath
		graph.printMatchingPath("devel", "devel", 4);

		System.out.println("Main Finished");

	}
}
