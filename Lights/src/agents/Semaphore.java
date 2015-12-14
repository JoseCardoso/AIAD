package agents;

import java.util.HashSet;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import trasmapi.sumo.SumoLane;
import trasmapi.sumo.SumoTrafficLight;
import trasmapi.sumo.SumoVehicle;

public class Semaphore extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String ID;
	public HashSet<String> adjacents;
	int MAX_WAITING_VEHICLES = 7;
	boolean stop = false;

	public Semaphore(String iD) {
		ID = iD;
	}

	public HashSet<String> getAdjacents() {
		return adjacents;
	}

	public void setAdjacents(HashSet<String> adjacentSemaphores) {
		// TODO Auto-generated method stub
		adjacents = adjacentSemaphores;
	}

	public int getStoppedVehicles(SumoLane lane) {
		SumoVehicle[] vehicles = lane.vehiclesList();
		int stopped = 0;
		for (int k = 0; k < vehicles.length; k++) {
			// if(vehicles[k].alive)//may not be alive
			try {
				if (vehicles[k].getSpeed() == 0.0)
					stopped++;
			} catch (IndexOutOfBoundsException e) {

			}
		}

		return stopped;
	}

	public int getStoppedVehicles() {
		SumoLane lane;
		SumoTrafficLight semaphore = new SumoTrafficLight(ID);
		int stopped = 0;
		int laneCounter = 0;
		// avaliar situação dos lados

		if (existAdjacent(1, 0)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			stopped += getStoppedVehicles(lane);
			laneCounter++;
		}
		// lefter
		if (existAdjacent(-1, 0)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			stopped += getStoppedVehicles(lane);
			laneCounter++;
		}

		if (existAdjacent(0, 1)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			stopped += getStoppedVehicles(lane);
			laneCounter++;
		}
		// downer
		if (existAdjacent(0, -1)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			stopped += getStoppedVehicles(lane);
			laneCounter++;
		}

		return stopped;

	}

	public int laneCounter(String s) {
		int laneCounter = 0;
		// UPPER
		if (existAdjacent(0, 1)) {
			if (s.equals("upper"))
				return laneCounter;
			laneCounter++;
		}
		// RIGHTER
		if (existAdjacent(1, 0)) {
			if (s.equals("righter"))
				return laneCounter;

			laneCounter++;
		}
		// DOWNER
		if (existAdjacent(0, -1)) {
			if (s.equals("below"))
				return laneCounter;
			laneCounter++;
		}
		// LEFTER
		if (existAdjacent(-1, 0)) {
			if (s.equals("lefter"))
				return laneCounter;
		}
		return laneCounter;
	}

	public String generateState(boolean position, boolean yellow) {
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

	public boolean existAdjacent(int x, int y) {
		int column = Integer.parseInt(ID.split("/")[0]);
		int line = Integer.parseInt(ID.split("/")[1]);
		String test = Integer.toString(column + x) + "/" + Integer.toString(line + y);
		if (getAdjacents().contains(test)) {
			return true;
		}
		return false;
	}

	public void stop() {
		stop = true;

	}

	public int getNumVehicles() {
		SumoLane lane;
		SumoTrafficLight semaphore = new SumoTrafficLight(ID);
		int num = 0;
		int laneCounter = 0;
		// avaliar situação dos lados

		if (existAdjacent(1, 0)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if (lane.vehiclesList() != null)
				num += lane.vehiclesList().length;
			laneCounter++;
		}
		// lefter
		if (existAdjacent(-1, 0)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if (lane.vehiclesList() != null)
				num += lane.vehiclesList().length;
			laneCounter++;
		}

		if (existAdjacent(0, 1)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if (lane.vehiclesList() != null)
				num += lane.vehiclesList().length;
			laneCounter++;
		}
		// downer
		if (existAdjacent(0, -1)) {
			lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
			if (lane.vehiclesList() != null)
				num += lane.vehiclesList().length;
			laneCounter++;
		}

		return num;

	}

}
