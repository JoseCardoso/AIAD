package agents;

import java.util.HashSet;
import java.util.Iterator;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import trasmapi.sumo.SumoTrafficLight;

public class MessageSemaphore extends Semaphore {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String ID;
	RequestServer server;
	private HashSet<String> adjacents;
	public MessageSemaphore( String string) {
		super();
		ID = string;
		server = new RequestServer(this);
		
	}

	public void setup() {
		

		if (ID.equals("1/0")){
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		 for (Iterator<String> it = adjacents.iterator(); it.hasNext(); ) {
		        String f = it.next();
		        
		        msg.addReceiver(new AID("Semaphore-" + f, AID.ISLOCALNAME));
		    }
		msg.setContent("Teste");
		send(msg);

		}
		executeSemaphore();
		
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
				
//				SumoLane lane = new SumoLane(semaphore.getControlledLanes().listIterator(0).next());
//				int stopped = getStoppedVehicles(lane);
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
	}*/
	
	
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

}
