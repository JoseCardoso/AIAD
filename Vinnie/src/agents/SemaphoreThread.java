package agents;

import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoTrafficLight;

public class SemaphoreThread implements Runnable{
	
	private Sumo sumo;
	private SemaphoreAgent agent;
	
	
	
	public SemaphoreThread(Sumo sumo, SemaphoreAgent agent) {
		this.sumo = sumo;
		this.agent = agent;
	}



	public void run()
	{
		SumoTrafficLight semaphore = new SumoTrafficLight(agent.ID);
		while(true)
		{
			//states of all semaphores
			semaphore.setState("rGyrGryGryGryGyrGrrrrrrrrrrrrGGGGGGGGGGGyyyGGGGGGGGGGGGGGGGGGGGGGGG");
			
		}
	}

}
