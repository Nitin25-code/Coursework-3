
/**
 * Write a description of class Plant here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Plant
{
    private boolean alive;
    private Location location;
    private int growthRate;
    private int age;
    
    public Plant(Location location, int growthRate) {
        this.alive = true;
        this.location = location;
        this.growthRate = growthRate;
        this.age = 0;
    }

    public void grow() {
        age += growthRate;
        if (age >= 10) { // Example: Grows to full maturity
            alive = true;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public Location getLocation() {
        return location;
    }
}
    

    