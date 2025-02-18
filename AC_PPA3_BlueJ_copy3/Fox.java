import java.util.List;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Fox extends Organism {
    // Characteristics shared by all foxes (class variables).
    private static final int BREEDING_AGE = Values.getInt("Fox", "breed_age");

    // The age to which a tiger can live.
    private static final int MAX_AGE = Values.getInt("Fox", "max_age");

    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = Values.getProb("Fox", "breeding");

    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    private static final int RABBIT_FOOD_VALUE = 8;
    private static final int DEER_FOOD_VALUE = 13;
    private static final int BISON_FOOD_VALUE = 21;
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a fox with an optional random age and gender.
     *
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param location  The location within the field.
     */
    public Fox(boolean randomAge, Location location) {
        super(randomAge, location);
    }

    protected Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getAnimalAt(loc);
            if (organism != null && organism.isAlive()) {
                if (organism instanceof Rabbit ||
                        organism instanceof Bison ||
                        organism instanceof Deer) {
                    setFoodLevel(organism.getFoodLevel());
                    organism.setDead();
                }
            }
        }
        return null;
    }

    private Fox findMate(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getAnimalAt(loc);
            if (organism instanceof Fox && canMateWith(organism)) {
                return (Fox) organism;
            }
        }
        return null;
    }

    private boolean canMate(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }

    @Override
    public boolean canMateWith(Organism organism) {
        return organism instanceof Fox && this.isMale() != organism.isMale();
    }

    protected void giveBirth(Field currentField, Field nextField, List<Location> freeLocations) {
        Fox mate = findMate(currentField);
        if (mate != null && canBreed(currentField)) {
            int births = breed(currentField);
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Fox young = new Fox(false, loc);
                nextField.placeAnimal(young, loc);
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
