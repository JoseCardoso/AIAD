package source;

import java.util.ArrayList;
import java.util.List;

import trasmapi.genAPI.TraSMAPI;
import trasmapi.sumo.Sumo;


public class main {
	// Sumo initialization.
    Sumo sumo = new Sumo("guisim");
    List<String> params = new ArrayList<String>();
    params.add("-c=TlMap/map.sumo.cfg");

    try {
//        SumoConfig conf = SumoConfig.load("TlMap/map.sumo.cfg");
//        sumo.addParameters(params);
//        sumo.addConnections("localhost", conf.getLocalPort());
    } catch (Exception e) {
        e.printStackTrace();
    }

    // TraSMAPI initialization.
    final TraSMAPI api = new TraSMAPI(); 
    api.addSimulator(sumo);
    api.launch();
    api.connect();
    api.start();  
}
