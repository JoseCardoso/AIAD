package agents;


import java.util.HashSet;

import q.Learning;
import trasmapi.sumo.SumoLane;
import trasmapi.sumo.SumoTrafficLight;
import trasmapi.sumo.SumoVehicle;

public class LearningSemaphore extends Semaphore{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String ID;
	private HashSet<String> adjacents;

	private int greenTime1, greenTime2, yellowTime;

	private Learning qLearn1, qLearn2;

	private int action1, action2;

	public LearningSemaphore( String string) {
		super();
		ID = string;
		greenTime1 = 30;
		greenTime2 = 30;
		yellowTime = 2;
		qLearn1 = new Learning();
		qLearn2 = new Learning();

		//assumindo que se manteve no início
		action1 = action2 = 0;
	}

	public void setup() {
		executeSemaphore();
		// esta funo inicializa o semaforo
		//	System.out.println("SEMAFORO INICIALIZADO  " + ID);
	}

	public HashSet<String> getAdjacents() {
		return adjacents;
	}

	public void setAdjacents(HashSet<String> adjacents) {
		this.adjacents = adjacents;
	}
	public void executeSemaphore() {
		boolean position = true, yellow = false;
		boolean updateQtable = true;
		SumoTrafficLight semaphore = new SumoTrafficLight(ID);
		int i = 0;
		while (true) {
			int laneCounter= 0;
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
				if (i > greenTime2 + yellowTime*2 + greenTime1) {
					i = 0;
					laneCounter =0;
					position = true;
					yellow = false;
					SumoLane lane;
					int stopped = 0;
					if (existAdjacent(0 ,1)){
						lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
					stopped += getStoppedVehicles(lane);
					laneCounter++;
						}
					if (existAdjacent(1 ,0)){
						laneCounter ++ ;
					}
					if (existAdjacent(0 ,-1)){
						lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
					stopped += getStoppedVehicles(lane);
						
					}
					

					qLearn2.updateTable(greenTime2, action2, stopped);

					action2=qLearn2.getAction(greenTime2);
					updateGreenTime(1, action1);

					updateQtable = true;
				} else if (i > greenTime2 + greenTime1 + yellowTime) {
					yellow = true;





				} else if (i > greenTime1+yellowTime) {
					position = false;

					yellow = false;
				} else if (i > greenTime1) {

					yellow = true;

					if(updateQtable)
					{	laneCounter =0;
						SumoLane lane;
						int stopped = 0;
						if (existAdjacent(0,1)){
							laneCounter++;
						}	
						if (existAdjacent(1 ,0)){
							lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
						stopped += getStoppedVehicles(lane);
						laneCounter++;
							}
						if (existAdjacent(0 ,-1)){
							laneCounter ++ ;
						}
						if (existAdjacent(-1 ,0)){
							lane = new SumoLane(semaphore.getControlledLanes().listIterator(laneCounter).next());
						stopped += getStoppedVehicles(lane);
							
						}
						
						qLearn1.updateTable(greenTime1, action1, stopped);

						action1 = qLearn1.getAction(greenTime1);


						updateGreenTime(2, action2);

						updateQtable = false;
					}
				}

				//System.out.println("Greentime 1: " + greenTime1);
				//System.out.println("Greentime 2: " + greenTime2);


			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//for(int i = 0; i < )

		}
	}


	private void updateGreenTime(int time, int action)
	{
		int timeChange = 5;

		if(time == 1)
			greenTime1 += timeChange*action;
		else if(time == 2)
			greenTime2 += timeChange*action;	

	}

	private int getStoppedVehicles(SumoLane lane)
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
					Str+="Gg";
				else
					Str+="yy";
			}
			if (getAdjacents().contains(righter)) {
				Str+="rr";
			}
			if (getAdjacents().contains(below)) {
				if (!yellow)
					Str+="gG";
				else
					Str+="yy";

			}
			if (getAdjacents().contains(lefter)) {
				Str+="rr";
			}
		} else {
			if (getAdjacents().contains(upper)) {
				Str+="rr";

			}
			if (getAdjacents().contains(righter)) {
				if (!yellow)
					Str+="Gg";
				else
					Str+="yy";
			}
			if (getAdjacents().contains(below)) {

				Str+="rr";

			}
			if (getAdjacents().contains(lefter)) {
				if (!yellow)
					Str+="gG";
				else
					Str+="yy";
			}
		}

		return Str.toString();
	}
private boolean existAdjacent(int x , int y){
	int column = Integer.parseInt(ID.split("/")[0]);
	int line = Integer.parseInt(ID.split("/")[1]);
	String test = Integer.toString(column + x) + "/" + Integer.toString(line + y);
	if (getAdjacents().contains(test)) {
		return true;
	}
	return false;
}
}


