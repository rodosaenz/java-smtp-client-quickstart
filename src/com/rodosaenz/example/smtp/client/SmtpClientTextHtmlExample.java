package com.rodosaenz.example.smtp.client;

import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Rodolfo
 */
public class SmtpClientTextHtmlExample {

    public static void main(String[] args) {

        // Recipient's email ID needs to be mentioned.
        String to = "to@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "from@gmail.com";
        final String username = "<YOUR-ACCOUNT>";//change accordingly
        final String password = "*******";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Testing Java Text And Html Email");
            
            //As this email has 2 parts, we have to create 2 parts
            //So we need a multipart to handle these           
            MimeMultipart multipart = new MimeMultipart("alternative");
            
            // first part (the text)
            BodyPart messageTextPart = new MimeBodyPart();
            String text = "Simple Text And Html Email";
            messageTextPart.setText(text);
            multipart.addBodyPart(messageTextPart);
            
            // second part (the html)
            BodyPart messageHtmlPart = new MimeBodyPart();
            String html = "<h1>Simple Text And Html Email</h1>";
            messageHtmlPart.setContent(html,"text/html");
            multipart.addBodyPart(messageHtmlPart);
            
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
