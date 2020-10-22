package test.java;

import main.java.model.Admin;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class testLogin {

    @Test
    public void testCreateUser() {
        Admin admin = Admin.getInstance();
        admin.createNewUser("Markus", "Hemligt!");
        assertTrue(admin.isLoginInformationCorrect("Markus", "Hemligt!"));
        admin.removeUser("Markus", "Hemligt!");
        assertTrue(admin.isLoginInformationCorrect("Markus", "Hemligt!") == false);
    }

}
