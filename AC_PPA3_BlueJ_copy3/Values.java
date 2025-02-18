public class Values {
    // Organism-specific parameters

    // Organism type
    private String animalType;
    private String dataType;

    // Method to set the settings based on organism type
    // Method to get the creation probability for the organism based on the type
    public static double getProb(String animalType, String dataType) {
        switch (animalType) {
            case "Deer":
                switch (dataType) {
                    case "creation":
                        return 0.012;
                    case "breeding":
                        return 0.24;
                }
            case "Fox":
                switch (dataType) {
                    case "creation":
                        return 0.009;
                    case "breeding":
                        return 0.24;
                }
            case "Rabbit":
                switch (dataType) {
                    case "creation":
                        return 0.012;
                    case "breeding":
                        return 0.24;
                }
            case "Tiger":
                switch (dataType) {
                    case "creation":
                        return 0.015;
                    case "breeding":
                        return 0.24;
                }
            case "Bison":
                switch (dataType) {
                    case "creation":
                        return 0.01;
                    case "breeding":
                        return 0.24;
                }
            case "Plant":
                switch (dataType) {
                    case "creation":
                        return 0.01;
                    case "breeding":
                        return 0.24;
                }
            default:
                throw new IllegalArgumentException("Unknown organism type: " + animalType);
        }
    }

    public static Integer getInt(String animalType, String dataType) {
        switch (animalType) {
            case "Deer":
                switch (dataType) {
                    case "breed_age":
                        return 5;
                    case "max_age":
                        return 30;

                    case "wake_time":
                        return 9;

                    case "sleep_time":
                        return 22;
                }
            case "Fox":
                switch (dataType) {
                    case "breed_age":
                        return 10;

                    case "max_age":
                        return 30;

                    case "wake_time":
                        return 8;

                    case "sleep_time":
                        return 22;
                }
            case "Rabbit":
                switch (dataType) {
                    case "breed_age":
                        return 5;

                    case "max_age":
                        return 25;

                    case "wake_time":
                        return 5;

                    case "sleep_time":
                        return 20;
                }
            case "Tiger":
                switch (dataType) {
                    case "breed_age":
                        return 10;

                    case "max_age":
                        return 30;

                    case "wake_time":
                        return 7;

                    case "sleep_time":
                        return 20;
                }
            case "Bison":
                switch (dataType) {
                    case "breed_age":
                        return 6;

                    case "max_age":
                        return 25;

                    case "wake_time":
                        return 6;

                    case "sleep_time":
                        return 20;
                }

            case "Plant":
                switch (dataType) {
                    case "breed_age":
                        return 6;

                    case "max_age":
                        return 25;

                    case "wake_time":
                        return 6;

                    case "sleep_time":
                        return 20;
                }
            default:
                throw new IllegalArgumentException("Unknown organism type: " + animalType);
        }
    }
}
