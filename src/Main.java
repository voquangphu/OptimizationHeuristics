public class Main {

    public static void main(String args[]) {
        Config config = new Config();
        if(config.getAlgorithm() == 3) {
            GA ga = new GA();
            ga.start();
        }
    }
}
