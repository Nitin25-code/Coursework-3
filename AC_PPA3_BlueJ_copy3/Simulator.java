import java.util.List;
import java.util.Random;

/**
 * A simple predator-prey simulator, based on a rectangular field containing
 * hamsters and wolves.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Simulator {
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a wolf will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = Values.getProb("Wolf", "creation");

    // The probability that a hamster will be created in any given position.
    private static final double RABBIT_CREATION_PROBABILITY = Values.getProb("Hamster", "creation");

    // AC adding for deer
    private static final double DEER_CREATION_PROBABILITY = Values.getProb("Deer", "creation");
    // AC adding for tiger
    private static final double TIGER_CREATION_PROBABILITY = Values.getProb("Tiger", "creation");

    // AC adding for Bison
    private static final double BISON_CREATION_PROBABILITY = Values.getProb("Bison", "creation");

    // A graphical view of the simulation.
    private final SimulatorView view;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    //(AC) Adding DayTime to the simulator class
    private final Environment environment;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     *
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        field = new Field(depth, width);
        view = new SimulatorView(depth, width);
        environment = new Environment(0);

        reset();
    }

    public static void main(String[] args) {
        Simulator simulator = new Simulator();  // Create an instance of Simulator
        simulator.simulate(700);  // Run the long simulation
    }

    /**
     * Run the simulation from its current state for a reasonably long
     * period (4000 steps).
     */
    public void runLongSimulation() {
        simulate(700);
    }

    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     *
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps) {
        System.out.println("Sim started");
        reportStats();
        for (int n = 1; n <= numSteps && field.isViable(); n++) {
            simulateOneStep();
            delay(50);         // adjust this to change execution speed
        }
        System.out.println("Sim ended");
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each wolf and hamster.
     */
    public void simulateOneStep() {
        step++;
        // Use a separate Field to store the starting state of
        // the next step.
        environment.advanceStep();
        //(AC) Advance hour by one
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());

        List<Organism> organisms = field.getOrganisms();
        for (Organism anOrganism : organisms) {
            anOrganism.act(field, nextFieldState, environment);
        }

        // Replace the old state with the new one.
        field = nextFieldState;

        reportStats();
        view.showStatus(step, environment, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        populate();
        view.showStatus(step, environment, field);
    }

    /**
     * Randomly populate the field with wolves and hamsters.
     */
    private void populate() {
        Random rand = Randomizer.getRandom();
        field.clear();
//AC changed values to also add random gender
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                if (rand.nextDouble() <= TIGER_CREATION_PROBABILITY) {
                    field.placeOrganism(new Tiger(true, location), location);
                } else if (rand.nextDouble() <= BISON_CREATION_PROBABILITY) {
                    field.placeOrganism(new Bison(true, location), location);
                } else if (rand.nextDouble() <= DEER_CREATION_PROBABILITY) {
                    field.placeOrganism(new Deer(true, location), location);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    field.placeOrganism(new Hamster(true, location), location);
                } else if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    field.placeOrganism(new Wolf(true, location), location);
                } else if (rand.nextDouble() <= Values.getProb("Plant", "creation")) {
                    field.placeOrganism(new Plant(true, location), location);
                }
                // If the number is greater than all combined probabilities, leave the cell empty.
            }
        }

    }

    /**
     * Report on the number of each type of organism in the field.
     */
    public void reportStats() {
        System.out.print("Step: " + step + " ");
        field.fieldStats();
    }

    /**
     * Pause for a given time.
     *
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // ignore
        }
    }

}
