import java.util.ArrayList;
import java.util.Random;

public class GA {

    private Config config = new Config();
    private double population[][] = new double[config.getNumSolutions()][config.getNumParameter()];
    private ArrayList<double[]> newPopulation;
    private double fitness[];
    private ArrayList<double[]> sortedNewPopulation;
    private double sortedNormalisedFitness[];
    private double bestFitness = config.getMaxOptimise() ? -Double.MAX_VALUE : Double.MAX_VALUE;
    private double[] bestSolution = new double[config.getNumParameter()];
    private Random random = new Random();

    private void crossover() { // uniform crossover
        ArrayList<double[]> parentPool = new ArrayList<>();
        for(int x = 0 ; x < newPopulation.size() ; x++) {
            parentPool.add(newPopulation.get(x));
        }

        while(parentPool.size() > 0) {
            int parent1Index = random.nextInt(parentPool.size());
            int parent2Index = parent1Index;
            while (parent2Index == parent1Index) {
                parent2Index = random.nextInt(parentPool.size());
            }

            double child1[] = new double[config.getNumParameter()];
            double child2[] = new double[config.getNumParameter()];

            boolean shouldCrossover = random.nextDouble() <= config.getCrossoverProbability() ? true : false;
            if(shouldCrossover) {
                for(int x = 0 ; x < config.getNumParameter() ; x++) {
                    if(random.nextDouble() <= 0.5) { // child 1 get from parent 1
                        child1[x] = population[parent1Index][x];
                        child2[x] = population[parent2Index][x];
                    }
                    else {
                        child2[x] = population[parent1Index][x];
                        child1[x] = population[parent2Index][x];
                    }
                }

                newPopulation.add(child1);
                newPopulation.add(child2);
            }
        }
    }

    private void mutation() {
        for(int x = 0 ; x < config.getNumSolutions() ; x++) {
            for(int y = 0 ; y < config.getNumParameter() ; y++) {
                boolean shouldMutation = random.nextDouble() <= config.getMutationProbability() ? true : false;
                if(shouldMutation) {
                    if(random.nextBoolean()) {
                        population[x][y] += config.getChangePerStep();
                    }
                    else
                        population[x][y] -= config.getChangePerStep();
                }
            }
        }
    }

    private void calculateFitness() {
        Evaluation eval = new Evaluation();
        fitness = new double[newPopulation.size()];
        for(int x = 0 ; x < newPopulation.size() ; x++) {
            fitness[x] = eval.fitness(newPopulation.get(x));
        }
    }

    private void selection() {
        ArrayList<Integer> removedMemberIndex = new ArrayList<>();
        int countMember = 0;
        for(int x = 0 ; x < newPopulation.size() ; x++) {
            if(random.nextDouble() > sortedNormalisedFitness[x]) {
                population[countMember] = sortedNewPopulation.get(x);
                ++countMember;
            }
            else
                removedMemberIndex.add(x);

            if(countMember == 100) {
                break;
            }
        }

        if(countMember < 100) {
            for(int x = countMember ; x < 100 ; x++) {
                population[x] = sortedNewPopulation.get(removedMemberIndex.get(x - countMember));
            }
        }
    }

    private void iteration() {
        newPopulation = new ArrayList<>();
        for(int x = 0 ; x < config.getNumSolutions() ; x++) {
            newPopulation.add(population[x]);
        }

        mutation();
        crossover();
        calculateFitness();
        sortFitness();
        updateBestSolution();
        selection();
    }

    private void updateBestSolution() {
        Evaluation eval = new Evaluation();
        if(config.getMaxOptimise() && eval.fitness(sortedNewPopulation.get(0)) > bestFitness) {
            bestFitness = eval.fitness(sortedNewPopulation.get(0));
            bestSolution = sortedNewPopulation.get(0);
        }
        if(!config.getMaxOptimise() && eval.fitness(sortedNewPopulation.get(0)) < bestFitness) {
            bestFitness = eval.fitness(sortedNewPopulation.get(0));
            bestSolution = sortedNewPopulation.get(0);
        }
    }

    private void sortFitness() {
        ArrayList<Double> sortedFitness = new ArrayList<>();
        ArrayList<double[]> sortedSolutions = new ArrayList<>();
        ArrayList<Integer> sortedIndex = new ArrayList<>();

        double previousBestFitness = config.getMaxOptimise() ? Double.MAX_VALUE : -Double.MAX_VALUE;
        double currentBestFitness = config.getMaxOptimise() ? -Double.MAX_VALUE : Double.MAX_VALUE;
        int currentBestFitnessIndex = -1;
        for(int x = 0 ; x < newPopulation.size() ; x++) {
            if(config.getMaxOptimise()) {
                if (sortedIndex.indexOf(x) == -1 && fitness[x] <= previousBestFitness && fitness[x] > currentBestFitness) {
                    currentBestFitness = fitness[x];
                    currentBestFitnessIndex = x;
                }
            }
            else {
                if (sortedIndex.indexOf(x) == -1 && fitness[x] >= previousBestFitness && fitness[x] < currentBestFitness) {
                    currentBestFitness = fitness[x];
                    currentBestFitnessIndex = x;
                }
            }

            sortedFitness.add(currentBestFitness);
            sortedSolutions.add(newPopulation.get(currentBestFitnessIndex));
            sortedIndex.add(currentBestFitnessIndex);
        }

        sortedNewPopulation = sortedSolutions;

        sortedNormalisedFitness = new double[newPopulation.size()];
        double sumFitness = 0;
        for(int x = 0 ; x < sortedFitness.size() ; x++) {
            sumFitness += sortedFitness.get(x);
        }

        for(int x = 0 ; x < newPopulation.size() ; x++) {
            sortedNormalisedFitness[x] = sortedFitness.get(x) / sumFitness;
        }
    }

    public void start() {
        Initialisation init = new Initialisation();
        population = init.initialise(config.getNumParameter(), config.getNumSolutions());

        int iterationCount = 0;
        while(true) {
            iteration();
            ++iterationCount;
            if(config.getMaxIteration() > 0 && iterationCount == config.getMaxIteration()) {
                break;
            }
        }

        System.out.println(bestFitness);
        System.out.println(bestSolution);
    }
}
