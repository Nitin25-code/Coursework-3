import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Fox extends Animal
{
    // Characteristics shared by all foxes (class variables).
    private static final int BREEDING_AGE = Values.getInt("Fox", "breed_age");;
    // The age to which a tiger can live.
    private static final int MAX_AGE = Values.getInt("Fox", "max_age");;
    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = Values.getProb("Fox", "breeding");;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    private static final int RABBIT_FOOD_VALUE = 8;
    private static final int DEER_FOOD_VALUE = 13;
    private static final int BISON_FOOD_VALUE = 21;
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    private int age;
    private int foodLevel;

    /**
     * Create a fox with an optional random age and gender.
     *
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param randomGender If true, the fox will have a random gender.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, boolean randomGender, Location location)
    {
        super(randomGender, location);
        age = randomAge ? rand.nextInt(MAX_AGE) : 0;
        foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
    }

    /**
     * This is what the fox does most of the time: it hunts for food,
     * breeds, moves, or dies of hunger or old age.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */

    @Override
    public void act(Field currentField, Field nextFieldState)
    {
        act(currentField, nextFieldState, new DayTime(12,0));  // Default DayTime, can be adjusted
    }


    //AC Added DayTime dayTime for writing behaviour of Deer.
    public void act(Field currentField, Field nextFieldState, DayTime dayTime)
    {
        incrementAge();
        if(isAlive()) {
            //AC Only does these things if it is Day
            if ((dayTime.getHour())>= (Values.getInt("Fox", "wake_time")) && (dayTime.getHour())<= (Values.getInt("Fox", "sleep_time")) ) {
                List<Location> freeLocations =
                        nextFieldState.getFreeAdjacentLocations(getLocation());
                if (!freeLocations.isEmpty()) {
                    giveBirth(currentField, nextFieldState, freeLocations);//AC added current field
                }
                // Try to move into a free location.
                if (!freeLocations.isEmpty()) {
                    Location nextLocation = freeLocations.get(0);
                    setLocation(nextLocation);
                    nextFieldState.placeAnimal(this, nextLocation);
                } else {
                    // Overcrowding.
                    setDead();
                }
            }
            else
            {
                //fox sleeping
            }
        }
    }

    @Override
    public String toString() {
        return "Fox{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", foodLevel=" + foodLevel +
                '}';
    }

    private void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    private void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    private Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Animal animal = field.getAnimalAt(loc);
            if(animal instanceof Rabbit rabbit && rabbit.isAlive()) {
                rabbit.setDead();
                foodLevel = RABBIT_FOOD_VALUE;
                return loc;
            }
            if(animal instanceof Deer deer && deer.isAlive()) {
                deer.setDead();
                foodLevel = DEER_FOOD_VALUE;
                return loc;
            }
            if(animal instanceof Bison bison && bison.isAlive()) {
                bison.setDead();
                foodLevel = BISON_FOOD_VALUE;
                return loc;
            }
        }
        return null;
    }

    private Fox findMate(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Animal animal = field.getAnimalAt(loc);
            if (animal instanceof Fox && canMateWith(animal)) {
                return (Fox) animal;
            }
        }
        return null;
    }

    private boolean canMate(Field field) {
        return age >= BREEDING_AGE && findMate(field) != null;
    }

    @Override
    public boolean canMateWith(Animal animal) {
        return animal instanceof Fox && this.isMale() != animal.isMale();
    }

    private void giveBirth(Field currentField, Field nextField, List<Location> freeLocations) {
        Fox mate = findMate(currentField);
        if (mate != null && canBreed(currentField)) {
            int births = breed(currentField);
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Fox young = new Fox(false, true, loc);
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
        return age >= BREEDING_AGE && findMate(field) != null;
    }
}
