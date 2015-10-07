//Dumitrescu Evelina 331CA Tema2 APD

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Comp implements Comparator<Data>
{

	@Override
	public int compare(Data o1, Data o2) {
		Integer occ1 = new Integer(o1.occurence);
		Integer occ2 = new Integer(o2.occurence);
		return -occ1.compareTo(occ2);
	}
	
}
public class Main {

	/**
	 * @param args
	 */
	
	public static WorkPool workpool ;
	

	public static CyclicBarrier cb;
	public static CyclicBarrier cb2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//citire si prelucrare date
		FileInputStream fis;
		DataInputStream dis;		
		BufferedReader br ;
		int NC = 0;
		int N=0;
		int D = 0;
		int X = 0;
		int ND=0;
		String [] doc = null;
		String [] words = null;
		RandomAccessFile [] doc_files;
		PrintWriter out=null;
		final int NTHREADS;
		NTHREADS=Integer.parseInt(args[0]);
		
		try {
			out=new PrintWriter(new BufferedWriter(new FileWriter(args[2])));			
			//initializare map workpool + reduce workpool
		
		
		
			fis = new FileInputStream(args[1]) ;
			dis=new DataInputStream(fis);
			br = new BufferedReader( new InputStreamReader(dis));
			String line;
			line = br.readLine();
			//numar de cuvinte
			NC=Integer.parseInt(line);
	
			words=new String[NC+1];
			
			line = br.readLine();
			//cuvinte
			words=line.split("\\s");
			
			line = br.readLine();
			
			//dimensiune chunk citire
			D=Integer.parseInt(line);
			
			line = br.readLine();
			
			//cele mai frecvente N cuvinte
			N=Integer.parseInt(line);
			line = br.readLine();
			
			//cele mai relevante X documente
			X=Integer.parseInt(line);
			line = br.readLine();
			
			//numar documente
			ND=Integer.parseInt(line);
			workpool = new WorkPool(NTHREADS);
			doc=new String[ND];
		
			for(int i=0;i<ND;i++)
			{
				line = br.readLine();
				doc[i]=line;
			
				Solution rs2 = new Solution(doc[i],"");
				
			
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc_files= new RandomAccessFile[ND+1];
		int offset=0;
		String buffer;
		int numbytes;
		String strprefix;
		byte[] temp;
		String strtemp ;
		int begin=0;
		for(int i=0;i<ND;i++)
		{
			try {
				
				doc_files[i] = new RandomAccessFile(doc[i], "r");
			
				MapSolution mapsolution;
				//citire chunkuri + creeare taskuri pt map
				offset=0;
				strprefix="";
				LinkedList<MapChunk> ll=new LinkedList<MapChunk>();
				
				while(offset<doc_files[i].length())
				{
					buffer="";
					begin=offset;
					temp = new byte[D];
					numbytes=doc_files[i].read(temp);
					
					offset+=numbytes;
					byte b=temp[numbytes-1];
					byte [] unit = new byte[1];
					buffer = new String(temp,0,numbytes);
				
					//daca  ultimul caracter citit  e litera se citeste pana 
					//cand se ajunge la un caracter diferite de a-z sau A-Z
					if((b>='a' && b<='z') || (b>='A'&& b<='Z'))
					{
					
						numbytes=doc_files[i].read(unit);
						offset+=1;
						b=unit[0];
						while(((b>='a' && b<='z' ) || (b>='A' && b<='Z')))
						{
							buffer+=new String(unit);		
							
							if(offset == doc_files[i].length()) break;
							unit = new byte[1];
							numbytes=doc_files[i].read(unit);
							b=unit[0];
							offset+=1;
							
						}
						
					}
					buffer=buffer.toLowerCase();
				
					
					Solution mapsol = new Solution(doc[i],buffer);
				
					ll.add(new MapChunk(buffer));

				
				}
				mapsolution= new MapSolution(doc[i], ll);
				workpool.putMapWork(mapsolution);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

	cb = new CyclicBarrier(NTHREADS+1 );
	//map stage workers
	Worker []workers = new Worker[NTHREADS];
	for(int i=0;i<NTHREADS;i++)
	{
	workers[i]=new Worker( cb,workpool,1,words);
	workers[i].setName(Integer.toString(i));
	workers[i].start();
	}
	
	try {
		cb.await();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch bloc
		e.printStackTrace();
		return;
	} catch (BrokenBarrierException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return;
	}
	
	//first stage reduce workers
	workers = new Worker[NTHREADS];
	for(int i=0;i<NTHREADS;i++)
	{
	workers[i]=new Worker( cb,workpool,2,words);
	workers[i].setName(Integer.toString(i));
	workers[i].start();
	}
	
	try {
		cb.await();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return;
	} catch (BrokenBarrierException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return;
	}
	
	
	int totalwords=0;

	//sortarea frecventelor + memorarea primelor N cuvinte din fiecare fisier	
	for(int j=0;j<workpool.reduce12linkedlist.size();j++)
			{
				ArrayList<Data> arrlist= new ArrayList<Data>();
				
				totalwords=0;
				for(String key:workpool.reduce12linkedlist.get(j).wordmap.keySet())
				{
					
					
				
					Data e = new Data(key,workpool.reduce12linkedlist.get(j).wordmap.get(key));
					totalwords+=workpool.reduce12linkedlist.get(j).wordmap.get(key);
					arrlist.add(e);
					
					
					
					
				}
				Collections.sort(arrlist,new Comp());
				ArrayList<Data> arrlist2= new ArrayList<Data>();
			
				for(int i=0;i<N;i++)
				{
					arrlist2.add(i, arrlist.get(i));
					
				}
				if(arrlist.get(N).occurence == arrlist.get(N-1).occurence)
				{
					int i=N;
					while(arrlist.get(i).occurence == arrlist.get(N-1).occurence && i<arrlist.size())
						{
						arrlist2.add(i, arrlist.get(i));
						i++;
						}
					
				}
				
			workpool.putReduce2Work(new Solution(workpool.reduce12linkedlist.get(j).filename, "", arrlist2,totalwords)); 
				
			}
			
			workers = new Worker[NTHREADS];
			for(int i=0;i<NTHREADS;i++)
			{
			workers[i]=new Worker( cb,workpool,3,words);
			workers[i].setName(Integer.toString(i));
			workers[i].start();
			}			
			try {
				cb.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}

	//afisare rezultate
	out.print("Rezultate pentru: (");
	for(int i=0;i<NC-1;i++)
		out.print(words[i] + ", ");
	out.println(words[NC-1]+")\n");
	int size=0;
	double freq;
	HashMap <String,String> solution = new HashMap<String, String>();
	for(int i=0;i<workpool.reduce22linkedlist.size()&&i<X;i++)
	{
		String value;
		
		value=" (";
		//out.print(workpool.reduce22linkedlist.get(i).filename +);
		size=workpool.reduce22linkedlist.get(i).arraylistmap.size();
		for(int j=0;j<size-1;j++)
		{
			freq=(double)workpool.reduce22linkedlist.get(i).arraylistmap.get(j).occurence/(double)workpool.reduce22linkedlist.get(i).totalwords;
			freq=(int)(freq*10000);
			freq/=100.0;
			value+=String.format("%.2f", (float)freq);
			value+=", ";
			
		}
		freq=(double)workpool.reduce22linkedlist.get(i).arraylistmap.get(size-1).occurence/(double)workpool.reduce22linkedlist.get(i).totalwords;
		freq=(int)(freq*10000);
		freq/=100.0;
		value+=String.format("%.2f", (float)freq);
		
		value+=")\n";
	
		solution.put(workpool.reduce22linkedlist.get(i).filename, value);
		
	}

	for(int i=0;i<ND;i++)
		for(String s:solution.keySet())
		{
			if(s.compareTo(doc[i])==0)			
			{
			
				out.print(doc[i] + solution.get(doc[i]));
			}
			
		}
			out.close();
	}
}
