/*
 * CSVReader class parses a CSV file downloaded from the internet
 * Code taken from: http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 * and modified by Pavlo Kuzhel for BTP500 Assignment 3
 * Date: Nov 26, 2014
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
 
public class CSVReader {

  public ArrayList<String> getArrayList(String url, int field) throws IOException {
 
	URL stockURL = new URL(url);
	
	BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

	//String csvFile = "/Users/mkyong/Downloads/GeoIPCountryWhois.csv";
	//BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
	ArrayList<String> list = new ArrayList<String>();

 /*
	try {
 
		Map<String, String> maps = new HashMap<String, String>();
 
		//br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
 
			// use comma as separator
			String[] country = line.split(cvsSplitBy);
 
			maps.put(country[0], country[1]);
 
		}
 
		//loop map
		for (Map.Entry<String, String> entry : maps.entrySet()) {
 
			System.out.println("Country [code= " + entry.getKey() + " , name="
				+ entry.getValue() + "]");
 
		}
 */
	try{
		
		while ((line = br.readLine()) != null) {
			 
			// use comma as separator
			String[] string = line.split(cvsSplitBy);
 
			list.add(string[field]);
 
		}
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	System.out.println("Done");
	return list;
  }
  public TreeMap<String,String> getMap(String url) throws IOException {
	  
		URL stockURL = new URL(url);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

		//String csvFile = "/Users/mkyong/Downloads/GeoIPCountryWhois.csv";
		//BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		TreeMap<String,String> map = new TreeMap<String,String>();
		
		while ((line = br.readLine()) != null) {
			 
			// use comma as separator
			String[] string = line.split(cvsSplitBy);
 
			map.put(string[0], string[1]);
 
		}
		return map;
  }
  
  public TreeMap<String,String> getMapSet(String url) throws IOException {
	  
		URL stockURL = new URL(url);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

		String line = "";
		String cvsSplitBy = ",";
		TreeMap<String,String> map = new TreeMap<String,String>();
		TreeSet<String> set = new TreeSet<String>();
		//Arra
		
		while ((line = br.readLine()) != null) {
			 
			// use comma as separator
			String[] string = line.split(cvsSplitBy);
 
			map.put(string[0], string[1]);
 
		}
		return map;
  }
  
  
}