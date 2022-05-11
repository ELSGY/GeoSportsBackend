package com.example.backend.service;

import com.example.backend.model.Activity;
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
		String code = qrCodeService.createQRCode(userName, userID, activityID);

		// insert ticket into db
		activityService.insertActivityTicket(userID, activityID, code);

//		MimeMessage message = emailSender.createMimeMessage();
//
//		MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//		helper.setFrom(new InternetAddress("geosports.srl@gmail.com", "GeoSports Team"));
//		helper.setTo(userMail);
//		helper.setSubject("Here is your QRCode for your activity! 🎉");
//		helper.setText("Hello " + userName + "," +
//					   "\n\nThis email has been sent to you because you are attending: " +
//					   "\n\nEvent: " + activityName +
//					   "\nAddress: " + activityAddress +
//					   "\nDate: " + activityDate +
//					   "\nTime: " + activityTime +
//					   "\n\nShow your QR Code to our team when you arrive there.See you soon!😋" +
//					   "\n\nDon't forget: don't show it to anyone 🤐" +
//					   "\n\nGeoSports Team 🏕");
//
//		FileSystemResource file = new FileSystemResource("src\\main\\resources\\qrcodes\\" + userName + ".png");
//		helper.addAttachment(activityName + "_QRCode.png", file);

		activityService.decreaseActivityParticipants(activityName);
		//		emailSender.send(message);
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

		//		MimeMessage message = emailSender.createMimeMessage();
		//		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		//
		//		helper.setFrom(new InternetAddress("geosports.srl@gmail.com", "GeoSports Team"));
		//		helper.setTo(userMail);
		//		helper.setSubject("Welcome onboard! 😋");
		//		helper.setText("Hello " + userName + ",\n" +
		//					   "\n" +
		//					   "Thank you for joining our team 🤩.\n" +
		//					   "\n" +
		//					   "We would like to confirm that your account was created successfully. To access our page click the link below:\n" +
		//					   "\n" +
		//					   "http://localhost:3000/\n" +
		//					   "\n" +
		//					   "If you experience any issues logging into your account, reach out to us at geosports.srl@gmail.com 📧.\n" +
		//					   "\n" +
		//					   "GeoSports Team 🏕");

		activityService.deleteUserTicketForActivity(userID, activityID);
		activityService.increaseActivityParticipants(activityName);
		//emailSender.send(message);
		return "Activity ticket sent to " + userMail + "| User unenrolled from event";
	}
}
