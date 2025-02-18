import java.util.*;

/**
 * A simple predator-prey simulator, based on a rectangular field containing 
 * rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = Values.getProb("Fox", "creation");;
    // The probability that a rabbit will be created in any given position.
    private static final double RABBIT_CREATION_PROBABILITY = Values.getProb("Rabbit", "creation");;
    // AC adding for deer
    private static final double DEER_CREATION_PROBABILITY = Values.getProb("Deer", "creation");
    // AC adding for tiger
    private static final double TIGER_CREATION_PROBABILITY = Values.getProb("Tiger", "creation");;
    // AC adding for Bison
    private static final double BISON_CREATION_PROBABILITY = Values.getProb("Bison", "creation");;

    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;

    //(AC) Adding DayTime to the simulator class
    private DayTime dayTime;
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        field = new Field(depth, width);
        view = new SimulatorView(depth, width);
        dayTime = new DayTime(0,0);

        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long 
     * period (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(700);
    }
    
    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        System.out.println("Sim started");
        reportStats();
        for(int n = 1; n <= numSteps && field.isViable(); n++) {
            simulateOneStep();
            delay(50);         // adjust this to change execution speed
        }
        System.out.println("Sim ended");
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        // Use a separate Field to store the starting state of
        // the next step.
        dayTime.advanceHour();
        //(AC) Advance hour by one
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());

        List<Animal> animals = field.getAnimals();
        for (Animal anAnimal : animals) {
            anAnimal.act(field, nextFieldState);
        }
        
        // Replace the old state with the new one.
        field = nextFieldState;

        reportStats();
        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        populate();
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate() {
        Random rand = Randomizer.getRandom();
        field.clear();
//AC changed values to also add random gender
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                double randValue = rand.nextDouble(); // Generate a single random number for this cell
                Location location = new Location(row, col);
                boolean randomGender = rand.nextBoolean(); // Randomly assign gender

                if (randValue <= TIGER_CREATION_PROBABILITY) {
                    field.placeAnimal(new Tiger(true, randomGender, location), location);
                }
                else if (randValue <= TIGER_CREATION_PROBABILITY + BISON_CREATION_PROBABILITY) {
                    field.placeAnimal(new Bison(true, randomGender, location), location);
                }
                else if (randValue <= TIGER_CREATION_PROBABILITY + BISON_CREATION_PROBABILITY + DEER_CREATION_PROBABILITY) {
                    field.placeAnimal(new Deer(true, randomGender, location), location);
                }
                else if (randValue <= TIGER_CREATION_PROBABILITY + BISON_CREATION_PROBABILITY + DEER_CREATION_PROBABILITY + RABBIT_CREATION_PROBABILITY) {
                    field.placeAnimal(new Rabbit(true, randomGender, location), location);
                }
                else if (randValue <= TIGER_CREATION_PROBABILITY + BISON_CREATION_PROBABILITY + DEER_CREATION_PROBABILITY + RABBIT_CREATION_PROBABILITY + FOX_CREATION_PROBABILITY) {
                    field.placeAnimal(new Fox(true, randomGender, location), location);
                }
                // If the number is greater than all combined probabilities, leave the cell empty.
            }
        }

    }


    /**
     * Report on the number of each type of animal in the field.
     */
    public void reportStats()
    {
        System.out.print("Step: " + step + " ");
        field.fieldStats();
    }
    
    /**
     * Pause for a given time.
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // ignore
        }
    }
    public static void main(String[] args) {
        Simulator simulator = new Simulator();  // Create an instance of Simulator
        simulator.simulate(700);  // Run the long simulation
    }

}
