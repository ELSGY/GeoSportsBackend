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
public class SendEnrollMail {

	private final MailService mailService;

	public SendEnrollMail(MailService mailService) {
		this.mailService = mailService;
	}

	@GetMapping("/welcome/{clientMail}")
	public String welcomeMail(@PathVariable String clientMail){
		mailService.sendMail(clientMail);

		return "Welcome mail sent to " + clientMail;
	}

	@GetMapping("/{clientMail}/{clientName}")
	public String sendEnrollMail(@PathVariable String clientMail, @PathVariable String clientName) throws MessagingException, IOException, WriterException {
		mailService.sendMessageWithAttachment(clientMail, clientName);

		return "Activity ticket sent to " + clientMail;
	}

}
