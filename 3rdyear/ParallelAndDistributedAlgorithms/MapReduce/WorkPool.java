import java.util.ArrayList;
import java.util.LinkedList;


public class WorkPool {
	int NTHREADS;
	int NWAITING=0;
	public boolean finish = false;
	public  LinkedList<MapSolution> maplinkedlist=new LinkedList<MapSolution>();
	public  LinkedList<MapSolution> reduce1linkedlist=new LinkedList<MapSolution>();
	public  LinkedList<Solution> reduce12linkedlist= new LinkedList<Solution>();
	public  LinkedList<Solution> reduce21linkedlist=new LinkedList<Solution>();
	public  LinkedList<Solution> reduce22linkedlist=new LinkedList<Solution>();

	public WorkPool(int NTHREADS)
	{
		this.NTHREADS=NTHREADS;
	}
	//extrage urmatorul map task
	public synchronized MapSolution getMapWork() {
		if (maplinkedlist.size() == 0) { // workpool gol
			NWAITING++;
			/* condtitie de terminare:
			 * nu mai exista nici un task in workpool si nici un worker nu e activ 
			 */
			if (NWAITING == NTHREADS) {
				finish = true;
				
				/* problema s-a terminat, anunt toti ceilalti workeri */
				notifyAll();
				return null;
			} else {
				while (!finish && maplinkedlist.size() == 0) {
					try {
						this.wait();
					} catch(Exception e) {e.printStackTrace();}
				}
				
				if (finish)
				    /* s-a terminat prelucrarea */
				    return null;

				NWAITING--;
				
			}
		}
		return maplinkedlist.remove();

	}
	//extrage urmatorul reduce 1 task
	public synchronized MapSolution getReduce1Work() {
		if (reduce1linkedlist.size() == 0) { // workpool gol
			NWAITING++;
			/* condtitie de terminare:
			 * nu mai exista nici un task in workpool si nici un worker nu e activ 
			 */
			if (NWAITING == NTHREADS) {
				finish = true;
			//	NWAITING=0;
				/* problema s-a terminat, anunt toti ceilalti workeri */
				notifyAll();
				return null;
			} else {
				while (!finish && reduce1linkedlist.size() == 0) {
					try {
						this.wait();
					} catch(Exception e) {e.printStackTrace();}
				}
				
				if (finish)
				    /* s-a terminat prelucrarea */
				    return null;

				NWAITING--;
				
			}
		}
		return reduce1linkedlist.remove();

	}
	
	//extrage urmatorulreduce second stage task
	public synchronized Solution getReduce2Work() {
		if (reduce21linkedlist.size() == 0) { // workpool gol
			NWAITING++;
			// condtitie de terminare:
			 // nu mai exista nici un task in workpool si nici un worker nu e activ 
			 
			if (NWAITING == NTHREADS) {
				finish = true;
		
				// problema s-a terminat, anunt toti ceilalti workeri 
				notifyAll();
				return null;
			} else {
				while (!finish && reduce21linkedlist.size() == 0) {
					try {
						this.wait();
					} catch(Exception e) {e.printStackTrace();}
				}
				
				if (finish)
				    // s-a terminat prelucrarea 
				    return null;

				NWAITING--;
				
			}
		}
		return reduce21linkedlist.remove();

	}
	//adauga map work
	public synchronized void putMapWork(MapSolution mapsol)
	{
		
		maplinkedlist.add(mapsol);
		this.notify();
		
	}
	//insereaza reduce first stage work
	public synchronized void putReduce1Work(MapSolution mapsol)
	{
		
		reduce1linkedlist.add(mapsol);
		this.notify();
		
	}
	//insereaza reduce second stage work
	public synchronized void putReduce2Work(Solution mapsol)
	{
		
		reduce21linkedlist.add(mapsol);
		this.notify();
		
	}

}
