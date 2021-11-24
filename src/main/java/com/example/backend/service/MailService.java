package com.example.backend.service;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
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

	public void sendMessageWithAttachment(String clientMail, String clientName) throws MessagingException, IOException, WriterException {

		qrCodeService.createQRCode(clientName);

		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("geosports.srl@gmail.com");
		helper.setTo(clientMail);
		helper.setSubject("Here is your QRCode for your activity! 🎉");
		helper.setText("Don't forget: don't show it to anyone 🤐");

		FileSystemResource file = new FileSystemResource("src\\main\\resources\\qrcodes\\" + clientName + ".png");
		helper.addAttachment("QRCode.png", file);

		emailSender.send(message);

	}
}
