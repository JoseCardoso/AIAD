package GUI;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;

import agents.AgentManager;
import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import trasmapi.genAPI.TraSMAPI;
import trasmapi.genAPI.exceptions.TimeoutException;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.Sumo;

import javax.swing.JComboBox;

public class MainMenu extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	static Vector<String>maps = new Vector<String>();
	static boolean JADE_GUI = true;
	private static ProfileImpl profile;
	private static ContainerController mainContainer;
	public  static void main (String[] args){
		MainMenu menu = new MainMenu();
		menu.setVisible(true);
		menu.setResizable(false);
		menu.setBounds(500, 500, 315, 150);
	}
	public MainMenu() {
		maps.add("manhattan3");
		maps.add("manhattan10");
		Vector<String>agentTypes = new Vector<String>();
		agentTypes.add("Normal");
		agentTypes.add("Message");
		agentTypes.add("Learning");
		agentTypes.add("Learning with Message");
		getContentPane().setLayout(null);

		JButton btnStart = new JButton("Start");
		btnStart.setBounds(0, 91, 312, 25);
		
		getContentPane().add(btnStart);

		JLabel lblMap = new JLabel("Mapa:");
		lblMap.setBounds(67, 16, 47, 16);
		getContentPane().add(lblMap);

		JComboBox map = new JComboBox(maps);
		map.setBounds(116, 13, 172, 22);
		getContentPane().add(map);

		JLabel lblAgentType = new JLabel("Tipo de Agente:");
		lblAgentType.setBounds(12, 51, 135, 16);
		getContentPane().add(lblAgentType);

		JComboBox type= new JComboBox(agentTypes);
		type.setBounds(116, 48, 172, 22);
		getContentPane().add(type);
		
		btnStart.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					start(map.getSelectedIndex(),type.getSelectedIndex());
				} catch (UnimplementedMethod | InterruptedException | IOException | TimeoutException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
	}



	public static void start(int mapName, int agentType)
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
		params.add("-c=maps\\"+maps.get(mapName)+"\\file.sumocfg");
		sumo.addParameters(params);
		sumo.addConnections("127.0.0.1", 8820);

		// Add Sumo to TraSMAPI
		api.addSimulator(sumo);

		// Launch and Connect all the simulators added
		api.launch();
		api.connect();
		AgentManager manager = new AgentManager(sumo, mainContainer);
		manager.initSemaphores(agentType);
		manager.startSemaphores();
		api.start();

		while (true) {
			if (!api.simulationStep(0)) {
				break;
			}
		}
	}
}