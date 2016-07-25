package com.rodosaenz.example.smtp.client;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
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
public class SmtpClientTextHtmlAttachmentImageInlineExample {

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
            message.addRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Testing Java Text, Html, Attachment And Image Inline");

            /*
            As this email has 4 parts, we have to create 4 parts
            using this:
                MainMultipart              
                |
                ---- TextBodypart -- NO VA
                |
                ---- HtmlBodypart
                |
                ---- AttachmentBodypart
                |
                ---- ImageInlineBodypart
             */
            MimeMultipart mainMultipart = new MimeMultipart("related");

            // first part (the text)
            // So, now, if we use image inline, no need text any more
            BodyPart textBodypart = new MimeBodyPart();
            String text = "Testing Java Text, Html, Attachment And Image Inline Email";
            textBodypart.setText(text);
            //mainMultipart.addBodyPart(textBodypart);

            // second part (the html)
            BodyPart htmlBodypart = new MimeBodyPart();
            String html = "<h1>Testing Java Text, Html, Attachment And Image Inline Email "
                    + "<img src=\"cid:image_id_random\"></h1>";
            htmlBodypart.setContent(html, "text/html");
            mainMultipart.addBodyPart(htmlBodypart);

            // third part (the attachment)
            BodyPart attachmentBodypart = new MimeBodyPart();
            String filepath = "aws-overview-2015-12.pdf";
            DataSource fileSource = new FileDataSource(filepath);
            attachmentBodypart.setDataHandler(new DataHandler(fileSource));
            attachmentBodypart.setFileName(fileSource.getName());
            //attachmentBodypart.setHeader("Content-Disposition", MimeBodyPart.ATTACHMENT);
            mainMultipart.addBodyPart(attachmentBodypart);

            // fourth part (the image inline)
            BodyPart imageInlineBodypart = new MimeBodyPart();
            DataSource imageSource = new FileDataSource("logo.png");
            imageInlineBodypart.setFileName(imageSource.getName());
            imageInlineBodypart.setDataHandler(new DataHandler(imageSource));
            imageInlineBodypart.setHeader("Content-ID", "<image_id_random>");
            imageInlineBodypart.setHeader("Content-Disposition", MimeBodyPart.INLINE);
            imageInlineBodypart.setHeader("Content-Type", imageSource.getContentType());
            mainMultipart.addBodyPart(imageInlineBodypart);

            message.setContent(mainMultipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
