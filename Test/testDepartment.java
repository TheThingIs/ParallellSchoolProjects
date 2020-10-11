import Model.Admin;
import Model.Department;
import Model.WorkShift;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class testDepartment {
    @Test
    public void testCreateBreak() {
        Admin admin=Admin.getInstance();
        admin.createNewDepartment("Kassa", 1);
        Date date = new Date();
        boolean repeat[] = {true, false, false, true, false, false, false};
        admin.getDepartmentByName("Kassa").getBreakHandler().setMinBreakLength(1000*60*15);
        admin.createWorkshift( admin.getDepartmentByName("Kassa"),date.getTime()+(1000 * 60 * 60 * 1),date.getTime()+(1000 * 60 * 60 * 5), repeat);
        admin.createWorkshift( admin.getDepartmentByName("Kassa"),date.getTime()+(1000 * 60 * 60 * 1),date.getTime()+(1000 * 60 * 60 * 5), repeat);
        admin.createWorkshift( admin.getDepartmentByName("Kassa"),date.getTime()+(1000 * 60 * 60 * 1),date.getTime()+(1000 * 60 * 60 * 5), repeat);
        assertTrue( admin.getDepartmentByName("Kassa").getAllShifts().get(0).getBreakTime().inBetween( admin.getDepartmentByName("Kassa").getAllShifts().get(2).getBreakTime().start, admin.getDepartmentByName("Kassa").getAllShifts().get(2).getBreakTime().end));}
}
