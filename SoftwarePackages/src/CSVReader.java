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
import java.util.HashMap;
import java.util.Map;
 
public class CSVReader {
 
  public static void main(String[] args) {
 
	CSVReader obj = new CSVReader();
	
	try {
		obj.run();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 
  }
 
  public void run() throws IOException {
 
	URL stockURL = new URL("http://england.proximity.on.ca/chris/outbound/lb/lb_dep_rpm.csv");
	
	BufferedReader br = new BufferedReader(new InputStreamReader(stockURL.openStream()));

	//String csvFile = "/Users/mkyong/Downloads/GeoIPCountryWhois.csv";
	//BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
 
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
  }
 
}