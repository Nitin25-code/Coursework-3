import java.util.List;
import java.util.Random;

/**
 * A simple model of a deer.
 * Deer age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Deer extends Organism {
    // Characteristics shared by all deers (class variables).
    // The age at which a deer can start to breed.
    private static final int BREEDING_AGE = Values.getInt("Deer", "breed_age");

    // The age to which a tiger can live.
    private static final int MAX_AGE = Values.getInt("Deer", "max_age");

    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = Values.getProb("Deer", "breeding");

    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    /**
     * Create a new deer. A deer may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the deer will have a random age.
     * @param location  The location within the field.
     */
    //AC random gender added
    public Deer(boolean randomAge, Location location) {
        super(randomAge, location); // Ensures gender is correctly assigned
    }

    @Override
    protected Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getAnimalAt(loc);
            if (organism != null && organism.isAlive()) {
                if (organism instanceof Plant) {
                    setFoodLevel(organism.getFoodLevel());
                    organism.setDead();
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this deer is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param field the field.
     */

    private Deer findMate(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getAnimalAt(loc);
            if (canMateWith(organism)) {
                return (Deer) organism;
            }
        }
        return null;
    }

    private boolean canMate(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }

    @Override
    public boolean canMateWith(Organism organism) {
        return organism instanceof Deer && this.isMale() != organism.isMale();
    }


    protected void giveBirth(Field currentField, Field nextField, List<Location> freeLocations) {
        Deer mate = findMate(currentField);
        if (mate != null && canBreed(currentField)) {
            int births = breed(currentField);
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Deer young = new Deer(false, loc);  // Random gender
                nextField.placeAnimal(young, loc);
            }
        }
    }


    /**
     * Generate a number representing the number of births,
     * if it can breed.
     *
     * @return The number of births (may be zero).
     */
    private int breed(Field field) {
        int births = 0;
        if (canBreed(field) && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }


    /**
     * A deer can breed if it has reached the breeding age.
     *
     * @return true if the deer can breed, false otherwise.
     */
    private boolean canBreed(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }
}
