package Model;

public enum DayOfWeek {
    monday(0),
    tuesday(1),
    wednesday(2),
    thursday(3),
    friday(4),
    saturday(5),
    sunday(6);

    public final int offset;

    DayOfWeek(int offset) {
        this.offset = offset;
    }

    static DayOfWeek getDay(int calendarDay){
        return switch (calendarDay) {
            case 1 -> monday;
            case 2 -> tuesday;
            case 3 -> wednesday;
            case 4 -> thursday;
            case 5 -> friday;
            case 6 -> saturday;
            case 7 -> sunday;
            default -> null;
        };
    }
}
