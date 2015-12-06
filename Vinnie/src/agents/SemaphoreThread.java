package agents;

import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoTrafficLight;

public class SemaphoreThread implements Runnable {

	private Sumo sumo;
	private SemaphoreAgent agent;

	public SemaphoreThread(Sumo sumo, SemaphoreAgent agent) {
		this.sumo = sumo;
		this.agent = agent;
	}

	public void run() {
		boolean position = true;
		SumoTrafficLight semaphore = new SumoTrafficLight(agent.ID);
		int i = 0;
		while (true) {
			// states of all semaphores
			if (agent.getAdjacents().size() == 4 && position) {
				semaphore.setState("rrrgGGrrrgGG");
				position = false;
			} else if (agent.getAdjacents().size() == 4 && !position) {
				semaphore.setState("gGGrrrgGGrrr");
				position = true;
			} else if (agent.getAdjacents().size() == 3 && position) {
				semaphore.setState("rrgGrr");
			}
			else if (agent.getAdjacents().size() == 3 && !position) {
				semaphore.setState("gGrrgG");
			}
			try {
				Thread.sleep(2);
				i++;
				if(i > 25)
					position = false;
				if(i >50)
				{
					i = 0;
					position = true;
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
