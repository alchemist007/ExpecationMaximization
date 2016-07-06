
/**
 * this class holds the parameters of the Bayes Network that we want to learn.
 * @author nesh2013
 *
 */
public class Theta {
	
	//parameters we want learn. we can find the other 5 by subtracting 1.
	double probG0=0;	//(G)ender: M=0
	double condProbW0_G0=0;	//(W)eight>130=0
	double condProbW0_G1=0;
	double condProbH0_G0=0;	//(H)eight>55=0
	double condProbH0_G1=0;
	
	/**
	 * constructor
	 * @param d probG0
	 * @param e condProbW0_G0
	 * @param f condProbW0_G1
	 * @param g condProbH0_G0
	 * @param h condProbH0_G1
	 */
	public Theta(double d, double e, double f, double g, double h) {
		
		probG0=d;	//(G)ender: M=0
		condProbW0_G0=e;	//(W)eight>130=0
		condProbW0_G1=f;
		condProbH0_G0=g;	//(H)eight>55=0
		condProbH0_G1=h;
		
	}
	
	/**
	 * calcuates P(G=0|W,H). used by EStep in ExpectationMax class
	 * (W,H)=(0,0), (0,1), (1,0), (1,1)
	 * @return returns a double of the calcuation
	 */
	public double getCondProbG0_WH(int x){
		
		/* P(G=0 | W, H)= P(G=0,W,H) / P(W,H) = P(G=0) P(H|G) P(W|G) / P(W,H)
		 * 
		 */

		double prob=0;
		
		if(x==0){
			prob= (probG0*condProbH0_G0*condProbW0_G0) / 
									( ((1-probG0) *condProbH0_G1*condProbW0_G1) + (probG0*condProbH0_G0*condProbW0_G0) );
			
		}
		else if(x==1){
			prob = (probG0*(1-condProbH0_G0)*condProbW0_G0) / 
									( (probG0*(1-condProbH0_G0)*condProbW0_G0) + ( (1-probG0)*(1-condProbH0_G0)*condProbW0_G1) )  ;
			
		}
		else if(x==2){
			prob = (probG0*condProbH0_G0*(1-condProbW0_G0)) / 
									( (probG0*condProbH0_G0*(1-condProbW0_G0)) + ( (1-probG0)*condProbH0_G1*(1-condProbW0_G0) ) );
			
		}
		else if(x==3){
			prob = (probG0*(1-condProbH0_G0)*(1-condProbW0_G0)) / 
									( (probG0*(1-condProbH0_G0)*(1-condProbW0_G0)) + ( (1-probG0)*(1-condProbH0_G0)*(1-condProbW0_G0)) ) ;
			
		}
		
		return prob;
		
	
		
	}
	
	/*move code from ExpectatioMax to here if time permits.
	public double getProbForCompleteData(int x){
		
		double prob=0;
		
		return prob;	
		
	}
	public double getProbForIncompleteData(int x){
		double prob=0;
		
		return prob;
	}*/

}
