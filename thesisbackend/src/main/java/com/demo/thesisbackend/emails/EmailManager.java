package com.demo.thesisbackend.emails;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

	private static HashMap<String, String> daysMap;

	public EmailManager() {
		daysMap = new HashMap<>();
		daysMap.put(DayOfWeek.MONDAY.toString(), "MO");
		daysMap.put(DayOfWeek.TUESDAY.toString(), "TU");
		daysMap.put(DayOfWeek.WEDNESDAY.toString(), "WE");
		daysMap.put(DayOfWeek.THURSDAY.toString(), "TH");
		daysMap.put(DayOfWeek.FRIDAY.toString(), "FR");
		daysMap.put(DayOfWeek.SATURDAY.toString(), "SA");
	}

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
			Multipart multipart = new MimeMultipart();
			String from = reservation.getOwner().getFirstName() + " " + reservation.getOwner().getLastName();

			message.setFrom(new InternetAddress("infoshareinfoshare2@gmail.com"));
			message.setSubject("InfoShare - Join The Meeting");

			BodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(setMessage(reservation, from));

			BodyPart attachBodyPart = buildCalendarPart(reservation);

			multipart.addBodyPart(textBodyPart);
			multipart.addBodyPart(attachBodyPart);
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
		String text = from + " " + "has created a new meeting. Do you want to partecipate? ";

		if (reservation.isRecurring()) {
			String days = "";
			for (String d : reservation.getDays())
				days += d + " ";
			days += "\n";
			text += "Days of the event: " + days;
		}
		return text;
	}

	private static BodyPart buildCalendarPart(Reservation res) {

		BodyPart calendarPart = new MimeBodyPart();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

		LocalDateTime start = LocalDateTime.of(res.getStartDate(), res.getStartTime());
		String formattedStart = start.format(formatter);

		LocalDateTime end = LocalDateTime.of(res.getEndDate(), res.getEndTime());
		String formattedEnd = end.format(formatter);
		
		String title = res.getTitle();

		final String calendarContent = setCalendarContent(formattedStart, formattedEnd, title);

		try {
			calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
			calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return calendarPart;
	}

	public static String setCalendarContent(String formattedStart, String formattedEnd, String title) {
		return "BEGIN:VCALENDAR\n" + "METHOD:REQUEST\n" + "PRODID: Meeting\n" + "VERSION:2.0\n" + "BEGIN:VEVENT\n"
				+ "DTSTART:" + formattedStart + "\n" + "DTEND:" + formattedEnd + "\n" + "DTSTAMP:" + formattedStart
				+ "\n" + "SUMMARY:" + title + "\n" /*+ "UID:324\n"*/
				+ "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:\n"
				+ "LOCATION:Sede centrale, Rende Cs\n" + "DESCRIPTION:Meeting\n" + "SEQUENCE:0\n" + "PRIORITY:5\n"
				+ "CLASS:PUBLIC\n" + "STATUS:CONFIRMED\n" + "TRANSP:OPAQUE\n" + "BEGIN:VALARM\n" + "ACTION:DISPLAY\n"
				+ "DESCRIPTION:REMINDER\n" + "END:VALARM\n" + "TRIGGER;RELATED=START:-PT00H15M00S\n" + "END:VEVENT\n"
				+ "END:VCALENDAR";
	}
	
}