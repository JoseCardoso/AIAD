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

	private int greenTime1, greenTime2, yellowTime;

	private Learning qLearn;

	private int action;

	public LearningSemaphore( String string) {
		super(string);
		greenTime1 = 30;
		greenTime2 = 30;
		yellowTime = 2;
		qLearn = new Learning();

		//assumindo que se manteve no início
		action = 0;
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
		int stopped =0;
		int i = 0;
		while (!stop) {
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


					qLearn.updateTable(greenTime2, action, stopped);

					action=qLearn.getAction(greenTime2);
					updateGreenTime(action);

					updateQtable = true;
					stopped = 0;//reset stoppped vehicles
				} else if (i > greenTime2 + greenTime1 + yellowTime) {
					yellow = true;





				} else if (i > greenTime1+yellowTime) {
					position = false;

					yellow = false;
				} else if (i > greenTime1) {

					yellow = true;

					//updates stopped vehicles
					if(updateQtable)
					{	laneCounter =0;
					SumoLane lane;
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


	private void updateGreenTime(int action)
	{
		int timeChange = 5;
		int time1Mult = 0, time2Mult = 0;//multipler to increase/decrease time

		switch (action) {
		case 0:
			time1Mult = 0;
			time2Mult = 0;
			break;
		case 1:

			time1Mult = 1;
			time2Mult = 0;
			break;
		case 2:

			time1Mult = 1;
			time2Mult = -1;
			break;
		case 3:

			time1Mult = 0;
			time2Mult = 1;
			break;
		case 4:

			time1Mult = 1;
			time2Mult = 1;
			break;
		case 5:

			time1Mult = 0;
			time2Mult = -1;
			break;
		case 6:

			time1Mult = -1;
			time2Mult = 1;
			break;
		case 7:

			time1Mult = -1;
			time2Mult = 0;
			break;
		case 8:

			time1Mult = -1;
			time2Mult = -1;
			break;
		default:
			break;
		}
		
		greenTime1 += timeChange*time1Mult;
		greenTime2 += timeChange*time2Mult;	
		
		if(greenTime1 < 20)
			greenTime1 = 20;
		if(greenTime2 < 20)
			greenTime2 = 20;
		if(greenTime1 > 60)
			greenTime1 = 60;
		if(greenTime2 > 60)
			greenTime2 = 60;
		

	}
	
}


