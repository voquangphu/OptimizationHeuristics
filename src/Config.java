public class Config {

    private int numParameter = 50; // number variables to estimate
    private boolean maxOptimise = true; // true: max opimisation, false: min optimisation
    private int algorithm = 3; // (1: hill climbing, 2: simulated annealing, 3: genetic algorithm)
    private int maxIteration = 100; // (condition for termination, max number of iteration, 0 for indefinite)
    private int numSolutions = 100; // (number of members in a population for genetic algorithm)
    private double crossoverProbability = 0.3;
    private double mutationProbability = 0.3;
    private double changePerStep = 0.1;

    public int getAlgorithm() {
        return algorithm;
    }

    public int getNumParameter() {
        return numParameter;
    }

    public int getNumSolutions() {
        return numSolutions;
    }

    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public double getChangePerStep() {
        return changePerStep;
    }

    public int getMaxIteration() {
        return maxIteration;
    }

    public boolean getMaxOptimise() {
        return maxOptimise;
    }
}

