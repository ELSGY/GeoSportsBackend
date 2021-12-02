package com.example.backend.service;

import com.example.backend.repository.UserRepository;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
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

	@Autowired
	public MailService(JavaMailSender emailSender, QRCodeService qrCodeService, UserRepository userRepository) {
		this.emailSender = emailSender;
		this.qrCodeService = qrCodeService;
		this.userRepository = userRepository;
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
					   "http://localhost:3000/home\n" +
					   "\n" +
					   "If you experience any issues logging into your account, reach out to us at geosports.srl@gmail.com 📧.\n" +
					   "\n" +
					   "GeoSports Team 🏕");
		emailSender.send(message);

	}

	public void sendMessageWithAttachment(String userMail, String userName, String activityName) throws MessagingException, IOException, WriterException {

		// get user id by name
		int userId = userRepository.getUserByName(userName).getId();

		// create qr code
		qrCodeService.createQRCode(userName, userId);

		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(new InternetAddress("geosports.srl@gmail.com", "GeoSports Team"));
		helper.setTo(userMail);
		helper.setSubject("Here is your QRCode for your activity! 🎉");
		helper.setText("Hello " + userName + "," +
					   "\n\nThis email has been sent to you because you are attending: " +
					   "\n\nEvent: " + activityName +
					   "\nDate: " + "23.07.2022" +
					   "\nTime: " + "07:00 - 20:00" +
					   "\n\nShow your QR Code to our team when you arrive there.See you soon!😋" +
					   "\n\nDon't forget: don't show it to anyone 🤐" +
					   "\n\nGeoSports Team 🏕");

		FileSystemResource file = new FileSystemResource("src\\main\\resources\\qrcodes\\" + userName + ".png");
		helper.addAttachment(activityName + "_QRCode.png", file);

		emailSender.send(message);

	}
}
