package com.example.backend.controller;

import com.example.backend.service.MailService;
import com.google.zxing.WriterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@CrossOrigin
@RestController
@RequestMapping("/mail")
public class MailController {

	private final MailService mailService;

	public MailController(MailService mailService) {
		this.mailService = mailService;
	}

	@GetMapping("/welcome/{userMail}/{userName}")
	public String welcomeMail(@PathVariable String userMail, @PathVariable String userName) throws MessagingException, UnsupportedEncodingException {
		mailService.sendMail(userMail, userName);

		return "Welcome mail sent to " + userMail;
	}

	@GetMapping("/{userMail}/{userName}/{activityName}")
	public String sendEnrollMail(@PathVariable String userMail, @PathVariable String userName, @PathVariable String activityName) throws MessagingException, IOException, WriterException {
		mailService.sendMessageWithAttachment(userMail, userName, activityName);

		return "Activity ticket sent to " + userMail;
	}

}
