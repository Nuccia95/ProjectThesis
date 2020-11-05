package com.demo.thesisbackend.emails;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
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
import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;

public class EmailManager {

	private static HashMap<String, String> daysMap = new HashMap<>(); 
	private Properties properties;
	private Session session;

	public EmailManager() {
		/* Setup */
		String host = "smtp.gmail.com";
		properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("infoshareinfoshare2@gmail.com", "infoshare2.");
			}
		});
		
		setUpDays();
	}
	
	public void sendEmailEnabledResource(List<String> emails , Resource res) {

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("infoshareinfoshare2@gmail.com"));
			message.setSubject("GeDiCo - Notification Enabled Resource");
			message.setText("The resource " + res.getName() + " will be disabled. You have some related "
					+ "reservations to it. Change them.");
			
			for (String email : emails) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
				System.out.println("sending...");
				Transport.send(message);
				System.out.println("Sent message successfully....");
			}

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void sendEmail(Reservation reservation) {

		try {
			MimeMessage message = new MimeMessage(session);
			Multipart multipart = new MimeMultipart();
			String from = reservation.getOwner().getFirstName() + " " + reservation.getOwner().getLastName();

			message.setFrom(new InternetAddress("infoshareinfoshare2@gmail.com"));
			message.setSubject("GeDiCo - Join The Meeting");

			BodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(from + " has created a new meeting. Do you want to partecipate?");

			BodyPart attachBodyPart = buildCalendarPart(reservation);

			multipart.addBodyPart(textBodyPart);
			multipart.addBodyPart(attachBodyPart);
			message.setContent(multipart);

			for (String email : reservation.getReceivers()) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
				System.out.println("sending...");
				Transport.send(message);
				System.out.println("Sent message successfully....");
			}

		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	private static BodyPart buildCalendarPart(Reservation res) {

		BodyPart calendarPart = new MimeBodyPart();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

		LocalDateTime start = LocalDateTime.of(res.getStartDate(), res.getStartTime());
		String formattedStart = start.format(formatter);

		LocalDateTime end = LocalDateTime.of(res.getEndDate(), res.getEndTime());
		String formattedEnd = end.format(formatter);
	
		
		String title = res.getTitle();
		String uid = res.getId().toString();
		
		final String calendarContent;
		if(res.isRecurring()) {
			LocalTime endRecTime = LocalTime.of(0, 0);
			LocalDateTime endRecurring = LocalDateTime.of(res.getEndRecurring(), endRecTime);
			String formattedRecEnd = endRecurring.format(formatter);
			calendarContent = setRecurringCalendarContent(res.getDays(), formattedStart, formattedEnd, formattedRecEnd, title, uid);
		}
		else 
			calendarContent = setCalendarContent(formattedStart, formattedEnd, title, uid);			

		try {
			calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
			calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return calendarPart;
	}

	public static String setCalendarContent(String formattedStart, String formattedEnd, String title,
			String uid) {
		return "BEGIN:VCALENDAR\n" 
		+ "METHOD:REQUEST\n" 
		+ "PRODID: GEDICO - Meeting\n" 
		+  "VERSION:2.0\n" 
		+ "BEGIN:VEVENT\n" 
		+ "DTSTAMP:" + formattedStart + "\n" 
		+ "DTSTART:" + formattedStart + "\n" 
		+ "DTEND:" + formattedEnd + "\n" 
		+ "SUMMARY:" + title + "\n" 
		+ "UID:" + uid +"X\n" 
		+ "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:infoshareinfoshare2@gmail.com\n" 
		+ "ORGANIZER:MAILTO:infoshareinfoshare2@gmail.com\n"
		+ "LOCATION:Company\n" 
		+ "DESCRIPTION:Meeting\n"
		+ "SEQUENCE:0\n" 
		+ "PRIORITY:5\n" 
		+ "CLASS:PUBLIC\n" 
		+ "STATUS:CONFIRMED\n" 
		+ "TRANSP:OPAQUE\n" 
		+ "BEGIN:VALARM\n" 
		+ "ACTION:DISPLAY\n" 
		+ "DESCRIPTION:REMINDER\n" 
		+ "END:VALARM\n" 
		+ "TRIGGER;RELATED=START:-PT00H15M00S\n" 
		+ "END:VEVENT\n" 
		+ "END:VCALENDAR";
	}
	
	public static String setRecurringCalendarContent(List<String> days, String formattedStart, String formattedEnd,
			String formattedRecEnd, String title, String uid) {
		
		/* GENERETING RULE FOR RECURRING EVENT */
		StringBuilder builder = new StringBuilder();
		builder.append("RRULE:FREQ=WEEKLY;BYDAY=");

		for(int i=0; i < days.size(); i++) {
			builder.append(daysMap.get(days.get(i)));
			if(i == days.size() - 1)
				builder.append(";");
			else
				builder.append(",");
		}
		
		builder.append("INTERVAL=1;UNTIL=" + formattedRecEnd + "\n");
		
		String rule = builder.toString();
		
		return "BEGIN:VCALENDAR\n" 
		+ "METHOD:REQUEST\n" 
		+ "PRODID: GEDICO - Meeting\n" 
		+  "VERSION:2.0\n" 
		+ "BEGIN:VEVENT\n" 
		+ "DTSTAMP:" + formattedStart + "\n" 
		+ "DTSTART:" + formattedStart + "\n" 
		+ "DTEND:" + formattedEnd + "\n" 
		+ rule
		+ "SUMMARY:" + title + "\n" 
		+ "UID:" + uid +"Y\n" 
		+ "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:infoshareinfoshare2@gmail.com\n" 
		+ "ORGANIZER:MAILTO:infoshareinfoshare2@gmail.com\n"
		+ "LOCATION:Company\n" 
		+ "DESCRIPTION:Meeting\n"
		+ "SEQUENCE:0\n" 
		+ "PRIORITY:5\n" 
		+ "CLASS:PUBLIC\n" 
		+ "STATUS:CONFIRMED\n" 
		+ "TRANSP:OPAQUE\n" 
		+ "BEGIN:VALARM\n" 
		+ "ACTION:DISPLAY\n" 
		+ "DESCRIPTION:REMINDER\n" 
		+ "END:VALARM\n" 
		+ "TRIGGER;RELATED=START:-PT00H15M00S\n" 
		+ "END:VEVENT\n" 
		+ "END:VCALENDAR";
	}
	
	public void setUpDays() {
		daysMap.put(DayOfWeek.MONDAY.toString(), "MO");
		daysMap.put(DayOfWeek.TUESDAY.toString(), "TU");
		daysMap.put(DayOfWeek.WEDNESDAY.toString(), "WE");
		daysMap.put(DayOfWeek.THURSDAY.toString(), "TH");
		daysMap.put(DayOfWeek.FRIDAY.toString(), "FR");
		daysMap.put(DayOfWeek.SATURDAY.toString(), "SA");
	}
	
	
}