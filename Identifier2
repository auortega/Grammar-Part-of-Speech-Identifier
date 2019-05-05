import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Set;

import org.w3c.dom.Element;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Node;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Identifier2 {

	private static String fileToString(String path, Charset encoding) throws IOException {
		  byte[] encoded = Files.readAllBytes(Paths.get(path));
		  return new String(encoded, encoding);
	}//end method
	
	private static void printStringArray(String [] arr) {
		int index = 0;
		while(index < arr.length) {
			System.out.println(arr[index]);
			index++;
		}
	}//end method
	
	private static void saveToFile(String[] tokens, String[] classifiers) throws IOException{
		
	    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Angel U. Ortega\\Desktop\\output.txt"));
	    
	    for (int i = 0; i < tokens.length; i++) {
	    	String str = tokens[i] + "\t\t" + classifiers[i];
	    	writer.write(str);
	    	writer.newLine();
	    }
	    writer.close();
	}//end method
	
	private static String[] tokensToLowerCase (String [] arr){
		String[] retval = new String[arr.length];
		int index = 0;
		while(index < retval.length) {
			retval[index] = arr[index].toLowerCase();
			index++;
		}
		return retval;
	}
	
	private static void saveToFile(String[] arr, String filename) throws IOException{
		
	    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Angel U. Ortega\\Desktop\\" + filename));
	    
	    for (int i = 0; i < arr.length; i++) {
	    	String str = arr[i];
	    	writer.write(str);
	    	writer.newLine();
	    }
	    writer.close();
	}//end method
	
	private static String expandAbreviations(String file) {
		
		for(int i = 0; i< file.length()-3; i++) {
			boolean check = false;
			if (file.charAt(i)=='\'') {
				String expansion = "";
				if(file.charAt(i+1)=='r' && file.charAt(i+2)=='e') {
					check = true;
					expansion = " are";
				}
				else if(file.charAt(i+1)=='v' && file.charAt(i+2)=='e') {
					check = true;
					expansion = " have";
				}
				else if(file.charAt(i+1)=='l' && file.charAt(i+2)=='l') {
					check = true;
					expansion = " will";
				}
				if (check) {
					String temp = file.substring(0, i) + expansion + file.substring(i+3);
					file = temp;
				}
			}
		}
		return file;
		//System.out.println(file);
	}//end method
	
	public static Hashtable <String, String> getHash(String[] words){
		//The structure of the line in file is:
		//	word-ident1,ident2,ident3
		Hashtable <String, String> hash = new Hashtable <String, String>();
		for(int i = 0; i<words.length; i++) {
			//get line, split, and add to hash
			String temp =words[i];
			System.out.println(temp);
			String[] word = temp.split("-");
			if (word.length>1) {
				hash.put(word[0], word[1]);
			}
		}
		System.out.println("hash: ");
		System.out.println(hash.toString());
		return hash;
	}
	
	public static String getIdentityAPI(String word) {
		String retval = "";
		String head = new String("https://dictionaryapi.com/api/v1/references/sd4/xml/");
        String apiKey = new String("?key=9de030e2-2ecf-4c79-a149-6ce0a7d40dea"); //My API Key for Merriam webster
        String finalURL = head.trim() + word.trim()+ apiKey.trim();
        try
        {
        	URL obj = new URL(finalURL);
        	 HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        	 int responseCode = con.getResponseCode();
        	 //System.out.println("Response Code : " + responseCode);
        	  BufferedReader in = new BufferedReader(
        		 new InputStreamReader(con.getInputStream()));
        		 String inputLine;
        		 StringBuffer response = new StringBuffer();
        		 while ((inputLine = in.readLine()) != null) {
        		   response.append(inputLine);
        		 }
        		in.close();
        		//print in String
        		//System.out.println(response.toString());
        		String []identities = response.toString().split("<fl>");
        		String[]identity = identities[1].split("</fl>");
        		retval = identity[0];
        		//printStringArray(identities);
        		//printStringArray(identity);
        }
        catch (Exception e) {
        	
        }
        return retval;
	}
	
	public static void saveHash (Hashtable <String, String> hash) {
		Set<String> keys = hash.keySet();
		String[] wordpairs = new String[keys.size()];
		int i = 0;
        for(String key: keys){
        	wordpairs[i]=key+"-"+hash.get(key);
        	i++;
            //System.out.println("Value of "+key+" is: "+hash.get(key));
        }
        try{
        	saveToFile(wordpairs, "identifiedwords.txt");
        }catch(Exception e){
        	System.out.println("Could not update identifiedwords file.");
        }
	}
	
	public static String[] identifylines(String[] lines, Hashtable <String, String> hash) {
		String[] retval = new String[lines.length*2];
		System.out.println("lines.length: "+lines.length);
		int index=0;
		for (int l = 0; l < lines.length; l++) {
			//reading each line
			String [] line = lines[l].split("\\W+");
			String identitiesinline = "";
			for (int w = 0; w < line.length; w++) {
				//reading each word
				if (hash.containsKey(line[w])) {//current word has been previously identified
					identitiesinline+=hash.get(line[w])+"|";
				} else {
					//TODO: API call to get identity of word and add to hash
					String identity = getIdentityAPI(line[w]);
					if (identity=="") identity="undef";
					hash.put(line[w], identity);
					identitiesinline+=hash.get(line[w])+"|";
				}
			}
			System.out.println("identitiesinline"+l+": "+identitiesinline);
			retval[l*2]=lines[l];
			retval[l*2+1]=identitiesinline;
		}
		
		//TODO Save hash to file for future use
		saveHash(hash);
		return retval;
	}
	
	
	
	public static void main(String[] args) {
		
		String inputfile = "";
		String identifiedwordsfile = "";
		
		
		try {
			inputfile = fileToString( "C:\\Users\\Angel U. Ortega\\Desktop\\inputfile.txt", StandardCharsets.UTF_8);
			System.out.println("inputfile opened");
			identifiedwordsfile = fileToString( "C:\\Users\\Angel U. Ortega\\Desktop\\identifiedwords.txt", StandardCharsets.UTF_8);
			System.out.println("identifiedwordsfile opened");
			
		} catch (Exception e){
			System.out.println("Unable to open file.");
		}
		inputfile = expandAbreviations(inputfile);
		String [] lines = inputfile.split("(?<=[a-z])\\.\\s+");
		//System.out.println("lines: ");
		//printStringArray(lines);
		lines = tokensToLowerCase(lines);
		//System.out.println("lines: ");
		//printStringArray(lines);
		String [] identifiedwords = identifiedwordsfile.split("\\r?\\n");
		Hashtable <String, String> identities = getHash(identifiedwords);
		
		String [] identityLines = identifylines(lines, identities);
		//tokens = tokensToLowerCase(tokens);
		
		
		try {
			saveToFile(identityLines, "identityLines.txt");
			//saveToFile(tokens, firstlevel);
			System.out.println("Saved file successfully.");
		} catch (Exception e) {
			System.out.println("Unable to write file.");
		}
		
		
//		System.out.println("\nlowercasetokens:");
//		printStringArray(lowercasetokens);
//		System.out.println("\nstopwords:");
//		printStringArray(stopwords);
//		System.out.println("\nidentifiers:");
//		printStringArray(identifiers);

		
	}

	
}
