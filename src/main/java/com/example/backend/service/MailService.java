package com.example.backend.service;

import com.example.backend.model.Activity;
import com.example.backend.model.ActivityTickets;
import com.example.backend.model.User;
import com.example.backend.repository.ActivityRepository;
import com.example.backend.repository.UserRepository;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

@Component
public class MailService {

	private final JavaMailSender emailSender;
	private final QRCodeService qrCodeService;
	private final UserRepository userRepository;
	private final ActivityRepository activityRepository;
	private final ActivityService activityService;

	@Autowired
	public MailService(JavaMailSender emailSender, QRCodeService qrCodeService, UserRepository userRepository, ActivityRepository activityRepository, ActivityService activityService) {
		this.emailSender = emailSender;
		this.qrCodeService = qrCodeService;
		this.userRepository = userRepository;
		this.activityRepository = activityRepository;
		this.activityService = activityService;
	}

	public void sendMail(String userMail, String userName) throws UnsupportedEncodingException, MessagingException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(new InternetAddress("geosports.srl@gmail.com", "GeoSports Team"));
		helper.setTo(userMail);
		helper.setSubject("Welcome onboard! 😋");
		helper.setText("Hello " + userName + ",\n" +
					   "\n" +
					   "Thank you for joining our team 🤩.\n" +
					   "\n" +
					   "We would like to confirm that your account was created successfully. To access our page click the link below:\n" +
					   "\n" +
					   "http://localhost:3000/\n" +
					   "\n" +
					   "If you experience any issues logging into your account, reach out to us at geosports.srl@gmail.com 📧.\n" +
					   "\n" +
					   "GeoSports Team 🏕");
		emailSender.send(message);

	}

	public String sendMessageWithAttachment(String userMail, String userName, String activityName) throws MessagingException, IOException, WriterException {

		// get user id by name and activity id by name
		User user = userRepository.getUserByUsername(userName);
		if (user == null) {
			return "Could not get user from DB";
		}
		int userID = user.getId();

		Activity activity = activityRepository.getActivityByName(activityName);
		if (activity == null) {
			return "Could not get activity from DB";
		}
		int activityID = activity.getId();
		String activityTime = activity.getTime();
		String activityDate = activity.getDate();
		String activityAddress = activity.getAddress();

		// create qr code
		String code = qrCodeService.createQRCode(userName, userID, activityID, activityName);
		// create event number
		qrCodeService.drawPhoto(userName, activityName, userID);

		// insert ticket into db
		activityService.insertActivityTicket(userID, activityID, code);

		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(new InternetAddress("geosports.srl@gmail.com", "GeoSports Team"));
		helper.setTo(userMail);
		helper.setSubject("Here is your QRCode for your activity! 🎉");
		helper.setText("Hello " + userName + "," +
					   "\n\nThis email has been sent to you because you are attending: " +
					   "\n\nEvent: " + activityName +
					   "\nAddress: " + activityAddress +
					   "\nDate: " + activityDate +
					   "\nTime: " + activityTime +
					   "\n\nShow your QR Code to our team when you arrive there.See you soon!😋" +
					   "\n\nDon't forget: don't show it to anyone 🤐" +
					   "\n\nGeoSports Team 🏕");

		FileSystemResource file = new FileSystemResource("src\\main\\resources\\qrcodes\\" + activityName + "_" + userName + ".png");
		helper.addAttachment(activityName + "_QRCode.png", file);
		FileSystemResource image = new FileSystemResource("src\\main\\resources\\layout\\" + activityName + "_" + userName + ".png");
		helper.addAttachment("Your_number.png", image);

		activityService.decreaseActivityParticipants(activityName);
		emailSender.send(message);
		return "Activity ticket sent to " + userMail + "| User enrolled";
	}

	public String sendUnsubscribeEventMail(String userMail, String userName, String activityName) throws MessagingException, IOException, WriterException {

		// get user id by name and activity id by name
		User user = userRepository.getUserByUsername(userName);
		if (user == null) {
			return "Could not get user from DB";
		}
		int userID = user.getId();

		Activity activity = activityRepository.getActivityByName(activityName);
		if (activity == null) {
			return "Could not get activity from DB";
		}
		int activityID = activity.getId();

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(new InternetAddress("geosports.srl@gmail.com", "GeoSports Team"));
		helper.setTo(userMail);
		helper.setSubject("We respect your decision! 😇");
		helper.setText("Hello " + userName + ",\n" +
					   "\n" +
					   "Your ticket to \"" + activityName + "\" was deleted.\n" +
					   "\n" +
					   "Thank you for letting know us in time. 🤗\n" +
					   "\n" +
					   "\n" +
					   "GeoSports Team 🏕");

		activityService.deleteUserTicketForActivity(userID, activityID);
		activityService.increaseActivityParticipants(activityName);
		emailSender.send(message);
		return "Activity ticket sent to " + userMail + "| User unenrolled from event";
	}

	public void sendActivityDeletedMail(int activityId) {

		Set<ActivityTickets> activityTickets = activityRepository.getUsersTicketByActivityId(activityId);
		if (activityTickets != null) {

			Activity activity = activityRepository.getActivityById(activityId);
			if (activity == null) {
				return;
			}

			String activityName = activity.getName();

			MimeMessage message = emailSender.createMimeMessage();

			activityTickets.forEach(activityTicket -> {
				int userId = activityTicket.getUser_id();
				// get user id by name and activity id by name
				User user = userRepository.getUserById(userId);
				String userName = user.getUsername();
				String userMail = user.getEmail();

				MimeMessageHelper helper;
				try {
					helper = new MimeMessageHelper(message, true);
					helper.setFrom(new InternetAddress("geosports.srl@gmail.com", "GeoSports Team"));
					helper.setTo(userMail);
					helper.setSubject("Bad news today! 😥");
					helper.setText("Hello " + userName + ",\n" +
								   "\n" +
								   "\"" + activityName + "\" got cancelled.\n" +
								   "\n" +
								   "We'll keep you in touch for further details. 🤗\n" +
								   "\n" +
								   "\n" +
								   "GeoSports Team 🏕");

					emailSender.send(message);
				} catch (MessagingException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}

			});

			activityRepository.deleteActivity(activityId);
		}
	}

	public void updateActivityMail(String name, String address, String date, String time, int avbPlaces, int id, double lat, double lng) {

		Set<ActivityTickets> activityTickets = activityRepository.getUsersTicketByActivityId(id);
		if (activityTickets != null) {

			Activity activity = activityRepository.getActivityById(id);

			String activityName = activity.getName();

			MimeMessage message = emailSender.createMimeMessage();

			activityTickets.forEach(activityTicket -> {
				int userId = activityTicket.getUser_id();
				// get user id by name and activity id by name
				User user = userRepository.getUserById(userId);
				String userName = user.getUsername();
				String userMail = user.getEmail();

				MimeMessageHelper helper;
				try {
					helper = new MimeMessageHelper(message, true);
					helper.setFrom(new InternetAddress("geosports.srl@gmail.com", "GeoSports Team"));
					helper.setTo(userMail);
					helper.setSubject("There are some changes! 😥");
					helper.setText("Hello " + userName + ",\n" +
								   "\n" +
								   "\"" + activityName + "\" got some updates:\n" +
								   "\nEvent's name: " + name +
								   "\nAddress: " + address +
								   "\nDate: " + date +
								   "\nTime: " + time +
								   "\nTickets remaining: " + avbPlaces +
								   "\n" +
								   "We hope we didn't messed up your plans.See you there. 🤗\n" +
								   "\n" +
								   "\n" +
								   "GeoSports Team 🏕");

					emailSender.send(message);
				} catch (MessagingException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}

			});
		}
		activityRepository.updateActivity(name, address, date, time, avbPlaces, id, lat, lng);
	}
}
