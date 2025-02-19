public class Environment {
    private int hour;
    private int stepCount;
    private WeatherType currentWeather;

    public Environment(int stepCount) {
        this.stepCount = stepCount;
        this.hour = stepCount % 24;
        currentWeather = WeatherType.NORMAL;
    }

    public void advanceStep() {
        stepCount++;
        hour++;
        if (hour >= 24) {
            hour = 0;
        }
        this.currentWeather = WeatherType.values()[Randomizer.getRandom().nextInt(WeatherType.values().length)];
    }

    public WeatherType getCurrentWeather() {
        return currentWeather;
    }

    public boolean isDay() {
        return hour >= 6 && hour <= 20;
    }

    public int getStep() {
        return stepCount;
    }

    public int getHour() {
        return hour;
    }
}
