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

  public ArrayList<String> getArrayList(String url, int field) throws IOException {
	URL stockURL = new URL(url);
	
	BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

	String line = "";
	String cvsSplitBy = ",";
	ArrayList<String> list = new ArrayList<String>();

	while ((line = br.readLine()) != null) {
		// use comma as separator
		String[] string = line.split(cvsSplitBy);
		list.add(string[field]);
	}

	br.close();
	
	return list;
  }
  
  public TreeMap<String,String> getMap(String url) throws IOException {
	URL stockURL = new URL(url);
	
	BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

	String line = "";
	String cvsSplitBy = ",";
	TreeMap<String,String> map = new TreeMap<String,String>();
	
	while ((line = br.readLine()) != null) {
		// use comma as separator
		String[] string = line.split(cvsSplitBy);
		map.put(string[0], string[1]);
 	}

	br.close();

	return map;
  }
  
  public TreeMap<String,String> getMapSet(String url) throws IOException {
	  
	URL stockURL = new URL(url);
	
	BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

	String line = "";
	String cvsSplitBy = ",";
	TreeMap<String,String> map = new TreeMap<String,String>();
	
	while ((line = br.readLine()) != null) {
		String[] string = line.split(cvsSplitBy);
 		map.put(string[0], string[1]);
 	}
	
	br.close();

	return map;
  }
  
  public TreeSet<String> getTreeSet(String url, int index) throws IOException{
  	URL stockURL = new URL(url);
	
	BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

	String line = "";
	String cvsSplitBy = ",";
	TreeSet<String> set = new TreeSet<String>();

	while ((line = br.readLine()) != null) {
		// use comma as separator
		String[] string = line.split(cvsSplitBy);
		set.add(string[index]);
	}

	br.close();

	return set;
  }
}