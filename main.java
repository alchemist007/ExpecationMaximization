/**
 * this is the main class where the ExpectationMax instance is created and run. It is run for differnt start points
 * must refactor if time permits
 */

import java.io.File;
import java.util.Arrays;


public class main {
	
	public static void main(String[] args) throws Exception {
		
		File inputFile;
		
		if(args.length>0){
			inputFile=new File(args[0]);
		}
		else{
			throw new Exception("Please specify an input file");		
		}
		
		//various starting points. last iteration is the final theta values.
		
		System.out.println("*******starting point: P(G=0.7), P(W=0|G=0)= 0.8, P(W=0|G=1)= 0.4, P(H=0|G=0)= 0.7, P(H=0|G=1)=0.3*****");
		Theta initial= new Theta(0.6, 0.5, 0.4, 0.7, 0.3);
		//instantiate Expectation Max object
		ExpectationMax emax=new ExpectationMax(initial);
		//read data from file
		emax.readData(inputFile);
		emax.run();
		System.out.println("*****************************************END************************************************************");
		System.out.println();
		
		
		System.out.println("*******starting point: P(G=0.5), P(W=0|G=0)= 0.5, P(W=0|G=1)= 0.5, P(H=0|G=0)= 0.5, P(H=0|G=1)=0.5*****");
		Theta initial1= new Theta(0.5, 0.5, 0.5, 0.5, 0.5);
		//instantiate Expectation Max object
		ExpectationMax emax1=new ExpectationMax(initial1);
		//read data from file
		emax1.readData(inputFile);
		emax1.run();
		System.out.println("*****************************************END************************************************************");
		System.out.println();
		
		System.out.println("*******starting point: P(G=0.2), P(W=0|G=0)= 0.3, P(W=0|G=1)= 0.4, P(H=0|G=0)= 0.7, P(H=0|G=1)=0.8*****");
		Theta initial3= new Theta(0.2, 0.3, 0.4, 0.7, 0.8);
		//instantiate Expectation Max object
		ExpectationMax emax3=new ExpectationMax(initial3);
		//read data from file
		emax3.readData(inputFile);
		emax3.run();
		System.out.println("*****************************************END************************************************************");
		System.out.println();
		

		
		
		/******************************Below this is all testing code************************************/
		
		
		/*testing EStep() and MStep() 
		for(int i=0;i<5;i++){
			
			System.out.println("Iteration "+i);
			emax.getThetaValues();
			emax.EStep();
			emax.MStep();
			
		}*/
		
		
		/*for testing missingDataCount. should be 15*2=30
		int sum=0;
		for(int i=0; i<emax.missingDataCount.length;i++){
			sum=sum+emax.missingDataCount[i];
			//System.out.println(emax.completeDataCount[i]);
		}
		System.out.println(sum);
		*/
		
		/*for testing the array which holds total rows accoridign to 000....111
		int sum=0;
		for(int i=0; i<emax.completeDataCount.length;i++){
			sum=sum+emax.completeDataCount[i];
			//System.out.println(emax.completeDataCount[i]);
		}
		System.out.println("sum of completeDataCount "+sum);
		System.out.println(emax.totalDataCount);
		*/
		
		/* testing getCondProbG0_WH(int x) 
		
		System.out.println("P(G=0|W,H)");
		System.out.println(Arrays.toString(emax.condProbG0_WH));
		System.out.println("P(G=1|W,H)");
		System.out.println(Arrays.toString(emax.condProbG1_WH));
		*/
		
		
		/*
		System.out.println(emax.totalDataCount);	//for debug
		System.out.println(Arrays.toString(emax.completeDataCount));
		System.out.println(Arrays.toString(emax.missingDataCount));
		*/

		/* testing 
		Theta initial= new Theta(0.7, 0.8, 0.4, 0.7, 0.3);
		
		//instantiate Expectation Max object
		ExpectationMax emax=new ExpectationMax(initial);
		
		emax.readData(inputFile);
		
		
		
		System.out.println("initial theta");
		emax.getThetaValues();
		
		System.out.println("Likelihood= "+emax.calculateLikelihood());
		
		emax.EStep();
		emax.MStep();
		System.out.println("theta after MSTEP");
		emax.getThetaValues();
		*/
	

		
		
		
	}

}
