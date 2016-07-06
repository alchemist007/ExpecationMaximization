/**
 * This class is for Expectation Maximization class. It contains all things related to the Expectation Maximization algo
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class ExpectationMax {
	
	Theta theta;	// current estimated parameters.
	
	int[] completeDataCount;	//totalcount for all complete data row
	int[] missingDataCount;		//total coutn for missing data row.
	
	
	double[] condProbG0_WH;	//holds P(G=0|W,H,theta). all values of W,H
	double[] condProbG1_WH;	//just 1-condProbG0
	
	
	int totalDataCount=0;	//total number of data points.
	
	double likelihood=0;
	
	final double THRESHOLD=0.00001;	//threshold for diff in likelihood.
	
	
	//expected counts from the E Step
	double expectedCountsG0=0;
	double expectedCountsG1=0;
	//double expectedCountsW0=0;
	//double expectedCountsH0=0;
	double expectedCountsW0G0=0;
	double expectedCountsW0G1=0;
	double expectedCountsH0G0=0;
	double expectedCountsH0G1=0;
	
	
	/**
	 * Constructor
	 * @param initial is the initial random guess of the parameters.
	 */
	public ExpectationMax(Theta initial){
		
		completeDataCount=new int[8];	//holds total counts of 000, 001, 010,...,111
								
		
		missingDataCount= new int[8];
		
		theta=initial;	//initialize with first random guess. starting point.
		
		condProbG0_WH=new double[4];	//holds P(G=0|W,H,theta). Used in E step
		condProbG1_WH=new double[4]; 
		
	}
	/**
	 * E-step  
	 * Estimate the missing data by Calculating the P(G=0|W,H,theta)
	 * using above calculate expected counts.
	 */
	public void EStep(){
		
		//set the P(G=0|W,H,theta)
		for(int i=0;i<4;i++){
			condProbG0_WH[i]=theta.getCondProbG0_WH(i);
			//calcuate the prob of females and store in condProbG1
			condProbG1_WH[i]=1-condProbG0_WH[i];
		}
		
		
		//in order to recalculate Theta values in M step we need expected counts of: G=0, W=0, G=1, H=0
		//later optimize by looping 
		expectedCountsG0=completeDataCount[0]+completeDataCount[1]+completeDataCount[2]+completeDataCount[3] +
							(missingDataCount[0]*condProbG0_WH[0]) +
							(missingDataCount[1]*condProbG0_WH[1]) +
							(missingDataCount[2]*condProbG0_WH[2]) +
							(missingDataCount[3]*condProbG0_WH[3]);
		
		expectedCountsG1=completeDataCount[4]+completeDataCount[5]+completeDataCount[6]+completeDataCount[7] +
							(missingDataCount[4]*condProbG1_WH[0]) +
							(missingDataCount[5]*condProbG1_WH[1]) +
							(missingDataCount[6]*condProbG1_WH[2]) +
							(missingDataCount[7]*condProbG1_WH[3]);
		
		
		expectedCountsW0G0=completeDataCount[0]+completeDataCount[1] +
							(missingDataCount[0]*condProbG0_WH[0]) +
							(missingDataCount[1]*condProbG0_WH[1]);
		
		expectedCountsW0G1=completeDataCount[4]+completeDataCount[5] +
							(missingDataCount[4]*condProbG1_WH[0]) +
							(missingDataCount[5]*condProbG1_WH[1]);
		
		expectedCountsH0G0=completeDataCount[0]+completeDataCount[2] +
						(missingDataCount[0]*condProbG0_WH[0]) +
						(missingDataCount[2]*condProbG0_WH[2]);
		
		expectedCountsH0G1=completeDataCount[4]+completeDataCount[6] +
						(missingDataCount[4]*condProbG1_WH[0]) +
						(missingDataCount[6]*condProbG1_WH[2]);
		
							
		/* dont need this 
		expectedCountsW0=completeDataCount[0]+completeDataCount[1]+completeDataCount[4]+completeDataCount[5] +
							(missingDataCount[0]*condProbG0_WH[0]) + 
							(missingDataCount[0]*condProbG0_WH[1]) +
							(missingDataCount[4]*condProbG1_WH[0]) + 
							(missingDataCount[5]*condProbG1_WH[1]);
		
		expectedCountsH0=completeDataCount[0]+completeDataCount[2]+completeDataCount[4]+completeDataCount[6] +
							(missingDataCount[0]*condProbG0_WH[0]) +
							(missingDataCount[2]*condProbG0_WH[2]) + 
							(missingDataCount[4]*condProbG1_WH[0]) + 
							(missingDataCount[6]*condProbG1_WH[2]);
		*/
		
		/* for debug
		System.out.println("----------ESTEP calculation-----------");
		System.out.println(this.expectedCountsG0);
		System.out.println(this.expectedCountsG1);
		//System.out.println(this.expectedCountsH0);
		//System.out.println(this.expectedCountsW0);
		System.out.println("---------------------------------------");
		*/
	}
	
	/**
	 * this is the MStep. It recalculates the theta using the expected coounts of EStep
	 */
	public void MStep(){
		
		//calculate new theta and update theta values
		theta.probG0= expectedCountsG0 / (expectedCountsG0+expectedCountsG1);
		
		theta.condProbW0_G0= expectedCountsW0G0 / expectedCountsG0;
		
		theta.condProbW0_G1= expectedCountsW0G1 / expectedCountsG1;
		
		theta.condProbH0_G0= expectedCountsH0G0 / expectedCountsG0;
		
		theta.condProbH0_G1= expectedCountsH0G1 / expectedCountsG1;

	}
	

	/**
	 * getter for thetaValues. Shows in human readable format.
	 */
	public void getThetaValues(){
		
		
		System.out.println("--------Theta Values-------------");
		System.out.println("P(G=0)= " + this.theta.probG0);
		System.out.println("P(W=0|G=0)= " + this.theta.condProbW0_G0);
		System.out.println("P(W=0|G=1)= " + this.theta.condProbW0_G1);
		System.out.println("P(H=0|G=0)= " + this.theta.condProbH0_G0);
		System.out.println("P(H=0|G=1)= " + this.theta.condProbH0_G1);
		System.out.println("--------------------------------");
		
	}

	/**
	 * calculates likelihood of the data given theta. P(D|theta)
	 * @return
	 */
	public double calculateLikelihood() {
		
		double likelihood=0;
		
		double completeData = 
				
//				Math.pow(theta.probG0 * theta.condProbW0_G0 * theta.condProbH0_G0, completeDataCount[0] ) +
//				Math.pow(theta.probG0 * theta.condProbW0_G0 * (1-theta.condProbH0_G0), completeDataCount[1]  ) +
//				Math.pow(theta.probG0 * (1-theta.condProbW0_G0) * theta.condProbH0_G0, completeDataCount[2] ) +
//				Math.pow( (theta.probG0 * (1-theta.condProbW0_G0) * (1-theta.condProbH0_G0) ),  completeDataCount[3]  ) +
//				
//				Math.pow( (1-theta.probG0) * theta.condProbW0_G0 * theta.condProbH0_G0, completeDataCount[4] ) +
//				Math.pow( (1-theta.probG0) * theta.condProbW0_G0 * (1-theta.condProbH0_G0), completeDataCount[5] ) +
//				Math.pow( (1-theta.probG0) * (1-theta.condProbW0_G0) * theta.condProbH0_G0, completeDataCount[6] ) +
//				Math.pow( (1-theta.probG0) * (1-theta.condProbW0_G0) * (1-theta.condProbH0_G0), completeDataCount[7] );
			
				
				
				
		/* for calculating incomplete data likelihood
		Suppose you want to calculate for (G,W,H)=(-,0,0)
		( P(W=0|G=0)* P(H=0|G=0) *P(G=0) + P(W=0|G=1)* P(H=0|G=1) *P(G=1) ) ^ (number of missing data with W=0,G=0)
		*/
		
//		double W0H0= (theta.condProbW0_G0 * theta.condProbH0_G0 * theta.probG0) +
//														(theta.condProbW0_G1* theta.condProbH0_G1 * (1-theta.probG0) );
//		
//		double W0H1= (theta.condProbW0_G0 * (1-theta.condProbH0_G0) * theta.probG0) +
//														(theta.condProbW0_G1* (1-theta.condProbH0_G1) * (1-theta.probG0) );
//		
//		double W1H0= ( (1-theta.condProbW0_G0) * theta.condProbH0_G0 * theta.probG0) +
//														((1-theta.condProbW0_G1) * theta.condProbH0_G1 * (1-theta.probG0) );
//		double W1H1= ( (1-theta.condProbW0_G0) * (1-theta.condProbH0_G0) * theta.probG0) +
//														((1-theta.condProbW0_G1) * (1-theta.condProbH0_G1) * (1-theta.probG0) );
//		
//		double incompleteData= Math.pow(W0H0, missingDataCount[0]) +
//								Math.pow(W0H1, missingDataCount[1]) +
//								Math.pow(W1H0, missingDataCount[2]) +
//								Math.pow(W1H1, missingDataCount[3]);
				
				( (theta.probG0 * theta.condProbW0_G0 * theta.condProbH0_G0) * completeDataCount[0] ) +
				( (theta.probG0 * theta.condProbW0_G0 * (1-theta.condProbH0_G0) ) * completeDataCount[1]  ) +
				( (theta.probG0 * (1-theta.condProbW0_G0) * theta.condProbH0_G0)* completeDataCount[2] ) +
				( (theta.probG0 * (1-theta.condProbW0_G0) * (1-theta.condProbH0_G0) )* completeDataCount[3]  ) +
				
				( ( (1-theta.probG0) * theta.condProbW0_G0 * theta.condProbH0_G0) * completeDataCount[4] ) +
				( ( (1-theta.probG0) * theta.condProbW0_G0 * (1-theta.condProbH0_G0) )* completeDataCount[5] ) +
				( ( (1-theta.probG0) * (1-theta.condProbW0_G0) * theta.condProbH0_G0 ) * completeDataCount[6] ) +
				( ( (1-theta.probG0) * (1-theta.condProbW0_G0) * (1-theta.condProbH0_G0)) * completeDataCount[7] );
		
		
		/* for calculating incomplete data likelihood
		Suppose you want to calculate for (G,W,H)=(-,0,0)
		( P(W=0|G=0)* P(H=0|G=0) *P(G=0) + P(W=0|G=1)* P(H=0|G=1) *P(G=1) ) ^ (number of missing data with W=0,G=0)
		*/
		
		double W0H0= (theta.condProbW0_G0 * theta.condProbH0_G0 * theta.probG0) +
														(theta.condProbW0_G1* theta.condProbH0_G1 * (1-theta.probG0) );
		
		double W0H1= (theta.condProbW0_G0 * (1-theta.condProbH0_G0) * theta.probG0) +
														(theta.condProbW0_G1* (1-theta.condProbH0_G1) * (1-theta.probG0) );
		
		double W1H0= ( (1-theta.condProbW0_G0) * theta.condProbH0_G0 * theta.probG0) +
														((1-theta.condProbW0_G1) * theta.condProbH0_G1 * (1-theta.probG0) );
		double W1H1= ( (1-theta.condProbW0_G0) * (1-theta.condProbH0_G0) * theta.probG0) +
														((1-theta.condProbW0_G1) * (1-theta.condProbH0_G1) * (1-theta.probG0) );
		
		double incompleteData= (W0H0*missingDataCount[0]) +
								(W0H0*missingDataCount[1]) +
								(W1H0*missingDataCount[2]) +
								(W1H1 * missingDataCount[3]);
				
			
		//likelihood= Math.log( completeData+ incompleteData);
		likelihood=completeData+incompleteData;
		return likelihood;
	}
	
	/**
	 * reads in the input file. data is missing only on G and is binary.
	 * @param inputFile
	 */
	public void readData(File inputFile) {
		
		String line;
		int inputAsInt;
		
		try{
			Scanner inFile=new Scanner(inputFile);
			
			inFile.nextLine();	//skip row 1
			while(inFile.hasNext()){
			
				totalDataCount++;

	            //convert input to binary string by removing whitespace
	            line = inFile.nextLine().replaceAll("\\s", "");	//make into binary string
	            
	            //System.out.println(line); debug
	            
	            
	            /*
	             000=0 and completeDataCount[0] holds count of all G=0,H=0,W=0
	             001=1 and completeDataCount[0] holds count of all G=0,H=0,W=1
	             .
	             .
	             .
	             111=7 and completeDataCount[7] holds count of all G=1,H=1,W=1
	             */
	            if(line.charAt(0)=='-'){
	            	inputAsInt=Integer.parseInt(line.substring(1,2));	//convert to int and store the coount of it.
	            	
	            	missingDataCount[inputAsInt]++;	//for case male	
	            	missingDataCount[4+inputAsInt]++;	//for case female.
	            }
	            else{
	            	inputAsInt=Integer.parseInt(line,2);
	            	completeDataCount[inputAsInt]++;
	            }
			}
			
            inFile.close();
		}
		
    	catch (FileNotFoundException e) {
	        System.out.println("File not found");
	        System.exit(1);
    	} 
		
	}
	
	public void run(){
		double currentLikelihood=this.calculateLikelihood();	//to start the while loop.
		double newLikelihood=0;
		
		int i=1;
		while( Math.abs(currentLikelihood-newLikelihood) > this.THRESHOLD){	//until threhold falls below .0001
			
			
			System.out.println("__________________________ITERATION "+i++ +"_____________________");
			
			currentLikelihood=this.calculateLikelihood();
			System.out.println("Current likelihood: "+currentLikelihood);
			
			
			this.getThetaValues();

			this.EStep();	//do estep
			this.MStep();	//do mstep
			
			newLikelihood=this.calculateLikelihood();
			System.out.println("New likelihood after M step: "+ newLikelihood);
			
			System.out.println("____________________________________________________________");
			System.out.println();
		}
	}

}
