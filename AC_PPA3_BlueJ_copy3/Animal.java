import java.util.Random;
/**
 * Common elements of foxes, rabbits and deer. (Add as you add animals)
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Animal
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position.
    private Location location;
    // (AC) If the animal is male or not
    private boolean isMale;
    //(AC) For using with random gender
    private static final Random rand = new Random(); // Shared random instance
    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     */
    public Animal(boolean randomGender, Location location)
    {
        this.alive = true;
        this.location = location;
        this.isMale = rand.nextBoolean();

    }


    public boolean canMateWith(Animal animal)
    {
        return false; // Default behavior, overridden by subclasses
    }

    /**
     * Act.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    abstract public void act(Field currentField, Field nextFieldState);
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    public boolean isMale()
    {
        return isMale;
    }

    /**
     * Indicate that the animal is no longer alive.
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }
    
    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Set the animal's location.
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }
}
