package main.java.model;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * @author Christian Lind
 * Used by EmployeeSorter.
 * Sends an email with provided information
 * @since 2020-10-15
 */

public class SendNotification {

    public SendNotification(String host, String to, String from, String username, String password, String subject, String text) {

        // Assuming you are sending email from through gmails smtp
        //String host = "smtp.gmail.com";

        Properties properties = setProperties(host);

        Session session = createSession(properties, from, password);

        sendMessage(session, from, to, subject, text);
    }

    public SendNotification(String host, String to, String from, String password, String subject, List<WorkShift> workshiftsNotFilled) {

        // Assuming you are sending email from through gmails smtp
        //String host = "smtp.gmail.com";

        Properties properties = setProperties(host);

        Session session = createSession(properties, from, password);

        StringBuilder sb = new StringBuilder();

        sb.append("There are " + workshiftsNotFilled.size() + " Workshift(s) not filled with ids");

        for (WorkShift ws : workshiftsNotFilled) {
            sb.append("\n" + ws.ID);
        }

        sb.append("\nSincerly EmployeeSorter");
        sendMessage(session, from, to, subject, sb.toString());
    }

    private Session createSession(Properties properties, String from, String password) {
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);

            }

        });
        return session;
    }

    private Properties setProperties(String host) {
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        return properties;
    }

    private void sendMessage(Session session, String from, String to, String subject, String text) {
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(text);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}


