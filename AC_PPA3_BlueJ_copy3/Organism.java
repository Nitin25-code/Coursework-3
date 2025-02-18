import java.util.List;
import java.util.Random;

/**
 * Common elements of wolves, hamsters and deer. (Add as you add organisms)
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Organism {
    //(AC) For using with random gender
    private static final Random rand = new Random(); // Shared random instance
    // Whether the organism is alive or not.
    private boolean alive;
    // The organism's position.
    private Location location;
    // (AC) If the organism is male or not
    private boolean isMale;
    private int foodLevel;
    private int age;

    /**
     * Constructor for objects of class Organism.
     *
     * @param randomAge
     * @param location  The organism's location.
     */
    public Organism(boolean randomAge, Location location) {
        this.alive = true;
        this.location = location;
        this.isMale = rand.nextBoolean();
        this.foodLevel = rand.nextInt(50) + 50;
        this.age = randomAge ? rand.nextInt(Values.getInt(getClass().getName(), "max_age")) : 0;
    }


    public boolean canMateWith(Organism organism) {
        return false; // Default behavior, overridden by subclasses
    }

    /**
     * Act.
     *
     * @param currentField   The current state of the field.
     * @param nextFieldState The new state being built.
     */
    public void act(Field currentField, Field nextFieldState, Environment environment) {
        incrementAge();
        incrementHunger();
        if (environment.getCurrentWeather() == WeatherType.SNOWY)//if snowy the hunger to go twice as much
            incrementHunger();
        if (isAlive()) {
            //AC Only does these things if it is Day
            if ((environment.getHour()) >= (Values.getInt(getClass().getName(), "wake_time")) && (environment.getHour()) <= (Values.getInt(getClass().getName(), "sleep_time"))) {
                List<Location> freeLocations =
                        nextFieldState.getFreeAdjacentLocations(getLocation());
                if (!freeLocations.isEmpty() && environment.getCurrentWeather() != WeatherType.WINDY) {
                    giveBirth(currentField, nextFieldState, freeLocations);//AC added current field
                }
                if (!(this instanceof Plant)) {
                    Location nextLocation = findFood(currentField);
                    if (nextLocation == null && !freeLocations.isEmpty()) {
                        nextLocation = freeLocations.remove(0);
                    }
                    // Try to move into a free location.
                    if (nextLocation != null) {
                        setLocation(nextLocation);
                        nextFieldState.placeAnimal(this, nextLocation);
                    } else {
                        // Overcrowding.
                        setDead();
                    }
                } else {
                    //plants do not move
                    nextFieldState.placeAnimal(this, getLocation());
                }
            } else {
                //wolf sleeping
                nextFieldState.placeAnimal(this, getLocation());
            }
        }
    }

    protected abstract Location findFood(Field currentField);

    protected abstract void giveBirth(Field currentField, Field nextFieldState, List<Location> freeLocations);

    protected void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    protected int getFoodLevel() {
        return this.foodLevel;
    }

    protected void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    /**
     * Increase the age.
     * This could result in the bison's death.
     */
    protected void incrementAge() {
        age++;
        if (age > Values.getInt(getClass().getName(), "max_age")) {
            setDead();

        }
    }

    /**
     * Check whether the organism is alive or not.
     *
     * @return true if the organism is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    public boolean isMale() {
        return isMale;
    }

    /**
     * Indicate that the organism is no longer alive.
     */
    protected void setDead() {
        alive = false;
        location = null;
    }

    /**
     * Return the organism's location.
     *
     * @return The organism's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set the organism's location.
     *
     * @param location The new location.
     */
    protected void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", foodLevel=" + foodLevel +
                '}';
    }

    protected int getAge() {
        return this.age;
    }
}
