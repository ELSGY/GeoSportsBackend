package com.example.backend.controller;

import com.example.backend.service.MailService;
import com.google.zxing.WriterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/mail")
public class MailController {

	private final MailService mailService;

	public MailController(MailService mailService) {
		this.mailService = mailService;
	}

	@GetMapping("/welcome/{clientMail}")
	public String welcomeMail(@PathVariable String clientMail){
		mailService.sendMail(clientMail);

		return "Welcome mail sent to " + clientMail;
	}

	@GetMapping("/{clientMail}/{clientName}/{activityName}")
	public String sendEnrollMail(@PathVariable String clientMail, @PathVariable String clientName, @PathVariable String activityName) throws MessagingException, IOException, WriterException {
		mailService.sendMessageWithAttachment(clientMail, clientName, activityName);

		return "Activity ticket sent to " + clientMail;
	}

}
