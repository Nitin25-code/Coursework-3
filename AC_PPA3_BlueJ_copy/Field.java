import java.util.*;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single animal/object.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public class Field
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    
    // The dimensions of the field.
    private final int depth, width;
    // Animals mapped by location.
    private final Map<Location, Animal> field = new HashMap<>();
    // The animals.
    private final List<Animal> animals = new ArrayList<>();
    //The plants
    private final Map<Location, Plant> plants = new HashMap<>();


    /**
     * Represent a field of the given dimensions.
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width)
    {
        this.depth = depth;
        this.width = width;
    }

    /**
     * Place an animal at the given location.
     * If there is already an animal at the location it will
     * be lost.
     * @param anAnimal The animal to be placed.
     * @param location Where to place the animal.
     */
    public void placeAnimal(Animal anAnimal, Location location)
    {
        assert location != null; //checks to see whether boolean condition is true. If false then return AssertionError
        Animal other = field.get(location); // allows the variable other to store any object type. Getting animal at that location
        if(other != null) { //if there is an animal
            animals.remove(other);
        }
        animals.put(location, anAnimal); //put new animal in that location
        animals.add(anAnimal); //add it to array list of animals
    }
    
    public void placePlant(Plant plant, Location location) 
    {
        assert location != null;
        plants.put(location, plant); // Only stores plants, does not modify animals
    }
    
    /**
     * Return the animal at the given location, if any.
     * @param location Where in the field.
     * @return The animal at the given location, or null if there is none.
     */
    public Animal getAnimalAt(Location location)
    {
        return animals.get(location);
    }
    
    public Animal getPlantAt(Location location)
    {
        return plants.get(location);
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location)
    {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = getAdjacentLocations(location);
        for(Location next : adjacent) {
            Animal anAnimal = field.get(next);
            if(anAnimal == null) { //if there is no animal there
                free.add(next);
            }
            else if(!anAnimal.isAlive()) { //if animal is dead then that space counts as free
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getAdjacentLocations(Location location)
    {
        // The list of locations to be returned.
        List<Location> locations = new ArrayList<>();
        if(location != null) {
            int row = location.row();
            int col = location.col();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            
            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Print out the number of foxes and rabbits in the field.
     */
    public void fieldStats()
    {
        int numFoxes = 0, numRabbits = 0, numDeer = 0;
        for(Animal anAnimal : field.values()) {
            if(anAnimal instanceof Fox fox) {
                if(fox.isAlive()) {
                    numFoxes++;
                }
            }
            else if(anAnimal instanceof Rabbit rabbit) {
                if(rabbit.isAlive()) {
                    numRabbits++;
                }
            }
            else if(anAnimal instanceof Deer deer) {
                if(deer.isAlive()) {
                    numDeer++;
                }
            }
        }
        System.out.println("Rabbits: " + numRabbits +
        " Deer: " + numDeer +
                           " Foxes: " + numFoxes);
    }

    /**
     * Empty the field.
     */
    public void clear()
    {
        field.clear();
    }

    /**
     * Return whether there is at least one rabbit and one fox in the field.
     * @return true if there is at least one rabbit and one fox in the field.
     */
    public boolean isViable()
    {
        boolean rabbitFound = false;
        boolean foxFound = false;
        boolean deerFound = false;
        boolean tigerFound = false;
        boolean bisonFound = false;
        Iterator<Animal> it = animals.iterator();
        while(it.hasNext() && ! (rabbitFound && foxFound && bisonFound && tigerFound && deerFound))
        {
            Animal anAnimal = it.next();
            if(anAnimal instanceof Rabbit rabbit) {
                if(rabbit.isAlive()) {
                    rabbitFound = true;
                }
            }
            else if(anAnimal instanceof Fox fox) {
                if(fox.isAlive()) {
                    foxFound = true;
                }
            }    
            else if(anAnimal instanceof Deer deer) {
                if(deer.isAlive()) {
                    deerFound = true;
                }    
            }
            else if(anAnimal instanceof Tiger tiger) {
                if(tiger.isAlive()) {
                    tigerFound = true;
                }
            }
            else if(anAnimal instanceof Bison bison) {
                if(bison.isAlive()) {
                    bisonFound = true;
                }
            }

        }
            return rabbitFound && foxFound && bisonFound && tigerFound && deerFound;
    }
    
    
    /**
     * Get the list of animals.
     */
    public List<Animal> getAnimals()
    {
        return animals;
    }

    /**
     * Return the depth of the field.
     * @return The depth of the field.
     */
    public int getDepth()
    {
        return depth;
    }
    
    /**
     * Return the width of the field.
     * @return The width of the field.
     */
    public int getWidth()
    {
        return width;
    }
}
