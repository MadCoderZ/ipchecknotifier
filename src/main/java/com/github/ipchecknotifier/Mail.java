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

public class Mail {

    /**
     * Used to send new IP over email.
     * @param readpropfile
     * @throws ConfigurationException
     */
    public void sendMail(ReadPropFile readpropfile) throws ConfigurationException {

            final String username = readpropfile.getMailfrom();
            final String password = readpropfile.getPasswd();
            
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", readpropfile.getSmtp());
            props.put("mail.smtp.port", readpropfile.getPort());

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                    }
              });
            try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(readpropfile.getMailfrom()));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(readpropfile.getMailto()));
                    message.setSubject(readpropfile.getSubject());
                    message.setText("Dear IP Check Notifier user,"
                            + "\n\nYour new IP is: " + Main.publicIP);

                    Transport.send(message);

                    System.out.println("Done");
            } catch (MessagingException e) {
                    throw new RuntimeException(e);
        }    
    }
}