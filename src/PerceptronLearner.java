import java.util.List;

/**
 * Implementation of the Perceptron Learner
 * @author Abdel Karim Bokharouss
 */

public class PerceptronLearner {
    
    /**
     * The method that implements perceptron learning
     * @param positive list with the positive training patterns
     * @param negative list with the negative training patterns
     * @param bias whether you need a bias for this training set or not
     * @param maxIterations max number of iterations the implementation may take
     * @param queries list of points whose classification form the output
     * @return answer string
     */
    public String execute(List<PVector> positive, List<PVector> negative, Boolean bias, Integer maxIterations, List<PVector> queries)
    {
        // should add a bias term to the vectors in case bias is set to true
        if (bias) {
            addBiasTerm(positive); addBiasTerm(negative); addBiasTerm(queries);
        }
        
        // vector length --> need to know the length of the (to be) randomly initialized weight vector
        int vcLength = queries.get(0).size();
        
        // start algorithm: initialize an arbitrary weight vector: in this implementation a vector with only 1's is used
        PVector weightsPV = PVector.constant(vcLength, 1);
            
        // track the number of iterations since: 1) #iterations should not be allowed to exceed maxIterations 2) #iterations is included in the output
        int iterations = 0;
        boolean weightsChanged; // change tracker
        do {
            weightsChanged = false; // if weights remain unchanged --> terminate loop
            // loop over positive patterns and check whether they are classified properly, else: update weights
            for (PVector p : positive) {
                if (weightsPV.dotProduct(p) <= 0) { // i.e. still not > 0 
                    weightsPV = weightsPV.add(p); // update weight by adding the vector to it
                    weightsChanged = true; // indicate that weight has been changed in the scope of the do-while loop
                }
            }
            // loop over negative patterns and check whether they are classified properly, else: update weights
            for (PVector n : negative) {
                if (weightsPV.dotProduct(n) > 0) { // i.e. still not <= 0 
                    weightsPV = weightsPV.subtract(n); // update weight by subtracting the vector to it
                    weightsChanged = true; // indicate that weight has been changed in the scope of the do-while loop
                }
            }
            iterations++; // update iteration-counter
        } while (iterations <= maxIterations && weightsChanged); // terminate loop if maxIterations has been exceeded or when no change of the weights has occured
        
       
        if (weightsChanged) { // loop terminated because iterations > maxIterations
            return maxIterations.toString();
        } else { // loop terminated because it found a valid solution in iterations <= maxIteerations
            return iterations + " " + classifyQueries(queries, weightsPV);
        }
    }
    
     /**
      * adds a bias term to the vectors
      * @param vectors to which it should be added
      */
    private void addBiasTerm(List<PVector> vectors) {
        vectors.forEach((v) -> {
            v.addCoord(1);
        });
    }
    
    private String classifyQueries(List<PVector> queries, PVector weights) {
        String classification = " "; // String that will be appended and returned
        for (PVector query : queries) { // iterate over sample
            // calculate dot product w * x
            int weigthsDOTquery = weights.dotProduct(query);
            if (weigthsDOTquery > 0 ) {classification += "+";} // if larger than 0: positive classification ("+" in our context)
            else {classification += "-";}; // and negative classifciation ("-" in our context") otherwise (i.e. <= 0)
        }
        // the results is a string with all the classifcication represented by a ("+"/"-")'s that are done for the queries
        return classification; 
    }
}
