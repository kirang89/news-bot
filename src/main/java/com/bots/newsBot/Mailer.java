package com.bots.newsBot;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Mailer {
	private String host;
	private boolean error = false;
	private Configuration mailerConfig;
	private final Properties props = new Properties();
	private String userName;
	private String password;
	private String sender;
	private String receiver;

	public Mailer(String file) {
		try {
			mailerConfig = new PropertiesConfiguration(file);
		} catch (ConfigurationException e) {
			error = true;
			e.printStackTrace();
		}

		host = mailerConfig.getProperty("host").toString();
		userName = mailerConfig.getProperty("username").toString();
		password = mailerConfig.getProperty("password").toString();
		sender = mailerConfig.getProperty("sender").toString();
		receiver = mailerConfig.getProperty("receiver").toString();

		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", "465");
		props.put(	"mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

	}

	public void sendMail(String subject, String body) {

		if (error == false) {
			Session session = Session
					.getDefaultInstance(props, new javax.mail.Authenticator() {
						protected PasswordAuthentication
								getPasswordAuthentication() {
							return new PasswordAuthentication(userName,
									password);
						}
					});

			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(sender));
				message.setRecipients(	Message.RecipientType.TO,
										InternetAddress.parse(receiver));
				message.setSubject(subject);
				message.setText(body);
				Transport.send(message);
				System.out.println("Mail has been sent successfully");
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		} else {
			System.out
					.println("Cannot send mail. Please provide the right configuration.");
		}
	}
}
