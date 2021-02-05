package server.multifactor;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class MultifactorAuthenticator {

    /*
     * Email client information:
     * Username: f2.scc363@gmail.com
     * Password: securepassword
     * Gender: Robot
     */

    String email = "f2.scc363@gmail.com";
    String password = "securepassword";
    Properties properties;
    Session session;

    public MultifactorAuthenticator()
    {
        init();
    }

    private void init()
    {
        properties = System.getProperties();
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.transport.protocol", "smtp");

        session = Session.getDefaultInstance(properties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
    }

    public void sendAuthenticationCode(String destinationEmail, String authenticationCode)
    {
        init();
        try
        {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinationEmail));

            message.setSubject("Authentication Code");
            message.setText(authenticationCode);

            Transport.send(message);
            System.out.println("Message send successfully.");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void failedLogInAttempt(String destinationEmail)
    {
        init();
        try
        {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinationEmail));

            message.setSubject("ALERT - Log In attempt");
            message.setText("An attempt was made to log in to your account. If this was not you please contact the administrator.");

            Transport.send(message);
            System.out.println("Failed log in message sent successfully.");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void permissionsUpdated(String destinationEmail)
    {
        init();
        try
        {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinationEmail));

            message.setSubject("ALERT - Permissions updated");
            message.setText("Your permissions have been updated. If you believe this to be a mistake, please contact the administrator.");

            Transport.send(message);
            System.out.println("Changed permissions message sent successfully.");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void newAccount(String destinationEmail)
    {
        init();
        try
        {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinationEmail));

            message.setSubject("Welcome");
            message.setText("A new account has been created using this email. " +
                    "Your email is your account name, the system administrator will be in contact with your password. " +
                    "Please update your password on your first time logging in. " +
                    "If you believe this to be a mistake, please contact the administrator.");

            Transport.send(message);
            System.out.println("New account message sent successfully.");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public String generateAuthenticationCode()
    {
        Random random = new Random();
        int code = random.nextInt(999999);
        return String.format("%06d", code);
    }

    //Test
    public static void main(String[] args)
    {
        MultifactorAuthenticator mfa = new MultifactorAuthenticator();
        String authenticationCode = mfa.generateAuthenticationCode();
        mfa.sendAuthenticationCode("chinkiu.pak@gmail.com", authenticationCode);
    }
}
