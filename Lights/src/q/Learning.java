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
	private ArrayList<Double[]> qTable;

	private double maxQuality;
	
	
	
	public Learning(double learning_rate, double discount_factor, double temperature) {
		this.learning_rate = learning_rate;
		this.discount_factor = discount_factor;
		this.temperature = temperature;
		maxQuality = 0;
		qTable = new ArrayList<Double[]> ();
	}

	public Learning() {
		maxQuality = 0;
		qTable = new ArrayList<Double[]> ();
	}
	
	
	public void updateTable(int state, int action, int stoppedCars)
	{
		//stopped cars are bad
		int reforce = 100-stoppedCars;
		boolean exists = false;
		double quality = 0.0;
		
		for(int i = 0; i < qTable.size();i++)
		{
			//if this state and action already exist
			if(qTable.listIterator(i).next()[0] == state &&
					qTable.listIterator(i).next()[1] == action)
			{
				exists = true;
				quality = qTable.listIterator(i).next()[2];
				
				quality+= learning_rate*( reforce - discount_factor* (maxQuality-quality) );
				qTable.listIterator(i).next()[2] = quality;
				
				break;
			}
		}
		
		if(!exists)
		{
			quality = learning_rate*reforce;
			Double[] estado = new Double[3];
			estado[0] = (double) state;
			estado[1] = (double) action;
			estado[2] = quality;
			qTable.add(estado);
		}
		
		if(quality > maxQuality)
			maxQuality = quality;
		
		
	}
	
	
	public int getAction(int state)
	{
		int a;
		int bestAction = getBestAction(state);
		double numerator = Math.exp(bestAction)/temperature;
		double denominator = 0.0;
		double prob = 0.0;
		double choice = Math.random();
		
		for(int i = 0; i < qTable.size();i++)
		{
			//Double[] cenas = qTable.listIterator(i).next();
			//System.out.println(cenas);
			denominator	+= 	Math.exp(qTable.listIterator(i).next()[1])/temperature;	
		}
		
		
		prob = numerator/denominator;
		
		
		
		if(choice < prob && denominator> 0)//para prob não dar infinito
			return bestAction;
		else	
			return randomAction();
	}
	
	//gera acções entre 0 e 8 ( descresce, 0 mantém e 1 sobe o tempo verde)
	private int randomAction() {
		// TODO Auto-generated method stub		
		Random r = new Random();
		int rand = r.nextInt(8);
		return rand;
	}

	private int getBestAction(int state)
	{
		double bestAction = 0;
		double bestQuality = 0;
		for(int i = 0; i < qTable.size();i++)
		{
			
			if(qTable.listIterator(i).next()[0] == state 
					&& qTable.listIterator(i).next()[2] > bestQuality )
				bestAction = qTable.listIterator(i).next()[1];
			
		}
		
		
		return (int) bestAction;
	}
	
	
	
	
	
}
