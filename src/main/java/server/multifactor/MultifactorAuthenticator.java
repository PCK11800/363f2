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

    public void sendAuthenticationCode(String destinationEmail, int authenticationCode)
    {
        init();
        try
        {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinationEmail));

            message.setSubject("Authentication Code");
            message.setText(Integer.toString(authenticationCode));

            Transport.send(message);
            System.out.println("Message send successfully.");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public int generateAuthenticationCode()
    {
        Random random = new Random();
        int code = random.nextInt(999999);
        return code;
    }

    //Test
    public static void main(String[] args)
    {
        MultifactorAuthenticator mfa = new MultifactorAuthenticator();
        int authenticationCode = mfa.generateAuthenticationCode();
        mfa.sendAuthenticationCode("chinkiu.pak@gmail.com", authenticationCode);
    }
}
