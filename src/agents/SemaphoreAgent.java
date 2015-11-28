package agents;

@SuppressWarnings("serial")
public class SemaphoreAgent extends jade.core.Agent{
	String ID;
	public SemaphoreAgent(String string) {
		ID = string;
	}

	public void setup(){
		//esta função inicializa o semaforo
		System.out.println("SEMÁFORO INICIALIZADO  "+ ID);
	}
	

}
