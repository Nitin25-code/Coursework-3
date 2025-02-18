import java.util.List;
import java.util.Random;

/**
 * A simple model of a wolf.
 * Wolves age, move, eat, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Wolf extends Organism {
    // Characteristics shared by all wolves (class variables).
    private static final int BREEDING_AGE = Values.getInt("Wolf", "breed_age");

    // The age to which a tiger can live.
    private static final int MAX_AGE = Values.getInt("Wolf", "max_age");

    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = Values.getProb("Wolf", "breeding");

    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    private static final int RABBIT_FOOD_VALUE = 8;
    private static final int DEER_FOOD_VALUE = 13;
    private static final int BISON_FOOD_VALUE = 21;
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a wolf with an optional random age and gender.
     *
     * @param randomAge If true, the wolf will have random age and hunger level.
     * @param location  The location within the field.
     */
    public Wolf(boolean randomAge, Location location) {
        super(randomAge, location);
    }

    protected Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getAnimalAt(loc);
            if (organism != null && organism.isAlive()) {
                if (organism instanceof Hamster ||
                        organism instanceof Bison ||
                        organism instanceof Deer) {
                    setFoodLevel(organism.getFoodLevel());
                    organism.setDead();
                }
            }
        }
        return null;
    }

    private Wolf findMate(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Organism organism = field.getAnimalAt(loc);
            if (organism instanceof Wolf && canMateWith(organism)) {
                return (Wolf) organism;
            }
        }
        return null;
    }

    private boolean canMate(Field field) {
        return getAge() >= BREEDING_AGE && findMate(field) != null;
    }

    @Override
    public boolean canMateWith(Organism organism) {
        return organism instanceof Wolf && this.isMale() != organism.isMale();
    }

    protected void giveBirth(Field currentField, Field nextField, List<Location> freeLocations) {
        Wolf mate = findMate(currentField);
        if (mate != null && canBreed(currentField)) {
            int births = breed(currentField);
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Wolf young = new Wolf(false, loc);
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
