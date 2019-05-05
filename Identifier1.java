import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
/* I used the porter stemmer from the following website: https://tartarus.org/martin/PorterStemmer/java.txt  */
/* I expanded on code I had written for CS 5390 last semester for a stemmer. 
 * @Author Angel U. Ortega
 */

public class Identifier1 {
	
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
	}
	
	private static String[] tokensToLowerCase (String [] arr){
		String[] retval = new String[arr.length];
		int index = 0;
		while(index < retval.length) {
			retval[index] = arr[index].toLowerCase();
			index++;
		}
		return retval;
	}
	
	private static String[] identifyStopWordsandArticles(String[] tokens, String [] stopwords, String [] articles) {
		
		String [] retval = new String [tokens.length];
		
		for(int i = 0; i < retval.length; i++) {
			retval[i] = "stem";
			for(int j = 0; j < stopwords.length; j++) {
				if (tokens[i].equals(stopwords[j])) retval[i] = "-pronoun";
			}
			for(int k = 0; k < articles.length; k++) {
				if (tokens[i].equals(articles[k])) retval[i] = "-article";
			}
		}
		return retval;
	}
	
	private static String[] stemTokens(String [] tokens, String [] identifiers) {
		
		Stemmer s = new Stemmer();// retrieved from: https://tartarus.org/martin/PorterStemmer/java.txt

		String test = "";
		String result = "";
		for (int i = 0; i < tokens.length; i++) {
			if (identifiers[i].equals("stem")) {
				test = tokens[i];
				char [] chararr = test.toCharArray();
				s.add(chararr, chararr.length);
				s.stem();
				result = s.toString();
				identifiers[i] = "-" + result;
			}
		}
		return identifiers;
	}
	
	private static void saveToFile(String[] tokens, String[] classifiers) throws IOException{
		
	    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Angel U. Ortega\\Desktop\\output.txt"));
	    
	    for (int i = 0; i < tokens.length; i++) {
	    	String str = tokens[i] + "\t\t" + classifiers[i];
	    	writer.write(str);
	    	writer.newLine();
	    }
	    writer.close();
	}
	
private static void saveToFile(String[] arr, String filename) throws IOException{
		
	    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Angel U. Ortega\\Desktop\\" + filename));
	    
	    for (int i = 0; i < arr.length; i++) {
	    	String str = arr[i];
	    	writer.write(str);
	    	writer.newLine();
	    }
	    writer.close();
	}
	
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
	}
	
	public static Hashtable<String, String> buildFirstSet (String[] pronouns, String [] articles, String[] verbs, String [] quantifiers, String [] conjunctions, String [] adjectives) {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		for (int i = 0 ; i < pronouns.length; i++) {
			hashtable.put(pronouns[i], "pronoun");
		}
		for (int i = 0 ; i < articles.length; i++) {
			hashtable.put(articles[i], "article");
		}
		for (int i = 0 ; i < verbs.length; i++) {
			hashtable.put(verbs[i], "verb");
		}
		for (int i = 0 ; i < quantifiers.length; i++) {
			hashtable.put(quantifiers[i], "quantifier");
		}
		for (int i = 0 ; i < conjunctions.length; i++) {
			hashtable.put(conjunctions[i], "conjunction");
		}
		for (int i = 0 ; i < adjectives.length; i++) {
			hashtable.put(adjectives[i], "adjective");
		}
		
		return hashtable;
	}
	
	public static String[] getfirstlevel(String[] tokens, Hashtable <String, String> hash) {
		String[] retval = new String [tokens.length];
		
		for (int i = 0; i < tokens.length; i++) {
			retval[i] = hash.get(tokens[i]);
		}
		return retval;
	}
	public static String[] generateNumbers () {
		Hashtable <Integer, String> map= new Hashtable<Integer, String>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");
        map.put(5, "five");
        map.put(6, "six");
        map.put(7, "seven");
        map.put(8, "eight");
        map.put(9, "nine");
		
		String [] retval = new String [79];
		retval[0]="twenty";
		retval[10]="thirty";
		retval[20]="fourty";
		retval[30]="fifty";
		retval[40]="sixty";
		retval[50]="seventy";
		retval[60]="eighty";
		retval[70]="ninety";
		String decimal = "";
		for (int i = 0; i< retval.length; i++) {
			if (i%10 == 0) decimal=retval[i];
			else {
				retval[i]=decimal+"-"+map.get(i%10);
			}
		}
		return retval;
	}


	public static void main(String[] args) {
		
		String input = "";
		String pronoun = "";
		String article = "";
		String verb = "";
		String quantifier = "";
		String conjunction = "";
		String adjective = "";
		
		
		try {
			input = fileToString( "C:\\Users\\Angel U. Ortega\\Desktop\\input.txt", StandardCharsets.UTF_8);
			System.out.println("input opened");
			pronoun = fileToString( "C:\\Users\\Angel U. Ortega\\Desktop\\pronoun.txt", StandardCharsets.UTF_8);
			System.out.println("stop_word opened");
			article = fileToString( "C:\\Users\\Angel U. Ortega\\Desktop\\article.txt", StandardCharsets.UTF_8);
			System.out.println("article opened");
			verb = fileToString( "C:\\Users\\Angel U. Ortega\\Desktop\\verb.txt", StandardCharsets.UTF_8);
			System.out.println("verb opened");
			quantifier = fileToString( "C:\\Users\\Angel U. Ortega\\Desktop\\quantifier.txt", StandardCharsets.UTF_8);
			System.out.println("quantifier opened");
			conjunction = fileToString( "C:\\Users\\Angel U. Ortega\\Desktop\\conjunction.txt", StandardCharsets.UTF_8);
			System.out.println("conjunction opened");
			adjective = fileToString( "C:\\Users\\Angel U. Ortega\\Desktop\\adjective.txt", StandardCharsets.UTF_8);
			System.out.println("adjective opened");
		} catch (Exception e){
			System.out.println("Unable to open file.");
		}
		input = expandAbreviations(input);
		String [] numbers = generateNumbers();
		String [] tokens = input.split("\\W+");
		String [] pronouns = pronoun.split("\\W+");
		String [] articles = article.split("\\W+");
		String [] verbs = verb.split("\\W+");
		String [] quantifiers = quantifier.split("\\W+");
		String [] conjunctions = conjunction.split("\\W+");
		String [] adjectives = adjective.split("\\W+");
		tokens = tokensToLowerCase(tokens);
		
		//will create hash table with pronouns, articles, and verbs to start the matching.
		Hashtable <String, String> firstset = buildFirstSet(pronouns, articles, verbs, quantifiers, conjunctions, adjectives);
		String [] firstlevel = getfirstlevel(tokens, firstset);
		
		//String [] identifiers = identifyStopWordsandArticles(lowercasetokens, stopwords, articles);
		//TODO: Identify verbs, adjectives, adverbs, objects, proper names, and numbers
		//identifiers = stemTokens(lowercasetokens, identifiers);
		try {
			saveToFile(numbers, "numbers.txt");
			saveToFile(tokens, firstlevel);
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
