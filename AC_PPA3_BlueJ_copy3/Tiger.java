import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a tiger.
 * Tigers age, move, eat rabbits, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Tiger extends Animal
{
    // Characteristics shared by all tigers (class variables).
    // The age at which a tiger can start to breed.
    private static final int BREEDING_AGE = Values.getInt("Tiger", "breed_age");;
    // The age to which a tiger can live.
    private static final int MAX_AGE = Values.getInt("Tiger", "max_age");;
    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = Values.getProb("Tiger", "breeding");;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 5;
    private static final int DEER_FOOD_VALUE = 13;
    private static final int FOX_FOOD_VALUE = 7;
    private static final int BISON_FOOD_VALUE = 15;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    // The tiger's age.
    private int age;
    // The tiger's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a tiger. A tiger can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the tiger will have random age and hunger level.
     * @param randomGender If true, the tiger will have a random gender.
     * @param location The location within the field.
     */
    public Tiger(boolean randomAge, boolean randomGender, Location location)

    {
        super(randomGender, location);
        age = randomAge ? rand.nextInt(MAX_AGE) : 0;
        foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
    }

    /**
     * This is what the tiger does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
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
            if ((dayTime.getHour())>= (Values.getInt("Tiger", "wake_time")) && (dayTime.getHour())<= (Values.getInt("Tiger", "sleep_time")) ) {
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
                //tiger sleeping
            }
        }
    }

    @Override
    public String toString() {
        return "Tiger{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", foodLevel=" + foodLevel +
                '}';
    }

    /**
     * Increase the age. This could result in the tiger's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this tiger more hungry. This could result in the tiger's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for prey adjacent to the current location.
     * Only the first live prey is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(Field field)
    {
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
            if(animal instanceof Fox fox && fox.isAlive()) {
                fox.setDead();
                foodLevel = FOX_FOOD_VALUE;
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

    private Tiger findMate(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Animal animal = field.getAnimalAt(loc);
            if (animal instanceof Tiger && canMateWith(animal)) {
                return (Tiger) animal;
            }
        }
        return null;
    }

    private boolean canMate(Field field)
    {
        return age >= BREEDING_AGE && findMate(field) != null;
    }

    @Override
    public boolean canMateWith(Animal animal)
    {
        return animal instanceof Tiger && this.isMale() != animal.isMale();
    }

    private void giveBirth(Field currentField, Field nextField, List<Location> freeLocations)
    {
        Tiger mate = findMate(currentField);
        if (mate != null && canBreed(currentField)) {
            int births = breed(currentField);
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Tiger young = new Tiger(false, true, loc);
                nextField.placeAnimal(young, loc);
            }
        }
    }

    private int breed(Field field)
    {
        int births = 0;
        if (canBreed(field) && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    private boolean canBreed(Field field)
    {
        return age >= BREEDING_AGE && findMate(field) != null;
    }
}
