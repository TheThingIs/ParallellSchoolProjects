package main.java.model;

/**
 * @author Oliver Andersson
 * Used By WorkShift, Employee
 * Represents a time span when someone is not available, has a start and an end value
 * @since ?
 */
public class OccupiedTime {
    public final long START, END;

    /**
     * Constructs an occupied time with a start and an end value
     *
     * @param start The start value of the time span
     * @param end   The end value of the time span
     */
    public OccupiedTime(long start, long end) {
        this.START = start;
        this.END = end;
    }

    /**
     * Checks if a start value and an end value is in between the occupied time
     *
     * @param start The start value of the time span
     * @param end   The end value of the time span
     * @return true if the time span is in between the occupied time span, false otherwise
     */
    public boolean inBetween(long start, long end) {
        return end >= this.START && this.END >= start;
    }
}
