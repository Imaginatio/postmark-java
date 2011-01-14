package com.postmark;

import org.junit.Test;

public class TestMailSender {

	@Test
	public void testSendMail() {
		PostmarkMailSender mailSender = new PostmarkMailSender("POSTMARK_API_TEST");
		PostmarkMessage m = new PostmarkMessage();
		m.setFrom("nicolas@gmail.com");
		m.setTo("nicolas@gmail.com");
		m.setSubject("Test Mail");
		m.setText("This is the body\n" +
				"these are accents é à è ' etc..");
		m.setTag("test-utf8");
		mailSender.send(m);
	}
}
