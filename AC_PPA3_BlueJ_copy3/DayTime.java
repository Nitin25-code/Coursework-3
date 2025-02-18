public class DayTime {
    private int hour;
    private int stepCount;

    public DayTime(int hour, int stepCount) {
        this.hour = hour;
        this.stepCount = stepCount;
    }

    public void advanceHour() {
        hour++;
        if (hour >= 24) {
            hour = 0;
        }
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
