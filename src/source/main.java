package source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import trasmapi.genAPI.TraSMAPI;
import trasmapi.genAPI.exceptions.TimeoutException;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoCom;

public class main {
	static public void main(String[] Args) {
		// Sumo initialization.
		Sumo sumo = new Sumo("guisim");
		List<String> params = new ArrayList<String>();
		params.add("-c=TlMap/map.sumo.cfg");
		sumo.addConnections("localhost", 4040);

		// TraSMAPI initialization.
		TraSMAPI api = new TraSMAPI();
		api.addSimulator(sumo);
		try {
			api.launch();
			api.connect();
			api.start();


			while(true)
			{
				int currentStep = SumoCom.getCurrentSimStep();
				if(!api.simulationStep(0))
					break;
			}
			api.close();
		}
		catch(IOException|

				UnimplementedMethod e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(

				TimeoutException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}