import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Solution {
//continut fragment fisier 
public String str;
//nume fisier
public String filename;
//nr total de cuvinte
public int totalwords;
//wordmap cuvinte

public  volatile HashMap<String, Integer> wordmap;

//lista frecventa cuvant pt cele NC cuvinte 
public  ArrayList<Data> arraylistmap;


public Solution(String filename, String str)
{
	this.filename=filename;

	this.str=str;
	this.wordmap=new HashMap<String, Integer>();
	this.arraylistmap=new ArrayList<Data>();
}
public Solution(String filename, String str, HashMap<String,Integer> wordmap)
{
	this.filename=filename;
	this.str=str;

	this.wordmap=wordmap;
	this.arraylistmap=new ArrayList<Data>();
}
public Solution(String filename, String str, ArrayList<Data> arrlist, int totalwords)
{
	this.filename=filename;

	this.str=str;
	this.arraylistmap=arrlist;
	this.wordmap=new HashMap<String, Integer>();
	this.totalwords=totalwords;
	
}



	
}
