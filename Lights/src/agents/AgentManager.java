package agents;

import java.util.ArrayList;
import java.util.HashSet;


import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoTrafficLight;

public class AgentManager {
	Sumo sumo;
	ContainerController mainContainer;
	ArrayList<SemaphoreAgent> agents = new ArrayList<>();
	
	public AgentManager(Sumo sumo, ContainerController mainContainer) {
		this.sumo = sumo;
		this.mainContainer = mainContainer;
	}

	public void initSemaphores() {
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
			SemaphoreAgent agent = new SemaphoreAgent( semaphoreIDS.get(i));
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

	public void startSemaphores()
	{
		for(int i = 0; i < agents.size();i++)
		{
			try {
				mainContainer.getAgent(agents.listIterator(i).next().getLocalName()).start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
