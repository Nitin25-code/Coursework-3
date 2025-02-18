public class Main {
    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        for (int n = 1; n <= 1000; n++) {
            simulator.simulateOneStep(); // Runs the simulation for a default number of steps
        }
    }
}
