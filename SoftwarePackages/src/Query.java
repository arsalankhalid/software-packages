/*
 * Query class deals with Linux packages and its dependencies
 * 
 * Written By: Pavlo Kuzhel
 * Date: Nov 30, 2014
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Iterator;


public class Query {
	
	public static ArrayList<String> matches(TreeSet<String> set, String string){
		Iterator<String> iterator;
		ArrayList<String> list = new ArrayList<String>();
		iterator = set.iterator();
		
		while(iterator.hasNext()){
			String item = iterator.next();
			if (item.contains(string)){
				list.add(item);
			}
		}
		return list;
	}
	public static void main(String[] args){
		
		Graph graph = new Graph();

		String goalsCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_goal.csv",
				depCapCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_cap.csv",
				capBinCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_rpm.csv",
				binDepCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_srpm.csv";

		TreeSet<String> goals = new TreeSet<String>();
		TreeMap<String,String> capabilityBinaries = new TreeMap<String,String>();
		ArrayList<String> sources = new ArrayList<String>();
		ArrayList<String> capabilities = new ArrayList<String>();

		ArrayList<String> binary = new ArrayList<String>();
		ArrayList<String> binSources = new ArrayList<String>();

		TreeMap<String, TreeSet<String>> sourceBinaries = new TreeMap<String, TreeSet<String>>();

		CSVReader readerCsv = new CSVReader();

		Iterator<String> iterator;

		System.out.println("--- Software packages and their dependencies ---");
		System.out.println("Initializing... \nThe required files are being loaded, please wait...");

		try {
			goals = readerCsv.getTreeSet(goalsCSV, 0);
			sources = readerCsv.getArrayList(depCapCSV, 0);
			capabilities = readerCsv.getArrayList(depCapCSV, 1);
	
			binary = readerCsv.getArrayList(binDepCSV, 0);
			binSources = readerCsv.getArrayList(binDepCSV, 1);
	
			capabilityBinaries = readerCsv.getMap(capBinCSV, 0, 1);
		} catch (IOException e) {
			System.out.println("Seems like you are not connected to the internet.");
			System.out.println("Please connect to the internet and re-start the program.");
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

		/*
		 * Adding direct dependencies
		 */
		iterator = goals.iterator();

		System.out.print("Adding package dependencies");
		int counter = 0;
		while(iterator.hasNext()){
			// print user feedback every thousand iterations
			// so the user knows that the program is still working
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
		

		Scanner reader = new Scanner( System.in );
		String line = "";
		String[] choices = null;
		System.out.print("\n");
		
		do {
			
			System.out.println("\n----------------------------- Menu -----------------------------");
			System.out.println("Please type in your command: ");

			System.out.println("-Type 'g pattern' to display all goals that match the pattern 'pattern'");
			System.out.println("-Type 's dependencies' to display the number of dependencies between all packages");
			System.out.println("  or 's dependents' to display the number of dependant packages");
			System.out.println("-Type 'd pattern1 pattern2' to display all packages that match the pattern 'pattern1'"
					+ "\n    and that are depended on a package that matches pattern 'pattern2'");
			System.out.println("-Type 'p pattern1 pattern2 integer' to display a dependency path from a package that "
					+ "\n    matches the pattern 'pattern1', to a package that matches pattern 'pattern2', with a length"
					+ " integer");
			System.out.println("-Type 'q' to quit");


			line = reader.nextLine();
			choices = line.split(" ");

			switch(choices[0]){
			case "g":
				ArrayList<String> matches = matches(goals, choices[1]);
				//System.out.println("The following goals contain the pattern '" + choices[1] + "':");
				for(int i=0; i<matches.size(); i++){
					System.out.println("* " + matches.get(i));
				}
				break;
			case "s":
				if(choices[1].equals("dependencies")){
					System.out.println("The number of dependencies is: " + graph.getNumberOfEdges());
				}else{
					System.out.println("The number of dependent packages is: " + graph.getNumberOfVertices());
				}
				break;
			case "d":
				graph.printEdges(choices[1], choices[2]);
				break;
			case "p":
				graph.printMatchingPath(choices[1], choices[2], Integer.parseInt(choices[3]));
				break;
			}
		} while(!choices[0].equalsIgnoreCase("q"));
		
		reader.close();

		System.out.println("Program Finished");

	}
}
