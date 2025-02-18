import java.util.List;
import java.util.Random;

/**
 * A simple model of a deer.
 * Deer age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Deer extends Animal
{
    // Characteristics shared by all deers (class variables).
    // The age at which a deer can start to breed.
    private static final int BREEDING_AGE = Values.getInt("Deer", "breed_age");;
    // The age to which a tiger can live.
    private static final int MAX_AGE = Values.getInt("Deer", "max_age");;
    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = Values.getProb("Deer", "breeding");;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    // The deer's age.
    private int age;

    /**
     * Create a new deer. A deer may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the deer will have a random age.
     * @param location The location within the field.
     */
    //AC random gender added
    public Deer(boolean randomAge, boolean randomGender, Location location)
    {
        super(randomGender, location); // Ensures gender is correctly assigned
        age = randomAge ? rand.nextInt(MAX_AGE) : 0;
    }


    /**
     * This is what the deer does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param currentField The field occupied.
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
            if ((dayTime.getHour())>= (Values.getInt("Deer", "wake_time")) && (dayTime.getHour())<= (Values.getInt("Deer", "sleep_time")) ) {
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
                //deer sleeping
            }
        }
    }

    @Override
    public String toString() {
        return "Deer{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                '}';
    }

    /**
     * Increase the age.
     * This could result in the deer's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Check whether or not this deer is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */

    private Deer findMate(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Animal animal = field.getAnimalAt(loc);
            if (animal instanceof Deer && canMateWith(animal)) {
                return (Deer) animal;
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
        return animal instanceof Deer && this.isMale() != animal.isMale();
    }






    private void giveBirth(Field currentField, Field nextField, List<Location> freeLocations)
    {
        Deer mate = findMate(currentField);
        if (mate != null && canBreed(currentField)) {
            int births = breed(currentField);
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Deer young = new Deer(false, rand.nextBoolean(), loc);  // Random gender
                nextField.placeAnimal(young, loc);
            }
        }
    }



    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed(Field field)
    {
        int births = 0;
        if (canBreed(field) && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }



    /**
     * A deer can breed if it has reached the breeding age.
     * @return true if the deer can breed, false otherwise.
     */
    private boolean canBreed(Field field)
    {
        return age >= BREEDING_AGE && findMate(field) != null;
    }
}
