package agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestServer extends CyclicBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestServer(Semaphore agent) {
		super(agent);
		//this.agent = agent;
	}

	public void action() {
		System.out.println("action");
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			// Message received. Process it
			String message = msg.getContent();
			System.out.println(message);

		}
		else 
			block();

	}
}