package com.github.ipchecknotifier;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.configuration.ConfigurationException;

public class Mailer {

    /**
     * Used to send new IP over email.
     * @param propReader
     * @throws ConfigurationException
     */
    public void sendMail(PropertyFileReader propReader, String publicIP) 
    {

            final String username = propReader.getMailfrom();
            final String password = propReader.getPasswd();
            
            Properties props = new Properties();
            props.put("mail.smtp.auth", propReader.isAuth());
            props.put("mail.smtp.starttls.enable", propReader.isTls());
            props.put("mail.smtp.host", propReader.getSmtp());
            props.put("mail.smtp.port", propReader.getPort());

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                    }
              });
            try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(propReader.getMailfrom()));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(propReader.getMailto()));
                    message.setSubject(propReader.getSubject());
                    message.setText("Dear IP Check Notifier user,"
                            + "\n\nYour new IP is: " + publicIP);

                    Transport.send(message);

                    System.out.println("Done");
            } catch (MessagingException e) {
                    throw new RuntimeException(e);
        }    
    }
}