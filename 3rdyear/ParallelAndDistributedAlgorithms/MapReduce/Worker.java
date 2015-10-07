import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CyclicBarrier;


public class Worker extends Thread{
	CyclicBarrier cb;
	 WorkPool workpool;
	 int type;
	PrintWriter out;
	String[] words;
	public Worker(CyclicBarrier cb, WorkPool workpool, int type,String[] words)
	{
		this.cb=cb;
		this.type = type;
		this.workpool=workpool;
		this.words=words;
			
		
	}
	
	//procesarea unei solutii map
	void  processMapSolution(MapSolution ms)
	{
		//impartire chunk in cuvinte
	
		LinkedList<MapChunk> ll=new LinkedList<MapChunk>();
		for(MapChunk mc : ms.ll)
		{
		String str=mc.content;
	
		String[] ops = str.split("\\s*[a-zA-Z]+\\s*");
		String[] notops = str.split("\\s*[^a-zA-Z]+\\s*");
		String[] res = new String[ops.length+notops.length];
	
	
		MapChunk mapchunk = new MapChunk(mc.content);
		for(String s : notops) 
			{
			
			//inserare cuvinte in hashmap
			if(s.compareTo("")!=0)
			if(mapchunk.wordhashmap.containsKey(s))
			{
			int i=mapchunk.wordhashmap.remove(s).intValue();
			mapchunk.wordhashmap.put(s, new Integer(i+1));
			}
			else 
			{
				mapchunk.wordhashmap.put(s, new Integer(1));
				
				
			}
			
			
			}
	
			
	
			ll.add(mapchunk);
			
		
		}
		MapSolution mapsol = new MapSolution(ms.filename, ll);
	
		workpool.reduce1linkedlist.add(mapsol);
			
	}
	//procesare reduce solution first stage
	void  processReduce1Solution(MapSolution rs1)
	{
			LinkedList<MapChunk> ll=rs1.ll;
			MapSolution ms ;
			/*
			 * fiecare intrare din lista de taskuri corespunde unui fisier
			 * fiecare intrare contine un hashmap cu fiecare cuvant + frecventa
			 */
			Solution newsol= new Solution(rs1.filename,"");
			for(MapChunk mchunk : ll)
			{
					for(String str1: mchunk.wordhashmap.keySet())
					{
						int occ_chunk=mchunk.wordhashmap.get(str1);
						if(newsol.wordmap.containsKey(str1))
						{
							int occ =newsol.wordmap.remove(str1);
							newsol.wordmap.put(str1, occ+occ_chunk);
							
						}
						else
						{
							newsol.wordmap.put(str1, occ_chunk);
						}
					}
			
			}
			
		workpool.reduce12linkedlist.add(newsol);
		
	}
	//procesare task reduce second stage
	void processReduce2Solution(Solution rs2)
	{
		boolean ok = false;
		float totalwords=(float)rs2.totalwords;
		
		int occ;
		ArrayList<Data> arrlist=new ArrayList<Data>();
		//inserare in lista de solutii a fisierelor care contin toate cele NC cuvinte 
		//si a  frecventelor de aparitie pentru fiecare cuvant
		
		for(int i=0;i<words.length;i++)
				
				{ ok=false;
				for(int j=0;j<rs2.arraylistmap.size();j++)
				{
					if(words[i].compareTo(rs2.arraylistmap.get(j).str)==0)
					{
						ok = true;
						occ=rs2.arraylistmap.get(j).occurence;
						arrlist.add(new Data(words[i],occ));
						break;
					}
					
				}
				if(ok == false) break; 
				}
	
		if(ok == true)
		
			workpool.reduce22linkedlist.add(new Solution(rs2.filename, "", arrlist, rs2.totalwords));
		
			
	}
	public void run()
	{
		//map worker
		if(type == 1)
		{
		while(true)
		{
			MapSolution s = workpool.getMapWork();
			if(s == null) break;
			processMapSolution(s);
		}
		
		try
		{
		 cb.await();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		}
	//first stage reduce	
	if(type == 2)
	{
		
		while(true)
		{
			MapSolution s = workpool.getReduce1Work();
			if(s == null) break;
			processReduce1Solution(s);
		}
	
		try
		{
		 cb.await();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}

		
		
		
	}
	//second stage reduce
	if(type == 3)
	{
		
		while(true)
		{
			Solution s = workpool.getReduce2Work();
			
			if(s == null) break;
			processReduce2Solution(s);
		}
	
	try
		{
		 cb.await();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}

		
		
		
	}
	}
}
