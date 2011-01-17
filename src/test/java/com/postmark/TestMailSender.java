package com.postmark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.mail.MailSendException;

public class TestMailSender {
	
	static final String TEST_API_KEY = "POSTMARK_API_TEST";
	
	static final String VALID_EMAIL = "test@exemple.com";
	static final String INVALID_EMAIL = "test-exemple.com";
	
	PostmarkMailSender mailSender = new PostmarkMailSender(TEST_API_KEY);
	
	
	@Test
	public void testSendMail() {
		PostmarkMessage m = new PostmarkMessage();
		m.setFrom(VALID_EMAIL);
		m.setTo(VALID_EMAIL);
		m.setSubject("Test Mail");
		m.setText("This is the body\n" +
				"these are accents é à è ' etc..");
		m.setTag("test-utf8");
		mailSender.send(m);
	}

	@Test
	public void testSendMails() {
		PostmarkMessage msg[] = new PostmarkMessage[4];
		for(int i=0; i<msg.length; i++) {
			PostmarkMessage m = msg[i]
					= new PostmarkMessage();
			m.setFrom(VALID_EMAIL);
			m.setTo(VALID_EMAIL);
			m.setSubject("Test multiple mails #" + i);
			m.setText("Testing sending of multiple emails.");
			m.setTag("test-multiple");
		}
		msg[0].setTo(INVALID_EMAIL);
		msg[2].setFrom(INVALID_EMAIL);
		
		try {
			mailSender.send(msg);
			fail("No Exception arose whilst giving incorrect e-mail addresses.");
		
		} catch(MailSendException mse) {
			assertEquals(2, mse.getFailedMessages().size());
		}
	}
}
