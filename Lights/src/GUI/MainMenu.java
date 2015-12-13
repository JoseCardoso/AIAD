package GUI;

import javax.swing.JFrame;
import javax.swing.JButton;
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
import jade.wrapper.AgentContainer;
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
	static TraSMAPI api;
	static AgentManager manager;
	static Vector<String> maps = new Vector<String>();
	static boolean JADE_GUI = true;
	static jade.core.Runtime rt;
	private static ProfileImpl profile;
	static Thread simulationThread;
	static Thread counterThread;
	static boolean stop = false;
	static int timeCounter = 0;
	static JLabel time;

	public static void main(String[] args) {
		MainMenu menu = new MainMenu();
		menu.setVisible(true);
		menu.setResizable(false);
		menu.setBounds(500, 500, 315, 170);
		menu.addWindowListener(new java.awt.event.WindowAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if(stop = false)
				try {
					stop = true;
					if (manager!= null){
						manager.closeSemaphores();
					Thread.sleep(250);}// wait for semaphores
					if(api != null)
						api.close();

				} catch (UnimplementedMethod | IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.exit(0);

			}
		});
	}

	public MainMenu() {
		maps.add("pequeno");
		maps.add("medio");
		maps.add("grande");
		Vector<String> agentTypes = new Vector<String>();
		agentTypes.add("Normal");
		agentTypes.add("Message");
		agentTypes.add("Learning");
		agentTypes.add("Learning with Message");
		getContentPane().setLayout(null);

		JButton btnStart = new JButton("Start");
		btnStart.setBounds(0, 110, 312, 25);

		getContentPane().add(btnStart);

		JLabel lblMap = new JLabel("Mapa:");
		lblMap.setBounds(67, 16, 47, 16);
		getContentPane().add(lblMap);

		JComboBox<String> map = new JComboBox<String>(maps);
		map.setBounds(116, 13, 172, 22);
		getContentPane().add(map);

		JLabel lblAgentType = new JLabel("Tipo de Agente:");
		lblAgentType.setBounds(12, 51, 135, 16);
		getContentPane().add(lblAgentType);

		JComboBox<String> type = new JComboBox<String>(agentTypes);
		type.setBounds(116, 48, 172, 22);
		getContentPane().add(type);
		
		time = new JLabel("Tempo decorrido: " + timeCounter + " sec");
		time.setBounds(0, 81, 288, 16);
		getContentPane().add(time);

		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				simulationThread = new Thread(new Runnable() {
					public void run() {
						try {
							start(map.getSelectedIndex(), type.getSelectedIndex());
						} catch (UnimplementedMethod | InterruptedException | IOException | TimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				simulationThread.start();
				counterThread = new Thread(new Runnable() {
					public void run() {
						System.out.println("aqui");
						countCars();
						
					}
				});
				counterThread.start();

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

		rt = Runtime.instance();
		ContainerController mainContainer = rt.createMainContainer(profile);

		// Init TraSMAPI framework
		api = new TraSMAPI();
		// Create SUMO
		Sumo sumo = new Sumo("guisim");
		List<String> params = new ArrayList<String>();
		params.add("-c=maps\\" + maps.get(mapName) + "\\file.sumocfg");
		sumo.addParameters(params);
		sumo.addConnections("127.0.0.1", 8820);

		// Add Sumo to TraSMAPI
		api.addSimulator(sumo);

		// Launch and Connect all the simulators added
		api.launch();
		api.connect();
		manager = new AgentManager(sumo, mainContainer);
		manager.initSemaphores(agentType);
		manager.startSemaphores();
		api.start();

		while (!stop) {
			if (!api.simulationStep(0)) {
				break;
			}
		}
	}

	private void countCars() {
		while (!stop) {
			try {
				Thread.sleep(1000);
				timeCounter++;
				time.setText("Tempo decorrido: " + timeCounter + " sec");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (manager.getNumVehicles() == 0) {
				try {
					stop = true;
					manager.closeSemaphores();
					Thread.sleep(250);// wait for semaphores
					api.close();

				} catch (UnimplementedMethod | IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		
	}
}
