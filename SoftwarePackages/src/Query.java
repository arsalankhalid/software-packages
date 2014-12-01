/*
 * Query class deals with Linux packages and its dependencies
 * Written By: Pavlo Kuzhel
 * For: Lew Baxter
 * Course: BTP500
 * Date: Nov 30, 2014
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Iterator;


public class Query {
	/*
	 * matches method: finds all occurrences of a pattern 'string' inside a TreeSet set
	 * parameters: TreeSet set - the set in which to search for the pattern
	 * 			   String string - the pattern for which to search
	 * returns: a list of type ArrayList<String> with strings that matched the pattern
	 */
	public static ArrayList<String> matches(TreeSet<String> set, String string){
		Iterator<String> iterator;
		ArrayList<String> list = new ArrayList<String>();
		iterator = set.iterator();

		// iterate through the 'set'
		// and add the entries that match the pattern 'string' to the 'list'
		while(iterator.hasNext()){
			String item = iterator.next();
			if (item.contains(string)){
				list.add(item);
			}
		}
		return list;
	}

	/*
	 * main method: analyzes which packages are dependent on each other
	 * by checking their capabilities, finding their binaries and 
	 * associating them with a source goal that creates the binary
	 * therefore, creating dependencies from one source goal to another
	 */
	public static void main(String[] args){

		// initialize a new graph object
		Graph graph = new Graph();

		// initialize CSV urls that will be required in order to populate all the data
		String goalsCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_goal.csv",
				depCapCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_cap.csv",
				capBinCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_rpm.csv",
				binDepCSV = "http://england.proximity.on.ca/chris/outbound/lb/lb_dep_srpm.csv";

		// initialize all data structures which will be used in the main method
		TreeSet<String> goals = new TreeSet<String>();
		TreeMap<String,String> capabilityBinaries = new TreeMap<String,String>();
		TreeMap<String, TreeSet<String>> sourceBinaries = new TreeMap<String, TreeSet<String>>();
		ArrayList<String> sources = new ArrayList<String>(),
				capabilities = new ArrayList<String>(),
				binary = new ArrayList<String>(),
				binSources = new ArrayList<String>();

		// initialize the CSVReader class which is responsible for parsing the CSV files
		CSVReader readerCsv = new CSVReader();

		// begin user output
		System.out.println("--- Software packages and their dependencies ---");
		System.out.println("Initializing... \nThe required files are being loaded, please wait...");

		// load the corresponding values from the provided CSV files
		try {
			// load all the 'goal sources' from the goal CSV file
			goals = readerCsv.getTreeSet(goalsCSV, 0);

			// load all the sources and their associated capabilities (many:many relationship)
			sources = readerCsv.getArrayList(depCapCSV, 0);
			capabilities = readerCsv.getArrayList(depCapCSV, 1);

			// load all the binaries and the sources that they are created by
			binary = readerCsv.getArrayList(binDepCSV, 0);
			binSources = readerCsv.getArrayList(binDepCSV, 1);

			// load all the capabilities and their associated binaries (1:1 relationship)
			capabilityBinaries = readerCsv.getMap(capBinCSV, 0, 1);

		} catch (IOException e) {
			// catch any IOExceptions, which most likely mean that there is no internet
			// accesss and display the error message to the user
			System.out.println("Seems like you are not connected to the internet.");
			System.out.println("Please connect to the internet and re-start the program.");
		}

		// extract all binaries that are true for a source and put them into a Map of TreeSet objects
		// from the 'created' table - a list of binaries that are created by sources (many:1) relationship
		for(int i=0; i<binSources.size(); i++){

			// for each source (key), add the binaries that the source creates as a TreeSet
			TreeSet<String> binarySet = new TreeSet<String>();

			for(int j=i; j<binSources.size(); j++){
				if (binSources.get(i).equals(binSources.get(j))){
					// add the binary(string) to the TreeSet
					binarySet.add(binary.get(j));
				}
			}

			// add the TreeSet to the Map
			sourceBinaries.put(binSources.get(i), binarySet);
		}

		// get the iterator from goal sources
		Iterator<String> iterator;
		iterator = goals.iterator();

		// initialize a counter which will be used for user feedback
		int counter = 0;

		// inform the user what is happening next
		System.out.print("Adding package dependencies");

		/*
		 * begin iterating through all goal sources in order to
		 * find all capabilities that are required for a particular goal source
		 * and for each capability, find its associated binary
		 * iterate through sources that create binaries and verify that
		 * the source is a goal source and that it contains the required binary
		 */
		while(iterator.hasNext()){
			// print user feedback every thousand iterations
			// so the user knows that the program is still running
			if(counter%1000 == 0){
				System.out.print(".");
			}

			// temporarily store the goal source for easier access
			String goal = iterator.next();

			for(int i=0; i<sources.size(); i++){
				// for each source that is equal to the goal get its associated binary
				if (sources.get(i).equals(goal)){
					String capab = capabilities.get(i);
					String bin = capabilityBinaries.get(capab);

					// for each goal source that creates a binary with the associated capability
					// that is required for the original source, add a graph edge
					for (Map.Entry<String,TreeSet<String>> entry : sourceBinaries.entrySet()) {
						TreeSet<String> bins = entry.getValue();
						String key = entry.getKey();
						if (bins.contains(bin) && goals.contains(key) ){
							graph.addEdge(goal, key);
						}
					} // end inner for loop
				}
			} // end outer for loop
			counter++;
		} // end while loop

		System.out.print("\n"); // add an extra line for formatting purposes

		// start a Scanner resource to capture user input
		Scanner reader = new Scanner( System.in );
		String line = "";
		String[] choices = null;

		// start a do/while loop for interaction with the menu
		do {
			// output the menu and its options
			System.out.println("\n--------------------------------- Menu --------------------------------");
			System.out.println("-Type 'g pattern' to display all goals that match the pattern 'pattern'");
			System.out.println("-Type 's dependencies' to display the number of dependencies between all packages");
			System.out.println("  or 's dependents' to display the number of dependant packages");
			System.out.println("-Type 'd pattern1 pattern2' to display all packages that match the pattern 'pattern1'"
					+ "\n    and that are depended on a package that matches pattern 'pattern2'");
			System.out.println("-Type 'p pattern1 pattern2 integer' to display a dependency path from a package that "
					+ "\n    matches the pattern 'pattern1', to a package that matches pattern 'pattern2', with a "
					+ "\n length integer");
			System.out.println("-Type 'q' to quit");
			System.out.print("*Please type in your command: ");

			// get the line of user input
			line = reader.nextLine();

			// separate user input by spaces to figure out the command which the user entered
			choices = line.split(" ");

			// switch cases responsible for the behaviour of the 'menu'
			switch(choices[0]){
			// user wants to find a goal that matches his pattern
			case "g":
				// call the matches method to search for a goal which contains the specified pattern
				ArrayList<String> matches = matches(goals, choices[1]);

				// if the size of matches is zero, none was found
				if(matches.size() == 0){
					System.out.println("Sorry no goals matching the pattern " + choices[1] + "were found.");
				} else {
					// iterate through the found source goals and output nicely to the user
					for(int i=0; i<matches.size(); i++){
						System.out.println("* " + matches.get(i));
					}
				}
				break;
				// user wants to know how many relationships between packages exist
				// or how many packages with relationships exist
				// ie. how many edges/vertices does the graph have
			case "s":
				// for relationships between packages
				if(choices[1].equals("dependencies")){
					System.out.println("The number of dependencies is: " + graph.getNumberOfEdges());
					// for packages with relationships
				}else{
					System.out.println("The number of dependent packages is: " + graph.getNumberOfVertices());
				}
				break;
				// the user wants to find if a relationship between two packages exists,
				// to see if package1 which contains pattern1 depends on package2 which contains pattern2
			case "d":
				// display relationship if exists
				graph.printEdges(choices[1], choices[2]);
				break;
				// user wants to find a path of a specified length from a package that contains a
				// pattern1 to a package that contains a pattern2
			case "p":
				graph.printMatchingPath(choices[1], choices[2], Integer.parseInt(choices[3]));
				break;
			}
		} while(!choices[0].equalsIgnoreCase("q")); // loop terminates if the user enters 'q' or 'Q'

		// close the reader resource - we are not expecting user input anymore
		reader.close();

		// final user output before terminating
		System.out.println("\nProgram Finished");

	} // end main method
} // end Query class