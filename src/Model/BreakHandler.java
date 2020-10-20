package Model;

/**
 * @author Victor Cousin and Moa Berglund
 * Represents a handler for the breaks of the work shifts, has information of how long a break should be
 * Used by Department, Uses Weekhandler
 * @since 2020-09-28
 */
public class BreakHandler {
    private long minBreakLength = WeekHandler.plusMinutes(15);
    private long midBreakLength = WeekHandler.plusMinutes(30);
    private long maxBreakLength = WeekHandler.plusMinutes(45);
    private static BreakHandler instance = null;

    private BreakHandler() {
    }

    public static BreakHandler getInstance() {
        if (instance == null)
            instance = new BreakHandler();
        return instance;
    }

    /**
     * Calculates length of the break according to chosen values of how long a break should be in various lengths of work shifts.
     *
     * @param start start value of the work shift
     * @param stop  stop value of the work shift
     * @return length of the break
     */
    public long calculateLengthOfBreak(long start, long stop) {
        if ((stop - start >= WeekHandler.plusHours(3)) && (stop - start <= WeekHandler.plusHours(5))) {
            return minBreakLength;
        }
        if (((stop - start) >= (WeekHandler.plusHours(5))) && ((stop - start) <= WeekHandler.plusHours(8))) {
            return midBreakLength;
        }
        if (stop - start > WeekHandler.plusHours(8)) {
            return maxBreakLength;
        }

        return 0;
    }

    public void setMinBreakLength(long minBreakLength) {
        this.minBreakLength = WeekHandler.plusMinutes((int) minBreakLength);
    }

    public void setMidBreakLength(long midBreakLength) {
        this.midBreakLength = WeekHandler.plusMinutes((int) midBreakLength);
    }

    public void setMaxBreakLength(long maxBreakLength) {
        this.maxBreakLength = WeekHandler.plusMinutes((int) maxBreakLength);
    }

    public long getMinBreakLength() {
        return minBreakLength;
    }

    public long getMinutesOfMinBreakLength() {
        return minBreakLength / WeekHandler.plusMinutes(1);
    }

    public long getMinutesOfMaxBreakLength() {
        return maxBreakLength / WeekHandler.plusMinutes(1);
    }

    public long getMinutesOfMidBreakLength() {
        return midBreakLength / WeekHandler.plusMinutes(1);
    }

    public long getMidBreakLength() {
        return midBreakLength;
    }

    public long getMaxBreakLength() {
        return maxBreakLength;
    }
}
