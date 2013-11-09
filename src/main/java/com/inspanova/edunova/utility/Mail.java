/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspanova.edunova.utility;

import java.util.List;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author krishna
 */
public class Mail {
    
    private static final String HOSTNAME = "smtp.googlemail.com";
	private static final int SMTP_PORT = 465;
	private static final String USERNAME = "kkumar.nml@gmail.com";
	private static final String PASSWORD = "10bfb632";

	private static final String FROM_ADDRESS = "kkumar.nml@gmail.com";

	public static void sendMail(List<String> mailRecipientList, String subject,
			String messageBody) throws EmailException {
		Email email = new SimpleEmail();
		email.setHostName(HOSTNAME);
		email.setSmtpPort(SMTP_PORT);
		email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
		email.setSSLOnConnect(true);
		email.setFrom(FROM_ADDRESS);
		email.setSubject(subject);
		email.setMsg(messageBody);
		for (String recipient : mailRecipientList) {
			email.addBcc(recipient);
		}
		email.send();
        }
}
