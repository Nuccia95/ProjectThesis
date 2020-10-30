package com.demo.thesisbackend.emails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import shared.thesiscommon.bean.Reservation;

public class EmailManager {

	public void sendEmail(Reservation reservation) {


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
			/*String from = reservation.getOwner().getFirstName() + " " + reservation.getOwner().getLastName();
			message.setText(setMessage(reservation, from));*/
			
			Multipart multipart = new MimeMultipart("alternative");

			BodyPart messageBodyPart = buildCalendarPart(reservation);
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

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
				+ "END TIME: " + reservation.getEndTime() + "\n" + "ORGANIZED by " + from;

		if (reservation.isRecurring()) {
			String days = "";
			for (String d : reservation.getDays())
				days += d + " ";
			days += "\n";
			text += "Each: " + days;
		}
		return text;
	}

	private static BodyPart buildCalendarPart(Reservation res){

		BodyPart calendarPart = new MimeBodyPart();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
		
		LocalDateTime start = LocalDateTime.of(res.getStartDate(), res.getStartTime());
		String formattedStart = start.format(formatter); 
		
		LocalDateTime end = LocalDateTime.of(res.getEndDate(), res.getEndTime());
		String formattedEnd = end.format(formatter); 
		
		int ending = res.getTitle().indexOf("-");
		String title = res.getTitle().substring(0, ending);
		
		final String calendarContent = "BEGIN:VCALENDAR\n" + "METHOD:REQUEST\n" + "PRODID: BCP - Meeting\n" +  "VERSION:2.0\n" + "BEGIN:VEVENT\n" + "DTSTAMP:" + formattedStart + "\n" + "DTSTART:" + formattedStart + "\n" + "DTEND:" + formattedEnd + "\n" + "SUMMARY:"+ title + "\n" +
				"LOCATION:Sede\n" + "BEGIN:VALARM\n" + "ACTION:DISPLAY\n" + "DESCRIPTION:REMINDER\n" + "END:VALARM\n" + "END:VEVENT\n" + "END:VCALENDAR";
	
		try {
			calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
			calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return calendarPart;
	}

}