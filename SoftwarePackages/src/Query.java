import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;


public class Query {
	  public static void main(String[] args) throws IOException{
		
		String goalsCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_goal.csv",
				depCapCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_cap.csv",
				capBinCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_rpm.csv",
				binDepCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_srpm.csv"
				;
		ArrayList<String> goals = new ArrayList<String>();
		TreeMap<String,String> capabilityBinaries = new TreeMap<String,String>();
		ArrayList<String> sources = new ArrayList<String>();
		ArrayList<String> capabilities = new ArrayList<String>();
		
		ArrayList<String> binary = new ArrayList<String>();
		ArrayList<String> binSources = new ArrayList<String>();
		
		TreeMap<String, TreeSet<String>> binarySources = new TreeMap<String, TreeSet<String>>();
		
		CSVReader reader = new CSVReader();
		
		// 
		goals = reader.getArrayList(goalsCSV, 0);
		sources = reader.getArrayList(depCapCSV, 0);
		capabilities = reader.getArrayList(depCapCSV, 0);

		binary = reader.getArrayList(binDepCSV, 0);
		binSources = reader.getArrayList(binDepCSV, 1);
		
		// extract all binary that are true for a source and put them in a tree set
		for(int i=0; i<binSources.size(); i++){
			TreeSet<String> binarySet = new TreeSet<String>();
			
			for(int j=i; j<binSources.size(); j++){
				if (binSources.get(i).equals(binSources.get(j))){
					binarySet.add(binary.get(j));
				}
			}
			
			binarySources.put(binSources.get(i), binarySet);
		}
		
		//
		capabilityBinaries = reader.getMap(capBinCSV);
		
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
