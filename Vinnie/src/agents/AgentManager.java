package agents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;


import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoCom;
import trasmapi.sumo.SumoTrafficLight;

public class AgentManager {
	Sumo sumo;
	ContainerController mainContainer;
	private final int numDrivers = 25;
	ArrayList<SemaphoreAgent> agents = new ArrayList<>();

	public AgentManager(Sumo sumo, ContainerController mainContainer) {
		this.sumo = sumo;
		this.mainContainer = mainContainer;
	}

	public void startSemaphores() {
		ArrayList<String> semaphoreIDS = SumoTrafficLight.getIdList();
		for (int i = 0; i < semaphoreIDS.size(); i++) {
			SumoTrafficLight semaphore = new SumoTrafficLight(semaphoreIDS.get(i));
			ArrayList<String> controlledLanes = semaphore.getControlledLanes();
			HashSet<String>  adjacentSemaphores= new HashSet<String>();


			for(int j = 0; j < controlledLanes.size(); j++)
			{
				String neighbour= controlledLanes.listIterator(j).next().split("to")[0];
				adjacentSemaphores.add(neighbour);
			}
			SemaphoreAgent agent = new SemaphoreAgent(semaphoreIDS.get(i));
			HashSet<String> agentSet = new HashSet<String>();
			agentSet.addAll(adjacentSemaphores);
			
			agent.setAdjacents(adjacentSemaphores);
			
				try {
				agents.add(agent);
				mainContainer.acceptNewAgent("Semaphore-" + semaphoreIDS.get(i), agent);
			}catch(Exception e){
				e.printStackTrace();
			}


		}
	}


}
