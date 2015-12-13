package agents;

import java.util.ArrayList;
import java.util.HashSet;

import jade.core.AID;
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
				((MessageSemaphore) myAgent).evaluateGreen(message.split(" ")[1]);
				
			}
			else if (message.startsWith("Learn")){
				//System.out.println("recebeu Learn");
				ACLMessage response = msg.createReply();
				response.setContent("NumVehicles "+ ((Semaphore) myAgent).getStoppedVehicles());
				response.setPerformative(ACLMessage.INFORM);
				myAgent.send(response);
			}
			else if (message.startsWith("NumVehicles")){
				//System.out.println("recebeu Resposta do Learn" + message);
				
				int exStop = Integer.parseInt(message.split(" ")[1]);
				((MessageLearnSemaphore) myAgent).addExternalStopped(exStop);		
				
			}

		}
	}
	

}