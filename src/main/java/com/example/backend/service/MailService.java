package com.example.backend.service;

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

@Component
public class MailService {

	private final JavaMailSender emailSender;
	private final QRCodeService qrCodeService;

	@Autowired
	public MailService(JavaMailSender emailSender, QRCodeService qrCodeService) {
		this.emailSender = emailSender;
		this.qrCodeService = qrCodeService;
	}

	public void sendMail(String clientMail) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("geosports.srl@gmail.com");
		message.setTo(clientMail);
		message.setSubject("Welcome on board! 😋");
		message.setText("");
		emailSender.send(message);

	}

	public void sendMessageWithAttachment(String clientMail, String clientName, String activityName) throws MessagingException, IOException, WriterException {

		qrCodeService.createQRCode(clientName);

		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(new InternetAddress("geosports.srl@gmail.com", "GeoSports Team") );
		helper.setTo(clientMail);
		helper.setSubject("Here is your QRCode for your activity! 🎉");
		helper.setText("Hello " + clientName + "," +
					   "\n\nThis email has been sent to you because you are attending: " +
					   "\n\nEvent: " + activityName +
					   "\nDate: " + "23.07.2022" +
					   "\nTime: " + "07:00 - 20:00" +
					   "\n\nShow your QR Code to our team when you arrive there.See you soon!😋" +
					   "\n\nDon't forget: don't show it to anyone 🤐" +
					   "\n\nGeoSports Team 🏕");

		FileSystemResource file = new FileSystemResource("src\\main\\resources\\qrcodes\\" + clientName + ".png");
		helper.addAttachment("My_QRCode.png", file);

		emailSender.send(message);

	}
}
