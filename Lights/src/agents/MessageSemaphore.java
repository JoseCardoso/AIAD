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
	private static final long serialVersionUID = 1L;
	String ID;
	private HashSet<String> adjacents;

	public MessageSemaphore(String string) {
		super();
		ID = string;

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

	public HashSet<String> getAdjacents() {
		return adjacents;
	}

	public void setAdjacents(HashSet<String> adjacents) {
		this.adjacents = adjacents;
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

				SumoLane lane = new SumoLane(semaphore.getControlledLanes().listIterator(0).next());
				if (getStoppedEmergencyVehicles(lane))

					System.out.println("");

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// for(int i = 0; i < )

		}
	}

	private boolean getStoppedEmergencyVehicles(SumoLane lane) {
		SumoVehicle[] vehicles = lane.vehiclesList();
		int stopped = 0;

		for (int k = 0; k < vehicles.length; k++) {
			System.out.println(vehicles[k].typeId);
			// if(vehicles[k].alive && vehicles[k].typeId.equals("emergency"))
			// if(vehicles[k].getSpeed() == 0.0)
			// return true;
		}

		return false;
	}

	private String generateState(boolean position, boolean yellow) {
		String Str = "";
		int column = Integer.parseInt(ID.split("/")[0]);
		int line = Integer.parseInt(ID.split("/")[1]);
		String upper = Integer.toString(column) + "/" + Integer.toString(line + 1);
		String righter = Integer.toString(column + 1) + "/" + Integer.toString(line);
		String below = Integer.toString(column) + "/" + Integer.toString(line - 1);
		String lefter = Integer.toString(column - 1) + "/" + Integer.toString(line);
		if (position) {
			if (getAdjacents().contains(upper)) {
				if (!yellow)
					Str += "Gg";
				else
					Str += "yy";
			}
			if (getAdjacents().contains(righter)) {
				Str += "rr";
			}
			if (getAdjacents().contains(below)) {
				if (!yellow)
					Str += "gG";
				else
					Str += "yy";

			}
			if (getAdjacents().contains(lefter)) {
				Str += "rr";
			}
		} else {
			if (getAdjacents().contains(upper)) {
				Str += "rr";

			}
			if (getAdjacents().contains(righter)) {
				if (!yellow)
					Str += "Gg";
				else
					Str += "yy";
			}
			if (getAdjacents().contains(below)) {

				Str += "rr";

			}
			if (getAdjacents().contains(lefter)) {
				if (!yellow)
					Str += "gG";
				else
					Str += "yy";
			}
		}

		return Str.toString();
	}

}
