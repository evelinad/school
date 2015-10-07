package transfers;

import java.util.Random;

import core.Mediator;
/**
 * 
 * Transfer class for keeping data of an ongoing transfer
 *
 */
public class Transfer extends Thread implements TransferStatusConstans{

	private String file;
	private String toUser;
	private String fromUser;
	private Mediator med;
	private int index;
	/**
	 * 0 - Download; 
	 * 1 - Upload; 
	 */
	private String type;
	/**
	 * 0 - started;
	 * 1 - active;
	 * 2 - paused;
	 * 3 - stopped;
	 * 4 - completed;
	 */
	private int status;
	private int progress = 0;
	
	public Transfer(String file,String fromUser,String toUser, Mediator med, int type) {
			this.file = file;
			this.toUser = toUser;
			this.fromUser = fromUser;
			status = STARTED;
			this.med = med;
			if (type == DOWNLOAD)
				this.type = new String("Downloading");
			else
				this.type = new String("Uploading");
			
	}
	
	public void updateProgress(int unit)
	{
		progress += unit;
		if (progress >= 100)
		{
			progress = 100;
			status = COMPLETED;
		}
		med.updateProgress(progress, index);
	}
	public void updateStatus(int status)
	{
		this.status = status;
	}
	
	public boolean isCompleted() {
		if (progress == 100) {
			return true;
		}
		return false;
	}
	
	public int getProgress()
	{
		return this.progress;
	}
	public int getStatus()
	{
		return this.status;
	}	
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getType()
	{
		return type;
	}

	@Override
	public void run() {
		Random rand = new Random();
		while(true)
		{
			if (status == ACTIVE) {
				updateProgress(rand.nextInt(5));
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
