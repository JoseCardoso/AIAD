package agents;

@SuppressWarnings("serial")
public class SemaphoreAgent extends jade.core.Agent{
	String ID;
	public SemaphoreAgent(String string) {
		ID = string;
	}

	public void setup(){
		//esta fun��o inicializa o semaforo
		System.out.println("SEM�FORO INICIALIZADO  "+ ID);
	}
	

}
