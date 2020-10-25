package com.demo.thesisbackend.emails;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import shared.thesiscommon.bean.Reservation;

public class EmailManager {

	private String from;
	public void sendEmail(Reservation reservation) {

		from = reservation.getOwner().getFirstName() + " " + reservation.getOwner().getLastName();

		/* Setup mail server */
		String host = "smtp.gmail.com";
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("infoshareinfoshare2@gmail.com", "infoshare2.");
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("infoshareinfoshare2@gmail.com"));
			message.setSubject("InfoShare New Meeting");
			message.setText(setMessage(reservation, from));

			for (String email : reservation.getReceivers()) {

				message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

				System.out.println("sending...");
				// Send message
				Transport.send(message);
				System.out.println("Sent message successfully....");
			}

		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	public String setMessage(Reservation reservation, String from) {
		String text = "You are invited to join the meeting " + "\n" + "START DATE: " + reservation.getStartDate() + "\n"
				+ "END DATE: " + reservation.getEndDate() + "\n" + "START TIME: " + reservation.getStartTime() + "\n"
				+ "END TIME: " + reservation.getEndTime() + "\n"
				+ "ORGANIZED by " + from;

		if (reservation.isRecurring()) {
			String days = "";
			for (String d : reservation.getDays())
				days += d + " ";
			days += "\n";
			text += "Each: " + days;
		}
		return text;
	}
}