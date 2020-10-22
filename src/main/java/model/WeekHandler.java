package main.java.model;

/**
 * @author Christian Lind
 * Used By Admin, BreakHandler, Department, ImportSErvicePackage, WorkShift.
 * Represents a static WeekHandler which is used to add time to the Java.Date object
 * @since 2020-10-11
 */

public class WeekHandler {

    private WeekHandler() {
    }

    /**
     * Converts the specified amount of minutes to a Java.Date object
     *
     * @param minutes The amount of minutes to add to the Date object
     * @return The calculated number of minutes to add to the Date object
     */
    public static long plusMinutes(int minutes) {
        return 1000 * 60 * minutes;
    }

    /**
     * Converts the specified amount of hours to a Java.Date object
     *
     * @param hours The amount of hours to add to the Date object
     * @return The calculated number of hours to add to the Date object
     */
    public static long plusHours(int hours) {
        return 1000 * 60 * 60 * hours;
    }

    /**
     * Converts the specified amount of days to a Java.Date object
     *
     * @param days The amount of days to add to the Date object
     * @return The calculated number of days to add to the Date object
     */
    public static long plusDays(int days) {
        return 1000 * 60 * 60 * 24 * days;
    }

    /**
     * Converts the specified amount of days, hours and minutes to a Java.Date object
     *
     * @param days    The amount of days to add to the Date object
     * @param hours   The amount of hours to add to the Date object
     * @param minutes The amount of minutes to add to the Date object
     * @return The calculated number of days, hours and minutes to add to the Date object
     */
    public static long plusDaysAndHoursAndMinutes(int days, int hours, int minutes) {
        return 1000 * 60 * 60 * hours + 1000 * 60 * minutes + 1000 * 60 * 60 * 24 * days;
    }


}
