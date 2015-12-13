package agents;


import java.util.HashSet;


import trasmapi.sumo.SumoTrafficLight;

public class SemaphoreAgent extends Semaphore{
	/**
	 * 
	 */
	public SemaphoreAgent( String string) {
		super(string);
	}

	public void setup() {
		executeSemaphore();
		// esta funo inicializa o semaforo
		//	System.out.println("SEMAFORO INICIALIZADO  " + ID);
	}
	
	
	public void executeSemaphore() {
		boolean position = true, yellow = false;
		SumoTrafficLight semaphore = new SumoTrafficLight(ID);
		int i = 0;
		while (!stop) {
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
				
				//SumoLane lane = new SumoLane(semaphore.getControlledLanes().listIterator(0).next());
				//int stopped = getStoppedVehicles(lane);
				//System.out.println("Stopped cars in lane "+semaphore.getControlledLanes().listIterator(0).next()+" are: "+stopped);
				
					
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//for(int i = 0; i < )
			
		}
	}

	/*private int getStoppedVehicles(SumoLane lane)
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
	*/
}


