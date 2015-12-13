package q;

import java.util.ArrayList;
import java.util.Random;

import javax.print.attribute.standard.RequestingUserName;
public class Learning {

	// 0 < alfa <= 1
	private double learning_rate = 1.0;

	//0 < gamma <= 1
	private double discount_factor = 0.5;

	//softmax temperature	
	private double temperature = 0.5;

	//list of (State, Action, Quality)
	private double[][] qTable;

	private double maxQuality;

	//SemaphoreTime must be between 20 and 60, with 5 secs intervals (8 states)
	private int qStates = 9;
	//Actions in both semaphore pairs
	private int qActions = 9;


	public Learning(double learning_rate, double discount_factor, double temperature) {
		this.learning_rate = learning_rate;
		this.discount_factor = discount_factor;
		this.temperature = temperature;
		maxQuality = 0;
		initializeQtable();
	}

	public Learning() {
		maxQuality = 0;
		initializeQtable();
	}


	public void initializeQtable()
	{
		qTable = new double[qStates][];

		for(int i = 0; i < qStates;i++)
			qTable[i] = new double[qActions];


		for(int i = 0; i < qStates;i++)
			for(int j = 0; j < qActions;j++)
				qTable[i][j] = 1.0;


	}

	public void updateTable(int state, int action, int stoppedCars)
	{
		//stopped cars are bad
		int reforce = 100-stoppedCars;
		double quality = 0.0;


		quality = qTable[state][action];

		quality+= learning_rate*( reforce - discount_factor* (maxQuality-quality) );
		qTable[state][action] = quality;


		if(quality > maxQuality)
			maxQuality = quality;


	}


	public int getAction(int state)
	{
		double denominator = 0.0;
		double prob = 0.0;
		double choice = Math.random();

		int bestAction = getBestAction(state);
		double numerator = Math.exp(bestAction)/temperature;

		for(int i = 0; i < qStates;i++)
			for(int j = 0; j < qActions;j++)
				denominator	+= 	Math.exp(qTable[i][j])/temperature;

		prob = numerator/denominator;



		if(choice < prob)//para prob não dar infinito
			return bestAction;
		else	
			return randomAction();
	}

	//gera acções entre 0 e 8 ( descresce, 0 mantém e 1 sobe o tempo verde)
	private int randomAction() {
		// TODO Auto-generated method stub		
		Random r = new Random();
		int rand = r.nextInt(qActions);
		return rand;
	}

	private int getBestAction(int state)
	{

		//state should be between 20 and 60 in ranks of five
		int innerState = (state-20)/5;

		int bestAction = 0;



		for(int j = 0; j < qActions;j++)
			if(qTable[innerState][j] > maxQuality )
				bestAction = j;


		return (int) bestAction;
	}





}
