/*
 * CSVReader class parses a CSV file downloaded from the internet
 * Code taken from: http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 * and modified by Pavlo Kuzhel for BTP500 Assignment 3
 * Date: Nov 26, 2014
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class CSVReader {
	private String csvSplitBy;
	
	/*
	 * CSVReader constructor
	 * initializes the default character to split lines by
	 * In this case: a comma - ","
	 */
	public CSVReader(){
		this.csvSplitBy = ",";
	} // end constructor
	
	/*
	 * getArrayList method
	 * parameters: String url - url link to the CSV file
	 * 			   int index  - the field index of the desired ArrayList
	 * returns an ArrayList of Strings from the specified index of the CSV file
	 */
	public ArrayList<String> getArrayList(String url, int index) throws IOException {
		URL stockURL = new URL(url);

		BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

		String line = "";
		ArrayList<String> list = new ArrayList<String>();

		// iterate through the file line by line to add each value found at the 
		// specified index to the ArrayList list
		while ((line = br.readLine()) != null) {
			String[] string = line.split(this.csvSplitBy);
			list.add(string[index]);
		}

		// close BufferedReader resource
		br.close();

		return list;
	} // end getArrayList method
	
	/*
	 * getMap method
	 * parameters: String url - url link to the CSV file
	 * 			   int index1 - the field index for the 'keys' of the map
	 * 			   int index2 - the field index for the 'values' of the map
	 * returns a TreeMap type of <String, String> key, value pairs at the specified indices
	 */
	public TreeMap<String,String> getMap(String url, int index1, int index2) throws IOException {
		URL stockURL = new URL(url);

		BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

		String line = "";
		TreeMap<String,String> map = new TreeMap<String,String>();

		// iterate through the file line by line to add each value found at the 
		// specified indices to the TreeMap map
		while ((line = br.readLine()) != null) {
			String[] string = line.split(this.csvSplitBy);
			map.put(string[index1], string[index2]);
		}

		// close BufferedReader resource
		br.close();

		return map;
	} // end getMap method

	/*
	 * getTreeSet method
	 * parameters: String url - url link to the CSV file
	 * 			   int index - the field index for the 'keys' of the map
	 * returns a TreeSet type of String at the specified index
	 */
	public TreeSet<String> getTreeSet(String url, int index) throws IOException{
		URL stockURL = new URL(url);

		BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

		String line = "";
		TreeSet<String> set = new TreeSet<String>();

		// iterate through the file line by line to add each value found at the 
		// specified index to the TreeSet set
		while ((line = br.readLine()) != null) {
			String[] string = line.split(this.csvSplitBy);
			set.add(string[index]);
		}
		
		// close BufferedReader resource
		br.close();

		return set;
	} // end getTreeSet method
	
} // end CSVReader class