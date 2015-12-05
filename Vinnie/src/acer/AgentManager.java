package acer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import agents.TrafficLightAgent;
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
			//ArrayList<String> lanes = semaphore.getControlledLanes();
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

	public void addDrivers() {

		SumoCom.createAllRoutes();

		DriverAgent.rand = new Random(423423);

		try {

			for (int i = 0; i < numDrivers; i++)
				mainContainer.acceptNewAgent("DRIVER#" + i, new DriverAgent(i)).start();

			// AccidentGUI accident = new AccidentGUI(numDrivers);
			// accident.setVisible(true);

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

	}

}
