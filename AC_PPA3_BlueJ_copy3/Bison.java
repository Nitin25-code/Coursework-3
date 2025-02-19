import java.util.List;
import java.util.Random;

/**
 * A simple model of a bison.
 * Bisons age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Bison extends Organism {
    // Characteristics shared by all bisons (class variables).
    // The age at which a bison can start to breed.
    private static final int BREEDING_AGE = Values.getInt("Bison", "breed_age");

    // The age to which a tiger can live.
    private static final int MAX_AGE = Values.getInt("Bison", "max_age");

    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = Values.getProb("Bison", "breeding");

    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    /**
     * Create a new bison. A bison may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the bison will have a random age.
     * @param location  The location within the field.
     */
    //AC random gender added
    public Bison(boolean randomAge, Location location) {
        super(randomAge, location); // Ensures gender is correctly assigned
    }

    /**
     * Check whether or not this bison is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param field the field.
     */

    private Bison findMate(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getOrganismAt(loc);
            if (organism instanceof Bison && canMateWith(organism)) {
                return (Bison) organism;
            }
        }
        return null;
    }

    private boolean canMate(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }

    @Override
    public boolean canMateWith(Organism organism) {
        return organism instanceof Bison && this.isMale() != organism.isMale();
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


    protected void giveBirth(Field currentField, Field nextField, List<Location> freeLocations) {
        Bison mate = findMate(currentField);
        if (mate != null && canBreed(currentField)) {
            int births = breed(currentField);
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Bison young = new Bison(false, loc);  // Random gender
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
     * A bison can breed if it has reached the breeding age.
     *
     * @return true if the bison can breed, false otherwise.
     */
    private boolean canBreed(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }
}
