package Cleaning;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataCleanser {
	
	public static HashMap<String , Boolean> stopWordsList;
	public static HashMap<String , Integer> bagOfWords;
	
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
	public static HashMap<String,Integer> getBagOfWords(String filePath)
	{
		List<String> sentences = readFromFile(filePath);
		
		bagOfWords= new HashMap<String , Integer>();		
		for(String sentence : sentences)
		{
			String[] words = sentence.split(" ");
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
				else if(word.contains("/") )
				{
					int pos = word.indexOf("/");
					if(pos>1)
						word= word.substring(0, pos);
					else
						continue;
				} 
				if(stopWordsList.containsKey(word))
				{
					continue;
				}
				if(bagOfWords.containsKey(word))
				{
					int count = bagOfWords.get(word);
					bagOfWords.put(word, ++count);
				}
				else
				{
					bagOfWords.put(word, 1);
				}
				
			}
		}
		
		HashMap<String, Integer> wList = new HashMap<String, Integer>(bagOfWords);
		return wList;
		
	}
	
	public static void main(String args[])
	{
		setStopWords(Constants.STOPWORDS);
		getBagOfWords(Constants.DATAINPUT);
		
	}

}
