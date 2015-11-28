package agents;

import java.util.ArrayList;
import java.util.HashMap;
import agents.SemaphoreAgent;
import jade.wrapper.ContainerController;
import trasmapi.sumo.SumoTrafficLight;

public class SemaphoreManager {
	ArrayList<SemaphoreAgent> agents = new ArrayList<>();

	public SemaphoreManager(ContainerController mainContainer) {
		ArrayList<String> semaphoreIDS = SumoTrafficLight.getIdList();
		for (int i = 0; i < semaphoreIDS.size(); i++) {
			SumoTrafficLight semaphore = new SumoTrafficLight(semaphoreIDS.get(i));
			ArrayList<String> lanes = semaphore.getControlledLanes();
			// ArrayList<String> adjacentSemaphores= new ArrayList<>();
			SemaphoreAgent agent = new SemaphoreAgent(semaphoreIDS.get(i));
			try {
				agents.add(agent);
				mainContainer.acceptNewAgent("TrafficLight-" + semaphoreIDS.get(i), agent);
			}catch(Exception e){
				e.printStackTrace();
			}
		
			
		}
	}

}
