package agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestServer extends CyclicBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			String message = msg.getContent();
			if(message.startsWith("Green")){
				//System.out.println("Too many vehicles waiting");
				((MessageSemaphore) myAgent).evaluateGreen(message.split(" ")[1]);
				
			}
			else if(message.equals("Public")){
				System.out.println("Public Vehicle Waiting");
			}

		}
	}
	

}