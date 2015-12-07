package agents;


import java.util.HashSet;

import trasmapi.sumo.Sumo;

public class SemaphoreAgent extends jade.core.Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String ID;
	private HashSet<String> adjacents;
	private Sumo sumo;
	
	public SemaphoreAgent(Sumo sumo, String string) {
		super();
		ID = string;
		this.sumo = sumo;
		
		new Thread(new SemaphoreThread(sumo, this)).start();
	}

	public void setup() {
		// esta funo inicializa o semaforo
		//	System.out.println("SEMAFORO INICIALIZADO  " + ID);
	}

	public HashSet<String> getAdjacents() {
		return adjacents;
	}

	public void setAdjacents(HashSet<String> adjacents) {
		this.adjacents = adjacents;
	}



}