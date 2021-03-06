package Cleaning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DataCleanser {
	
	public static HashMap<String , Boolean> stopWordsList;
	public static LinkedHashMap<String , Integer> bagOfWords;
	public static HashMap<String, Boolean> nlpTags;
	public static List<String> sentences;
	public static ArrayList<LinkedHashMap<String, Integer>> wordsForSentences;
	
	public static void writeDataToFile(String fileName, List<String> content) {
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(fileName);
			out = new BufferedWriter(fstream);
			for (String str : content) {
				out.write(str);
				out.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				fstream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addPosNegtoBagOfWords()
	{
		List <String>  posSentences = readFromFile("data/positive-words.txt");
		List <String>  negSentences = readFromFile("data/negative-words.txt");
		
		for(String word : posSentences)
		{
			word=word.trim();
			word=word.toLowerCase();
			bagOfWords.put(word, 0);
			
		}
		for(String word : negSentences)
		{
			word=word.trim();
			word=word.toLowerCase();
			bagOfWords.put(word, 0);
			
		}
		
	}
	public static void filesCleanUp() {
		FileWriter fstream = null;
		try {
			fstream = new FileWriter(Constants.TRAININGDATAOUTPUT);
			fstream.write("");
			fstream.close();
			fstream = new FileWriter(Constants.TESTDATAOUTPUT);
			fstream.write("");
			fstream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void appendStringToFile(String fileName, String content) {
		FileWriter fstream = null;
		try {
			fstream = new FileWriter(fileName,true);
			fstream.write(content+"\n");
			fstream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> readFromFile(String fileName) {
		FileInputStream fstream = null;
		List<String> fileRecords = new ArrayList<String>();
		try {
			fstream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		try {
			while ((strLine = br.readLine()) != null) {
				
				if(strLine.length()>2 && !strLine.substring(0, 2).equals("!!"))
				{
					strLine = strLine.trim();
					fileRecords.add(strLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileRecords;
	}
	
	public static List<String> readStopsFromFile(String fileName) {
		FileInputStream fstream = null;
		List<String> fileRecords = new ArrayList<String>();
		try {
			fstream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		try {
			while ((strLine = br.readLine()) != null) {
				
					strLine = strLine.trim();
					fileRecords.add(strLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileRecords;
	}
	
	public static void setStopWords(String filePath)
	{
		stopWordsList = new HashMap<String,Boolean>();
		List<String> wordList = readStopsFromFile(filePath);
		for(String word : wordList)
		{
			word= word.toLowerCase();
			word=word.trim();
			stopWordsList.put(word, true);
		}
	}
	public static LinkedHashMap<String,Integer> getBagOfWords(String filePath)
	{
		sentences = readFromFile(filePath);
		bagOfWords= new LinkedHashMap<String , Integer>();
		wordsForSentences = new ArrayList<LinkedHashMap<String, Integer>>();
		
		
		for(String sentence : sentences)
		{
			LinkedHashMap<String,Integer > forSentence = new LinkedHashMap<String,Integer>();
			String[] words = sentence.split("/|\\s");
			for(String word : words)
			{
				word=word.trim();
				word=word.toLowerCase();
				if(word.contains("//") )
				{
					int pos = word.indexOf("//");
					if(pos>1)
						word=word.substring(0, pos);
					else
						continue;
				}
				if( nlpTags.containsKey(word) || word.matches("\\d+(/|-)\\d+(/|-)\\d+") || word.matches("\\.*\\$\\d+(\\.\\d+)?\\.*|\\.*\\d+(\\.\\d+)?\\.*|\\.*#\\d+(\\.\\d+)?\\.*"))
				{
					continue;
				}
				
				if(bagOfWords.containsKey(word))
				{
					bagOfWords.put(word, 0);
				}
				else
				{
					bagOfWords.put(word, 0);
				}
				
				if(forSentence.containsKey(word))
				{
					int value = forSentence.get(word);
					value++;
					forSentence.put(word, value);
				}
				else
				{
					forSentence.put(word, 1);
				}
				
			}
			wordsForSentences.add(forSentence);
		}
		
		System.out.println("THe number of sentences picked are"+ wordsForSentences.size());
		
		bagOfWords.remove("+");
		bagOfWords.remove("-");
		bagOfWords.remove("=");
		bagOfWords.remove("1");
		bagOfWords.remove("-1");
		bagOfWords.remove("0");
		
		LinkedHashMap<String, Integer> wList = new LinkedHashMap<String, Integer>(bagOfWords);
		return wList;
		
	}
	public static void setTestData(String filePath)
	{
		sentences = readFromFile(filePath);
		
		
		for(String sentence : sentences)
		{
			LinkedHashMap<String,Integer > forSentence = new LinkedHashMap<String,Integer>();
			String[] words = sentence.split("/|\\s");
			int position =0;
			int len =words.length;
			for(String word : words)
			{
				position++;
				if(position == len)
					System.out.println("reach");
				word=word.trim();
				word=word.toLowerCase();
				if(word.contains("//") )
				{
					int pos = word.indexOf("//");
					if(pos>1)
						word=word.substring(0, pos);
					else
						continue;
				}
				if( nlpTags.containsKey(word) || word.matches("\\d+(/|-)\\d+(/|-)\\d+") )
				{
					continue;
				}
				
				if(word.equals("1")|word.equals("-1")|word.equals("0"))
				{
					if(position!=words.length)
						continue;
				}
				
				if(forSentence.containsKey(word))
				{
					int value = forSentence.get(word);
					value++;
					forSentence.put(word, value);
				}
				else
				{
					forSentence.put(word, 1);
				}
				
			}
			wordsForSentences.add(forSentence);
		}
		
		
	}
	
	
	
	public static void setnlpTags()
	{
		String tags="prps nnps nnp pdt pos prp jjr jjs nns vbd vbg vbn vbp rbr rbs sym vbz wdt wps wrb dt ex fw in ls md rb rp wp cd cc jj nn vb to uh";
		String[] arrTags = tags.split(" ");
		nlpTags= new HashMap<String, Boolean>();
		for(String tag : arrTags)
			nlpTags.put(tag, true);
		
	}
	
	public static void createVector(int trainingStart, int trainingEnd , int testStart , int testEnd)
	{
		if((trainingStart>trainingEnd) || (testStart>testEnd) )
			return;
		
		int currLine =0;
		
		
		for(LinkedHashMap<String,Integer> map : wordsForSentences)
		{
			if(currLine>=testEnd)
			{
				System.out.println("FINISHED"+ currLine);
				break;
			}
			
			LinkedHashMap<String, Integer > newMap = new LinkedHashMap<String,Integer>(bagOfWords);
			currLine++;
			int type =-1; 
			for (String key : newMap.keySet()) {
				
				if(map.containsKey(key))
				{
					newMap.put(key,map.get(key));
				}
				else
				{
					newMap.put(key, 0);
				}
				
			}
			
			// MAP : ORIGINAL SENTENCE - TRAINING / TEST 
			// newMap : BAG OF WORDS 
			
			if(currLine<=trainingEnd)
			{
			if(map.containsKey("+") )
			{
				type=1;
			}
			else if(map.containsKey("-") )
			{
				type=2;
			}
			else if(map.containsKey("=") )
			{
				type=4;
			}
			
			}
			else
			{
			
			if( map.containsKey("1"))
			{
				type=1;
			}
			else if( map.containsKey("-1"))
			{
				type=2;
			}
			else if(map.containsKey("0"))
			{
				type=4;
			}
			}
			
			if(type==-1)
				continue;
			
			newMap.put("Class",type);
			int total=newMap.values().toString().length();
			
			if(currLine<=trainingEnd)
			{
				appendStringToFile(Constants.TRAININGDATAOUTPUT,newMap.values().toString().substring(1, total-1));
			}
			else
			{
				appendStringToFile(Constants.TESTDATAOUTPUT,newMap.values().toString().substring(1, total-1));
			}
		}
	}
	
	
	
	public static void main(String args[])
	{
		setStopWords(Constants.STOPWORDS);
		setnlpTags();
		
		filesCleanUp();
		writeDataToFile(Constants.BAGOFWORDSOUTPUT, new ArrayList<String>(Arrays.asList(getBagOfWords(Constants.DATAINPUT).keySet().toArray(new String[100] ))));
		
		int train = wordsForSentences.size();
		setTestData(Constants.TESTINPUT);
		System.out.println("STARTING vector generation");
		System.out.println("The number of words are: "+bagOfWords.size() + bagOfWords.get("+") + bagOfWords.get("-") + bagOfWords.get("="));
		createVector(0, train, 0, 19750);
		
	}

}
