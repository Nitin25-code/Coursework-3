public class Values {
    // Animal-specific parameters

    // Animal type
    private String animalType;
    private String dataType;
    // Method to set the settings based on animal type
    // Method to get the creation probability for the animal based on the type
    public static double getProb(String animalType, String dataType) {
        switch (animalType) {
            case "Deer":
                switch (dataType) {case "creation": return 0.012;}
                switch (dataType) {case "breeding": return 0.24;}
            case "Fox":
                switch (dataType) {case "creation": return 0.009;}
                switch (dataType) {case "breeding": return 0.24;}
            case "Rabbit":
                switch (dataType) {case "creation": return 0.012;}
                switch (dataType) {case "breeding": return 0.24;}
            case "Tiger":
                switch (dataType) {case "creation": return 0.015;}
                switch (dataType) {case "breeding": return 0.24;}
            case "Bison":
                switch (dataType) {case "creation": return 0.01;}
                switch (dataType) {case "breeding": return 0.24;}
            default:
                throw new IllegalArgumentException("Unknown animal type: " + animalType);
        }
    }

    public static Integer getInt(String animalType, String dataType) {
        switch (animalType) {
            case "Deer":
                switch(dataType) {case "breed_age": return 5;}
                switch(dataType) {case "max_age": return 30;}
                switch(dataType) {case "wake_time": return 9;}
                switch(dataType) {case "sleep_time": return 22;}
            case "Fox":
                switch(dataType) {case "breed_age": return 10;}
                switch(dataType) {case "max_age": return 30;}
                switch(dataType) {case "wake_time": return 8;}
                switch(dataType) {case "sleep_time": return 22;}
            case "Rabbit":
                switch(dataType) {case "breed_age": return 5;}
                switch(dataType) {case "max_age": return 25;}
                switch(dataType) {case "wake_time": return 5;}
                switch(dataType) {case "sleep_time": return 20;}
            case "Tiger":
                switch(dataType) {case "breed_age": return 10;}
                switch(dataType) {case "max_age": return 30;}
                switch(dataType) {case "wake_time": return 7;}
                switch(dataType) {case "sleep_time": return 20;}
            case "Bison":
                switch(dataType) {case "breed_age": return 6;}
                switch(dataType) {case "max_age": return 25;}
                switch(dataType) {case "wake_time": return 6;}
                switch(dataType) {case "sleep_time": return 20;}
            default:
                throw new IllegalArgumentException("Unknown animal type: " + animalType);
        }
    }
}
