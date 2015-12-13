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
	private boolean sentGreenProposal;
	int tickCounter = 0;

	public MessageSemaphore(String string) {
		super(string);

	}

	public void setup() {
		// System.out.println("setup semaforo");
		addBehaviour(new RequestServer());

		sentGreenProposal = false;
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
		tickCounter = 0;
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
				tickCounter++;
				if (tickCounter > 50) {
					tickCounter = 0;
					position = true;
					yellow = false;
					sentGreenProposal = false;
				} else if (tickCounter > 45) {
					yellow = true;

					if(!sentGreenProposal)
					{
						greenProposal(semaphore, position);
						sentGreenProposal = true;
					}
				} else if (tickCounter > 25) {
					position = false;

					yellow = false;
					sentGreenProposal = false;
				} else if (tickCounter > 20) {

					yellow = true;

					if(!sentGreenProposal)
					{
						greenProposal(semaphore, position);
						sentGreenProposal = true;
					}

				}

				//emergencyEvaluator(semaphore, position);
				/*
				 * SumoLane lane = new
				 * SumoLane(semaphore.getControlledLanes().listIterator(0).next(
				 * )); if (getStoppedEmergencyVehicles(lane))
				 * 
				 * System.out.println("");
				 */
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// for(int i = 0; i < )

		}
	}



	private void greenProposal(SumoTrafficLight semaphore,boolean position)
	{
		SumoLane lane;
		int laneCounter = 0;

		int column = Integer.parseInt(ID.split("/")[0]);
		int line = Integer.parseInt(ID.split("/")[1]);

		
		// UPPER
		if (existAdjacent(0, 1)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());

			if (position) {
				// EMERGENCY
				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newLine = line + 1;
				msg.setContent("Green " + column + "/" + line);
				msg.addReceiver(new AID("Semaphore-" + column + "/" + newLine, AID.ISLOCALNAME));

				send(msg);

			}
			laneCounter++;
		}
		// RIGHTER
		if (existAdjacent(1, 0)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if (!position) {
				// EMERGENCY

				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newCol = column + 1;
				msg.setContent("Green " + column + "/" + line);
				msg.addReceiver(new AID("Semaphore-" + newCol + "/" + line, AID.ISLOCALNAME));

				send(msg);
			}
			laneCounter++;
		}
		// DOWNER
		if (existAdjacent(0, -1)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if (position) {
				// EMERGENCY

				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newLine = line - 1;
				msg.setContent("Green " + column + "/" + line);
				msg.addReceiver(new AID("Semaphore-" + column + "/" + newLine, AID.ISLOCALNAME));

				send(msg);
			}
			laneCounter++;
		}
		// LEFTER
		if (existAdjacent(-1, 0)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if (!position) {
				// EMERGENCY
				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newCol = column - 1;
				msg.setContent("Green " + column + "/" + line);
				msg.addReceiver(new AID("Semaphore-" + newCol + "/" + line, AID.ISLOCALNAME));

				send(msg);
			}
		}


	}


	private void emergencyEvaluator(SumoTrafficLight semaphore, boolean position) {
		SumoLane lane;
		int laneCounter = 0;

		int column = Integer.parseInt(ID.split("/")[0]);
		int line = Integer.parseInt(ID.split("/")[1]);

		// UPPER
		if (existAdjacent(0, 1)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());

			if (getStoppedVehicles(lane) > MAX_WAITING_VEHICLES && position) {
				// EMERGENCY
				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newLine = line + 1;
				msg.setContent("Red " + column + "/" + line);
				msg.addReceiver(new AID("Semaphore-" + column + "/" + newLine, AID.ISLOCALNAME));

				send(msg);

			}
			laneCounter++;
		}
		// RIGHTER
		if (existAdjacent(1, 0)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if (getStoppedVehicles(lane) > MAX_WAITING_VEHICLES && !position) {
				// EMERGENCY

				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newCol = column + 1;
				msg.setContent("Red " + column + "/" + line);
				msg.addReceiver(new AID("Semaphore-" + newCol + "/" + line, AID.ISLOCALNAME));

				send(msg);
			}
			laneCounter++;
		}
		// DOWNER
		if (existAdjacent(0, -1)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if (getStoppedVehicles(lane) > MAX_WAITING_VEHICLES && position) {
				// EMERGENCY

				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newLine = line - 1;
				msg.setContent("Red " + column + "/" + line);
				msg.addReceiver(new AID("Semaphore-" + column + "/" + newLine, AID.ISLOCALNAME));

				send(msg);
			}
			laneCounter++;
		}
		// LEFTER
		if (existAdjacent(-1, 0)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if (getStoppedVehicles(lane) > MAX_WAITING_VEHICLES && !position) {
				// EMERGENCY
				ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
				int newCol = column - 1;
				msg.setContent("Red " + column + "/" + line);
				msg.addReceiver(new AID("Semaphore-" + newCol + "/" + line, AID.ISLOCALNAME));

				send(msg);
			}
		}

	}

	public void evaluateProposal(String string) {
		/*int column = Integer.parseInt(ID.split("/")[0]);
		int line = Integer.parseInt(ID.split("/")[1]);
		String upper = Integer.toString(column) + "/" + Integer.toString(line + 1);
		String righter = Integer.toString(column + 1) + "/" + Integer.toString(line);
		String below = Integer.toString(column) + "/" + Integer.toString(line - 1);
		String lefter = Integer.toString(column - 1) + "/" + Integer.toString(line);
		SumoLane lane ;
		SumoTrafficLight semaphore = new SumoTrafficLight(ID);
		if (string.equals(upper)) {
			if (existAdjacent(0, -1)) {
				lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter("upper")).next());
				if (getStoppedVehicles(lane) < MAX_WAITING_VEHICLES)
					tickCounter = 20;
			}
			else

		}
		if (string.equals(righter)) {
			if (getStoppedVehicles(lane) > MAX_WAITING_VEHICLES)
				tickCounter = 45;
		}
		if (string.equals(below)) {
			if (getStoppedVehicles(lane) > MAX_WAITING_VEHICLES)
				tickCounter = 20;

		}
		if (string.equals(lefter)) {
			if (getStoppedVehicles(lane) > MAX_WAITING_VEHICLES)
				tickCounter = 45;
		}
		 */
	}

	public void evaluateGreen(String string) {
		// TODO Auto-generated method stub
		int column = Integer.parseInt(ID.split("/")[0]);
		int line = Integer.parseInt(ID.split("/")[1]);
		String upper = Integer.toString(column) + "/" + Integer.toString(line + 1);
		String righter = Integer.toString(column + 1) + "/" + Integer.toString(line);
		String below = Integer.toString(column) + "/" + Integer.toString(line - 1);
		String lefter = Integer.toString(column - 1) + "/" + Integer.toString(line);

		SumoLane lane ;
		SumoTrafficLight semaphore = new SumoTrafficLight(ID);

		int stopped1 = 0, stopped2 = 0;
		//avaliar situação dos lados
		if (string.equals(upper) || string.equals(below)) {
			//Righter			
			if (existAdjacent(1, 0)) {
				int laneC = laneCounter("righter");
				lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneC).next());
				stopped1 = getStoppedVehicles(lane);
			}
			//lefter
			if (existAdjacent(-1, 0)) {
				int laneC = laneCounter("righter");
				lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneC).next());
				stopped2 = getStoppedVehicles(lane);
			}

			//nenhum dos lados tems um número excessivo de carros
			if(stopped1 < MAX_WAITING_VEHICLES && stopped2 < MAX_WAITING_VEHICLES)
			{
				if(tickCounter < 20)
					tickCounter = 0;
				else
					tickCounter = 45;
			}

		}
		if (string.equals(righter) || string.equals(lefter)) {
			//upper
			if (existAdjacent(0,1)) {
				int laneC = laneCounter("upper");
				lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneC).next());
				stopped1 = getStoppedVehicles(lane);
			}
			//downer
			if (existAdjacent(0,-1)) {
				int laneC = laneCounter("downer");
				lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneC).next());
				stopped2 = getStoppedVehicles(lane);
			}

			//nenhum dos lados tems um número excessivo de carros
			if(stopped1 < MAX_WAITING_VEHICLES && stopped2 < MAX_WAITING_VEHICLES)
			{
				if(tickCounter > 25)
					tickCounter = 25;
				else
					tickCounter = 20;
			}
		}


	}

}
