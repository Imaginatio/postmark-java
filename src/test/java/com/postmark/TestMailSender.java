package com.postmark;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestMailSender {

	@Test
	public void testSendMail() {
		PostmarkMailSender mailSender = new PostmarkMailSender("***");
		PostmarkMessage m = new PostmarkMessage();
		m.setFrom("nicolas@gmail.com");
		m.setTo("nicolas@gmail.com");
		m.setSubject("Test Mail");
		m.setText("This is the body");
		m.setTag("test");
		mailSender.send(m);
	}
}
