package start;

public class SemaphoreAgent extends jade.core.Agent {
	String ID;

	public SemaphoreAgent(String string) {
		ID = string;
	}

	public void setup() {
		// esta funo inicializa o semaforo
		System.out.println("SEMAFORO INICIALIZADO  " + ID);
	}

}