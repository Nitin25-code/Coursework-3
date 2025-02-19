import java.util.List;
import java.util.Random;

/**
 * A simple model of a tiger.
 * Tigers age, move, eat hamsters, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Tiger extends Organism {
    // Characteristics shared by all tigers (class variables).
    // The age at which a tiger can start to breed.
    private static final int BREEDING_AGE = Values.getInt("Tiger", "breed_age");

    // The age to which a tiger can live.
    private static final int MAX_AGE = Values.getInt("Tiger", "max_age");

    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = Values.getProb("Tiger", "breeding");

    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single hamster. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 5;
    private static final int DEER_FOOD_VALUE = 13;
    private static final int FOX_FOOD_VALUE = 7;
    private static final int BISON_FOOD_VALUE = 15;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a tiger. A tiger can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the tiger will have random age and hunger level.
     * @param location  The location within the field.
     */
    public Tiger(boolean randomAge, Location location) {
        super(randomAge, location);
    }

    /**
     * Look for prey adjacent to the current location.
     * Only the first live prey is eaten.
     *
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getOrganismAt(loc);
            if (organism != null && organism.isAlive()) {
                if (organism instanceof Hamster ||
                        organism instanceof Deer ||
                        organism instanceof Wolf ||
                        organism instanceof Bison) {
                    setFoodLevel(organism.getFoodLevel());
                    organism.setDead();
                }
            }
        }
        return null;
    }

    private Tiger findMate(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getOrganismAt(loc);
            if (organism instanceof Tiger && canMateWith(organism)) {
                return (Tiger) organism;
            }
        }
        return null;
    }

    private boolean canMate(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }

    @Override
    public boolean canMateWith(Organism organism) {
        return organism instanceof Tiger && this.isMale() != organism.isMale();
    }

    protected void giveBirth(Field currentField, Field nextField, List<Location> freeLocations) {
        Tiger mate = findMate(currentField);
        if (mate != null && canBreed(currentField)) {
            int births = breed(currentField);
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Tiger young = new Tiger(false, loc);
                nextField.placeOrganism(young, loc);
            }
        }
    }

    private int breed(Field field) {
        int births = 0;
        if (canBreed(field) && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    private boolean canBreed(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }
}
