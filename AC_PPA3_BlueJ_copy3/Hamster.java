import java.util.List;
import java.util.Random;

/**
 * A simple model of a hamster.
 * Hamsters age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Hamster extends Organism {
    // Characteristics shared by all hamsters (class variables).
    // The age at which a hamster can start to breed.
    private static final int BREEDING_AGE = Values.getInt("Hamster", "breed_age");

    // The age to which a tiger can live.
    private static final int MAX_AGE = Values.getInt("Hamster", "max_age");

    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = Values.getProb("Hamster", "breeding");

    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new hamster. A hamster may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the hamster will have a random age.
     * @param location  The location within the field.
     */
    //AC random gender added
    public Hamster(boolean randomAge, Location location) {
        super(randomAge, location); // Ensures gender is correctly assigned
    }

    @Override
    protected Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getOrganismAt(loc);
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
     * Check whether or not this hamster is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param field The field.
     */

    private Hamster findMate(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getOrganismAt(loc);
            if (organism instanceof Hamster && canMateWith(organism)) {
                return (Hamster) organism;
            }
        }
        return null;
    }

    private boolean canMate(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }

    @Override
    public boolean canMateWith(Organism organism) {
        return organism instanceof Hamster && this.isMale() != organism.isMale();
    }


    protected void giveBirth(Field currentField, Field nextField, List<Location> freeLocations) {
        Hamster mate = findMate(currentField);
        if (mate != null && canBreed(currentField)) {
            int births = breed(currentField);
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Hamster young = new Hamster(false, loc);  // Random gender
                nextField.placeOrganism(young, loc);
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
     * A hamster can breed if it has reached the breeding age.
     *
     * @return true if the hamster can breed, false otherwise.
     */
    private boolean canBreed(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }
}
