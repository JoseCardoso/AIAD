package agents;

import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoTrafficLight;

public class SemaphoreThread implements Runnable {

	private Sumo sumo;
	private SemaphoreAgent agent;

	public SemaphoreThread(Sumo sumo, SemaphoreAgent agent) {
		this.sumo = sumo;
		this.agent = agent;
	}

	public void run() {
		boolean position = true, yellow = false;
		SumoTrafficLight semaphore = new SumoTrafficLight(agent.ID);
		int i = 0;
		while (true) {
			// states of all semaphores
			if (agent.getAdjacents().size() == 4 && position) {
				if (!yellow)
					semaphore.setState("rrrGGgrrrgGG");
				else
					semaphore.setState("rrryyyrrryyy");
			} else if (agent.getAdjacents().size() == 4 && !position) {
				if (!yellow)
					semaphore.setState("GGgrrrGGgrrr");
				else
					semaphore.setState("yyyrrryyyrrr");
			} else if (agent.getAdjacents().size() == 3) {

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

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String generateState(boolean position, boolean yellow) {
		String Str = "";
		int column = Integer.parseInt(agent.ID.split("/")[0]);
		int line = Integer.parseInt(agent.ID.split("/")[1]);
		String upper = Integer.toString(column) + "/" + Integer.toString(line + 1);
		String righter = Integer.toString(column + 1) + "/" + Integer.toString(line);
		String below = Integer.toString(column) + "/" + Integer.toString(line - 1);
		String lefter = Integer.toString(column - 1) + "/" + Integer.toString(line);
		if (position) {
			if (agent.getAdjacents().contains(upper)) {
				if (!yellow)
					Str+="Gg";
				else
					Str+="yy";
			}
			if (agent.getAdjacents().contains(righter)) {
				Str+="rr";
			}
			if (agent.getAdjacents().contains(below)) {
				if (!yellow)
					Str+="gG";
				else
					Str+="yy";

			}
			if (agent.getAdjacents().contains(lefter)) {
				Str+="rr";
			}
		} else {
			if (agent.getAdjacents().contains(upper)) {
				Str+="rr";

			}
			if (agent.getAdjacents().contains(righter)) {
				if (!yellow)
					Str+="Gg";
				else
					Str+="yy";
			}
			if (agent.getAdjacents().contains(below)) {

				Str+="rr";

			}
			if (agent.getAdjacents().contains(lefter)) {
				if (!yellow)
					Str+="gG";
				else
					Str+="yy";
			}
		}

		return Str.toString();
	}

}