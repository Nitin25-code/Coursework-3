import java.util.List;
import java.util.Random;

/**
 * A simple model of a plant.
 * Plants age, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Plant extends Organism {
    // Characteristics shared by all plants (class variables).
    // The age at which a plant can start to breed.
    private static final int BREEDING_AGE = Values.getInt("Bison", "breed_age");

    // The age to which a tiger can live.
    private static final int MAX_AGE = Values.getInt("Bison", "max_age");

    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = Values.getProb("Bison", "breeding");

    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new plant. A plant may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the plant will have a random age.
     * @param location  The location within the field.
     */
    //AC random gender added
    public Plant(boolean randomAge, Location location) {
        super(randomAge, location);
    }

    @Override
    protected Location findFood(Field currentField) {
        return null;//plants do not eat anything
    }

    @Override
    public void act(Field currentField, Field nextFieldState, Environment environment) {
        super.act(currentField, nextFieldState, environment);
    }

    /**
     * Check whether or not this plant is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param field The field.
     */

    private Plant findMate(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getAnimalAt(loc);
            if (organism instanceof Plant && canMateWith(organism)) {
                return (Plant) organism;
            }
        }
        return null;
    }

    private boolean canMate(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }

    @Override
    public boolean canMateWith(Organism organism) {
        return organism instanceof Plant && this.isMale() != organism.isMale();
    }


    protected void giveBirth(Field currentField, Field nextField, List<Location> freeLocations) {
        Plant mate = findMate(currentField);
        if (mate != null && canBreed(currentField)) {
            int births = breed(currentField);
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Plant young = new Plant(false, loc);  // Random gender
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
     * A plant can breed if it has reached the breeding age.
     *
     * @return true if the plant can breed, false otherwise.
     */
    private boolean canBreed(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }
}
