package com.example.emailAPI;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 * This class is used to send email
 */

@Slf4j
@SpringBootApplication
@RestController
public class EmailApiApplication {

	@Value("${gmail.username}")
	private String username;
	@Value("${gmail.password}")
	private String password;

	public static void main(String[] args) {
		SpringApplication.run(EmailApiApplication.class, args);
	}

	/**
	 * This method gets the employee email and calls sendMail() method
	 * @param employeeId
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws IOException
	 */

	@GetMapping("/sendEmail/{employeeId}")
	public String sendEmailToEmployee(@PathVariable String employeeId)  throws AddressException, MessagingException, IOException {
		log.info("Reimbursement emailId : "+employeeId);
		sendMail(employeeId);
		log.info("Email successfully sent");
		return "Email sent successfully";
	}

	/**
	 * This method is to send email
	 * @param emailId
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void sendMail(String emailId) throws AddressException, MessagingException, IOException {
		log.info("send mail starting");
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(username, false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailId));
		msg.setSubject("Reimbursement Request");
		msg.setContent("Hi, Your reimbursement request has been updated. Please contact the hr for more information.", "text/html");
		msg.setSentDate(new Date());

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent("Hi, Your reimbursement request has been updated. Please contact the hr for more information.", "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		msg.setContent(multipart);

		Transport.send(msg);
		log.info("Email sent");

	}

//	@RequestMapping(value="/sendEmail", method= RequestMethod.POST)
//	public String sendEmail(@RequestBody EmailMessage emailmessage) throws AddressException, MessagingException, IOException {
//		sendmail(emailmessage);
//		return "Email sent successfully";
//	}




//	private void sendmail(EmailMessage emailmessage) throws AddressException, MessagingException, IOException {
//
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "smtp.gmail.com");
//		props.put("mail.smtp.port", "587");
//
//		Session session = Session.getInstance(props,
//				new javax.mail.Authenticator() {
//					protected PasswordAuthentication getPasswordAuthentication() {
//						return new PasswordAuthentication(username, password);
//					}
//				});
//
//		Message msg = new MimeMessage(session);
//		msg.setFrom(new InternetAddress(username, false));
//
//		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailmessage.getTo_address()));
//		msg.setSubject(emailmessage.getSubject());
//		msg.setContent(emailmessage.getBody(), "text/html");
//		msg.setSentDate(new Date());
//
//		MimeBodyPart messageBodyPart = new MimeBodyPart();
//		messageBodyPart.setContent(emailmessage.getBody(), "text/html");
//
//		Multipart multipart = new MimeMultipart();
//		multipart.addBodyPart(messageBodyPart);
//		//MimeBodyPart attachPart = new MimeBodyPart();
//
//		//attachPart.attachFile("C:\\talk2amareswaran-downloads\\mysql2.png");
//
//		//multipart.addBodyPart(attachPart);
//		msg.setContent(multipart);
//		// sends the e-mail
//		Transport.send(msg);
//
//	}
}
