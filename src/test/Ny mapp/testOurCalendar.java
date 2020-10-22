package test.java;

import main.java.model.OurCalendar;
import main.java.model.WorkDay;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class testOurCalendar {
    @Test
    public void testInit() {
        OurCalendar calendar = OurCalendar.getInstance();
        ArrayList<WorkDay> tempList = new ArrayList<>();
        for (int i = 0; i < calendar.getOurDateSize(); i++) {
            tempList.add(calendar.getWorkday(i));
        }
        List<WorkDay> list = tempList.subList(0, 365);
        assertEquals(365, list.size());  // kollar om kalendern skapar ett helt år
    }
}
