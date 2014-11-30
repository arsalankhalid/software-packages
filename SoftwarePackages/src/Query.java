import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.text.html.HTMLDocument.Iterator;


public class Query {
	  public static void main(String[] args) throws IOException{
		
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
		int counter = 0 ;
		while(iterator.hasNext()){
			if (goals.contains(iterator.next())){
				counter++;
			}
		}
		*/
		//System.out.println("Found Keys: " + counter);
		
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
						//System.out.println(bins);
						if (bins.contains(bin) ){//&& goals.contains(entry.getKey()) ){
						   //addEdge(entry.getKey());
							counter++;
						   //System.out.println("Adding Edge between **" + goal + "** and ##" + entry.getKey() + "##");
						}
					}
					
				}
			}
		}
		System.out.println("Total Edges added: " + counter);
		
		/*
		 * Print loop for testing purposes
		 *
		for(int i=0; i<capabilities.size(); i++){
			String item;
			item = capabilities.get(i);
			System.out.println(item);
		}
		*/
		
		/*
		 * Iterate through a map for testing purposes
		 *
		for(Map.Entry<String,TreeSet<String>> entry : binarySources.entrySet()) {
			  String key = entry.getKey();
			  TreeSet<String> value = entry.getValue();

			  System.out.println(key + " => " + value);
		}
		*/
		
		
	  }
}
