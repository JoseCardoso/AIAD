package start;

import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import trasmapi.genAPI.Simulator;
import trasmapi.genAPI.TraSMAPI;
import trasmapi.genAPI.exceptions.TimeoutException;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.Sumo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import agents.AgentManager;

public class Main {

	static boolean JADE_GUI = true;
	private static ProfileImpl profile;
	private static ContainerController mainContainer;

	public static void main(String[] args)
			throws UnimplementedMethod, InterruptedException, IOException, TimeoutException {

		if (JADE_GUI) {
			List<String> params = new ArrayList<String>();
			params.add("-gui");
			profile = new BootProfileImpl(params.toArray(new String[0]));
		} else {
			profile = new ProfileImpl();
		}

		jade.core.Runtime rt = Runtime.instance();
		mainContainer = rt.createMainContainer(profile);

		// Init TraSMAPI framework
		TraSMAPI api = new TraSMAPI();
		// Create SUMO
		Sumo sumo = new Sumo("guisim");
		List<String> params = new ArrayList<String>();
		params.add("-c=maps\\manhattan3\\file.sumocfg");
		sumo.addParameters(params);
		sumo.addConnections("127.0.0.1", 8820);

		// Add Sumo to TraSMAPI
		api.addSimulator(sumo);

		// Launch and Connect all the simulators added
		api.launch();
		api.connect();
		AgentManager manager = new AgentManager(sumo, mainContainer);
		manager.initSemaphores(false);
		manager.startSemaphores();
		api.start();

		while (true) {
			if (!api.simulationStep(0)) {
				break;
			}
		}
	}
}
