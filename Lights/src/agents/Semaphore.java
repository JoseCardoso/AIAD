package agents;

import java.util.HashSet;

import jade.core.Agent;
import trasmapi.sumo.SumoLane;
import trasmapi.sumo.SumoVehicle;

public class Semaphore extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String ID;
	private HashSet<String> adjacents;

	
	

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

	

	public int getStoppedVehicles(SumoLane lane)
	{
		SumoVehicle[] vehicles = lane.vehiclesList();
		int stopped = 0;

		for(int k = 0; k < vehicles.length;k++)
		{
			if(vehicles[k].alive)//may not be alive
				if(vehicles[k].getSpeed() == 0.0)
					stopped++;
		}


		return stopped;
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
	
	

	public boolean existAdjacent(int x , int y){
		int column = Integer.parseInt(ID.split("/")[0]);
		int line = Integer.parseInt(ID.split("/")[1]);
		String test = Integer.toString(column + x) + "/" + Integer.toString(line + y);
		if (getAdjacents().contains(test)) {
			return true;
		}
		return false;
	}


	
}
