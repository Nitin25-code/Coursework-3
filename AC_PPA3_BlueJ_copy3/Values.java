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
                        return 0.032;
                    case "breeding":
                        return 0.34;
                }
            case "Wolf":
                switch (dataType) {
                    case "creation":
                        return 0.029;
                    case "breeding":
                        return 0.38;
                }
            case "Hamster":
                switch (dataType) {
                    case "creation":
                        return 0.032;
                    case "breeding":
                        return 0.22;
                }
            case "Tiger":
                switch (dataType) {
                    case "creation":
                        return 0.025;
                    case "breeding":
                        return 0.24;
                }
            case "Bison":
                switch (dataType) {
                    case "creation":
                        return 0.03;
                    case "breeding":
                        return 0.29;
                }
            case "Plant":
                switch (dataType) {
                    case "creation":
                        return 0.05;
                    case "breeding":
                        return 0.31;
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
            case "Wolf":
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
            case "Hamster":
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
