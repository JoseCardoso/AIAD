package source;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import agents.SemaphoreAgent;
import agents.SemaphoreManager;
import trasmapi.genAPI.Simulator;
import trasmapi.genAPI.TraSMAPI;
import trasmapi.genAPI.exceptions.TimeoutException;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.Sumo;

import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import start.ODManager;

public class Main {
	
	static boolean JADE_GUI = true;
	private static ProfileImpl profile;
	private static ContainerController mainContainer;
	
	public static void main(String[] args) throws UnimplementedMethod, InterruptedException, IOException, TimeoutException {	

		//Init JADE platform w/ or w/out GUI
		if(JADE_GUI){
			List<String> params = new ArrayList<String>();
			params.add("-gui");
			profile = new BootProfileImpl(params.toArray(new String[0]));
		} else
			profile = new ProfileImpl();

		Runtime rt = Runtime.instance();
		
		//mainContainer - agents' container
		mainContainer = rt.createMainContainer(profile);
		
		TraSMAPI api = new TraSMAPI(); 

		//Create SUMO
		Simulator sumo = new Sumo("guisim");
		List<String> params = new ArrayList<String>();
		// get current path
		Path currentRelativePath = Paths.get("");
		String current_path = currentRelativePath.toAbsolutePath().toString();
		//add to params
		params.add("-c="+current_path +"/bin/Maps/cross1ltl/cross1ltl.sumocfg");
		sumo.addParameters(params);
		
		sumo.addConnections("127.0.0.1", 8870);
		
		//Add Sumo to TraSMAPI
		api.addSimulator(sumo);
		
		//Launch and Connect all the simulators added
		api.launch();

		api.connect();

		SemaphoreManager manager = new SemaphoreManager(mainContainer);
		
		
		api.start();
		//simulation loop
		while(true)
			if(!api.simulationStep(0))
				break;
	}	
}
