package agents;


import java.util.HashSet;

public class SemaphoreAgent extends jade.core.Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String ID;
	private HashSet<String> adjacents;

	public SemaphoreAgent(String string) {
		super();
		ID = string;
	}

	public void setup() {
		// esta funo inicializa o semaforo
		System.out.println("SEMAFORO INICIALIZADO  " + ID);
	}

	public HashSet<String> getAdjacents() {
		return adjacents;
	}

	public void setAdjacents(HashSet<String> adjacents) {
		this.adjacents = adjacents;
	}

	
	
}