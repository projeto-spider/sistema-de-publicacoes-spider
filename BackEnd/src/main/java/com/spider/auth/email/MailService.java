package com.spider.auth.email;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.spider.auth.models.User;
import com.spider.auth.models.Calendar;

/**
 * 
 * @author Mukuljaiswal
 *
 */
@Service
public class MailService {

	/*
	 * The Spring Framework provides an easy abstraction for sending email by using
	 * the JavaMailSender interface, and Spring Boot provides auto-configuration for
	 * it as well as a starter module.
	 */
	private JavaMailSender javaMailSender;

	/**
	 * 
	 * @param javaMailSender
	 */
	@Autowired
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	/**
	 * This function is used to send mail without attachment.
	 * @param user
	 * @throws MailException
	 */

	public void sendEmail(User user, Calendar event, boolean eventSub) throws MailException {

		/*
		 * This JavaMailSender Interface is used to send Mail in Spring Boot. This
		 * JavaMailSender extends the MailSender Interface which contains send()
		 * function. SimpleMailMessage Object is required because send() function uses
		 * object of SimpleMailMessage as a Parameter
		 */

		SimpleMailMessage mail = new SimpleMailMessage();
		System.out.println("enviando email para "+ user.getEmail());
		mail.setTo(user.getEmail());
		
		if(eventSub) {
			mail.setSubject("Alerta de submissão - " + event.getPlace());
			mail.setText(SubmissionMessage(event));
		}else {
			mail.setSubject("Alerta de realização - " + event.getPlace());
			mail.setText(StartCalendarMessage(event));
		}
		/*
		 * This send() contains an Object of SimpleMailMessage as an Parameter
		 */
		javaMailSender.send(mail);
	}

	/**
	 * This fucntion is used to send mail that contains a attachment.
	 * 
	 * @param user
	 * @throws MailException
	 * @throws MessagingException
	 */
	public void sendSimpleMail(String emailAdress, String subject, String body) throws MailException, MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

		helper.setTo(emailAdress);
		helper.setSubject(subject);
		helper.setText(body);

		javaMailSender.send(mimeMessage);
	}
	
	private static String SubmissionMessage(Calendar event) {
        return  "Caro membro do Spider, \n" + 
				"O Spider alerta que a data de submissão do evento " + event.getPlace() + " se aproxima.\n" + 
				"O deadline de submissão é dia " + formatDateToHumans(event.getSubmissionDate()) + " (faltam 15 dias).\n" + 
				"Não esqueça de agilizar a preparação do seu artigo, caso este evento seja do seu interesse.\n" + 
				"Atenciosamente, \n\n" + 
				"SpiderBot.\n";
    }
	
	private static String StartCalendarMessage(Calendar event) {
        return  "Caro membro do Spider, \n" + 
				"O Spider alerta que a data de realização do evento " + event.getPlace() + " se aproxima.\n" + 
				"O data de início do evento é dia " + formatDateToHumans(event.getStartDate()) + " (faltam 30 dias).\n" + 
				"Não esqueça de agilizar a sua inscrição no evento, caso este evento seja do seu interesse.\n" + 
				"Atenciosamente, \n\n" + 
				"SpiderBot.\n";
    }
	
	private static String NewPlanning(User user) {
        return  "Caro membro do Spider, \n" + 
				
				"Atenciosamente, \n\n" + 
				"SpiderBot.\n";
    }
	
	private static String UpdatePlanning(User user) {
        return  "Caro membro do Spider, \n" + 
				
				"Atenciosamente, \n\n" + 
				"SpiderBot.\n";
    }
	static public String MinusDays(String date, int minusDays) {
		return LocalDate.parse(date).minusDays(minusDays).toString();
	}
	
	private static String formatDateToHumans(String date) {
		DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(date).format(myFormatDate).toString();
	}

}