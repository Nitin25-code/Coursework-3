public class Environment {
    private int hour;
    private int stepCount;
    private WeatherType currentWeather;

    public Environment(int hour, int stepCount) {
        this.hour = hour;
        this.stepCount = stepCount;
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

    public String DayorNight() {
        if (hour >= 6 && hour <= 20) {
            return "Day";
        } else {
            return "Night";
        }
    }

    public int getHour() {
        return hour;
    }
}
