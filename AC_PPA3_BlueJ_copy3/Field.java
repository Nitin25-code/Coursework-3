import java.util.*;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single organism/object.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public class Field {
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();

    // The dimensions of the field.
    private final int depth, width;
    // Organisms mapped by location.
    private final Map<Location, Organism> field = new HashMap<>();
    // The organisms.
    private final List<Organism> organisms = new ArrayList<>();

    /**
     * Represent a field of the given dimensions.
     *
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
    }

    /**
     * Place an organism at the given location.
     * If there is already an organism at the location it will
     * be lost.
     *
     * @param anOrganism The organism to be placed.
     * @param location   Where to place the organism.
     */
    public void placeAnimal(Organism anOrganism, Location location) {
        assert location != null;
        Object other = field.get(location);
        if (other != null) {
            organisms.remove(other);
        }
        field.put(location, anOrganism);
        organisms.add(anOrganism);
    }

    /**
     * Return the organism at the given location, if any.
     *
     * @param location Where in the field.
     * @return The organism at the given location, or null if there is none.
     */
    public Organism getAnimalAt(Location location) {
        return field.get(location);
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     *
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location) {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = getAdjacentLocations(location);
        for (Location next : adjacent) {
            Organism anOrganism = field.get(next);
            if (anOrganism == null) {
                free.add(next);
            } else if (!anOrganism.isAlive()) {
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     *
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getAdjacentLocations(Location location) {
        // The list of locations to be returned.
        List<Location> locations = new ArrayList<>();
        if (location != null) {
            int row = location.row();
            int col = location.col();
            for (int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if (nextRow >= 0 && nextRow < depth) {
                    for (int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
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
    public void fieldStats() {
        HashMap<String, Integer> animalCounts = new HashMap<>();
        for (Organism anOrganism : field.values()) {
            if (anOrganism.isAlive()) {
                animalCounts.put(anOrganism.getClass().getName(), animalCounts.getOrDefault(anOrganism.getClass().getName(), 0) + 1);
            }
        }
        System.out.println(animalCounts);
    }

    /**
     * Empty the field.
     */
    public void clear() {
        field.clear();
    }

    /**
     * Return whether there is at least one rabbit and one fox in the field.
     *
     * @return true if there is at least one rabbit and one fox in the field.
     */
    public boolean isViable() {
        boolean rabbitFound = false;
        boolean foxFound = false;
        boolean deerFound = false;
        boolean tigerFound = false;
        boolean bisonFound = false;
        boolean plantFound = false;
        Iterator<Organism> it = organisms.iterator();
        while (it.hasNext() && !(rabbitFound && foxFound && bisonFound && tigerFound && deerFound && plantFound)) {
            Organism anOrganism = it.next();
            if (anOrganism instanceof Rabbit rabbit) {
                if (rabbit.isAlive()) {
                    rabbitFound = true;
                }
            } else if (anOrganism instanceof Fox fox) {
                if (fox.isAlive()) {
                    foxFound = true;
                }
            } else if (anOrganism instanceof Deer deer) {
                if (deer.isAlive()) {
                    deerFound = true;
                }
            } else if (anOrganism instanceof Tiger tiger) {
                if (tiger.isAlive()) {
                    tigerFound = true;
                }
            } else if (anOrganism instanceof Bison bison) {
                if (bison.isAlive()) {
                    bisonFound = true;
                }
            } else if (anOrganism instanceof Plant plant) {
                if (plant.isAlive()) {
                    plantFound = true;
                }
            }

        }
        return rabbitFound && foxFound && bisonFound && tigerFound && deerFound && plantFound;
    }


    /**
     * Get the list of organisms.
     */
    public List<Organism> getAnimals() {
        return organisms;
    }

    /**
     * Return the depth of the field.
     *
     * @return The depth of the field.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Return the width of the field.
     *
     * @return The width of the field.
     */
    public int getWidth() {
        return width;
    }
}
