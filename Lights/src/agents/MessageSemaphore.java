package agents;

import java.util.HashSet;
import java.util.Iterator;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import trasmapi.sumo.SumoLane;
import trasmapi.sumo.SumoTrafficLight;
import trasmapi.sumo.SumoVehicle;

public class MessageSemaphore extends Semaphore {
	/**
	 * 
	 */
	public MessageSemaphore(String string) {
		super(string);

	}

	public void setup() {
		// System.out.println("setup semaforo");
		addBehaviour(new RequestServer());

		Thread thread = new Thread(new Runnable() {
			public void run() {
				executeSemaphore();
			}
		});
		thread.start();

		// System.out.println("SEMAFORO INICIALIZADO " + ID);
	}

	public void executeSemaphore() {
		boolean position = true, yellow = false;
		SumoTrafficLight semaphore = new SumoTrafficLight(ID);
		int i = 0;
		while (true) {
			// states of all semaphores
			if (getAdjacents().size() == 4 && position) {
				if (!yellow)
					semaphore.setState("rrrGGgrrrgGG");
				else
					semaphore.setState("rrryyyrrryyy");
			} else if (getAdjacents().size() == 4 && !position) {
				if (!yellow)
					semaphore.setState("GGgrrrGGgrrr");
				else
					semaphore.setState("yyyrrryyyrrr");
			} else if (getAdjacents().size() == 3) {

				semaphore.setState(generateState(position, yellow));

			}
			try {
				Thread.sleep(2);
				i++;
				if (i > 55) {
					i = 0;
					position = true;
					yellow = false;
				} else if (i > 50) {
					yellow = true;
				} else if (i > 25) {
					position = false;

					yellow = false;
				} else if (i > 20) {

					yellow = true;

				}
				
				
				
				emergencyEvaluator(semaphore);
		/*		SumoLane lane = new SumoLane(semaphore.getControlledLanes().listIterator(0).next());
				if (getStoppedEmergencyVehicles(lane))

					System.out.println("");
*/
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// for(int i = 0; i < )

		}
	}
	
	
	private void emergencyEvaluator(SumoTrafficLight semaphore)
	{
		SumoLane lane;
		int laneCounter = 0;
		
		int column = Integer.parseInt(ID.split("/")[0]);
		int line = Integer.parseInt(ID.split("/")[1]);

		//	UPPER
		if (existAdjacent(0,1)){
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if( getStoppedVehicles(lane) > 15)
			{
				//EMERGENCY
				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newLine = line+1;
				msg.setContent("Red "+column+"/"+line);
				msg.addReceiver(new AID("Semaphore-"+column+"/"+newLine, AID.ISLOCALNAME));
				
				send(msg);
								
				
			}
			laneCounter++;
		}	
		//RIGHTER
		if (existAdjacent(1 ,0)){
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if( getStoppedVehicles(lane) > 15)
			{
				//EMERGENCY

				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newCol = column+1;
				msg.setContent("Red "+column+"/"+line);
				msg.addReceiver(new AID("Semaphore-"+newCol+"/"+line, AID.ISLOCALNAME));
				
				send(msg);
			}
			laneCounter++;
		}
		//DOWNER
		if (existAdjacent(0 ,-1)){
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if( getStoppedVehicles(lane) > 15)
			{
				//EMERGENCY

				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newLine = line-1;
				msg.setContent("Red "+column+"/"+line);
				msg.addReceiver(new AID("Semaphore-"+column+"/"+newLine, AID.ISLOCALNAME));
				
				send(msg);
			}
			laneCounter ++ ;
		}
		//LEFTER
		if (existAdjacent(-1 ,0)){
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if( getStoppedVehicles(lane) > 15)
			{
				//EMERGENCY
				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newCol = column-1;
				msg.setContent("Red "+column+"/"+line);
				msg.addReceiver(new AID("Semaphore-"+newCol+"/"+line, AID.ISLOCALNAME));
				
				send(msg);
			}
		}
		
		
	}
	

}
